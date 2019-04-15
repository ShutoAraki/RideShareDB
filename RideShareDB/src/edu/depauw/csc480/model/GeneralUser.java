package edu.depauw.csc480.model;

import java.util.Collection;

import edu.depauw.csc480.dao.GeneralUserDAO;

/**
 * GeneralUser table contains information about users (riders and drivers):
 * <ul>
 * 	<li>userId (primary key)</li>
 * 	<li>user name</li>
 * 	<li>password</li>
 * 	<li>email address (it has to be depauw.edu email address)</li>
 * 	<li>average rating</li>
 * 	
 * </ul>
 */
public class GeneralUser {
	private GeneralUserDAO dao;
	private int userId;
	private String username;
	private String password;
	private String email;
	private double avgRating;
	private double latitude;
	private double longitude;
	private String venmoId;
	private Collection<Request> requests;

	public GeneralUser(GeneralUserDAO dao, int userId, String username, String password,
					   String email, double avgRating, double latitude, double longitude, String venmoId) {
		this.dao = dao;
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.avgRating = avgRating;
		this.latitude = latitude;
		this.longitude = longitude;
		this.venmoId = venmoId;
	}
	
	public String toString() {
		return "username: " + username + "\n\temail: " + email + "\n\tAverage Rating: " + avgRating;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		dao.changeUsername(userId, username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		dao.changePassword(userId, password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		dao.changeEmail(userId, email);
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
		dao.changeAvgRating(userId, avgRating);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		dao.changeLatitude(userId, latitude);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		dao.changeLongitude(userId, longitude);
	}

	public String getVenmoId() {
		return venmoId;
	}

	public void setVenmoId(String venmoId) {
		this.venmoId = venmoId;
		dao.changeVenmoId(userId, venmoId);
	}
	
	public Collection<Request> getRequests() {
		if (requests == null) requests = dao.getRequests(userId);
		return requests;
	}
}
