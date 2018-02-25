package com.horne.cdbg.neo4j;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;

import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import com.horne.cdbg.neo4j.model.*;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
/**
 */
public class GetEventList
{

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    final static private String QUERY_PROGRAM_LIST = "Match (event:Event)-->(program:Program)-->(agent:Agency) "
    		+ " WITH event, agent, program Optional Match(program)-[:HAS_CONTRACT_BUDGET]->(b1:Budget), (program)-[:HAS_ACTUAL_BUDGET]->(b2:Budget) "
    		+ "Return id(event) as eventID, event.name as EventName, event.Type as EventType, id(program) as ProgramID, program.name as ProgramName, "
    		+ "program.Type as ProgramType, program.State as ProgramLocation, agent.name as agentName, b1.MaxValue as Budget, b2.Value as ActualSpending";

    
    @UserFunction(value = "horne.getEventList")
    @Description("Return Event List")
    public String getEventList() throws JsonGenerationException, IOException
    {
    	ObjectMapper mapper = new ObjectMapper();    	
    	EventListResponse res = new EventListResponse();
        try {           
            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        	res.getEvent().addAll(getEvents());
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
    
    private java.util.List<Event> getEvents()
    {
    	java.util.List<Event> rtn = new java.util.ArrayList<Event>();
    	//String str = QUERY_AGENCY_LIST;
    	org.neo4j.graphdb.Label label = org.neo4j.graphdb.Label.label("Event");
    	ResourceIterator<Event> rr = db.findNodes(label).map(Event::new);   	
    	while (rr.hasNext())
    	{
    		rtn.add(rr.next());
    	}
    	return rtn;
    }
}
