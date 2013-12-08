package com.arawaney.tumascotik.client.model;

public class Pet {

	private long id;
	private String system_id;
	private String name;
	private User owner;
	private String comment;
	private String breed;
	private String pet_properties;
	private String specie;
	private Integer gender;
	private Integer puppy;
	
	public final static int GENDER_MALE = 1;
	public final static int GENDER_FEMALE = 2;
	
	public final static int AGE_ADULT = 1;
	public final static int AGE_PUPPY = 2;


	
	
	
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
}
