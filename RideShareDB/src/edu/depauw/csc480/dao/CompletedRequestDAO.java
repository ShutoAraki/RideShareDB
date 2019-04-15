package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.depauw.csc480.model.CompletedRequest;
import edu.depauw.csc480.model.DriverUser;
import edu.depauw.csc480.model.Request;

public class CompletedRequestDAO {

	private Connection conn;
	private DatabaseManager dbm;

	public CompletedRequestDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	/**
	 * Create the Course table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		System.out.println("Creating CompletedRequest...");
		String s = "create table CompletedRequest("
				+ "requestId int not null, "
				+ "driverId int not null, "
				+ "driverRating int, "
				+ "riderRating int, "
				+ "completion timestamp, "
				+ "primary key (requestId), "
				+ "check (driverRating >= 0 and driverRating <= 5), "
				+ "check (riderRating >= 0 and riderRating <= 5) "
				+ ")";
		stmt.executeUpdate(s);
		System.out.println("DONE!!!");
	}
	
	/**
	 * Modify the Course table to add foreign key constraints (needs to happen
	 * after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "alter table CompletedRequest add constraint fk_reqcomp "
				+ "foreign key (requestId) references Request on delete cascade";
		stmt.executeUpdate(s);
		String s2 = "alter table CompletedRequest add constraint fk_reqdriver "
				+ "foreign key (driverId) references DriverUser on delete cascade";
		stmt.executeUpdate(s2);
	}

	/**
	 * Retrieve a CompletedRequest object given its key.
	 * 
	 * @param userId
	 * @return the Driver object, or null if not found
	 */
	public CompletedRequest find(int requestId) {
		try {
			String qry = "select * from CompletedRequest where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, requestId);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			int driverId = rs.getInt("driverId");
			double driverRating = rs.getDouble("driverRating");
			double riderRating = rs.getDouble("riderRating");
			String completion = rs.getString("completion");
			
			rs.close();
			
			Request request = dbm.findRequest(requestId);
			DriverUser driver = dbm.findDriverUser(driverId);
			CompletedRequest compRequest = new CompletedRequest(this, request, driver, driverRating, riderRating, completion);

			return compRequest;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding the user", e);
		}
	}

	/**
	 * Add a new Course with the given attributes.
	 * 
	 * @param userId
	 * @param UserName
	 * @return the new Course object, or null if key already exists
	 */
	public CompletedRequest insert(int requestId, int driverId, double driverRating, double riderRating, String completion) {
		try {
			// make sure that the userId is currently unused
			if (find(requestId) != null)
				return null;

			String cmd = "insert into CompletedRequest(requestId, driverId, driverRating, riderRating, completion) "
					+ "values(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, requestId);
			pstmt.setInt(2, driverId);
			pstmt.setDouble(3, driverRating);
			pstmt.setDouble(4, riderRating);
			pstmt.setString(5, completion);
			
			pstmt.executeUpdate();

			Request request = dbm.findRequest(requestId);
			DriverUser driver = dbm.findDriverUser(driverId);
			CompletedRequest compRequest = new CompletedRequest(this, request, driver, driverRating, riderRating, completion);

			return compRequest;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new course", e);
		}
	}

	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param licensePlate
	 */
	public void changeDriverId(int requestId, int driverId) {
		try {
			String cmd = "update CompletedRequest set driverId = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, driverId);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing driverId", e);
		}
	}
	
	public void changeDriverRating(int requestId, double driverRating) {
		try {
			String cmd = "update CompletedRequest set driverRating = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, driverRating);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing driver rating", e);
		}
	}
	
	public void changeRiderRating(int requestId, double riderRating) {
		try {
			String cmd = "update CompletedRequest set riderRating = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, riderRating);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing rider rating", e);
		}
	}
	
	public void changeCompletion(int requestId, String completion) {
		try {
			String cmd = "update CompletedRequest set completion = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, completion);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing toLat", e);
		}
	}
	
	/**
	 * Clear all data from the Course table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from CompletedRequest";
		stmt.executeUpdate(s);
	}
	
}
