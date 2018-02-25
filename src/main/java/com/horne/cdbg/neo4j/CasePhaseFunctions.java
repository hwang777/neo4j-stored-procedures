package com.horne.cdbg.neo4j;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Result;

import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import com.horne.cdbg.neo4j.model.*;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import static com.horne.cdbg.neo4j.Util.rb;;
/**
 */
public class CasePhaseFunctions
{

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    @UserFunction(value = "horne.getCasePhase")
    @Description("Return CasePhase List")
    public String getCasePhase(
    		@Name("request") String request
    ) throws JsonGenerationException, JsonMappingException, IOException
    {
    	log.info("Request: " +request);
    	System.out.println("Request: " +request);
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode req = mapper.readTree(request);
    	System.out.println("Request: " + mapper.writeValueAsString(req));
    	String rtn = "";
        try {           
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            GetCasePhaseResponse res = queryCasePhase(req);
        	rtn = mapper.writeValueAsString(res);

         } catch (Throwable e) {
        	 DefaultResponse res = new DefaultResponse();
        	 ErrorMessage err = new ErrorMessage();
        	 err.setCode("Error");
        	 err.setMessage(e.getMessage());
        	 res.getError().add(err);
             log.error(e.getMessage()); 
             res.getError().add(err);
             rtn = mapper.writeValueAsString(res);
         }    	
    	log.info("Response: " + rtn);
    	return rtn;

    }
    @Procedure(value = "horne.postCasePhase", mode = Mode.WRITE)
    @Description("Return CasePhase List")
    public java.util.stream.Stream<Output> postCasePhase(
    		@Name("request") String request
    ) throws JsonGenerationException, JsonMappingException, IOException
    {
    	log.info("Request: " +request);
    	System.out.println("Request: " +request);
    	ObjectMapper mapper = new ObjectMapper();
    	PostCasePhaseRequest req = mapper.readValue(request.getBytes(), PostCasePhaseRequest.class);
    	System.out.println("Request: " + mapper.writeValueAsString(req));
    	String rtn = "";
    	Transaction transaction = db.beginTx();
        try {           
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            DefaultResponse res = saveCasePhase(req);
            if(res.getSuccess().equals("false"))
            {
            	transaction.failure();
            }
            else
            {
            	transaction.success();
            }
        	rtn = mapper.writeValueAsString(res);
         } catch (Throwable e) {
        	 DefaultResponse res = new DefaultResponse();
        	 ErrorMessage err = new ErrorMessage();
        	 err.setCode("Error");
        	 err.setMessage(e.getMessage());
        	 res.getError().add(err);
             log.error(e.getMessage()); 
             res.getError().add(err);
             rtn = mapper.writeValueAsString(res);
             transaction.failure();
         }
        finally{
            transaction.close();
        }
    	log.info("Response: " + rtn);
    	Output output = new Output();
    	output.response = rtn;
    	java.util.stream.Stream.Builder<Output> builder = java.util.stream.Stream.builder();
    	builder.add(output);
    	return builder.build();
    }

    public class Output {
    	public String response;
    }
    
    
    private DefaultResponse saveCasePhase(PostCasePhaseRequest request)
    {
    	DefaultResponse rtn = new DefaultResponse();
    	CasePhase casePhase = request.casePhase;
    	if(casePhase.id == null)
    	{
    		Label label = Label.label("CasePhase");
    		Node casePhaseNode = db.createNode(label);
    		casePhase.id = casePhaseNode.getId();
    	}
    	String query = "Match(Case:Case)  where (Case.CaseID = {caseID})  Match (phase:Phase) where id(phase)= {programPhaseId}  Match (CasePhase:CasePhase) where id(CasePhase) = {CasePhaseID} with Case, phase, CasePhase Merge (CasePhase)-[:IS_FOR_PHASE]-> (phase) Merge (Case)-[:HAS_CASEPHASE]->(CasePhase) set CasePhase.Status = {Status} return id(CasePhase)";
    	java.util.Map<String, Object> map = new java.util.HashMap<>();
    	map.put("caseID", casePhase.caseId);
    	map.put("programPhaseId", casePhase.programPhaseId);
    	map.put("Status", casePhase.status);
    	map.put("CasePhaseID", casePhase.id);    	
    	db.execute(query, map);
    	for(RequirementValue value : casePhase.requirementValues)
    	{
        	if(value.id == null)
        	{
        		Label label = Label.label("RequirementData");
        		Node casePhaseNode = db.createNode(label);
        		value.id = casePhaseNode.getId();
        	}
            String statement = "Match (Requirement:Requirement) where id(Requirement) = {RequirementID} Match (CasePhase:CasePhase) where id(CasePhase) = {CasePhaseID} match (data:RequirementData) where id(data) = {DataID} Merge (CasePhase)-[:HAS_REQUIREMENT_DATA]->(data) Merge (data)-[:IS_FOR_REQUIREMENT]->(Requirement) set data.Value = {DataValue}, data.Status = {DataStatus} "
            		+ ", data.StatusUpdatedDate = {StatusUpdatedDate}, data.VerificationMethod = {VerificationMethod}"
            		+ ", data.AutomationResult = {AutomationResult}, data.CompletedBy = {CompletedBy}, data.CompletedDate = {CompletedDate}"
            		+ "return id(data)";
            map = new java.util.HashMap<>();

        	map.put("RequirementID", value.requirementId);
        	map.put("CasePhaseID", value.casePhaseId);
        	map.put("DataID", value.id);
        	map.put("DataValue", value.requirementValue);
        	map.put("DataStatus", value.status);
        	map.put("StatusUpdatedDate", value.statusUpdatedDate);
        	map.put("VerificationMethod", value.verificationMethod);
        	map.put("AutomationResult", value.automationResult);
        	map.put("CompletedBy", value.completedBy);
        	map.put("CompletedDate", value.completedDate);
        	Result result = db.execute(statement,  map);
        	if(!result.hasNext())
        	{
        		log.warn("Cannot Create Requirement Data with Parameters: " + map.toString());
        		rtn.setSuccess("false");
        		rtn.getError().add(new ErrorMessage("Error", "Cannot Create Requirement Data."));
        	}
    	}
    	return rtn;
    }
    private GetCasePhaseResponse queryCasePhase(JsonNode req)
    {
		String caseId 			= req.findValue("caseId").getTextValue();
		String requestedBy 		= req.findValue("requestedBy").getTextValue();
		String programPhaseId 	= req.findValue("programPhaseId").getTextValue();
		
		log.info("Enter queryCasePhase");
    	log.info("requestedBy: " +requestedBy);
		log.info("caseId: " +caseId);
		log.info("programPhaseId: " +programPhaseId);



    	String string = "Match (phase:Phase)<--(CasePhase:CasePhase)<-[]-(Case:Case)  where (Case.CaseID = {caseID} or {caseID} = '' or {caseID} is null ) and (id(phase) = {programPhaseId} or {programPhaseId} = '' or {programPhaseId} is null) "
    			+ "Return id(Case) as CaseID, Case.CaseID as CaseTextID, id(phase) as PhaseID, id(CasePhase) as CasePhaseID, Case.Status as CaseStatus, CasePhase.Status as CasePhaseStatus";

    	System.out.println(string);
    	log.debug(string);

    	java.util.Map<String, Object> map = new java.util.HashMap<>();
    	map.put("caseID", caseId);
    	map.put("programPhaseId", programPhaseId);
    	org.neo4j.graphdb.Result result = db.execute(string, map);
    	GetCasePhaseResponse rtn = new GetCasePhaseResponse();
    	while(result.hasNext())
    	{
    		CasePhase casePhase = new CasePhase();
    		java.util.Map<String, Object> res = result.next();
    		log.info(res.toString());
    		Object o = res.get("CasePhaseID");
    		//log.info("CasePhaseID: " + o + "-" + o.getClass()+"-"+o.toString());
    		casePhase.id = new Long(o.toString());
    		o = res.get("PhaseID");
     		casePhase.programPhaseId = new Long(o.toString());
     		o = res.get("CaseTextID");
     		casePhase.caseId = o == null ? null : o.toString();
    	    o = res.get("CasePhaseStatus");
     		casePhase.status = o == null ? null : o.toString();
     		rtn.casePhase.add(casePhase);
    	}
    	result.close();
    	log.info("Querying RequirementData");
    	for(CasePhase casePhase: rtn.casePhase)
    	{
    		
	    	string = "Match (Requirement:Requirement)<-[]-(Phase:Phase)<-[]-(CasePhase:CasePhase)<-[]-(case:Case)-[]->(program:Program) where id(CasePhase) = {CasePhaseID} Match (CasePhase)-[]->(RequirementData:RequirementData)-[]->(Requirement) "
	    			+ "Return id(program) as ProgramID, id(Phase) as PhaseID, id(Requirement) as RequirementID, id(case) as CaseID, case.ID as CaseTextID, id(CasePhase) as CasePhaseID, "
	    			+ "id(RequirementData) as RequirementDataID, RequirementData.Value as DataValue, RequirementData.Status as DataStatus, "
		    		+ "RequirementData.StatusUpdatedDate as StatusUpdatedDate, "
		    		+ "RequirementData.VerificationMethod as VerificationMethod,"
		    		+ "RequirementData.AutomationResult as AutomationResult,"
		    		+ "RequirementData.CompletedBy as CompletedBy,"
		    		+ "RequirementData.CompletedDate as CompletedDate";
	    	log.info(string);
	    	map = new java.util.HashMap<>();
	    	map.put("CasePhaseID", casePhase.id);

	    	log.info(map.toString());
	    	result = db.execute(string, map);
	    	while(result.hasNext())
	    	{
	    		RequirementValue item = new RequirementValue();
	    		java.util.Map<String, Object> res = result.next();

		    	log.debug(res.toString());
	    		Object o = null;
	    		o = res.get("ProgramID");
	    		item.programId = o == null ? null : Long.parseLong(o.toString());
	    		o = res.get("PhaseID");
	    		item.casePhaseId = o == null ? null : Long.parseLong(o.toString());
	    		o = res.get("RequirementID");
	    		item.requirementId = o == null ? null : Long.parseLong(o.toString());
	    		//o = res.get("CaseID");
	    		//item.caseId = o == null ? null : Long.getLong(o.toString());
	    		o = res.get("CaseTextID");
	    		item.caseId = o == null ? "" : o.toString();
	    		o = res.get("CasePhaseID");
	    		item.casePhaseId = o == null ? null : Long.parseLong(o.toString());
	    		o = res.get("RequirementDataID");
	    		item.id = o == null ? null : Long.parseLong(o.toString());
	    		o = res.get("DataValue");
	    		item.requirementValue = o == null ? "" : o.toString();
	    		o = res.get("DataStatus");
	    		item.status = o == null ? "" : o.toString();
	    		o = res.get("StatusUpdatedDate");
	    		item.statusUpdatedDate = o == null ? "" : o.toString();
	    		o = res.get("VerificationMethod");
	    		item.verificationMethod = o == null ? "" : o.toString();
	    		o = res.get("AutomationResult");
	    		item.automationResult = o == null ? "" : o.toString();
	    		o = res.get("CompletedBy");
	    		item.completedBy = o == null ? "" : o.toString();
	    		o = res.get("CompletedDate");
	    		item.completedDate = o == null ? "" : o.toString();
	    		casePhase.requirementValues.add(item);
	    	}
	    	result.close();
    	}
    	return rtn;
    }

    class GetCasePhaseResponse extends DefaultResponse
    {
        //public String caseId, requirementValue, status;
        public final java.util.List<CasePhase> casePhase 
        	= new java.util.ArrayList<CasePhase>();
        @Override
        public int getTotalCount()
        {
        	return casePhase.size();
        }
    }
}
