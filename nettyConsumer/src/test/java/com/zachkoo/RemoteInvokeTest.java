package com.zachkoo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.zachkoo.consumer.annotation.RemoteInvoke;
import com.zachkoo.consumer.param.Response;
import com.zachkoo.user.bean.User;
import com.zachkoo.user.remote.UserRemote;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RemoteInvokeTest.class)
@ComponentScan("com.zachkoo")
public class RemoteInvokeTest {
	
	@RemoteInvoke
	public UserRemote userRemote;
	
	@Test
	public void testSaveUser(){
		User user = new User();
		user.setId(1000);
		user.setName("张三");
		Response response = userRemote.saveUser(user);
		System.out.println(JSONObject.toJSONString(response));
	}
	
	@Test
	public void testSaveUsers(){
		List<User> users = new ArrayList<>();
		User u1 = new User();
		u1.setId(1);
		u1.setName("A");
		User u2 = new User();
		u2.setId(2);
		u2.setName("B");
		User u3 = new User();
		u3.setId(3);
		u3.setName("C");
		users.add(u1);
		users.add(u2);
		users.add(u3);
		Response response = userRemote.saveUsers(users);
		System.out.println(JSONObject.toJSONString(response));
	}		


}