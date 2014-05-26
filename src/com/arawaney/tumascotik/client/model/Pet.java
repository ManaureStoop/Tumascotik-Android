package com.arawaney.tumascotik.client.model;

import java.util.Calendar;

public class Pet {

	private long id;
	private String system_id;
	private String name;
	private User owner;
	private String comment;
	private Breed breed;
	private Integer gender;
	private Integer puppy;
	private Integer agressive;
	private Calendar updated_at;
	//Only to be used when updating pet from Background DB. Not to save on local DB
	private boolean active;

	
	public final static int GENDER_MALE = 1;
	public final static int GENDER_FEMALE = 2;
	
	public final static int AGE_ADULT = 1;
	public final static int AGE_PUPPY = 2;
	
	public final static int NOT_AGRESSIVE = 1;
	public final static int AGRESSIVE = 2;
	


	
	
	
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
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getGender() {
		if (gender == null) {
			return 0;
		}else
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public Integer getPuppy() {
		if (puppy == null) {
			return 0;
		}else
		return puppy;
	}
	public void setPuppy(Integer puppy) {
		this.puppy = puppy;
	}
	public Breed getBreed() {
		return breed;
	}
	public void setBreed(Breed breed) {
		this.breed = breed;
	}
	public Integer getAgressive() {
		return agressive;
	}
	public void setAgressive(Integer agressive) {
		this.agressive = agressive;
	}
	public Calendar getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Calendar updated_at) {
		this.updated_at = updated_at;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
