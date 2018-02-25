package com.horne.cdbg.neo4j.model;


public class CasePhase
{
    public Long id, programPhaseId;

    public String caseId, status;
    public final java.util.List<RequirementValue> requirementValues 
    	= new java.util.ArrayList<RequirementValue>();
}