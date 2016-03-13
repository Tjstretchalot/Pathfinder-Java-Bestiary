package me.timothy.pathfinder.bestiary.persisters.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import me.timothy.pathfinder.bestiary.models.Environment;
import me.timothy.pathfinder.bestiary.persisters.EnvironmentPersister;

public abstract class EnvironmentPersisterTest {
	protected EnvironmentPersister persister;
	
	@Test
	public void testPersist() {
		Environment forests = new Environment(0, "forests");
		persister.persist(forests);
		assertTrue("Expected strictly positive id but got " + forests.id, forests.id > 0);
	}

	@Test
	public void testFetchAll() {
		Environment forests = new Environment(0, "forests");
		persister.persist(forests);
		
		List<Environment> fromDb = persister.fetchAll();
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(forests));
		
		Environment oceans = new Environment(0, "oceans");
		persister.persist(oceans);
		
		fromDb = persister.fetchAll();
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(forests));
		assertTrue(fromDb.contains(oceans));
		
		Environment deserts = new Environment(0, "deserts");
		persister.persist(deserts);
		
		fromDb = persister.fetchAll();
		assertEquals(3, fromDb.size());
		assertTrue(fromDb.contains(forests));
		assertTrue(fromDb.contains(oceans));
		assertTrue(fromDb.contains(deserts));
	}
	
	@Test
	public void testFetchById() {
		Environment forests = new Environment(0, "forests");
		persister.persist(forests);
		
		Environment fromDb = persister.fetchById(forests.id);
		assertEquals(forests, fromDb);
		
		Environment oceans = new Environment(0, "oceans");
		persister.persist(oceans);
		
		fromDb = persister.fetchById(forests.id);
		assertEquals(forests, fromDb);
		
		fromDb = persister.fetchById(oceans.id);
		assertEquals(oceans, fromDb);
	}
	
	@Test
	public void testFetchLikeName() {
		Environment forests = new Environment(0, "forests");
		persister.persist(forests);
		
		List<Environment> fromDb = persister.fetchLikeName("d%");
		assertEquals(0, fromDb.size());
		
		Environment oceans = new Environment(0, "oceans");
		persister.persist(oceans);
		
		fromDb = persister.fetchLikeName("o%");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(oceans));
		
		fromDb = persister.fetchLikeName("%o%");
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(forests));
		assertTrue(fromDb.contains(oceans));
		
		fromDb = persister.fetchLikeName("fore%");
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(forests));
	}
	
	@Test
	public void testFetchByName() {
		Environment forests = new Environment(0, "forests");
		persister.persist(forests);
		
		Environment fromDb = persister.fetchByName("forests");
		assertEquals(forests, fromDb);
		
		fromDb = persister.fetchByName("oceans");
		assertNull(fromDb);
		
		fromDb = persister.fetchByName("fore%");
		assertNull(fromDb);
		
		Environment oceans = new Environment(0, "oceans");
		persister.persist(oceans);
		
		fromDb = persister.fetchByName("forests");
		assertEquals(forests, fromDb);
		
		fromDb = persister.fetchByName("oceans");
		assertEquals(oceans, fromDb);
	}
}
