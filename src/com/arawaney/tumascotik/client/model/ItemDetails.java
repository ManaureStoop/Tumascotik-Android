package com.arawaney.tumascotik.client.model;

import java.util.Date;

public class ItemDetails {
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setName(String nam) {
		this.name = nam;
	}
	public String getName() {
		return name;
	}
	public void setMotive(String motiv) {
		this.motive = motiv ;
	}
	public String getMotive() {
		return motive;
	}
	public void setInitalDate(Date date) {
		this.initialdate = date;
	}
	public Date getInitialDate() {
		return initialdate;
	}
	public void setFinalDate(Date date) {
		this.finaldate = date;
	}
	public Date getFinalDate() {
		return finaldate;
	}

	public String getRace() {
		return race;
	}
	public void setRace(String rac) {
		this.race = rac;
	}
	
	private String title ;
	private String itemDescription;
	private String time;
	private Date initialdate;
	private Date finaldate;
	private String name;
	private String motive;
	private String race;

	
}
