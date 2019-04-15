package edu.depauw.csc480.model;
import java.util.Collection;

import edu.depauw.csc480.dao.DriverUserDAO;

public class DriverUser {
	
	private DriverUserDAO dao;
	private GeneralUser user;
	private int userId;
	private String licensePlate;
	private String birthdate;
	private double pricePerMile;
	private Collection<CompletedRequest> compRequests;
	
	public DriverUser(DriverUserDAO dao, GeneralUser user, String licensePlate, String birthdate, double pricePerMile) {
		this.dao = dao;
		this.user = user;
		userId = user.getUserId();
		this.licensePlate = licensePlate;
		this.birthdate = birthdate;
		this.pricePerMile = pricePerMile;
	}
	
	public String toString() {
		return user.toString() + "\n\tLicense Plate: " + licensePlate + "\n\tPrice per Mile: " + pricePerMile;
	}
	
	public String getDriverName() {
		return user.getUsername();
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
		dao.changeLicensePlate(userId, licensePlate);
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
		dao.changeBirthdate(userId, birthdate);
	}

	public double getPricePerMile() {
		return pricePerMile;
	}

	public void setPricePerMile(double pricePerMile) {
		this.pricePerMile = pricePerMile;
		dao.changePricePerMile(userId, pricePerMile);
	}
	
	public Collection<CompletedRequest> getCompletedRequests() {
		if (compRequests == null) {
			System.out.println("Driver ID: " + userId);
			compRequests = dao.getCompletedRequests(userId);
		}
		return compRequests;
	}
	
}
