package com.dq.test;
import java.util.HashMap;
import java.util.Map;
import com.jfinal.weixin.util.zhangwu.MD5Util;
import com.google.gson.Gson;
import com.jfinal.weixin.util.HttpClientHelper;

public class Duifu
{
	public static void main(String args[])
	{
		Map<String,String> map=new HashMap<String,String>();
		map.put("earnings","130.93");
		map.put("cash_id","1309794");
		map.put("cash_time","2016-07-14 17:31:47");
		map.put("Ip","10.173.44.107");
		map.put("sub_order_no","");
		map.put("remark","进债匹报盘兑付通知");
		map.put("pay_type","1");
		map.put("fun_code","200001");
		map.put("order_no","VIPCB201801311317340564021591");
		map.put("channel_id","VIPCB");
		String signStr = "order_cash_notice_" + map.get("channel_id") + "|"
				+ map.get("order_no") + "|" + map.get("pay_type") + "|"
				+ map.get("cash_time") + "|" + map.get("earnings") + "|"
				+ map.get("fun_code");
		String signMD5 = MD5Util.MD5Encode(signStr, "UTF-8");
		map.put("sign", signMD5);
		System.out.println("发送："+new Gson().toJson(map));
		String resp = HttpClientHelper.postForm("http://123.57.149.63/vipcaibao/loanMatch/cashDubbo.html", map);
//		String resp = HttpClientHelper.postForm("http://192.168.90.85:8090/caibao/loanMatch/cashDubbo.html", map);
		System.out.println("返回："+resp);
		System.out.println("test333");
    }
}
