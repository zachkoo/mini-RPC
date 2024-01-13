package factory;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.RetryPolicy;

public class ZooKeeperFactory {
	
	public static CuratorFramework client;
	
	public static CuratorFramework create() {
		if(client == null) {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
			client.start();
		}
		return client;
	}
	
	public static void main(String[] args) throws Exception {
//		CuratorFramework client = create();
//		client.create().forPath("/netty");
		
	}
}
