package com.horne.cdbg.neo4j.model;

import java.util.Map;

public class TaskListResponse  extends DefaultResponse{
	private java.util.List<Task> task = new java.util.ArrayList<Task>();
	public java.util.List<Task> getTask() {
		return task;
	}

	@Override
	public int getTotalCount()
	{
		return task.size();
	}
	public class Task
	{
	    public Task( Map<String, Object> rtn)
	    {
	    	Object o = rtn.get("ProgramID");
	    	programId = o == null?null:new Long(o.toString());
	    	
	    	o = rtn.get("ProgramName");
	    	programName = o == null?"":o.toString(); 
	    	
	    	o = rtn.get("CaseID");
	    	caseId = new Long(o.toString()); 
	    	
	    	o = rtn.get("CaseTextID");
	    	caseTextId = o == null?null:o.toString(); 
	    	
	    	o = rtn.get("CaseStatus");
	    	//caseStatus = o == null?null:o.toString(); 
	    	
	    	o = rtn.get("ProgramPhaseID");
	    	programPhaseId = o == null?null:new Long(o.toString()); 
	    	
	    	o = rtn.get("PhaseName");
	    	phaseName = o == null?null:o.toString();	    	
	    	
	    	o = rtn.get("CasePhaseID");
	    	casePhaseId = o == null?null:new Long(o.toString()); 
	    	
	    	//o = rtn.get("CasePhaseName");
	    	//casePhaseName = o == null?null:o.toString(); 
	    	
	    	o = rtn.get("TaskID");
	    	taskId = o == null?null:new Long(o.toString());
	    	
	    	o = rtn.get("TaskName");
	    	taskName = o == null?null:o.toString();
	    	
	    	o = rtn.get("AppianTaskStatus");
	    	appianTaskStatus = o == null?null:o.toString();
	    	
	    	o = rtn.get("AppianTaskId");
	    	appianTaskId = o == null?null:o.toString();
	    	
	    	o = rtn.get("AppianTaskAssignee");
	    	appianTaskAssignee = o == null?null:o.toString();
	    	
	    	o = rtn.get("City");
	    	city = o == null?null:o.toString();
	    	
	    	o = rtn.get("County");
	    	county = o == null?null:o.toString();
	    	
	    	//o = rtn.get("ApplicantFirstName");
	    	//applicantFirstName = o == null?null:o.toString();
	    	
	    	//o = rtn.get("ApplicantLastName");
	    	//applicantLastName = o == null?null:o.toString();
	    	
	    	o = rtn.get("AppianCreateDate");
	    	appianCreateDate = o == null?null:o.toString();
	    	
	    	o = rtn.get("role");
	    	role = o == null?null:o.toString();
	    	
	    }
		public Long
		taskId,
		programId,
		programPhaseId,
		caseId,
		casePhaseId

		;
		public String 
		taskName,
		programName,
		phaseName,
		caseTextId,
		//casePhaseName,
		//applicantFirstName,
		//applicantLastName,
		city,
		county,
		appianTaskId,
		appianTaskStatus,
		appianTaskAssignee,
		appianCreateDate,
		role;

	}

}
