package com.bg.trans.entity.sd;

public class RequestEntity<T> {
	private RequestHead head;
	private T body;
	public RequestHead getHead() {
		return head;
	}
	public void setHead(RequestHead head) {
		this.head = head;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	
	
}
