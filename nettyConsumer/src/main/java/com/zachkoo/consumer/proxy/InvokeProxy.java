package com.zachkoo.consumer.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import com.zachkoo.consumer.core.NettyClient;
import com.zachkoo.consumer.param.Response;
import com.zachkoo.consumer.annotation.RemoteInvoke;
import com.zachkoo.consumer.param.ClientRequest;

@Component
public class InvokeProxy implements BeanPostProcessor {
	
	public static Enhancer enhancer = new Enhancer();

	public Object postProcessBeforeInitialization(Object bean, String arg1) throws BeansException {

		Field[] fields = bean.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(RemoteInvoke.class)){
				field.setAccessible(true);
				
				final Map<Method, Class> methodClassMap = new HashMap<>();
				putMethodClass(methodClassMap, field);
				enhancer.setInterfaces(new Class[]{field.getType()});
				enhancer.setCallback(new MethodInterceptor() {
					@Override
					public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
						ClientRequest request = new ClientRequest();
						request.setContent(args[0]);
//						String command= methodClassMap.get(method).getName()+"."+method.getName();
						String command = method.getName();
						request.setCommand(command);
						
						Response response = NettyClient.send(request);
						return response;
					}
				});
				try {
					field.set(bean, enhancer.create());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return bean;
	}
	
	// 对属性的所有方法和属性接口类型放入到HashMap中
	private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
		Method[] methods = field.getType().getDeclaredMethods();
		for(Method method : methods){
			methodClassMap.put(method, field.getType());
		}
		
	}
	

	public Object postProcessAfterInitialization(Object bean, String arg1) throws BeansException {
		return bean;
	}

}
