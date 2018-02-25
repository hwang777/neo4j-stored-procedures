package com.horne.cdbg.neo4j.model;

import java.util.Map;

public class Program {
	private Long id, budgetAmount, budgetSpentAmmount;
	private String name, programType,eventName,eventType, eventDate, location,eligibleCensusTract;
	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setBudgetAmount(Long budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

	public void setBudgetSpentAmmount(Long budgetSpentAmmount) {
		this.budgetSpentAmmount = budgetSpentAmmount;
	}

	public Program(){}

    public Program( Map<String, Object> rtn)
    {
    	Object o = rtn.get("ProgramID");
    	id = o == null?null:new Long(o.toString());
    	
    	o = rtn.get("ProgramName");
    	name = o == null?null:o.toString(); 
    	
    	o = rtn.get("EventName");
    	this.eventName = o == null?null:o.toString(); 
    	
    	o = rtn.get("EventType");
    	this.eventType = o == null?null:o.toString(); 
    	
    	o = rtn.get("EventDate");
    	this.eventDate = o == null?null:o.toString(); 
    	
    	o = rtn.get("ProgramType");
    	this.programType = o == null?null:o.toString(); 
    	
    	o = rtn.get("ProgramLocation");
    	this.location = o == null?null:o.toString(); 
    	
    	o = rtn.get("Budget");
    	this.budgetAmount = o == null?null:new Long(o.toString());
    	
    	o = rtn.get("ActualSpending");
    	this.budgetSpentAmmount = o == null?null:new Long(o.toString());
    	
    }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getBudgetAmount() {
		return budgetAmount;
	}
	public void setBudgetAmount(long budgetAmount) {
		this.budgetAmount = budgetAmount;
	}
	public Long getBudgetSpentAmmount() {
		return budgetSpentAmmount;
	}
	public void setBudgetSpentAmmount(long budgetSpentAmmount) {
		this.budgetSpentAmmount = budgetSpentAmmount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEligibleCensusTract() {
		return eligibleCensusTract;
	}
	public void setEligibleCensusTract(String eligibleCensusTract) {
		this.eligibleCensusTract = eligibleCensusTract;
	}
}
