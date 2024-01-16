package com.zachkoo.netty.handler;


import com.alibaba.fastjson.JSONObject;
import com.zachkoo.netty.param.Response;
import com.zachkoo.netty.param.ResultFuture;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg.toString().equals("ping")) {
			System.out.println("收到读写空闲ping,向服务端发送pong");
			ctx.channel().writeAndFlush("pong\r\n");
			return;
		}
		
		Response response = JSONObject.parseObject(msg.toString(), Response.class);
		// 通过response的ID可以在map中找到对应的Request,并为相应的request设置response,使得调用get()客户端得到结果
		ResultFuture.receive(response);
	}

}
