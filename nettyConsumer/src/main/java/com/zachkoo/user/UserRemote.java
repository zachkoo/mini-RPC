package com.zachkoo.user;

import java.util.List;

import com.zachkoo.consumer.param.Response;

public interface UserRemote {
	public Response saveUser(User user);
	public Response saveUserList(List<User> userlist);
}
