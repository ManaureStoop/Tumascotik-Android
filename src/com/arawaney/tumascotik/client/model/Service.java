package com.arawaney.tumascotik.client.model;

import java.util.Calendar;

public class Service {

	private long id;
	private String system_id;
	private String name;
	private int needsRequest;
	private int duration;
	private Calendar updated_at;


	public final static int NEED_REQUEST = 1;
	public final static int NOT_NEED_REQUEST = 2;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSystem_id() {
		return system_id;
	}
	public void setSystem_id(String system_id) {
		this.system_id = system_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNeedsRequest() {
		return needsRequest;
	}
	public void setNeedsRequest(int needsRequest) {
		this.needsRequest = needsRequest;
	}
	public Calendar getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Calendar updated_at) {
		this.updated_at = updated_at;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
