package com.arawaney.tumascotik.client.model;



public class BudgetItemDetails {
	
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}

public void setPrice(int pric) {
	this.price =pric ;
}
public int getPrice() {
	return price;
}

public int getImageNumber() {
	return imageNumber;
}
public void setImageNumber(int imageNumber) {
	this.imageNumber = imageNumber;
}


private String title ;
private int price;
private int imageNumber;


}
