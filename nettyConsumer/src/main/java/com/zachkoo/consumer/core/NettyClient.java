package com.zachkoo.consumer.core;


import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;

import com.alibaba.fastjson.JSONObject;
import com.zachkoo.consumer.constant.Constants;
import com.zachkoo.consumer.handler.SimpleClientHandler;
import com.zachkoo.consumer.param.ClientRequest;
import com.zachkoo.consumer.param.Response;
import com.zachkoo.consumer.zookeeper.ServerWatcher;
import com.zachkoo.consumer.zookeeper.ZooKeeperFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
public class NettyClient {
	
	 public static final Bootstrap bootstrap = new Bootstrap();
	 static ChannelFuture f = null;
	 static {
		 String host = "localhost";
		 int port = 8090;
		 EventLoopGroup workerGroup = new NioEventLoopGroup(); 
		 
         try {
            bootstrap.group(workerGroup)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
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
            
			CuratorFramework client = ZooKeeperFactory.getClient();
			List<String> serverPaths = client.getChildren().forPath(Constants.SERVER_PATH);
			System.out.println("serverPaths: " + serverPaths);
			//客户端加上zookeeper监听服务器的变化
			CuratorWatcher watcher = new ServerWatcher();
			client.getChildren().usingWatcher(watcher).forPath(Constants.SERVER_PATH);
			
			for(String path :serverPaths){
				String[] str = path.split("#");
				int weight = Integer.valueOf(str[2]);
				if(weight > 0) {
					for (int w = 0; w <= weight; w++) {
						ChannelManager.realServerPaths.add(str[0]+"#"+str[1]);
						ChannelFuture channnelFuture = bootstrap.connect(str[0], Integer.valueOf(str[1]));
						ChannelManager.addChannel(channnelFuture);
					}
				}else {
					ChannelManager.realServerPaths.add(str[0]+"#"+str[1]);
					ChannelFuture channnelFuture = bootstrap.connect(str[0], Integer.valueOf(str[1]));
					ChannelManager.addChannel(channnelFuture);
				}
			}
			
			if(ChannelManager.realServerPaths.size()>0){
				String[] netMessageArray = ChannelManager.realServerPaths.toArray()[0].toString().split("#");
				host = netMessageArray[0];
				port = Integer.valueOf(netMessageArray[1]);
			}
	
		 } catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 // 发送数据
	 public static Response send(ClientRequest request) {
		
		f = ChannelManager.get(ChannelManager.position);
		f.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
		Long timeout = 60l;
		ResultFuture future = new ResultFuture(request);
		return future.get(timeout);
		 
	 }
 
}
