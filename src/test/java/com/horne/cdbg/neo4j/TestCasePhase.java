package com.horne.cdbg.neo4j;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.junit.Neo4jRule;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.neo4j.driver.v1.Values.parameters;

public class TestCasePhase
{
    // This rule starts a Neo4j instance for us
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFunction(com.horne.cdbg.neo4j.CasePhaseFunctions.class );

   @Test
    public void testGetCasePhase() throws Throwable
    {
        try(Driver driver = GraphDatabase.driver( neo4j.boltURI() , Config.build()
                .withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig());
            Session session = driver.session() )
        {
       	//Object o = session.run("Create (n:Program{name:\"TestName\", Type:\"TestType\", EventDate:\"TestEventData\"}) return id(n) as ID").next().get(0);
    	String req = "{\"requestedBy\":2322,\"caseId\": 4343,\"programPhaseId\": 2222}";

    	String request = "return horne.getCasePhase({req})";
    	java.util.Map<String, Object> map = new java.util.HashMap<>();
    	map.put("req", req);
    	org.neo4j.driver.v1.Statement st  
    		= new org.neo4j.driver.v1.Statement(request, map);
    	
    	System.out.println(request);        	
    	String rtn = session.run(st).next().get(0).toString();
    	System.out.println(rtn);

        }
    
    }
    //@Test
    public void testPostCasePhase() throws Throwable
    {
        try(Driver driver = GraphDatabase.driver( neo4j.boltURI() , Config.build()
                .withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig());
            Session session = driver.session() )
        {
        	String pre = "Merge (c:Case{CaseID:\"Test-Case-123\"})-[:FILED_FOR]->(p:Program{name:\"Test Program\"})-[:HAS_PHASE]->(phase:Phase{name:\"Intake\"})-[:REQUIRES]->(req:Requirement)"
        			+ " Merge (c)-[:IS_IN_PHASE]->(phase) return id(p) as programId, id(phase) as phaseId, id(req) as reqId, c.CaseID as caseID";
        	Record result  = session.run(pre).next();
        	Long programId = result.get(0).asLong();
        	Long phaseId   = result.get(1).asLong();
        	Long reqId     = result.get(2).asLong();
        	String caseId  = result.get(3).asString();
        	
        	String request = "{\"requestedBy\":2322,\"casePhase\": {\"id\": null,"
        		        + "\"caseId\": \"Test-Case-123\",\"programPhaseId\":"+ phaseId+
        		        ",\"status\": \"In Review\","
        		        +"\"requirementValues\": []}}";
        	String statement =  "call horne.postCasePhase({req})";
        	java.util.Map<String, Object> map = new java.util.HashMap<>();
        	map.put("req", request);
        	System.out.println(session.run(statement, map).next().get(0));

        }
    	
    }
}
