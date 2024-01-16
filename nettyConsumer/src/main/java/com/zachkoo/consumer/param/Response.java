package com.zachkoo.consumer.param;

public class Response {
	
	private Long id;
	private Object result;
	private String code;// 00000表示成功，其他失败
	private String msg;// 失败的原因
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getcode() {
		return code;
	}
	public void setCode(String status) {
		this.code = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
