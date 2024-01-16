package com.zachkoo.miniRPC;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zachkoo.netty.client.NettyClient;
import com.zachkoo.netty.param.ClientRequest;
import com.zachkoo.netty.param.Response;
import com.zachkoo.user.bean.User;

public class TestTcp {
	
	@Test
	public void testSaveUsers() {
		ClientRequest request = new ClientRequest();
		List<User> users = new ArrayList<User>();
		User u1 = new User();
		u1.setId(1);
		u1.setName("AAA");
		User u2 = new User();
		u2.setId(2);
		u2.setName("BBB");
		User u3 = new User();
		u3.setId(3);
		u3.setName("CCC");
		users.add(u1);
		users.add(u2);
		users.add(u3);
		request.setCommand("com.zachkoo.user.controller.UserController.saveUsers");
		request.setContent(users);
		Response response = NettyClient.send(request);
		System.out.println(response.getResult());
	} 

}
