package com.zachkoo.user.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.zachkoo.user.bean.User;
import com.zachkoo.user.service.UserService;

@Controller
public class UserController {
	
	@Resource
	private UserService userService;
	
	public void saveUser(User user) {
		userService.save(user);

	}
}
