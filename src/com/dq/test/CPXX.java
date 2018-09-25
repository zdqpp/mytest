package com.dq.test;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dq.util.FileUtil;
import com.google.gson.Gson;
import com.jfinal.weixin.util.HttpClientHelper;


public class CPXX
{
	public static void main(String args[])
	{
		Map<String,String> map=new HashMap<String,String>();
		map.put("fun_code","500002");
		map.put("productId","36");
		map.put("issuePeriod","1");
		System.out.println("·¢ËÍ£º"+new Gson().toJson(map));
		String resp = HttpClientHelper.postForm("http://123.57.149.63/vipcaibao/product/newLCHandler.html", map);
//		String resp = HttpClientHelper.postForm("http://127.0.0.1:8888/caibao/product/newLCHandler.html", map);
		System.out.println("·µ»Ø£º"+resp);
		System.exit(0);
    }
}
