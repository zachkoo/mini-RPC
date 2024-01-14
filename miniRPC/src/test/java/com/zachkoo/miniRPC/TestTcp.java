package com.zachkoo.miniRPC;

import org.junit.Test;

import param.ClientRequest;
import param.Response;
import client.NettyClient;

public class TestTcp {
	@Test
	public void testGetResponse() {
		ClientRequest request = new ClientRequest();
		request.setContent("测试TCP长连接请求");
		Response response = NettyClient.send(request);
		System.out.println(response.getResult());
	}

}
