package com.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
/*
 *  当WebSocket 与新客户端已成功握手完成，通过写入信息到 ChannelGroup 中的 Channel 来通知所有连接的客户端，然后添加新 Channel 到 ChannelGroup
	如果接收到 TextWebSocketFrame，调用 retain() ，
	并将其写、刷新到 ChannelGroup，使所有连接的 WebSocket Channel 都能接收到它。
	和以前一样，retain() 是必需的，因为当 channelRead0（）返回时，TextWebSocketFrame 的引用计数将递减。
	由于所有操作都是异步的，writeAndFlush() 可能会在以后完成，我们不希望它来访问无效的引用。
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception { // (1)
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			if (channel != incoming) {
				channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
			} else {
				channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text()));
			}
		}
	}

	/*
	 * 2.覆盖了 handlerAdded() 事件处理方法。
	 * 每当从服务端收到新的客户端连接时， 客户端的 Channel 存入ChannelGroup
	 * 列表中，并通知列表中的其他客户端 Channel
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
		}
		channels.add(ctx.channel());
		System.out.println("Client:" + incoming.remoteAddress() + "加入");
	}

	/*
	 * 覆盖了 handlerRemoved() 事件处理方法。每当从服务端收到客户端断开时，
	 * 客户端的 Channel 移除 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
		}
		System.out.println("Client:" + incoming.remoteAddress() + "离开");
		channels.remove(ctx.channel());
	}
	/*
	 * 覆盖了 channelRead0() 事件处理方法。
	 * 每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel。
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		Channel incoming = ctx.channel();
		System.out.println("Client:" + incoming.remoteAddress() + "在线");
	}
	//覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
		Channel incoming = ctx.channel();
		System.out.println("Client:" + incoming.remoteAddress() + "掉线");
	}
	//覆盖了 channelInactive() 事件处理方法。服务端监听到客户端不活动
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("Client:" + incoming.remoteAddress() + "异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}

}