package com.horne.cdbg.neo4j.model;

public class GetTaskListRequest extends AbstractRequest{
	public Long casePhaseId;
	public String status, appianTaskAssignee, appianTaskId, caseTextId;
}
