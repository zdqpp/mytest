package com.jf.common.tools.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpRequestUtil {

	private static Logger logger = LoggerFactory
			.getLogger(HttpRequestUtil.class);

	private static final String DEFAULT_CHARSET = "UTF-8";
	// ���ݴ��䴦��ʱʱ��
	private static int SOCKET_TIMEOUT = 40000;
	// �����ӳ��к�ȥ���ӵĳ�ʱʱ��
	private static int CONNECTIONREQUEST_TIMEOUT = 5000;
	// �������ӳ�ʱʱ��
	private static int CONNECTTIMEOUT_TIMEOUT = 5000;

	private static int POOL_COUNT = 200;// ���ӳ���������

	private static int MAX_PER_ROUTE = 40;// ͬһ��վ���������������

	private static RequestConfig requestConfig = null;
	private static CloseableHttpClient httpclient = null;
	private static PoolingHttpClientConnectionManager connectionManager = null;

	static {
		init();
	}

	/**
	 * ��ʼ������
	 */
	private static void init() {

		// https��������֤��
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null,
					new TrustStrategy() {
						@Override
						public boolean isTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType) throws CertificateException {
							// TODO Auto-generated method stub
							return true;
						}
					}).build();
		} catch (KeyManagementException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (KeyStoreException e) {
			logger.error(e.getMessage(), e);
			// throw e;s
		}
		
//		X509HostnameVerifier  hostNameVerify =new X509HostnameVerifier(){
//			
//		};
		
		
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			public void verify(String arg0, SSLSocket arg1) throws IOException {}
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
			public void verify(String arg0, X509Certificate arg1) throws SSLException {}
		};		
		//��Щssl��Ҫhostname ��֤�����hostname��֤
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext,hostnameVerifier);
		// �Զ����socket��������Ժ�ָ����Э�飨Http��Https����ϵ���������������Զ�������ӹ�������
		RegistryBuilder<ConnectionSocketFactory> r = RegistryBuilder
				.<ConnectionSocketFactory> create();
		PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory
				.getSocketFactory();
		r = r.register("https", sslsf);
		r = r.register("http", plainsf);

		//
		
		try{
			
			SOCKET_TIMEOUT=Integer.parseInt(PropertyManager.getString("http_client_socket_timeout"));
			CONNECTTIMEOUT_TIMEOUT=Integer.parseInt(PropertyManager.getString("http_client_connectiontimeout"));
			CONNECTIONREQUEST_TIMEOUT=Integer.parseInt(PropertyManager.getString("http_client_connectionrequest_timeout"));
			MAX_PER_ROUTE=Integer.parseInt(PropertyManager.getString("http_client_max_perroute"));
			POOL_COUNT=Integer.parseInt(PropertyManager.getString("http_client_max_poolcount"));
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		// ��ʼ�����ӹ�����
		connectionManager = new PoolingHttpClientConnectionManager(r.build());
		connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);// ͬһ��վ���������������
		connectionManager.setMaxTotal(POOL_COUNT);// ���ӳ���������

		// ���ó�ʱʱ��
		requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECTTIMEOUT_TIMEOUT)
				.setConnectionRequestTimeout(CONNECTIONREQUEST_TIMEOUT).build();
	}

	/**
	 * ��ȡhttpclientʵ��
	 * 
	 * @return
	 */
	private static CloseableHttpClient getClient() {

		if (null == httpclient) {
			httpclient = HttpClients.custom()
					.setConnectionManager(connectionManager).build();
		}
		return httpclient;
	}

	/**
	 * get ����
	 * 
	 * @param url
	 * @return
	 */
	public static CloseableHttpResponse doGet(String url) throws Exception {

		// String responseStr = null;

		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);

		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = getClient().execute(get);

			return httpResponse;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {

		}
	}

	/**
	 * @param httpResponse
	 * @return
	 * @throws Exception
	 * 
	 *             ״̬��ΪOK���������ݣ������OK���׳��쳣
	 * 
	 *             �÷������ر�response�����Լ��ر�response
	 */
	public static String getContent(CloseableHttpResponse httpResponse)
			throws Exception {
		String result = "";
		int responStatusCode = httpResponse.getStatusLine().getStatusCode();
		if (responStatusCode == HttpStatus.SC_OK) {
			result = EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_CHARSET);

			return result;
		} else {
			throw new Exception("״̬���OK��");
		}
	}

	/**
	 * @param httpResponse
	 * @throws Exception
	 * 
	 *             �ر� httpResponse
	 */
	public static void close(CloseableHttpResponse httpResponse)
			throws Exception {
		if (null != httpResponse) {
			try {
				httpResponse.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	/**
	 * @param url
	 *            ����url
	 * @return ���״̬Ϊ200����string�����������״̬�׳��쳣������뾫ȷ���㷵��״̬����Ҫʹ�ô˷���
	 * @throws Exception
	 * 
	 * 
	 */
	public static String doGetStrRes(String url) throws Exception {
		CloseableHttpResponse response = null;
		try {
			response = doGet(url);
			return getContent(response);
		} catch (Exception e) {
			throw e;
		} finally {
			close(response);
		}

	}

	/**
	 * @param url
	 *            ����url
	 * @return ���״̬Ϊ200����string�����������״̬�׳��쳣������뾫ȷ���㷵��״̬����Ҫʹ�ô˷���
	 * @throws Exception
	 */
	public static String doPostStrRes(String url, Map<String, String> params)
			throws Exception {

		CloseableHttpResponse response = null;
		try {
			response = doPost(url, params);
			return getContent(response);
		} catch (Exception e) {
			throw e;
		} finally {
			close(response);
		}
	}

    /**
     * @param url
     *            ����url
     * @return ���״̬Ϊ200����string�����������״̬�׳��쳣������뾫ȷ���㷵��״̬����Ҫʹ�ô˷���
     * @throws Exception
     */
    public static String doPostStrRes(String url, String strParam)
            throws Exception {

        CloseableHttpResponse response = null;
        try {
            response = doPost(url, strParam);
            return getContent(response);
        } catch (Exception e) {
            throw e;
        } finally {
            close(response);
        }
    }

	/**
	 * post ����
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static CloseableHttpResponse doPost(String url,
			Map<String, String> params) throws Exception {

		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);

		CloseableHttpResponse httpResponse = null;
		try {
			List<NameValuePair> nvps = toNameValuePairList(params);
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

			httpResponse = getClient().execute(post);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {

		}
		return httpResponse;
	}

    /**
     * post ����
     * 
     * @param url
     * @param strParam
     * @return
     */
    public static CloseableHttpResponse doPost(String url, String strParam) throws Exception {

        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity(strParam,"UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/text;charset=UTF-8"); 
            
            post.setEntity(entity);

            httpResponse = getClient().execute(post);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {

        }
        return httpResponse;
    }

	/**
	 * post����ת��ͨ��url
	 * 
	 * @param parameters
	 * @return
	 */
	private static List<NameValuePair> toNameValuePairList(
			Map<String, String> params) {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		return nvps;
	}

	/**
	 * POST�ύ
	 *
	 * @param strUrl
	 *            ����URL
	 * @param strJson
	 *            ����json�ַ���
	 * @return
	 */
	public static String postJson(String strUrl, String strJson) throws Exception {

		String body = null;
		if (strJson != null && !"".equals(strJson.trim())) {

			// ʵ��
			StringEntity entity = new StringEntity(strJson.toString(), "utf-8");
			// post
			HttpPost postMethod = new HttpPost(strUrl);
			postMethod.setEntity(entity);

			long startTime = System.currentTimeMillis();

			// ��Ӧ
			CloseableHttpResponse response = getClient().execute(postMethod);
			long endTime = System.currentTimeMillis();
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode:" + statusCode);
			logger.info("����API ����ʱ��(��λ������)��" + (endTime - startTime));
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed:" + response.getStatusLine());
			}

			// Read the response body
			body = EntityUtils.toString(response.getEntity());
		}

		return body;
	}

	public static void main(String[] args) {

		for (int i = 0; i < 100; i++) {
			try {
				String content = HttpRequestUtil
						.doGetStrRes("https://www.baidu.com/");

				System.out.println(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
