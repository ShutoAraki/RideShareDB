package edu.depauw.csc480.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Request {
	@Id
	private int requestId;
	
	@ManyToOne
	private GeneralUser rider;
	
	@Basic
	private String requestTime;
	
	@Basic
	private double fromLat;
	
	@Basic
	private double fromLon;
	
	@Basic
	private double toLat;
	
	@Basic
	private double toLon;
	
	@Basic
	private double price;
	
	@Basic
	private int groupSize;
	
	public Request() {}
	
	public Request(int requestId, GeneralUser rider, String requestTime, double fromLat, double fromLon,
			double toLat, double toLon, double price, int groupSize) {
		this.requestId = requestId;
		this.rider = rider;
		this.requestTime = requestTime;
		this.fromLat = fromLat;
		this.fromLon = fromLon;
		this.toLat = toLat;
		this.toLon = toLon;
		this.price = price;
		this.groupSize = groupSize;
	}
	
	public String toString() {
		String ret = "RequestID: " + requestId;
		ret += "\n\tRider: " + getRiderName();
		ret += "\n\tPrice: " + price;
		ret += "\n\tGroup Size: " + groupSize;
		
		return ret;
	}
	
	public int getRequestId() {
		return requestId;
	}
	
	public String getRiderName() {
		return rider.getUsername();
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public double getFromLat() {
		return fromLat;
	}

	public void setFromLat(double fromLat) {
		this.fromLat = fromLat;
	}

	public double getFromLon() {
		return fromLon;
	}

	public void setFromLon(double fromLon) {
		this.fromLon = fromLon;
	}

	public double getToLat() {
		return toLat;
	}

	public void setToLat(double toLat) {
		this.toLat = toLat;
	}

	public double getToLon() {
		return toLon;
	}

	public void setToLon(double toLon) {
		this.toLon = toLon;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
	
	public CompletedRequest completed(DriverUser driver, double driverRating,
			double riderRating, String completion) {
		CompletedRequest comp = new CompletedRequest(requestId + 100000000, rider, requestTime, fromLat, fromLon, 
				toLat, toLon, price, groupSize, driver, driverRating, riderRating, completion);
		return comp;
	}

}
