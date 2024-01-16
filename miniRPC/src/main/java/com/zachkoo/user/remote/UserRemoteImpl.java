package com.zachkoo.user.remote;

import java.util.List;

import javax.annotation.Resource;

import com.zachkoo.netty.annotation.Remote;
import com.zachkoo.netty.param.Response;
import com.zachkoo.netty.util.ResponseUtil;
import com.zachkoo.user.bean.User;
import com.zachkoo.user.service.UserService;

@Remote
public class UserRemoteImpl implements UserRemote {
	
	@Resource
	private UserService userService;
	
	public Response saveUser(User user) {
		userService.save(user);
		return ResponseUtil.createSuccessResponse(user);
	}
	
	public Response saveUsers(List<User> users) {
		userService.saveList(users);
		return ResponseUtil.createSuccessResponse(users);
	}
}
