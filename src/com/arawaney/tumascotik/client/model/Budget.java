package com.arawaney.tumascotik.client.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Budget {
	
	private long id;
	private String system_id;
	private Calendar updatedAt;
	private Calendar createdAt;
	private Integer total;
	private int status;
	private Integer delivery;
	private Integer active;
	private String userId;
	private List<Service> services;

	public final static int STATUS_CANCELED = 2 ;	
	public final static int STATUS_WORKING = 5;
	public final static int STATUS_IN_PROCESS = 6;
	public final static int STATUS_READY = 7;
	public final static int STATUS_DELIVERED = 8;
	
	public final static int ACTIVE = 1;
	public final static int INACTIVE = 0;
	
	public final static int IS_DELIVERY = 1;
	public final static int IS__NOT_DELIVERY = 0;
	

	
	
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

	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer isDelivery() {
		return delivery;
	}
	public void setDelivery(Integer delivery) {
		this.delivery = delivery;
	}
	public Integer isActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}

	public Calendar getUpdated_at() {
		return updatedAt;
	}
	public void setUpdated_at(Calendar updated_at) {
		this.updatedAt = updated_at;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Service> getServices() {
		return services;
	}
	public void setServices(List<Service> services) {
		this.services = services;
	}
	public void addService(Service service) {
		if (this.services == null) {
			this.services = new ArrayList<Service>();
		}
		this.services.add(service);
	}
	public Calendar getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

}
