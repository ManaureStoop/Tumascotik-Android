package com.arawaney.tumascotik.client.model;

public class User {


	private long id;
	private long system_id;
	private String username;
	private String password;
	private String name;
	private String lastname;
	private Integer cedula;
	private String address;
	private String email;
	private String gender;
	private Integer mobile_telephone;
	private String house_telephone;
	private Integer admin;
	
	public long getId(){
		return this.id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getSystemId(){
		return this.system_id;
	}
	
	public void setSystemId(long system_id){
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getMobile_telephone() {
		return mobile_telephone;
	}

	public void setMobile_telephone(Integer mobile_telephone) {
		this.mobile_telephone = mobile_telephone;
	}

	public String getHouse_telephone() {
		return house_telephone;
	}

	public void setHouse_telephone(String house_telephone) {
		this.house_telephone = house_telephone;
	}

	public Integer getisAdmin() {
		return admin;
	}

	public void setAdmin(Integer  admin) {
		this.admin = admin;
	}

}
