package com.zachkoo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.zachkoo")
public class SpringServer {
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringServer.class);
		
	}

}
