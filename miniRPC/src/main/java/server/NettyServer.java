package server;

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

public class NettyServer {
	
	 public static void main(String[] args) {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		 try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(parentGroup, childGroup);
			bootstrap.option(ChannelOption.SO_BACKLOG, 128)
					 .childOption(ChannelOption.SO_KEEPALIVE, false)
					 .channel(NioServerSocketChannel.class)
					 .childHandler(new ChannelInitializer<SocketChannel>() { 
			             @Override
			             public void initChannel(SocketChannel ch) throws Exception {
			            	 ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
			            	 ch.pipeline().addLast(new StringDecoder()); 
			                 ch.pipeline().addLast(new SimpleServerHandler());
			                 ch.pipeline().addLast(new StringEncoder());
			             }
			         });
			
            ChannelFuture f = bootstrap.bind(8080).sync();
            f.channel().closeFuture().sync();
            
		} catch (Exception e) {
			e.printStackTrace();
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}  
		
	}
	 
}
