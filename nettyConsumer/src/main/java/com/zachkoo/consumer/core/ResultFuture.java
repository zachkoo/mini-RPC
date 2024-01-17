package com.zachkoo.consumer.core;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zachkoo.consumer.param.Response;
import com.zachkoo.consumer.param.ClientRequest;

public class ResultFuture {
	
	public static ConcurrentHashMap<Long, ResultFuture> allResultFuture = new ConcurrentHashMap<Long, ResultFuture>();
	final Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Response response = null;
	private Long timeout = 2*60*1000l;
	private Long start = System.currentTimeMillis();
	
	public ResultFuture(ClientRequest request) {		
		allResultFuture.put(request.getId(), this);
	}
	
	// 主线程获取数据，首先要等待结果
	public Response get() {
		lock.lock();
		try {
			while (!done()) {
				condition.await();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return this.response;
	}
	
	public Response get(Long time){
		lock.lock();
		try {
			while(!done()){
				condition.await(time,TimeUnit.MILLISECONDS);
				if((System.currentTimeMillis()-start)>time){
					System.out.println("Future中的请求超时");                                                                                                                                                                                                   
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
			System.out.println(Thread.currentThread().getName() + "get处释放锁!");
		}
		
		return this.response;
		
	}
	
	public static void receive(Response response) {
		ResultFuture future = allResultFuture.get(response.getId());
		if(future!=null) {
			Lock lock =  future.lock;
			lock.lock();
			try {
				future.setResponse(response);
				future.condition.signal();
				allResultFuture.remove(response.getId());	
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
		}
	}
	
	public Response getResponse(){
		return response;
	}
	
	public void setResponse(Response response){
		this.response = response;
	}

	private boolean done() {
		if(this.response!=null) {
			return true;
		}
		return false;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}
	
	//清理线程
	static class ClearFutureThread extends Thread{
		@Override
		public void run() {
			Set<Long> ids = allResultFuture.keySet();
			for(Long id : ids){
				ResultFuture future = allResultFuture.get(id);
				if(future==null){
					allResultFuture.remove(id);
				}else if(future.getTimeout()<(System.currentTimeMillis()-future.getStart()))
				{//链路超时
					Response response = new Response();
					response.setId(id);
					response.setCode("333333");
					response.setMsg("链路请求超时");
					receive(response);
				}
			}
		}
	}
	
	static{
		ClearFutureThread clearThread = new ClearFutureThread();
		clearThread.setDaemon(true);
		clearThread.start();
	}
}
