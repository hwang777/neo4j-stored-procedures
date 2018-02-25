package com.horne.cdbg.neo4j;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import org.neo4j.logging.Log;
import org.neo4j.procedure.*;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
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
public class TaskFunctions
{

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;


    @Procedure(value = "horne.postTask", mode = Mode.WRITE)
    @Description("Save Task Info")
    public java.util.stream.Stream<Output> postTask(
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
            DefaultResponse res = postTask(req);
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
    	Output output = new Output();
    	output.response = rtn;
    	java.util.stream.Stream.Builder<Output> builder = java.util.stream.Stream.builder();
    	builder.add(output);
    	return builder.build();
    }

    public class Output {
    	public String response;
    }
    
    @UserFunction(value = "horne.getTaskList")
    @Description("Return Task List")
    public String getTaskList(
    		@Name("request") String request
    ) throws JsonGenerationException, JsonMappingException, IOException
    {
    	log.info("Request: " +request);

    	ObjectMapper mapper = new ObjectMapper();
    	GetTaskListRequest req = mapper.readValue(request.getBytes(), GetTaskListRequest.class);
    	System.out.println("Request: " + mapper.writeValueAsString(req));
    	String rtn = "";
        try {           
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            TaskListResponse res = getTaskList(req);
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

    private TaskListResponse getTaskList(GetTaskListRequest req)
    {
    		String AppianTaskAssignee = req.appianTaskAssignee;
    		String Status = req.status; 
    		String AppianTaskId = req.appianTaskId;
    		Long CasePhaseId = req.casePhaseId;
    		String caseId = req.caseTextId;
    		
    		log.info("AppianTaskAssignee: " +AppianTaskAssignee);
    		log.info("Status: " +Status);
    		log.info("AppianTaskId: " +AppianTaskId);
    		log.info("CasePhaseId: " +CasePhaseId);
    		log.info("caseId: " +caseId);
    	System.out.println("Enter getProgramDetail");
    	String string = "Match (program:Program)<-[:FILED_FOR]-(case:Case)-[:HAS_CASEPHASE]->(casePhase:CasePhase)-[:HAS_TASK]->(task:Task) Match (case)-[:IS_IN_PHASE]->(phase:Phase)where (task.AppianTaskId = {AppianTaskId} OR {AppianTaskId} = '' OR {AppianTaskId} is null) and (task.AppianTaskStatus = {Status} OR {Status} = '' OR {Status} is null) and (task.AppianTaskAssignee={AppianTaskAssignee} or {AppianTaskAssignee} = ''or {AppianTaskAssignee} is null )  and (id(casePhase)={CasePhaseId} or {CasePhaseId} = '' or {CasePhaseId} is null )  and (case.CaseID={caseId} or {caseId} ='' or {caseId} is null )  Optional Match (case)-[r]->(property:Property)-[r1]->(address:Address)-[r2]->(city:City) Optional Match(address)-[r3]->(county:County)   Optional Match (case)-[:HAS_APPLICANT]->(applicant:Person) "
    			+ "Return id(program) as ProgramID, program.name as ProgramName, id(case) as CaseID, case.CaseID as CaseTextID, case.Status as CaseStatus, id(phase) as ProgramPhaseID, phase.name as PhaseName, id(task) as TaskID, task.Name as TaskName, task.AppianTaskStatus as AppianTaskStatus, task.AppianTaskId as AppianTaskId, task.AppianTaskAssignee as AppianTaskAssignee, city.name as City, county.name as County, applicant.FirstName as ApplicantFirstName, applicant.LastName as ApplicantLastName, task.AppianCreateDate as AppianCreateDate, task.Role as role, id(casePhase) as CasePhaseID, casePhase.Name as CasePhaseName";
    	
    	System.out.println(string);
    	log.debug(string);

    	java.util.Map<String, Object> map = new java.util.HashMap<>();
    	map.put("AppianTaskAssignee", AppianTaskAssignee);
    	map.put("Status", Status);
    	map.put("AppianTaskId", AppianTaskId);
    	map.put("CasePhaseId", CasePhaseId);
    	map.put("caseId", caseId);
    	org.neo4j.graphdb.Result result = db.execute(string, map);
    	TaskListResponse rtn = new TaskListResponse();
    	while(result.hasNext())
    	{
    		java.util.Map<String, Object> record = result.next();
    		log.info(record.toString());
    		rtn.getTask().add(rtn.new Task(record));
    	}
    	return rtn;
    }
    
    private DefaultResponse postTask(JsonNode req)
    {
    	
		Long CasePhaseId 			= req.findValue("casePhaseId").getLongValue();
		String requestedBy 			= req.findValue("requestedBy").getTextValue();
		String name 				= req.findValue("name").getTextValue();
		String appianTaskAssignee 	= req.findValue("appianTaskAssignee").getTextValue();
		String appianTaskStatus 	= req.findValue("appianTaskStatus").getTextValue();
		String appianTaskId 		= req.findValue("appianTaskId").getTextValue();
		String appianCreateDate 	= req.findValue("appianCreateDate").getTextValue();
		String role 				= req.findValue("role").getTextValue();
		
		log.info("CasePhaseId: " +CasePhaseId);
		log.info("AppianTaskAssignee: " +appianTaskAssignee);
		log.info("Status: " +appianTaskStatus);
		log.info("AppianTaskId: " +appianTaskId);
		log.info("name: " +name);
		log.info("appianCreateDate: " +appianCreateDate);
		log.info("role: " +role);
		log.info("requestedBy: " +requestedBy);
    	System.out.println("Enter getProgramDetail");
    	String string = "Match (casePhase:CasePhase) where id(casePhase) = {CasePhaseID} "
    			+ "Merge (casePhase)-[:HAS_TASK]->(task:Task{AppianTaskId:{AppianTaskId}}) "
    			+ "set task.Name ={Name}, task.AppianTaskStatus ={AppianTaskStatus}, "
    			+ "task.AppianTaskAssignee = {AppianTaskAssignee}, task.AppianCreateDate = {AppianCreateDate}, "
    			+ "task.Role = {Role} return id(task) as taskID";
    	System.out.println(string);
    	log.debug(string);

    	java.util.Map<String, Object> map = new java.util.HashMap<>();
    	map.put("CasePhaseID", CasePhaseId);
    	map.put("AppianTaskId", appianTaskId);
    	map.put("AppianTaskStatus", appianTaskStatus);
    	map.put("Name", name);
    	map.put("AppianTaskAssignee", appianTaskAssignee);
    	map.put("AppianCreateDate", appianCreateDate);
    	map.put("Role", role);
    	org.neo4j.graphdb.Result result = db.execute(string, map);
    	DefaultResponse rtn = new DefaultResponse();
    	if(result.hasNext())
    	{
    		log.info("Task posted: " + result.next().get(0));
    		rtn.setSuccess("true");
    	}
    	else
    	{
    		rtn.setSuccess("false");
    	}
    	return rtn;
    }

}
