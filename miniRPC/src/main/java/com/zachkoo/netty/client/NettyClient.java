package com.zachkoo.netty.client;


import com.alibaba.fastjson.JSONObject;
import com.zachkoo.netty.handler.SimpleClientHandler;
import com.zachkoo.netty.param.ClientRequest;
import com.zachkoo.netty.param.Response;
import com.zachkoo.netty.param.ResultFuture;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
public class NettyClient {
	
	 static ChannelFuture f = null;
	 
	 static {
		 String host = "localhost";
		 int port = 8081;
		 
		 EventLoopGroup workerGroup = new NioEventLoopGroup();
         try {
        	Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	//入站处理器（InboundHandler）
                   ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                   ch.pipeline().addLast(new StringDecoder());
                   ch.pipeline().addLast(new SimpleClientHandler());
                   // 出站处理器（OutboundHandler）
               	   ch.pipeline().addLast(new StringEncoder());
                }
            });
            
			f = b.connect(host, port).sync();
			
		 } catch (InterruptedException e) {
			e.printStackTrace();
		 }
	 }
	 
	 // 注意：1. 每一个请求都是同一个连接，并发问题
	 // 发送数据
	 public static Response send(ClientRequest request) {
		 
		f.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
		ResultFuture future = new ResultFuture(request);
		return future.get();
		 
	 }
}
