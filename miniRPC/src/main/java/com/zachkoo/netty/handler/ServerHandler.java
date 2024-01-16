package com.zachkoo.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.zachkoo.netty.medium.Medium;
import com.zachkoo.netty.param.Response;
import com.zachkoo.netty.param.ServerRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
		
		Medium medium = Medium.newInstance();
		Object result = medium.process(request);
		
		Response response = new Response();
		response.setId(request.getId());
		response.setResult("is ok");
		ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event.state().equals(IdleState.READER_IDLE)) {
				System.out.println("读空闲===");
			}else if(event.state().equals(IdleState.WRITER_IDLE)){
				System.out.println("写空闲===");
			}else if(event.state().equals(IdleState.ALL_IDLE)){
				System.out.println("读写空闲===");
				ctx.channel().writeAndFlush("ping\r\n");
			}
		}
	}
	
	
	
	

}
