package com.libertymutual.goforcode.spark.app.models;

import org.javalite.activejdbc.Model;

public class Apartment extends Model {
	
	public Apartment() {}

	public Apartment(int rent, int numberOfBedrooms, Double numberOfBathrooms, int squareFootage, String address, String city,
			String state, int zipCode, boolean isActive) {
			
		setRent(rent);
		setNumberOfBathrooms(numberOfBathrooms);
		setNumberOfBedrooms(numberOfBedrooms);
		setSquareFootage(squareFootage);
		setAddress(address);
		setCity(city);
		setState(state);
		setZipCode(zipCode);
		setIsActive(isActive);
	}

	public int getRent() {
		return getInteger("rent");
	}

	public void setRent(int rent) {
		set("rent", rent);
	}
	
	public int getNumberOfBedrooms() {
		return getInteger("number_of_bedrooms");
	}

	public void setNumberOfBedrooms(int numberOfBedrooms) {
		set("number_of_bedrooms", numberOfBedrooms);
	}

	public double getNumberOfBathrooms() {
		return getInteger("number_of_bathrooms");
	}

	public void setNumberOfBathrooms(double numberOfBathrooms) {
		set("number_of_bathrooms", numberOfBathrooms);
	}

	public long getSquareFootage() {
		return getInteger("square_footage");
	}

	public void setSquareFootage(int squareFootage) {
		set("square_footage",  squareFootage);
	}

	public String getAddress() {
		return getString("address");
	}

	public void setAddress(String address) {
		set("address", address);
	}

	public String getCity() {
		return getString("city");
	}

	public void setCity(String city) {
		set("city", city);
	}

	public String getState() {
		return getString("state");
	}

	public void setState(String state) {
		set("state", state);
	}

	public int getZipCode() {
		return getInteger("zip_code");
	}

	public void setZipCode(int zipCode) {
		set("zip_code", zipCode);
	}
	
	public boolean getIsActive() {
		return getBoolean("is_active");
	}

	public void setIsActive(boolean isActive) {
		set("is_active", isActive);
	}

}
