package com.zachkoo.consumer.core;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelFuture;

public class ChannelManager {
	public static CopyOnWriteArrayList<ChannelFuture>  channelFutures = new CopyOnWriteArrayList<ChannelFuture>();
	public static CopyOnWriteArrayList<String> realServerPaths = new CopyOnWriteArrayList<String>();
	public static AtomicInteger position = new AtomicInteger(0); // 先采用轮询的方式使用send

	public static void removeChannel(ChannelFuture channel){
		channelFutures.remove(channel);
	}
	
	public static void addChannel(ChannelFuture channel){
		channelFutures.add(channel);
	}
	public static void clearChannel(){
		channelFutures.clear();
	}

	public static ChannelFuture get(AtomicInteger i) {
		
		// 目前采用轮循机制
		ChannelFuture channelFuture = null;
		int size = channelFutures.size();
		if(i.get()>=size){
			channelFuture = channelFutures.get(0);
			ChannelManager.position= new AtomicInteger(1);
		}else{
			channelFuture = channelFutures.get(i.getAndIncrement());
		}
		return channelFuture;
	}
	
}
