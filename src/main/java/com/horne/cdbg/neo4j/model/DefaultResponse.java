package com.horne.cdbg.neo4j.model;

public class DefaultResponse {
	protected int totalCount = 0;
	protected String success = "true";
	protected java.util.List<ErrorMessage> error = new java.util.ArrayList<ErrorMessage>();
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public java.util.List<ErrorMessage> getError() {
		return error;
	}
	public void setError(java.util.List<ErrorMessage> error) {
		this.error = error;
	}
	public int getTotalCount() {return 0;}
}
