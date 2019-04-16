package edu.depauw.csc480.model;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class DriverUser extends GeneralUser {
	
	@Basic
	private String licensePlate;
	
	@Basic
	private String birthdate;
	
	@Basic
	private double pricePerMile;
	
	@OneToMany(mappedBy="driver")
	private Collection<CompletedRequest> compRequests;
	
	public DriverUser() {}
	
	public DriverUser(int userId, String username, String password,
			   String email, double avgRating, double latitude, double longitude, 
			   String venmoId, String licensePlate, String birthdate, double pricePerMile) {
		super(userId, username, password, email, avgRating, latitude, longitude, venmoId);
		this.licensePlate = licensePlate;
		this.birthdate = birthdate;
		this.pricePerMile = pricePerMile;
	}
	
	public String toString() {
		return super.toString() + "\n\tLicense Plate: " + licensePlate + "\n\tPrice per Mile: " + pricePerMile;
	}
	
	public String getDriverName() {
		return getUsername();
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public double getPricePerMile() {
		return pricePerMile;
	}

	public void setPricePerMile(double pricePerMile) {
		this.pricePerMile = pricePerMile;
	}
	
	public Collection<CompletedRequest> getCompletedRequests() {
		return compRequests;
	}
	
}
