package com.horne.cdbg.neo4j;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import com.horne.cdbg.neo4j.model.*;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import static com.horne.cdbg.neo4j.Util.rb;;
/**
 */
public class PeogramFunctions
{

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    
    @UserFunction(value = "horne.getProgram")
    @Description("Return Program Details")
    public String getProgram(
    		@Name("request") String request
    ) throws JsonGenerationException, JsonMappingException, IOException
    {
    	log.info(request);
    	ObjectMapper mapper = new ObjectMapper();
    	GetProgramRequest req = mapper.readValue(request.getBytes(), GetProgramRequest.class);
    	System.out.println("Request: " + mapper.writeValueAsString(req));
    	String rtn = "";
        try {           
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        	ProgramDetails res = getProgramDetail(req.getProgramId()+"");
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
    	log.debug(rtn);
    	return mapper.writeValueAsString(rtn);
    }

    private ProgramDetails getProgramDetail(String programId)
    {
    	System.out.println("Enter getProgramDetail");
    	String strr = "Match(program:Program)<--(event:Event) where id(program) = {0} Optional Match (program)-[:HAS_CONTRACT_BUDGET]->(budget:Budget) Optional Match (program)-[:HAS_ACTUAL_BUDGET]->(spent:Budget) Optional Match (program)-[:HAS_AGENCY]->(fagency) Optional Match (program)-[:HAS_STATE_AGENCY]->(sagency) Return id(event) as eventID, event.name as EventName, event.Type as EventType, id(program) as ProgramID, program.name as ProgramName, program.Type as ProgramType, program.State as ProgramState, program.EligibleCensusTract as EligibleCensusTract, program.ActionPlanDocId as ActionPlanDocId, program.ContractDocId as ContractDocId, id(fagency) as FederalAgencyId, fagency.name as FederalAgencyName , id(sagency) as StateAgencyId, sagency.name as StateAgencyName, event.EventDate as EventDate, budget.MaxValue as budgetAmount, spent.Value as budgetSpentAmount";
    	strr = java.text.MessageFormat.format(strr, programId);
    	System.out.println(strr);
    	org.neo4j.graphdb.Result result = db.execute(strr);
    	
    	return (result.hasNext()? new ProgramDetails(result.next()) : null);
    }
    
    

    final static private String QUERY_PROGRAM_LIST = "Match (event:Event)-->(program:Program) "
    		+ " WITH event, program Optional Match(program)-[:HAS_CONTRACT_BUDGET]->(b1:Budget), (program)-[:HAS_ACTUAL_BUDGET]->(b2:Budget) "
    		+ "Return id(event) as eventID, event.name as EventName, event.EventDate as EventDate, event.Type as EventType, id(program) as ProgramID, program.name as ProgramName, "
    		+ "program.Type as ProgramType, program.State as ProgramLocation, b1.MaxValue as Budget, b2.Value as ActualSpending";

    
    @UserFunction(value = "horne.getProgramList")
    @Description("Return Program List")
    public String getProgramList() throws JsonGenerationException, JsonMappingException, IOException
    {
    	ObjectMapper mapper = new ObjectMapper();    	
    	ProgramListResponse res = new ProgramListResponse();
        try {           
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        	res.getProgram().addAll(getPrograms());

         } catch (Exception e) {
        	 ErrorMessage err = new ErrorMessage();
        	 err.setCode("Error");
        	 err.setMessage(e.getMessage());
        	 res.getError().add(err);
             log.error(e.getMessage()); 
         }    	
        String rtn = mapper.writeValueAsString(res);
    	log.debug(rtn);
    	return rtn;
    }

    private java.util.List<Program> getPrograms()
    {
    	java.util.List<Program> rtn = new java.util.ArrayList<Program>();
    	String str = QUERY_PROGRAM_LIST;
    	log.debug(str);
    	org.neo4j.graphdb.Result result = db.execute(str);
    	while(result.hasNext())
    	{
    		rtn.add(new Program(result.next()));
    	}
    	return rtn;
    }    
    
}
