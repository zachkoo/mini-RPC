package com.zachkoo.netty.medium;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class InitialMedium implements BeanPostProcessor{

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		if(bean.getClass().isAnnotationPresent(Controller.class)) {
			Method[] methods = bean.getClass().getDeclaredMethods();
			for(Method m: methods) {
				String key = bean.getClass().getName()+"."+m.getName();
				Map<String, BeanMethod> beanMap = Medium.beanMap;
				BeanMethod beanMethod = new BeanMethod();
				beanMethod.setBean(bean);
				beanMethod.setMethod(m);
				beanMap.put(key, beanMethod);
			}
		}
		
		return bean;
	}
	

}
