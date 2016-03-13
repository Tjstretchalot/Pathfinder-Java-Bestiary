package me.timothy.pathfinder.bestiary.persisters.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;

public class TemperaturePersisterTest {
	protected TemperaturePersister persister;
	
	@Test
	public void testPersist() {
		Temperature temperate = new Temperature(0, "temperate");
		persister.persist(temperate);
		assertTrue("Expected strictly positive id, got " + temperate.id, temperate.id > 0);
	}
	
	@Test
	public void testFetchAll() {
		Temperature cold = new Temperature(0, "cold");
		persister.persist(cold);
		
		List<Temperature> fromDb = persister.fetchAll();
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(cold));
		
		Temperature temperate = new Temperature(0, "temperate");
		persister.persist(temperate);
		
		fromDb = persister.fetchAll();
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(cold));
		assertTrue(fromDb.contains(temperate));
		
		Temperature warm = new Temperature(0, "warm");
		persister.persist(warm);
		
		fromDb = persister.fetchAll();
		assertEquals(3, fromDb.size());
		assertTrue(fromDb.contains(cold));
		assertTrue(fromDb.contains(temperate));
		assertTrue(fromDb.contains(warm));
	}
	
	@Test
	public void testFetchById() {
		Temperature cold = new Temperature(0, "cold");
		persister.persist(cold);
		
		Temperature fromDb = persister.fetchById(cold.id);
		assertEquals(cold, fromDb);
		
		Temperature temperate = new Temperature(0, "temperate");
		persister.persist(temperate);
		
		fromDb = persister.fetchById(cold.id);
		assertEquals(cold, fromDb);
		
		fromDb = persister.fetchById(temperate.id);
		assertEquals(temperate, fromDb);
	}
	
	@Test
	public void testFetchLikeName() {
		Temperature cold = new Temperature(0, "cold");
		persister.persist(cold);
		
		List<Temperature> fromDb = persister.fetchLikeName("co%");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(cold));
		
		fromDb = persister.fetchLikeName("cold");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(cold));
		
		Temperature temperate = new Temperature(0, "temperate");
		persister.persist(temperate);
		
		fromDb = persister.fetchLikeName("temp%");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(temperate));
		
		Temperature warm = new Temperature(0, "warm");
		persister.persist(warm);
		
		fromDb = persister.fetchLikeName("%a%");
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(temperate));
		assertTrue(fromDb.contains(warm));
	}
	
	@Test
	public void fetchByName() {
		Temperature cold = new Temperature(0, "cold");
		persister.persist(cold);
		
		Temperature fromDb = persister.fetchByName("cold");
		assertEquals(cold, fromDb);
		
		fromDb = persister.fetchByName("temperate");
		assertNull(fromDb);
		
		Temperature temperate = new Temperature(0, "temperate");
		persister.persist(temperate);
		
		fromDb = persister.fetchByName("cold");
		assertEquals(cold, fromDb);
		
		fromDb = persister.fetchByName("temperate");
		assertEquals(temperate, fromDb);
	}
}
