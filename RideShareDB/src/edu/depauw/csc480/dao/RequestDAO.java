package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import edu.depauw.csc480.model.GeneralUser;
import edu.depauw.csc480.model.Request;

public class RequestDAO {
	
	private Connection conn;
	private DatabaseManager dbm;
	private Map<Integer, Request> cache;

	public RequestDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<Integer, Request>();
	}

	/**
	 * Create the Course table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		System.out.println("... Creating Request table ...");
		String s = "create table Request("
				+ "requestId int not null, "
				+ "riderId int not null, "
				+ "requestTime timestamp not null, "
				+ "fromLat decimal(15, 10) not null, "
				+ "fromLon decimal(15, 10) not null, "
				+ "toLat decimal(15, 10) not null, "
				+ "toLon decimal(15, 10) not null, "
				+ "price decimal(6, 2) not null, "
				+ "groupSize int not null, "
				+ "primary key (requestId), "
				+ "check (price >= 0), "
				+ "check (groupSize >= 1)"
				+ ")";
		stmt.executeUpdate(s);
		System.out.println("Successfully created the Request table!");
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
		String s = "alter table Request add constraint fk_gurequest "
				+ "foreign key(riderId) references GeneralUser on delete cascade";
		stmt.executeUpdate(s);
	}

	/**
	 * Retrieve a Request object given its key.
	 * 
	 * @param requestId
	 * @return the Request object, or null if not found
	 */
	public Request find(int requestId) {
		if (cache.containsKey(requestId)) return cache.get(requestId);
		
		try {
			String qry = "select * from Request where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, requestId);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			int riderId = rs.getInt("riderId");
			String requestTime = rs.getString("requestTime");
			double fromLat = rs.getDouble("fromLat");
			double fromLon = rs.getDouble("fromLon");
			double toLat = rs.getDouble("toLat");
			double toLon = rs.getDouble("toLon");
			double price = rs.getDouble("price");
			int groupSize = rs.getInt("groupSize");
			
			rs.close();

			GeneralUser rider = dbm.findGenralUser(riderId);
			Request request = new Request(this, requestId, rider, requestTime, fromLat, fromLon, toLat, toLon, price, groupSize);

			return request;
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
	public Request insert(int requestId, int riderId, String requestTime, double fromLat, double fromLon,
			double toLat, double toLon, double price, int groupSize) {
		try {
			// make sure that the userId is currently unused
			if (find(requestId) != null)
				return null;

			String cmd = "insert into Request(requestId, riderId, requestTime, fromLat, fromLon, toLat, toLon, price, groupSize) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, requestId);
			pstmt.setInt(2, riderId);
			pstmt.setString(3, requestTime);
			pstmt.setDouble(4, fromLat);
			pstmt.setDouble(5, fromLon);
			pstmt.setDouble(6, toLat);
			pstmt.setDouble(7, toLon);
			pstmt.setDouble(8, price);
			pstmt.setInt(9, groupSize);
			
			pstmt.executeUpdate();

			GeneralUser rider = dbm.findGenralUser(riderId);
			Request request = new Request(this, requestId, rider, requestTime, fromLat, fromLon, toLat, toLon, price, groupSize);

			return request;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new request", e);
		}
	}

	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param licensePlate
	 */
	public void changeRequestTime(int requestId, String requestTime) {
		try {
			String cmd = "update Request set requestTime = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, requestTime);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing requestTime", e);
		}
	}
	
	public void changeFromLat(int requestId, double fromLat) {
		try {
			String cmd = "update Request set fromLat = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, fromLat);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing fromLat", e);
		}
	}
	
	public void changeFromLon(int requestId, double fromLon) {
		try {
			String cmd = "update Request set fromLon = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, fromLon);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing fromLon", e);
		}
	}
	
	public void changeToLat(int requestId, double toLat) {
		try {
			String cmd = "update Request set toLat = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, toLat);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing toLat", e);
		}
	}
	
	public void changeToLon(int requestId, double toLon) {
		try {
			String cmd = "update Request set toLon = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, toLon);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing toLon", e);
		}
	}
	
	public void changePrice(int requestId, double price) {
		try {
			String cmd = "update Request set price = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, price);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing price", e);
		}
	}
	
	public void changeGroupSize(int requestId, int groupSize) {
		try {
			String cmd = "update Request set groupSize = ? where requestId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, groupSize);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing groupSize", e);
		}
	}
	
	/**
	 * Clear all data from the Course table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from Request";
		stmt.executeUpdate(s);
	}

}
