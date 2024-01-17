package com.zachkoo.consumer.zookeeper;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import com.zachkoo.consumer.core.ChannelManager;
import com.zachkoo.consumer.core.NettyClient;

import io.netty.channel.ChannelFuture;

public class ServerWatcher implements CuratorWatcher {

	@Override
	public void process(WatchedEvent event) throws Exception {
		System.out.println("process------------------------");
		CuratorFramework client = ZooKeeperFactory.getClient();
		String path = event.getPath();
		client.getChildren().usingWatcher(this).forPath(path); // 监视器是一次性的，一旦触发就需要重新设置。
		List<String> newServerPaths = client.getChildren().forPath(path);
		System.out.println("newServerPaths: " + newServerPaths);
		ChannelManager.realServerPaths.clear();
		
		for(String p : newServerPaths){
			String[] str = p.split("#");
			int weight = Integer.valueOf(str[2]);
			if(weight > 0) {
				for (int w = 0; w <= weight; w++) {
					ChannelManager.realServerPaths.add(str[0]+"#"+str[1]);
				}
			}else{
				ChannelManager.realServerPaths.add(str[0]+"#"+str[1]);
			}
		}
		
		ChannelManager.clearChannel();
		
		for(String realServer : ChannelManager.realServerPaths){
			String[] str = realServer.split("#");
			int weight = Integer.valueOf(str[2]);
			if(weight > 0) {
				for (int w = 0; w <= weight; w++) {
					ChannelFuture channnelFuture = NettyClient.bootstrap.connect(str[0], Integer.valueOf(str[1]));
					ChannelManager.addChannel(channnelFuture);		
				}
			}else{
				ChannelFuture channnelFuture = NettyClient.bootstrap.connect(str[0], Integer.valueOf(str[1]));
				ChannelManager.addChannel(channnelFuture);		
			}
		}
	}
}
