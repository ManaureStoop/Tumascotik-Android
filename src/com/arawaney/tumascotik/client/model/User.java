package com.arawaney.tumascotik.client.model;

import java.util.Calendar;

public class User {


	private long id;
	private String system_id;
	private String username;
	private String password;
	private String name;
	private String lastname;
	private Integer cedula;
	private String address;
	private String email;
	private int gender;
	private Long mobile_telephone;
	private Long house_telephone;
	private Calendar updated_at;
	private Integer admin;
	
	public final static int GENDER_MAN= 1;
	public final static int GENDER_WOMAN = 2;
	
	public User(User user) {
		id = user.getId();
		system_id = user.getSystemId();
		username = user.getUsername();
		password = user.getPassword();
		name = user.getName();
		lastname = user.getLastname();
		cedula = user.getCedula();
		address = user.getAddress();
		email = user.getEmail();
		gender = user.getGender();
		mobile_telephone = user.getMobile_telephone();
		house_telephone = user.getHouse_telephone();
		admin = user.getisAdmin();
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getSystemId(){
		return this.system_id;
	}
	
	public void setSystemId(String system_id){
		this.system_id = system_id;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getCedula() {
		return cedula;
	}

	public void setCedula(Integer cedula) {
		this.cedula = cedula;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String mail) {
		this.email = mail;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Long getMobile_telephone() {
		return mobile_telephone;
	}

	public void setMobile_telephone(Long mobile_telephone) {
		this.mobile_telephone = mobile_telephone;
	}

	public Long getHouse_telephone() {
		return house_telephone;
	}

	public void setHouse_telephone(Long house_telephone) {
		this.house_telephone = house_telephone;
	}

	public Integer getisAdmin() {
		return admin;
	}

	public void setAdmin(Integer  admin) {
		this.admin = admin;
	}

	public Calendar getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Calendar updated_at) {
		this.updated_at = updated_at;
	}

}
