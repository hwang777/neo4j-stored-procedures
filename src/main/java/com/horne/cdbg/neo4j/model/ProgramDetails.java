package com.horne.cdbg.neo4j.model;

import java.util.Map;

public class ProgramDetails extends DefaultResponse{
	private String name, programType, eventType, eventName,eventDate, location;

	private String eligibleCensusTract, actionPlanDocId, contractDocId, federalAgencyName, stateAgencyName;

	private long id, eventId, budgetAmount, budgetSpentAmount;
	public ProgramDetails(){}
	
	public ProgramDetails(org.neo4j.graphdb.Node node)
	{
		if (node.hasProperty("name"))
			this.name = (String)node.getProperty("name");
		if (node.hasProperty("Type"))
			this.programType = (String)node.getProperty("Type");
	}
    public ProgramDetails( Map<String, Object> rtn)
    {
    	if(rtn == null) return;
    	Object o = rtn.get("ProgramID");
    	id = o == null?null:new Long(o.toString());    	
    	o = rtn.get("eventID");
    	eventId = o == null?null:new Long(o.toString());
    	
    	eventName = (String)rtn.get("EventName");
    	eventType = (String)rtn.get("EventType");
    	eventType = (String)rtn.get("EventType");
    	name = (String)rtn.get("ProgramName");
    	programType = (String)rtn.get("ProgramType");
    	location = (String)rtn.get("ProgramState");
    	eligibleCensusTract = (String)rtn.get("EligibleCensusTract");
    	actionPlanDocId = (String)rtn.get("ActionPlanDocId");
    			
    	contractDocId = (String)rtn.get("ContractDocId");
    	federalAgencyName = (String)rtn.get("FederalAgencyName");
    			
    	stateAgencyName = (String)rtn.get("StateAgencyName");
    	o = rtn.get("budgetAmount");
    	budgetAmount =  o == null?null:new Long(o.toString());
    	o = rtn.get("budgetSpentAmount");
    	budgetSpentAmount =  o == null?null:new Long(o.toString());
    }
	public String getName() {
		return name;
	}

	@Override
	public int getTotalCount()
	{
		return 1;
	}

}
