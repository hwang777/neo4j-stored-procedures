package com.horne.cdbg.neo4j.model;

public class AbstractRequest {
	private long requestedBy;

	public long getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(long requestedBy) {
		this.requestedBy = requestedBy;
	}
}
