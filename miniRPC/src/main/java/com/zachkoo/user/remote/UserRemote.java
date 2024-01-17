package com.zachkoo.user.remote;

import java.util.List;

import com.zachkoo.netty.param.Response;
import com.zachkoo.user.bean.User;

public interface UserRemote {
	public Response saveUser(User user);
	public Response saveUserList(List<User> userlist);
}
