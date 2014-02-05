package com.arawaney.tumascotik.client.model;

import java.util.Calendar;

public class Breed {

	private long id;
	private String system_id;
	private String name;
	private Specie specie;
	private PetPropertie petPropertie;	
	private Calendar updated_at;

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
	public Specie getSpecie() {
		return specie;
	}
	public void setSpecie(Specie specie) {
		this.specie = specie;
	}
	public PetPropertie getPetPropertie() {
		return petPropertie;
	}
	public void setPetPropertie(PetPropertie petPropertie) {
		this.petPropertie = petPropertie;
	}
	public Calendar getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Calendar updated_at) {
		this.updated_at = updated_at;
	}

	
}
