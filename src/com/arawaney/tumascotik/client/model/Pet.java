package com.arawaney.tumascotik.client.model;

public class Pet {

	private long id;
	private long system_id;
	private String name;
	private User owner;
	private String comment;
	private String breed;
	private String pet_properties;
	private String specie;
	private String gender;
	private Integer puppy;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSystem_id() {
		return system_id;
	}
	public void setSystem_id(long system_id) {
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
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public String getPet_properties() {
		return pet_properties;
	}
	public void setPet_properties(String pet_properties) {
		this.pet_properties = pet_properties;
	}
	public String getSpecie() {
		return specie;
	}
	public void setSpecie(String specie) {
		this.specie = specie;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer isPuppy() {
		return puppy;
	}
	public void setPuppy(Integer puppy) {
		this.puppy = puppy;
	}
}
