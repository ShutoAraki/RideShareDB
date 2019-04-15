package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import edu.depauw.csc480.model.CompletedRequest;
import edu.depauw.csc480.model.DriverUser;
import edu.depauw.csc480.model.GeneralUser;
import edu.depauw.csc480.model.Request;

public class DriverUserDAO {
	
	private Connection conn;
	private DatabaseManager dbm;

	public DriverUserDAO(Connection conn, DatabaseManager dbm) {
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
		String s = "create table DriverUser ("
				+ "userId int not null, "
				+ "licensePlate varchar(8) not null, "
				+ "birthdate date not null, "
				+ "pricePerMile decimal(4, 2), "
				+ "primary key (userId), "
				+ "check (pricePerMile >= 0)"
				+ ")";
		stmt.executeUpdate(s);
		System.out.println("SUCCESS");
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
		String s = "alter table DriverUser add constraint fk_gudriver "
				+ "foreign key(userId) references GeneralUser on delete cascade";
		stmt.executeUpdate(s);
	}

	/**
	 * Retrieve a DriverUser object given its key.
	 * 
	 * @param userId
	 * @return the DriverUser object, or null if not found
	 */
	public DriverUser find(int userId) {
		try {
			String qry = "select * from DriverUser where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			// return null if DriverUser doesn't exist
			if (!rs.next()) 
				return null;
			
			GeneralUser user = dbm.findGenralUser(userId);
			String licensePlate = rs.getString("licensePlate");
			String birthdate = rs.getString("birthdate");
			double pricePerMile = rs.getDouble("pricePerMile");
			rs.close();

			DriverUser driver = new DriverUser(this, user, licensePlate, birthdate, pricePerMile);

			return driver;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding the user", e);
		}
	}

	/**
	 * Add a new DriverUser with the given attributes.
	 * 
	 * @param userId
	 * @param UserName
	 * @return the new DriverUser object, or null if key already exists
	 */
	public DriverUser insert(int userId, String licensePlate, String birthdate, double pricePerMile) {
		try {
			// make sure that the userId is currently unused
			if (find(userId) != null)
				return null;

			String cmd = "insert into DriverUser(userId, licensePlate, birthdate, pricePerMile) "
					+ "values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, userId);
			pstmt.setString(2, licensePlate);
			pstmt.setString(3, birthdate);
			pstmt.setDouble(4, pricePerMile);
			pstmt.executeUpdate();

			GeneralUser user = dbm.findGenralUser(userId);
			DriverUser driver = new DriverUser(this, user, licensePlate, birthdate, pricePerMile);
			
			return driver;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new driver user", e);
		}
	}

	/**
	 * Retrieve a Collection of all CompletedRequests for the given driver user. Backwards
	 * direction of driverId foreign key from CompletedRequest.
	 * 
	 * @param driverId
	 * @return the Collection
	 */
	public Collection<CompletedRequest> getCompletedRequests(int driverId) {
		try {
			Collection<CompletedRequest> compRequests = new ArrayList<CompletedRequest>();
			String qry = "select requestId from CompletedRequest where driverId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, driverId);
			ResultSet rs = pstmt.executeQuery();
//			if (!rs.next())
//				System.out.println("There is no completed requests.");
			while (rs.next()) {
				int requestId = rs.getInt("requestId");
				compRequests.add(dbm.findCompletedRequest(requestId));
			}
			rs.close();
			return compRequests;
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
	 * @param licensePlate
	 */
	public void changeLicensePlate(int userId, String licensePlate) {
		try {
			String cmd = "update DriverUser set licensePlate = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, licensePlate);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing license plate", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param birthdate
	 */
	public void changeBirthdate(int userId, String birthdate) {
		try {
			String cmd = "update DriverUser set birthdate = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, birthdate);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing birthdate", e);
		}
	}
	
	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param userId
	 * @param pricePerMile
	 */
	public void changePricePerMile(int userId, double pricePerMile) {
		try {
			String cmd = "update DriverUser set pricePerMile = ? where userId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setDouble(1, pricePerMile);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing price per mile", e);
		}
	}

	/**
	 * Clear all data from the Course table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from DriverUser";
		stmt.executeUpdate(s);
	}

}
