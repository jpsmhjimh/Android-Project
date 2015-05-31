package com.example.thitruong;

public class Sanpham {
	private String name ="";
	private Double[] price ;

	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(Double[] price) {
		//this.price = new String[price.length];
		this.price = price;
	}
	public String  getName() {
		return this.name;
	}
	public Double[] getPrice() {
		return this.price;
	}
}
