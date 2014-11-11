package com.arawaney.tumascotik.client.model;

import java.util.Calendar;

public class SocialNetwork {


	private String system_id;
	private String name;
	private String field;
	private int enabled;
	private Calendar updated_at;


	
	public final static int ENABLED = 1;
	public final static int NOT_ENABLED = 0 ;
	
	public final static String YOUTUBE = "youtube";
	public final static String TWITTER_APP = "twitterapp" ;
	public final static String TWITTER_WEB = "twitterweb" ;
	public final static String FACEBOOK_APP = "facebookapp" ;
	public final static String FACEBOOK_WEB = "facebookweb";
	public final static String PHONE = "phone" ;
	
	
	public SocialNetwork(SocialNetwork socialNetwork) {
		system_id = socialNetwork.getSystemId();
		name = socialNetwork.getName();
		field =socialNetwork.getField();
		enabled = socialNetwork.getEnabled();
	
	}

	public SocialNetwork() {
		// TODO Auto-generated constructor stub
	}

	
	public String getSystemId(){
		return this.system_id;
	}
	
	public void setSystemId(String system_id){
		this.system_id = system_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Calendar updated_at) {
		this.updated_at = updated_at;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	


}
