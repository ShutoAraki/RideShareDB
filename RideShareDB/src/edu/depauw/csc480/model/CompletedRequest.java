package edu.depauw.csc480.model;

import edu.depauw.csc480.dao.CompletedRequestDAO;

public class CompletedRequest {
	
	private CompletedRequestDAO dao;
	private Request request;
	private int requestId;
	private DriverUser driver;
	private double driverRating;
	private double riderRating;
	private String completion;
	
	public CompletedRequest(CompletedRequestDAO dao, Request request, DriverUser driver, double driverRating,
			double riderRating, String completion) {
		this.dao = dao;
		this.request = request;
		requestId = request.getRequestId();
		this.driver = driver;
		this.driverRating = driverRating;
		this.riderRating = riderRating;
		this.completion = completion;
	}
	
	public String toString() {
		String ret = request.toString();
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
		dao.changeDriverRating(requestId, driverRating);
	}

	public double getRiderRating() {
		return riderRating;
	}

	public void setRiderRating(double riderRating) {
		this.riderRating = riderRating;
		dao.changeRiderRating(requestId, riderRating);
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
		dao.changeCompletion(requestId, completion);
	}
	
}
