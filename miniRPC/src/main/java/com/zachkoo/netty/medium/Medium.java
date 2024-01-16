package com.zachkoo.netty.medium;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zachkoo.netty.param.Response;
import com.zachkoo.netty.param.ServerRequest;

public class Medium {
	
	public static Map<String, BeanMethod> beanMap;
	static {
		beanMap = new HashMap<String, BeanMethod>();
	}
	
	private static Medium medium = null;
	
	
	private Medium(){}
	
	public static Medium newInstance(){
		if(medium == null){
			medium = new Medium();
		}
		return medium;
	}
	
	// 反射处理业务代码
	public Response process(ServerRequest request) {
		Response result = null;
		try {
			String command = request.getCommand();
			BeanMethod beanMethod = beanMap.get(command);
			if(beanMethod == null) {
				return null;
			}
			Object bean = beanMethod.getBean();
			Method method = beanMethod.getMethod();
			Class<?> paramType = method.getParameterTypes()[0]; // 先只实现1个参数的方法
			Object content = request.getContent();
			Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
			result = (Response) method.invoke(bean, args);
			result.setId(request.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
