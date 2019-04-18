package edu.depauw.csc480.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
public class CompletedRequest extends Request {
	
	@ManyToOne
	private DriverUser driver;
	
	@Basic
	private double driverRating;
	
	@Basic
	private double riderRating;
	
	@Basic
	private String completion;
	
	public CompletedRequest() {}
	
	public CompletedRequest(int requestId, GeneralUser rider, String requestTime, double fromLat, double fromLon,
			double toLat, double toLon, double price, int groupSize, DriverUser driver, double driverRating,
			double riderRating, String completion) {
		super(requestId, rider, requestTime, fromLat, fromLon, toLat, toLon, price, groupSize);
		this.driver = driver;
		this.driverRating = driverRating;
		this.riderRating = riderRating;
		this.completion = completion;
		rider.updateAvgRating(riderRating);
		driver.updateAvgRating(driverRating);
	}
	
	public String toString() {
		String ret = super.toString();
		ret += "\n\tDriver: " + driver.getDriverName();
		ret += "\n\tCompleted at " + completion;
		ret += "\n\tDriver Rating: " + driverRating;
		ret += "\n\tRider Rating: " + riderRating;
		return ret;
	}

	public double getDriverRating() {
		return driverRating;
	}

	public void setDriverRating(double driverRating) {
		this.driverRating = driverRating;
	}

	public double getRiderRating() {
		return riderRating;
	}

	public void setRiderRating(double riderRating) {
		this.riderRating = riderRating;
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}
	
}
