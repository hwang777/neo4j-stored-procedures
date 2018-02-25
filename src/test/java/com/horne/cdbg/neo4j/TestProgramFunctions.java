package com.horne.cdbg.neo4j;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.junit.Neo4jRule;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.neo4j.driver.v1.Values.parameters;

public class TestProgramFunctions
{
    // This rule starts a Neo4j instance for us
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFunction(com.horne.cdbg.neo4j.PeogramFunctions.class );

   @Test
    public void testGetProgram() throws Throwable
    {
        // In a try-block, to make sure we close the driver and session after the test
        try(Driver driver = GraphDatabase.driver( neo4j.boltURI() , Config.build()
                .withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig());
            Session session = driver.session() )
        {
        	
        	Object o = session.run("Create (n:Program{name:\"TestName\", Type:\"TestType\", EventDate:\"TestEventData\"}) return id(n) as ID").next().get(0);
        	String req = "{\"requestedBy\":1111,\"programId\":"+o+"}\"";
        	String request = "return horne.getProgram({req})";
        	java.util.Map<String, Object> map = new java.util.HashMap<>();
        	map.put("req", req);
        	org.neo4j.driver.v1.Statement st  
        		= new org.neo4j.driver.v1.Statement(request, map);
        	
        	System.out.println(request);        	
        	String rtn = session.run(st).next().get(0).toString();
        	System.out.println(rtn);
        }
    }

   @Test
    public void testGetProgramList() throws Throwable
    {
        // In a try-block, to make sure we close the driver and session after the test
        try(Driver driver = GraphDatabase.driver( neo4j.boltURI() , Config.build()
                .withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig());
            Session session = driver.session() )
        {
        	//session.run("Create (n:Event{name:\"TestName\", Type:\"TestType\", EventDate:\"TestEventData\"})");
        	String rtn = session.run("return horne.getProgramList()").next().get(0).toString();
        	System.out.println(rtn);
        }
    }


}
