package com.horne.cdbg.neo4j.model;

public class Event {

	private String name, type, eventDate;
	
	public Event(){}
	
	public Event(org.neo4j.graphdb.Node node)
	{
		if (node.hasProperty("name"))
			this.name = (String)node.getProperty("name");
		if (node.hasProperty("Type"))
			this.type = (String)node.getProperty("Type");
		if (node.hasProperty("EventDate"))
			this.eventDate = (String)node.getProperty("EventDate");
	}
	
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
