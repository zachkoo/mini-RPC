//package com.zachkoo.netty.param;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//import org.apache.zookeeper.data.Id;
//
//public class ResultFuture {
//	
//	public static ConcurrentHashMap<Long, ResultFuture> allResultFuture = new ConcurrentHashMap<Long, ResultFuture>();
//	final Lock lock = new ReentrantLock();
//	private Condition condition = lock.newCondition();
//	private Response response = null;
//	
//	public ResultFuture(ClientRequest request) {		
//		allResultFuture.put(request.getId(), this);
//	}
//	
//	// 主线程获取数据，首先要等待结果
//	public Response get() {
//		lock.lock();
//		try {
//			while (!done()) {
//				condition.await();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			lock.unlock();
//		}
//		return this.response;
//	}
//	
//	public static void receive(Response response) {
//		ResultFuture future = allResultFuture.get(response.getId());
//		if(future!=null) {
//			Lock lock =  future.lock;
//			lock.lock();
//			try {
//				future.setResponse(response);
//				future.condition.signal();
//				allResultFuture.remove(response.getId());	
//			} catch (Exception e) {
//				e.printStackTrace();
//			}finally {
//				lock.unlock();
//			}
//		}
//	}
//	
//	public Response getResponse(){
//		return response;
//	}
//	
//	public void setResponse(Response response){
//		this.response = response;
//	}
//
//	private boolean done() {
//		if(this.response!=null) {
//			return true;
//		}
//		return false;
//	}
//}
