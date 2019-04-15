package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import edu.depauw.csc480.model.GeneralUser;
import edu.depauw.csc480.model.Request;

/**
 * Data Access Object for the GeneralUser table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @author ShutoAraki
 */
public class GeneralUserDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public GeneralUserDAO(Connection conn, DatabaseManager dbm) {
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
		String s = "create table GeneralUser ("
				+ "userId int not null, "
				+ "username varchar(255) not null, "
				+ "password varchar(60) not null, "
				+ "email varchar(255) not null, "
				+ "avgRating decimal(3, 2), "
				+ "latitude decimal(15, 10), "
				+ "longitude decimal(15, 10), "
				+ "venmoId varchar(255) not null, "
				+ "primary key (userId), "
				+ "check (email like '_%@depauw.edu')"
				+ ")";
		stmt.executeUpdate(s);
		System.out.println("Now, the GeneralUser table exists!");
	}

	/**
	 * Retrieve a GeneralUser object given its key.
	 * 
	 * @param userId
	 * @return the Course object, or null if not found
	 */
	public GeneralUser find(int userId) {
		try {
			String qry = "select * from GeneralUser where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			String username = rs.getString("username");
			String password = rs.getString("password");
			String email = rs.getString("email");
			double avgRating = rs.getDouble("avgRating");
			double latitude = rs.getDouble("latitude");
			double longitude = rs.getDouble("longitude");
			String venmoId = rs.getString("venmoId");
			rs.close();

			GeneralUser guser = new GeneralUser(this, userId, username, 
											   password, email, avgRating, 
											   latitude, longitude, venmoId);

			return guser;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding the user", e);
		}
	}
	
	/**
	 * Retrieve a GeneralUser object given its username.
	 * (if there are multiple users with the same username, the one 
	 * that happened to be on the top of the resulting table will
	 * be returned)
	 */
	public GeneralUser findUserByName(String username) {
		try {
			String qry = "select * from GeneralUser where username = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;
			
			int userId = rs.getInt("userId");
			String password = rs.getString("password");
			String email = rs.getString("email");
			double avgRating = rs.getDouble("avgRating");
			double latitude = rs.getDouble("latitude");
			double longitude = rs.getDouble("longitude");
			String venmoId = rs.getString("venmoId");
			rs.close();

			GeneralUser guser = new GeneralUser(this, userId, username, 
											   password, email, avgRating, 
											   latitude, longitude, venmoId);

			return guser;
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
	public GeneralUser insert(int userId, String username, String password, String email, 
							 double avgRating, double latitude, double longitude, String venmoId) {
		try {
			// make sure that the userId is currently unused
			if (find(userId) != null)
				return null;

			String cmd = "insert into GeneralUser(userId, username, password, email, avgRating, latitude, longitude, venmoId) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, userId);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, email);
			pstmt.setDouble(5, avgRating);
			pstmt.setDouble(6, latitude);
			pstmt.setDouble(7, longitude);
			pstmt.setString(8, venmoId);
			pstmt.executeUpdate();

			GeneralUser guser = new GeneralUser(this, userId, username, 
											   password, email, avgRating, 
											   latitude, longitude, venmoId);
			
			return guser;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new general user", e);
		}
	}
	
	/**
	 * Retrieve a Collection of all Requests for the given rider user. Backwards
	 * direction of riderId foreign key from Request.
	 * 
	 * @param riderId
	 * @return the Collection
	 */
	public Collection<Request> getRequests(int riderId) {
		try {
			Collection<Request> requests = new ArrayList<Request>();
			String qry = "select requestId from Request where riderId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, riderId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int requestId = rs.getInt("requestId");
				requests.add(dbm.findRequest(requestId));
			}
			rs.close();
			return requests;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error getting requests", e);
		}
	}
	

	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param username
	 */
	public void changeUsername(int userId, String username) {
		try {
			String cmd = "update GeneralUser set username = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, username);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing username", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param password
	 */
	public void changePassword(int userId, String password) {
		try {
			String cmd = "update GeneralUser set password = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, password);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing password", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param email
	 */
	public void changeEmail(int userId, String email) {
		try {
			String cmd = "update GeneralUser set email = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, email);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing email", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param avgRating
	 */
	public void changeAvgRating(int userId, double avgRating) {
		try {
			String cmd = "update GeneralUser set avgRating = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, avgRating);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing average rating", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param latitude
	 */
	public void changeLatitude(int userId, double latitude) {
		try {
			String cmd = "update GeneralUser set latitude = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, latitude);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing latitude", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param longitude
	 */
	public void changeLongitude(int userId, double longitude) {
		try {
			String cmd = "update GeneralUser set longitude = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, longitude);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing longitude", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param venmoId
	 */
	public void changeVenmoId(int userId, String venmoId) {
		try {
			String cmd = "update GeneralUser set venmoId = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, venmoId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing venmo ID", e);
		}
	}

	/**
	 * Clear all data from the GeneralUser table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from GeneralUser";
		stmt.executeUpdate(s);
	}
	
}
