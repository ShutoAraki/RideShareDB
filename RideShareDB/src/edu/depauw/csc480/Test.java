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

		tx = em.getTransaction();
		tx.begin();

		// department heads are set to null for now; see below

		GeneralUser shuto = new GeneralUser(42, "arashuto", "123456", 
				"shutoaraki_2020@depauw.edu", 4.7, 39.6397676, -86.861676, "Shuto-Araki");
		
		GeneralUser jon = new GeneralUser(67, "jon", "jonthegreatest",
				"jon@depauw.edu", 3.6, 39.6497676, -86.865676, "jon123");
		
		DriverUser james = new DriverUser(43, "james", "987865", 
				"james@depauw.edu", 4.8, 38.435463, -83.4354663, "james456", "DEP123", "1992-11-23", 0.55);
		
		em.persist(shuto);
		em.persist(jon);
		em.persist(james);
		
		em.persist(new Request(33, shuto, "2019-04-02 4:42:42", 39.6397676, -86.861676, 40.234546, -87.34536, 20.0, 3));

		try {
			tx.commit();
		} catch (RollbackException ex) {
			ex.printStackTrace();
			tx.rollback();
		}

		tx = em.getTransaction();
		tx.begin();
		
		Collection<Request> requests = shuto.getRequests();
		for (Request req : requests) {
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

		System.out.println("Done");
		
	}

}
