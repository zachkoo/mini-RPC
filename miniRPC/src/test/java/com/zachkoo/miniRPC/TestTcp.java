package com.zachkoo.miniRPC;

import org.junit.Test;

import com.zachkoo.netty.client.NettyClient;
import com.zachkoo.netty.param.ClientRequest;
import com.zachkoo.netty.param.Response;
import com.zachkoo.user.bean.User;

public class TestTcp {
	@Test
	public void testGetResponse() {
		ClientRequest request = new ClientRequest();
		request.setContent("测试TCP长连接请求");
		Response response = NettyClient.send(request);
		System.out.println(response.getResult());
	}
	
	@Test
	public void saveUser() {
		ClientRequest request = new ClientRequest();
		User user = new User();
		user.setId(1);
		user.setName("zach");
		request.setCommand("com.zachkoo.user.controller.UserController.saveUser");
		request.setContent(user);
		Response response = NettyClient.send(request);
		System.out.println(response.getResult());
	} 

}
