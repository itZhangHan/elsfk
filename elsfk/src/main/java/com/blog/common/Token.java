//package com.blog.common;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.client.HttpClient;
//
//public class Token {
//	public static GeneralToken getToken(String appid, String secret){
//		GeneralToken gt = null;
//		try {
//			HttpClient hc = new HttpClient();
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("appid", appid);
//			params.put("secret", secret);
//			params.put("grant_type", "client_credential");
//			String url = "https://api.weixin.qq.com/cgi-bin/token";
//			gt =  hc.post(url, params, new JsonParser<GeneralToken>(GeneralToken.class));
//		} catch (IOException e) {
//			log.error("get token error message:" + e.getMessage() , e); 
//			e.printStackTrace();
//		}
//		return gt;
//	}
//	public class GeneralToken {
//		private String expires_in; //成功有效时间
//		private String access_token;  // 普通Token
//		private String errcode; //失败ID
//		private String errmsg; //失败消息
//		//get set 忽略
//		public String getExpires_in() {
//			return expires_in;
//		}
//		public void setExpires_in(String expires_in) {
//			this.expires_in = expires_in;
//		}
//		public String getAccess_token() {
//			return access_token;
//		}
//		public void setAccess_token(String access_token) {
//			this.access_token = access_token;
//		}
//		public String getErrcode() {
//			return errcode;
//		}
//		public void setErrcode(String errcode) {
//			this.errcode = errcode;
//		}
//		public String getErrmsg() {
//			return errmsg;
//		}
//		public void setErrmsg(String errmsg) {
//			this.errmsg = errmsg;
//		}
//	}
//}
