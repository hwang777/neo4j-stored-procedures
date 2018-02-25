package com.horne.cdbg.neo4j.model;

public class ProgramListResponse  extends DefaultResponse{
	private java.util.List<Program> program = new java.util.ArrayList<Program>();
	public java.util.List<Program> getProgram() {
		return program;
	}

	@Override
	public int getTotalCount()
	{
		return program.size();
	}
}
