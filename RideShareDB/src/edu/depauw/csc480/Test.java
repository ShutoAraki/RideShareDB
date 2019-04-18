package edu.depauw.csc480;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

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
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("rideshareDB");
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		// Clear the tables
		em.createQuery("delete from DriverUser").executeUpdate();
		em.createQuery("delete from GeneralUser").executeUpdate();
		em.createQuery("delete from CompletedRequest").executeUpdate();
		em.createQuery("delete from Request").executeUpdate();
		
		try {
			tx.commit();
		} catch (RollbackException ex) {
			ex.printStackTrace();
			tx.rollback();
		}

		// T1 begin
		tx = em.getTransaction();
		tx.begin();

		GeneralUser shuto = new GeneralUser(42, "arashuto", "123456", 
				"shutoaraki_2020@depauw.edu", 4.7, 39.6397676, -86.861676, "Shuto-Araki");
		
		GeneralUser jon = new GeneralUser(67, "jon", "jonthegreatest",
				"jon@depauw.edu", 3.6, 39.6497676, -86.865676, "jon123");
		
		DriverUser james = new DriverUser(43, "james", "987865", 
				"james@depauw.edu", 4.8, 38.435463, -83.4354663, "james456", "DEP123", "1992-11-23", 0.55);
		
		em.persist(shuto);
		em.persist(jon);
		em.persist(james);
		
		Request r1 = new Request(33, shuto, "2019-04-02 4:42:42", 39.6397676, -86.861676, 40.234546, -87.34536, 20.0, 3);
		Request r2 = new Request(34, shuto, "2019-04-04 4:42:41", 39.6397676, -86.861676, 40.234546, -87.34536, 20.0, 3);
		Request r3 = new Request(35, jon, "2019-04-04 4:37:41", 39.6397676, -86.861676, 40.234546, -87.34536, 20.0, 3);
		
		em.persist(r1);
		em.persist(r2);
		em.persist(r3);

		try {
			tx.commit();
		} catch (RollbackException ex) {
			ex.printStackTrace();
			tx.rollback();
		}
		// T1 end
		
		// T2 begin
		tx = em.getTransaction();
		tx.begin();
		
		DriverUser shutoD = shuto.becomeDriver("SHU1018", "1996-10-18", 0.42);
		CompletedRequest c1 = r1.completed(james, 3.4, 5.0, "2019-04-02 4:55:42");
		CompletedRequest c2 = r2.completed(james, 5, 5, "2019-04-02 5:05:42");
		CompletedRequest c3 = r3.completed(shutoD, 4.5, 3.1, "2019-04-02 5:09:42");
		
		em.persist(shutoD);
		em.persist(c1);
		em.persist(c2);
		em.persist(c3);
		
		try {
			tx.commit();
		} catch (RollbackException ex) {
			ex.printStackTrace();
			tx.rollback();
		}
		// T2 end

		// T3 begin
		tx = em.getTransaction();
		tx.begin();
		
		Collection<Request> requests = shuto.getRequests();
		for (Request req : requests) {
			System.out.println(req);
		}
		
		Collection<CompletedRequest> compRequests = james.getCompletedRequests();
		for (CompletedRequest req : compRequests) {
			System.out.println(req);
		}

		try {
			tx.commit();
		} catch (RollbackException ex) {
			ex.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// T3 end

		System.out.println("Done");
		
	}

}
