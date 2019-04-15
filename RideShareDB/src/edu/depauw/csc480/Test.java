package edu.depauw.csc480;

import java.util.Collection;

import edu.depauw.csc480.dao.DatabaseManager;
import edu.depauw.csc480.model.*;

/**
 * Simple client that inserts sample data then runs a query.
 * 
 * @author ShutoAraki
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
		dbm.clearTables();
		
		GeneralUser shuto = dbm.insertGeneralUser(42, "arashuto", "123456", 
				"shutoaraki_2020@depauw.edu", 4.7, 39.6397676, -86.861676, "Shuto-Araki");
		
		dbm.insertGeneralUser(67, "jon", "jonthegreatest",
				"jon@depauw.edu", 3.6, 39.6497676, -86.865676, "jon123");
		DriverUser jon = dbm.insertDriverUser(67, "UVF-8232", "1996-10-18", 0.5);
		
		dbm.insertRequest(32, 42, "2019-03-24 3:30:34", 39.6397676, -86.861676, 40.234546, -87.34536, 20.0, 3);
		dbm.insertRequest(33, 42, "2019-04-02 4:42:42", 39.6397676, -86.861676, 40.234546, -87.34536, 20.0, 3);
		dbm.insertCompletedRequest(32, 67, 4.7, 5.0, "2019-03-24 3:45:21");
		
		dbm.commit();
		
		// Now retrieve a table of Shuto's requests
		Collection<Request> requests = shuto.getRequests();
		for (Request req : requests) {
			System.out.println(req);
		}
		
		// Retrieve a table of Jon's completed requests
		Collection<CompletedRequest> compRequests = jon.getCompletedRequests();
		for (CompletedRequest compreq : compRequests) {
			System.out.println(compreq);
		}
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}

}
