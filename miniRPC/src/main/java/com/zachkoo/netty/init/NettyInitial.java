package com.zachkoo.netty.init;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.zachkoo.netty.constant.Constants;
import com.zachkoo.netty.factory.ZooKeeperFactory;
import com.zachkoo.netty.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class NettyInitial implements ApplicationListener<ContextRefreshedEvent> {
	
	 public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		 try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
			               .option(ChannelOption.SO_BACKLOG, 128)
					       .childOption(ChannelOption.SO_KEEPALIVE, false)
					       .channel(NioServerSocketChannel.class)
					       .childHandler(new ChannelInitializer<SocketChannel>() {
					    	   @Override
					             public void initChannel(SocketChannel ch) throws Exception {  		
					    		     //入站处理器（InboundHandler）
					            	 ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
					            	 ch.pipeline().addLast(new StringDecoder());
					            	 ch.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));					            	 
					                 ch.pipeline().addLast(new ServerHandler());
					                 // 出站处理器（OutboundHandler）
					                 ch.pipeline().addLast(new StringEncoder());
					             }				    	   
					       });
			
			int port = 8081;
            ChannelFuture f = serverBootstrap.bind(port).sync();
            
    		CuratorFramework client = ZooKeeperFactory.getClient();
    		InetAddress netAddress = InetAddress.getLocalHost();
    		client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.SERVER_PATH + netAddress.getHostAddress());
    		
            f.channel().closeFuture().sync();
            
		} catch (Exception e) {
			e.printStackTrace();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}  
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.start();
	}
	 
}
