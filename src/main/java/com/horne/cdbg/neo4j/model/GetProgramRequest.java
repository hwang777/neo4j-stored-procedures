package com.horne.cdbg.neo4j.model;

public class GetProgramRequest extends AbstractRequest{
	private long programId;

	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}
}
