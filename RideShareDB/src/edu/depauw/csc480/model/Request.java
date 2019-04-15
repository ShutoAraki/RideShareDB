package edu.depauw.csc480.model;

import edu.depauw.csc480.dao.RequestDAO;

public class Request {
	
	private RequestDAO dao;
	private int requestId;
	private GeneralUser rider;
	private String requestTime;
	private double fromLat;
	private double fromLon;
	private double toLat;
	private double toLon;
	private double price;
	private int groupSize;
	
	public Request(RequestDAO dao, int requestId, GeneralUser rider, String requestTime, double fromLat, double fromLon,
			double toLat, double toLon, double price, int groupSize) {
		this.dao = dao;
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
		ret += "\n\tRequested by " + getRiderName();
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
		dao.changeRequestTime(requestId, requestTime);
	}

	public double getFromLat() {
		return fromLat;
	}

	public void setFromLat(double fromLat) {
		this.fromLat = fromLat;
		dao.changeFromLat(requestId, fromLat);
	}

	public double getFromLon() {
		return fromLon;
	}

	public void setFromLon(double fromLon) {
		this.fromLon = fromLon;
		dao.changeFromLon(requestId, fromLon);
	}

	public double getToLat() {
		return toLat;
	}

	public void setToLat(double toLat) {
		this.toLat = toLat;
		dao.changeToLat(requestId, toLat);
	}

	public double getToLon() {
		return toLon;
	}

	public void setToLon(double toLon) {
		this.toLon = toLon;
		dao.changeToLon(requestId, toLon);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		dao.changePrice(requestId, price);
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
		dao.changeGroupSize(groupSize, groupSize);
	}

}
