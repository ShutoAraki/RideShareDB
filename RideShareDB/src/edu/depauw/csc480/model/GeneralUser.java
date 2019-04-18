package edu.depauw.csc480.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class GeneralUser {
	@Id
	private int userId;
	
	@Basic
	private String username;
	
	@Basic
	private String password;
	
	@Basic
	private String email;
	
	@Basic
	protected double avgRating;
	
	@Basic
	private double latitude;
	
	@Basic
	private double longitude;
	
	@Basic
	private String venmoId;
	
	@OneToMany(mappedBy="rider")
	private Collection<Request> requests;
	
	public GeneralUser() {}

	public GeneralUser(int userId, String username, String password,
					   String email, double avgRating, double latitude, double longitude, String venmoId) {
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
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void updateAvgRating(double newRating) {
		Collection<Double> ratings = new ArrayList<Double>();
		// Obtain the user's past ratings from his/her past completed requests
		for (Request req : requests) {
			if (req instanceof CompletedRequest) {
				ratings.add(((CompletedRequest) req).getRiderRating());
			}
		}
		ratings.add(newRating);
		// Calculate the average of the ratings
		double sum = 0.0;
		for (double rate : ratings) {
			sum += rate;
		}
		// Finally update the average
		this.avgRating = sum / ratings.size();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getVenmoId() {
		return venmoId;
	}

	public void setVenmoId(String venmoId) {
		this.venmoId = venmoId;
	}
	
	public Collection<Request> getRequests() {
		return requests;
	}
	
	public DriverUser becomeDriver(String licensePlate, String birthdate, double pricePerMile) {
		DriverUser driver = new DriverUser(userId + 100000000, username, password, email, avgRating, latitude, longitude, venmoId, licensePlate, birthdate, pricePerMile);
		return driver;
	}
}
