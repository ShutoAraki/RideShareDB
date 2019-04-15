package edu.depauw.csc480.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDriver;

import edu.depauw.csc480.model.*;

/**
 * This class mediates access to the RideShare database, hiding the lower-level
 * DAO objects from the client.
 * 
 * @author ShutoAraki
 */
public class DatabaseManager {
   private Driver driver;
   private Connection conn;
   private GeneralUserDAO generalUserDAO;
   private DriverUserDAO driverUserDAO;
   private RequestDAO requestDAO;
   private CompletedRequestDAO compRequestDAO;

   private final String url = "jdbc:derby:rideshareDB";

   public DatabaseManager() {
      driver = new EmbeddedDriver();

      Properties prop = new Properties();
      prop.put("create", "false");

      // try to connect to an existing database
      try {
         conn = driver.connect(url, prop);
         conn.setAutoCommit(false);
      } catch (SQLException e) {
         // database doesn't exist, so try creating it
         try {
            prop.put("create", "true");
            conn = driver.connect(url, prop);
            conn.setAutoCommit(false);
            create();
         } catch (SQLException e2) {
            throw new RuntimeException("cannot connect to database", e2);
         }
      }
      
      generalUserDAO = new GeneralUserDAO(conn, this);
      driverUserDAO = new DriverUserDAO(conn, this);
      requestDAO = new RequestDAO(conn, this);
      compRequestDAO = new CompletedRequestDAO(conn, this);
   }

   /**
    * Initialize the tables and their constraints in a newly created database
    * 
    * @throws SQLException
    */
   private void create() throws SQLException {
      GeneralUserDAO.create(conn);
      DriverUserDAO.create(conn);
      RequestDAO.create(conn);
      CompletedRequestDAO.create(conn);
      DriverUserDAO.addConstraints(conn);
      RequestDAO.addConstraints(conn);
      CompletedRequestDAO.addConstraints(conn);
      conn.commit();
   }

   // ***************************************************************
   // Data retrieval functions -- find a model object given its key

   public GeneralUser findGenralUser(int userId) {
      return generalUserDAO.find(userId);
   }
   
   public GeneralUser findUserByName(String username) {
	   return generalUserDAO.findUserByName(username);
   }
   
   public DriverUser findDriverUser(int userId) {
	   return driverUserDAO.find(userId);
   }
   
   public Request findRequest(int requestId) {
	   return requestDAO.find(requestId);
   }
   
   public CompletedRequest findCompletedRequest(int requestId) {
	   return compRequestDAO.find(requestId);
   }
   
   // ***************************************************************
   // Data insertion functions -- create new model object from attributes

   public GeneralUser insertGeneralUser(int userId, String username, String password, String email, 
									 double avgRating, double latitude, double longitude, String venmoId) {
      return generalUserDAO.insert(userId, username, password, email, 
    		  						  avgRating, latitude, longitude, venmoId);
   }
   
   public DriverUser insertDriverUser(int userId, String licensePlate, String birthdate, double pricePerMile) {
	   return driverUserDAO.insert(userId, licensePlate, birthdate, pricePerMile);
   }
   
   public Request insertRequest(int requestId, int riderId, String requestTime, double fromLat, double fromLon,
			double toLat, double toLon, double price, int groupSize) {
	   return requestDAO.insert(requestId, riderId, requestTime, fromLat, fromLon, toLat, toLon, price, groupSize);
   }
   
   public CompletedRequest insertCompletedRequest(int requestId, int driverId, double driverRating,
			double riderRating, String completion) {
	   return compRequestDAO.insert(requestId, driverId, driverRating, riderRating, completion);
   }

   // ***************************************************************
   // Utility functions

   /**
    * Commit changes since last call to commit
    */
   public void commit() {
      try {
         conn.commit();
      } catch (SQLException e) {
         throw new RuntimeException("cannot commit database", e);
      }
   }

   /**
    * Abort changes since last call to commit, then close connection
    */
   public void cleanup() {
      try {
         conn.rollback();
         conn.close();
      } catch (SQLException e) {
         System.out.println("fatal error: cannot cleanup connection");
      }
   }

   /**
    * Close connection and shutdown database
    */
   public void close() {
      try {
         conn.close();
      } catch (SQLException e) {
         throw new RuntimeException("cannot close database connection", e);
      }

      // Now shutdown the embedded database system -- this is Derby-specific
      try {
         Properties prop = new Properties();
         prop.put("shutdown", "true");
         conn = driver.connect(url, prop);
      } catch (SQLException e) {
         // This is supposed to throw an exception...
         System.out.println("Derby has shut down successfully");
      }
   }

   /**
    * Clear out all data from database (but leave empty tables)
    */
   public void clearTables() {
      try {
         generalUserDAO.clear();
         requestDAO.clear();
         driverUserDAO.clear();
         compRequestDAO.clear();
         conn.commit();
      } catch (SQLException e) {
         // Attempt to handle errors caused by a previous failed attempt to
         // clear the database
         System.err.println("Unable to clear tables -- recreating instead");
         try {
            create();
         } catch (SQLException e2) {
            throw new RuntimeException(
               "unable to recreate database -- delete and retry",
               e);
         }
      }
   }
}
