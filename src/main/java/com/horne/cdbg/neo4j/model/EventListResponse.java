package com.horne.cdbg.neo4j.model;

public class EventListResponse extends DefaultResponse{
	private java.util.List<Event> event = new java.util.ArrayList<Event>();
	public java.util.List<Event> getEvent() {
		return event;
	}
	
	@Override
	public int getTotalCount()
	{
		return event.size();
	}
}
