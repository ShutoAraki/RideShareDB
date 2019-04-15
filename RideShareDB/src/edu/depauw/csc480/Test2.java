package edu.depauw.csc480;

import java.util.Collection;

import edu.depauw.csc480.dao.DatabaseManager;
import edu.depauw.csc480.model.GeneralUser;
import edu.depauw.csc480.model.Request;

/**
 * Simple client that retrieves data from an already created database.
 * Running this after Test will check that the same data may be retrieved
 * from the database and not just from the in-memory cache.
 * 
 * @author ShutoAraki
 */
public class Test2 {

	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
		GeneralUser shuto = dbm.findUserByName("arashuto");
		
		// Now retrieve a table of Shuto's requests
		Collection<Request> requests = shuto.getRequests();
		for (Request req : requests) {
			System.out.println(req);
		}
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}

}
