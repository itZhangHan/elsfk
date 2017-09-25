//package com.blog.common;
//
//import java.beans.BeanInfo;
//import java.beans.IntrospectionException;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.net.ConnectException;
//import java.net.InetAddress;
//import java.net.URL;
//import java.net.UnknownHostException;
//import java.security.KeyStore;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import javax.json.JsonObject;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.servlet.http.HttpServletRequest;
//
//import org.dom4j.io.SAXReader;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//public class WxUtils {
//	/**
//	 * 
//	 * @param requestUrl
//	 *            接口地址
//	 * @param requestMethod
//	 *            请求方法：POST、GET...
//	 * @param output
//	 *            接口入参
//	 * @param needCert
//	 *            是否需要数字证书
//	 * @return
//	 */
//	private static StringBuffer httpsRequest(String requestUrl, String requestMethod, String output, boolean needCert)
//			throws Exception {
//
//		URL url = new URL(requestUrl);
//		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//
//		// 是否需要数字证书
//		if (needCert) {
//			// 设置数字证书
//			setCert(connection);
//		}
//		connection.setDoOutput(true);
//		connection.setDoInput(true);
//		connection.setUseCaches(false);
//		connection.setRequestMethod(requestMethod);
//		if (null != output) {
//			OutputStream outputStream = connection.getOutputStream();
//			outputStream.write(output.getBytes("UTF-8"));
//			outputStream.close();
//		}
//
//		// 从输入流读取返回内容
//		InputStream inputStream = connection.getInputStream();
//		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//		String str = null;
//		StringBuffer buffer = new StringBuffer();
//		while ((str = bufferedReader.readLine()) != null) {
//			buffer.append(str);
//		}
//
//		bufferedReader.close();
//		inputStreamReader.close();
//		inputStream.close();
//		inputStream = null;
//		connection.disconnect();
//		return buffer;
//	}
//
//	/**
//	 * 给HttpsURLConnection设置数字证书
//	 * 
//	 * @param connection
//	 * @throws IOException
//	 */
//	private static void setCert(HttpsURLConnection connection) throws IOException {
//		FileInputStream instream = null;
//		try {
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			// 读取本机存放的PKCS12证书文件
//			instream = new FileInputStream(new File("certPath")); // certPath:数字证书路径
//
//			// 指定PKCS12的密码(商户ID)
//			keyStore.load(instream, "商户ID".toCharArray());
//			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "商户ID".toCharArray()).build();
//			// 指定TLS版本
//			SSLSocketFactory ssf = sslcontext.getSocketFactory();
//			connection.setSSLSocketFactory(ssf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			instream.close();
//		}
//	}
//
//	/**
//	 * 如果返回JSON数据包,转换为 JSONObject
//	 * 
//	 * @param requestUrl
//	 * @param requestMethod
//	 * @param outputStr
//	 * @param needCert
//	 * @return
//	 */
//	public static JsonObject httpsRequestToJsonObject(String requestUrl, String requestMethod, String outputStr,
//			boolean needCert) {
//		JsonObject jsonObject = null;
//		try {
//			StringBuffer buffer = httpsRequest(requestUrl, requestMethod, outputStr, needCert);
//			jsonObject = ((Object) jsonObject).fromObject(buffer.toString());
//		} catch (ConnectException ce) {
//			log.error("连接超时：" + ce.getMessage());
//		} catch (Exception e) {
//			log.error("https请求异常：" + e.getMessage());
//		}
//
//		return jsonObject;
//	}
//
//	/**
//	 * 如果返回xml数据包，转换为Map<String, String>
//	 * 
//	 * @param requestUrl
//	 * @param requestMethod
//	 * @param outputStr
//	 * @param needCert
//	 * @return
//	 */
//	public static Map<String, String> httpsRequestToXML(String requestUrl, String requestMethod, String outputStr,
//			boolean needCert) {
//		Map<String, String> result = new HashMap<>();
//		try {
//			StringBuffer buffer = httpsRequest(requestUrl, requestMethod, outputStr, needCert);
//			result = parseXml(buffer.toString());
//		} catch (ConnectException ce) {
//			log.error("连接超时：" + ce.getMessage());
//		} catch (Exception e) {
//			log.error("https请求异常：" + e.getMessage());
//		}
//		return result;
//	}
//
//	/**
//	 * xml转为map
//	 * 
//	 * @param xml
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String, String> parseXml(String xml) {
//		Map<String, String> map = new HashMap<String, String>();
//		try {
//			Document document = DocumentHelper.parseText(xml);
//
//			Element root = document.getRootElement();
//			List<Element> elementList = root.elements();
//
//			for (Element e : elementList) {
//				map.put(e.getName(), e.getText());
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return map;
//	}
//
//	public static Map<Object, Object> parseXml(HttpServletRequest request) {
//		// 解析结果存储在HashMap
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		try {
//			InputStream inputStream;
//
//			inputStream = request.getInputStream();
//
//			// 读取输入流
//			SAXReader reader = new SAXReader();
//			Document document = (Document) reader.read(inputStream);
//			System.out.println(document);
//			// 得到xml根元素
//			Element root = document.getRootElement();
//			// 得到根元素的所有子节点
//			List<Element> elementList = root.elements();
//
//			// 遍历所有子节点
//			for (Element e : elementList)
//				map.put(e.getName(), e.getText());
//
//			// 释放资源
//			inputStream.close();
//			inputStream = null;
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return map;
//	}
//
//	/**
//	 * 将一个 JavaBean 对象转化为一个 Map
//	 * 
//	 * @param bean
//	 *            要转化的JavaBean 对象
//	 * @return 转化出来的 Map 对象
//	 * @throws IntrospectionException
//	 *             如果分析类属性失败
//	 * @throws IllegalAccessException
//	 *             如果实例化 JavaBean 失败
//	 * @throws InvocationTargetException
//	 *             如果调用属性的 setter 方法失败
//	 */
//	@SuppressWarnings({ "rawtypes" })
//	public static SortedMap<Object, Object> convertBean(Object bean) {
//		SortedMap<Object, Object> returnMap = new TreeMap<Object, Object>();
//		try {
//			Class type = bean.getClass();
//			BeanInfo beanInfo = Introspector.getBeanInfo(type);
//			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//			for (int i = 0; i < propertyDescriptors.length; i++) {
//				PropertyDescriptor descriptor = propertyDescriptors[i];
//				String propertyName = descriptor.getName();
//				if (!propertyName.equals("class")) {
//					Method readMethod = descriptor.getReadMethod();
//					Object result = readMethod.invoke(bean, new Object[0]);
//					if (result != null) {
//						returnMap.put(propertyName, result);
//					} else {
//						returnMap.put(propertyName, "");
//					}
//				}
//			}
//		} catch (IntrospectionException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		return returnMap;
//	}
//
//	/**
//	 * 生成签名
//	 * 
//	 * @param parameters
//	 * @return
//	 */
//	public static String createSgin(SortedMap<Object, Object> parameters) {
//		StringBuffer sb = new StringBuffer();
//		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
//		Iterator it = es.iterator();
//		while (it.hasNext()) {
//			Map.Entry entry = (Map.Entry) it.next();
//			String k = (String) entry.getKey();
//			Object v = entry.getValue();
//			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
//				sb.append(k + "=" + v + "&");
//			}
//		}
//		sb.append("key=" + wxConfig.getWxKey());
//		String sgin = MD5.sign(sb.toString());
//		return sgin;
//	}
//
//	/**
//	 * 获取ip地址
//	 * 
//	 * @param request
//	 * @return
//	 */
//	public static String getIpAddr(HttpServletRequest request) {
//		InetAddress addr = null;
//		try {
//			addr = InetAddress.getLocalHost();
//		} catch (UnknownHostException e) {
//			return request.getRemoteAddr();
//		}
//		byte[] ipAddr = addr.getAddress();
//		String ipAddrStr = "";
//		for (int i = 0; i < ipAddr.length; i++) {
//			if (i > 0) {
//				ipAddrStr += ".";
//			}
//			ipAddrStr += ipAddr[i] & 0xFF;
//		}
//		return ipAddrStr;
//	}
//
//	/**
//	 * 获得指定长度的随机字符串
//	 * 
//	 * @author Administrator
//	 *
//	 */
//	public class StringWidthWeightRandom {
//		private int length = 32;
//		private char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
//				'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
//				'A', 'B', 'V', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
//				'V', 'W', 'X', 'Y', 'Z' };
//		private Random random = new Random();
//
//		// 参数为生成的字符串的长度，根据给定的char集合生成字符串
//		public String getNextString(int length) {
//
//			char[] data = new char[length];
//
//			for (int i = 0; i < length; i++) {
//				int index = random.nextInt(chars.length);
//				data[i] = chars[index];
//			}
//			String s = new String(data);
//			return s;
//		}
//
//		public int getLength() {
//			return length;
//		}
//
//		public void setLength(int length) {
//			this.length = length;
//		}
//	}
//
//}
