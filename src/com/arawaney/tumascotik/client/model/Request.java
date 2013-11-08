package com.arawaney.tumascotik.client.model;

import java.util.Calendar;

public class Request {
	
	private long id;
	private String system_id;
	private Calendar start_date;
	private Calendar finish_date;
	private String service;
	private Integer price;
	private String comment;
	private String status;
	private Integer delivery;
	private Integer is_appointment;
	private Integer active;
	private Pet pet;
	
	
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
	public Calendar getStart_date() {
		return start_date;
	}
	public void setStart_date(Calendar start_date) {
		this.start_date = start_date;
	}
	public Calendar getFinisch_date() {
		return finish_date;
	}
	public void setFinisch_date(Calendar finisch_date) {
		this.finish_date = finisch_date;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer isDelivery() {
		return delivery;
	}
	public void setDelivery(Integer delivery) {
		this.delivery = delivery;
	}
	public Integer Is_appointment() {
		return is_appointment;
	}
	public void setIs_appointment(Integer is_appointment) {
		this.is_appointment = is_appointment;
	}
	public Integer isActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Pet getPet() {
		return pet;
	}
	public void setPet(Pet pet) {
		this.pet = pet;
	}

}
