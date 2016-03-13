package me.timothy.pathfinder.bestiary.persisters.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.models.Creature;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;
import me.timothy.pathfinder.bestiary.persisters.CreaturePersister;

public abstract class CreaturePersisterTest {
	protected CRPersister crPersister;
	protected CreaturePersister persister;
	
	@Test
	public void testPersist() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "bat", cr1_2.id);
		persister.persist(bat);
		assertTrue("Expected strictly positive id, got " + bat.id, bat.id > 0);
	}

	@Test
	public void testFetchAll() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "bat", cr1_2.id);
		persister.persist(bat);
		
		List<Creature> fromDb = persister.fetchAll();
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(bat));
		
		Creature viper = new Creature(0, "viper", cr1_2.id);
		persister.persist(viper);
		
		fromDb = persister.fetchAll();
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(bat));
		assertTrue(fromDb.contains(viper));
	}
	
	@Test
	public void testFetchById() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "bat", cr1_2.id);
		persister.persist(bat);
		
		Creature fromDb = persister.fetchById(bat.id);
		assertEquals(bat, fromDb);
		
		Creature viper = new Creature(0, "viper", cr1_2.id);
		persister.persist(viper);
		
		fromDb = persister.fetchById(bat.id);
		assertEquals(bat, fromDb);
		
		fromDb = persister.fetchById(viper.id);
		assertEquals(viper, fromDb);
	}
	
	@Test
	public void testFetchLikeName() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "Bat", cr1_2.id);
		persister.persist(bat);
		
		List<Creature> fromDb = persister.fetchLikeName("B%");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(bat));
		
		fromDb = persister.fetchLikeName("Bat");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(bat));
		
		fromDb = persister.fetchLikeName("bat");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(bat));
		
		fromDb = persister.fetchLikeName("Vi%");
		assertEquals(0, fromDb.size());
		
		Creature viper = new Creature(0, "Viper", cr1_2.id);
		persister.persist(viper);
		
		fromDb = persister.fetchLikeName("Vi%");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(viper));
	}
	
	@Test
	public void testFetchByName() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "Bat", cr1_2.id);
		persister.persist(bat);
		
		Creature fromDb = persister.fetchByName("Bat");
		assertEquals(bat, fromDb);
		
		fromDb = persister.fetchByName("Ba%");
		assertNull(fromDb);
		
		Creature viper = new Creature(0, "Viper", cr1_2.id);
		persister.persist(viper);
		
		fromDb = persister.fetchByName("Viper");
		assertEquals(viper, fromDb);
		
		fromDb = persister.fetchByName("Bat");
		assertEquals(bat, fromDb);
		
		fromDb = persister.fetchByName("Dog");
		assertNull(fromDb);
	}
}
