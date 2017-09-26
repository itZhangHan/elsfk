package com.els.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ServerEndpoint("/websocket")
public class WSController {
	// 默认连接数
	private static int onlineCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	// 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static ConcurrentMap<String, Object> webSocketSet = new ConcurrentHashMap<>();
	// ��ĳ���ͻ��˵����ӻỰ����Ҫͨ��������ͻ��˷������
	private Session session;

	/**
	 * ���ӽ����ɹ����õķ���
	 * 
	 * @param session
	 *            ��ѡ�Ĳ���sessionΪ��ĳ���ͻ��˵����ӻỰ����Ҫͨ��������ͻ��˷������
	 */
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		webSocketSet.put("ߴ������", this); // ����set��
		addOnlineCount(); // �������
		System.out.println("�������Ӽ��룡��ǰ��������Ϊ" + getOnlineCount());
	}

	/**
	 * ���ӹرյ��õķ���
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		subOnlineCount(); // �������
		System.out.println("��һ���ӹرգ���ǰ��������Ϊ" + getOnlineCount());
	}

	/**
	 * �յ��ͻ�����Ϣ����õķ���
	 * 
	 * @param message
	 *            �ͻ��˷��͹�������Ϣ
	 * @param session
	 *            ��ѡ�Ĳ���
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("���Կͻ��˵���Ϣ:" + message);
		JSONObject meg = JSONObject.fromObject(message);
		String str = "";
		switch ((String) meg.get("key")) {
		case "test1":
			int[] aaa = { 1, 2, 3, 7, 98 };
			str = JSONArray.fromObject(aaa).toString();
			try {
				this.sendMessage(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "getdata":
			String test = "{'name':'tom','age':16}";
			String str2 = JSONObject.fromObject(test).toString();
			try {
				this.sendMessage(str2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * �������ʱ����
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("�������");
		error.printStackTrace();
	}

	/**
	 * ������������漸��������һ��û����ע�⣬�Ǹ���Լ���Ҫ��ӵķ�����
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
		// this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WSController.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WSController.onlineCount--;
	}
	
	
	@RequestMapping("show")
	public String show() {

		return "ws";
	}

	@RequestMapping("hello")
	public String hello() {

		return "hello";
	}

	@RequestMapping("index")
	public String index() {

		return "index";
	}

	@RequestMapping("login")
	public String login() {

		return "index";
	}
}
