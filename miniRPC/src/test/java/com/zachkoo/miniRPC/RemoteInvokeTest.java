package com.zachkoo.miniRPC;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zachkoo.netty.annotation.RemoteInvoke;
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
		User u1 = new User();
		u1.setId(1);
		u1.setName("AAA");
		userRemote.saveUser(u1);
	}
	
	@Test
	public void testSaveUsers(){
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
		userRemote.saveUsers(users);
	}
}
