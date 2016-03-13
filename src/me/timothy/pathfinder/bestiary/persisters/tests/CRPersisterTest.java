package me.timothy.pathfinder.bestiary.persisters.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;

public abstract class CRPersisterTest {
	protected CRPersister persister;
	
	@Test
	public void testPersist() {
		ChallengeRating cr = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		persister.persist(cr);
		assertTrue("Expected strictly positive id, but got " + cr.id, cr.id > 0);
	}
	
	@Test
	public void testFetchAll() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		persister.persist(cr1_2);
		
		List<ChallengeRating> allCR = persister.fetchAll();
		assertEquals(1, allCR.size());
		assertTrue(allCR.contains(cr1_2));
		
		ChallengeRating cr1 = new ChallengeRating(0, "1", 400, 135, 100, 65);
		persister.persist(cr1);
		
		allCR = persister.fetchAll();
		assertEquals(2, allCR.size());
		assertTrue(allCR.contains(cr1_2));
		assertTrue(allCR.contains(cr1));
	}
	
	@Test
	public void testFetchById() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		persister.persist(cr1_2);

		ChallengeRating crFromDb = persister.fetchByID(cr1_2.id);
		assertEquals(cr1_2, crFromDb);
		
		persister.persist(cr1_2);
		assertEquals(cr1_2, crFromDb);
	}
	
	@Test
	public void testFetchLikeName() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		persister.persist(cr1_2);

		List<ChallengeRating> crFromDb = persister.fetchLikeName("%2");
		assertEquals(1, crFromDb.size());
		assertTrue(crFromDb.contains(cr1_2));
		
		ChallengeRating cr1_4 = new ChallengeRating(0, "1/4", 100, 35, 25, 15);
		persister.persist(cr1_4);
		
		crFromDb = persister.fetchLikeName("%2");
		assertEquals(1, crFromDb.size());
		assertTrue(crFromDb.contains(cr1_2));
		
		crFromDb = persister.fetchLikeName("1%");
		assertEquals(2, crFromDb.size());
		assertTrue(crFromDb.contains(cr1_2));
		assertTrue(crFromDb.contains(cr1_4));
	}
	
	@Test
	public void testFetchByName() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		persister.persist(cr1_2);
		
		ChallengeRating cr1 = new ChallengeRating(0, "1", 400, 135, 100, 65);
		persister.persist(cr1);
		
		ChallengeRating cr2 = new ChallengeRating(0, "2", 600, 200, 150, 100);
		persister.persist(cr2);
		
		ChallengeRating fromDb = persister.fetchByName("0");
		assertNull(fromDb);
		
		fromDb = persister.fetchByName("11/22");
		assertNull(fromDb);
		
		fromDb = persister.fetchByName("1/2");
		assertEquals(cr1_2, fromDb);
		
		fromDb = persister.fetchByName("1");
		assertEquals(cr1, fromDb);
		
		fromDb = persister.fetchByName("2");
		assertEquals(cr2, fromDb);
	}
}
