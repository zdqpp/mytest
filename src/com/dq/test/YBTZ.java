package com.dq.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jfinal.weixin.util.HttpClientHelper;
import com.jfinal.weixin.util.UtilJson;

public class YBTZ{
	public static void main(String args[]) throws Exception
	{

		String proJson=null;
		try{
			proJson="";
			System.out.println(proJson);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		JSONObject proJsonObj=JSONObject.parseObject(proJson);
		Map<String,Object> map1=UtilJson.json2Map(proJson);
		Map<String,String> map=new HashMap<String,String>();
		for(String key:map1.keySet())
		{
			map.put(key, String.valueOf(map1.get(key)));
		}
		
		System.out.println("·¢ËÍ£º"+new Gson().toJson(map));
//		String resp = HttpClientHelper.postForm("http://123.57.149.63/vipcaibao/wapPay/wapNotify.html", map);
		String resp = HttpClientHelper.postForm("http://127.0.0.1:8888/caibao/product/sysnProductNew.html", map);
		System.out.println("·µ»Ø£º"+resp);
		System.exit(0);
	
	}
}