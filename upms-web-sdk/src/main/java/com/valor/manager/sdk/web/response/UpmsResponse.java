package com.valor.manager.sdk.web.response;


import org.springframework.http.HttpStatus;

public class UpmsResponse<T> {

	private T result;
	private String serverId;
	private long timeMs;
	private String status;
	private String msg;

	public UpmsResponse() {
		super();
	}

	public UpmsResponse(T result) {
		this.result = result;
		this.setStatus(String.valueOf(HttpStatus.OK.value()));
		this.setMsg("success");
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public long getTimeMs() {
		return timeMs;
	}

	public void setTimeMs(long timeMs) {
		this.timeMs = timeMs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "UpmsResponse{" +
				", status='" + status + '\'' +
				", msg='" + msg + '\'' +
				'}';
	}
}
