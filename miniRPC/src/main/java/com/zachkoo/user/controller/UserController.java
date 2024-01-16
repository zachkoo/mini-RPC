package com.zachkoo.user.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.zachkoo.netty.param.Response;
import com.zachkoo.netty.util.ResponseUtil;
import com.zachkoo.user.bean.User;
import com.zachkoo.user.service.UserService;

@Controller
public class UserController {
	
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
