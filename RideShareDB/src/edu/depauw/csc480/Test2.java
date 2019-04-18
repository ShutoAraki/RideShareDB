package edu.depauw.csc480;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

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
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("rideshareDB");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		
		String query = "select gu from GeneralUser gu where gu.username='jon'";
		TypedQuery<GeneralUser> q = em.createQuery(query, GeneralUser.class);
		GeneralUser shuto = q.getSingleResult();
		
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
