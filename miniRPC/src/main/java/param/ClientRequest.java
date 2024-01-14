package param;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
	private long id;
	private Object content;
	private static AtomicLong aid = new AtomicLong(0);
	
	public ClientRequest() {
		id = aid.incrementAndGet();
	}
	
	public long getId() {
		return id;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
}
