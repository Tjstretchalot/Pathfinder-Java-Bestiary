package me.timothy.pathfinder.bestiary.persisters.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.models.Creature;
import me.timothy.pathfinder.bestiary.models.Environment;
import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;
import me.timothy.pathfinder.bestiary.persisters.CreaturePersister;
import me.timothy.pathfinder.bestiary.persisters.EnvironmentPersister;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;

public abstract class PersisterIntegrationTests {
	protected CreaturePersister cPersister;
	protected EnvironmentPersister ePersister;
	protected TemperaturePersister tPersister;
	protected CRPersister crPersister;
	
	@Test
	public void testCreatureEnvironment() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "bat", cr1_2.id);
		cPersister.persist(bat);
		
		Creature spider = new Creature(0, "spider", cr1_2.id);
		cPersister.persist(spider);
		
		Environment forests = new Environment(0, "forests");
		ePersister.persist(forests);
		
		Environment urban = new Environment(0, "urban");
		ePersister.persist(urban);
		
		cPersister.linkCreatureToEnvironment(bat.id, forests.id);
		
		cPersister.linkCreatureToEnvironment(spider.id, urban.id);
		cPersister.linkCreatureToEnvironment(spider.id, forests.id);
		
		List<Creature> fromDb = cPersister.fetchByEnvironment(forests.id);
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(bat));
		assertTrue(fromDb.contains(spider));
		
		fromDb = cPersister.fetchByEnvironment(urban.id);
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(spider));
		
		List<Environment> fromDb2 = ePersister.fetchByCreature(bat.id);
		assertEquals(1, fromDb2.size());
		assertTrue(fromDb2.contains(forests));
		
		fromDb2 = ePersister.fetchByCreature(spider.id);
		assertEquals(2, fromDb2.size());
		assertTrue(fromDb2.contains(forests));
		assertTrue(fromDb2.contains(urban));
	}
	
	@Test
	public void testCreatureTemperature() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Creature bat = new Creature(0, "bat", cr1_2.id);
		cPersister.persist(bat);
		
		Creature spider = new Creature(0, "spider", cr1_2.id);
		cPersister.persist(spider);
		
		Temperature cold = new Temperature(0, "cold");
		tPersister.persist(cold);
		
		Temperature temperate = new Temperature(0, "temperate");
		tPersister.persist(temperate);
		
		cPersister.linkCreatureToTemperature(bat.id, temperate.id);
		
		cPersister.linkCreatureToTemperature(spider.id, cold.id);
		cPersister.linkCreatureToTemperature(spider.id, temperate.id);
		
		List<Temperature> fromDb = tPersister.fetchByCreature(bat.id);
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(temperate));
		
		fromDb = tPersister.fetchByCreature(spider.id);
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(cold));
		assertTrue(fromDb.contains(temperate));
		
		List<Creature> fromDb2 = cPersister.fetchByTemperature(cold.id);
		assertEquals(1, fromDb2.size());
		assertTrue(fromDb2.contains(spider));
		
		fromDb2 = cPersister.fetchByTemperature(temperate.id);
		assertEquals(2, fromDb2.size());
		assertTrue(fromDb2.contains(bat));
		assertTrue(fromDb2.contains(spider));
	}

	@Test
	public void testCreatureChallengeRating() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		ChallengeRating cr1 = new ChallengeRating(0, "1", 400, 135, 100, 65);
		crPersister.persist(cr1);
		
		ChallengeRating cr4 = new ChallengeRating(0, "4", 1200, 400, 300, 200);
		
		Creature bat = new Creature(0, "Bat", cr1_2.id);
		cPersister.persist(bat);
		
		Creature viper = new Creature(0, "Viper", cr1_2.id);
		cPersister.persist(viper);
		
		Creature direBat = new Creature(0, "Bat, Dire", cr4.id);
		cPersister.persist(direBat);
		
		Creature giantRat = new Creature(0, "Rat, Giant", cr1.id);
		cPersister.persist(giantRat);
		
		List<Creature> fromDb = cPersister.fetchByChallengeRating(cr1_2.id);
		assertEquals(2, fromDb.size());
		assertTrue(fromDb.contains(bat));
		assertTrue(fromDb.contains(viper));
		
		fromDb = cPersister.fetchByChallengeRating(cr1.id);
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(giantRat));
		
		fromDb = cPersister.fetchByChallengeRating(cr4.id);
		assertEquals(1, fromDb.size());
		assertTrue(fromDb.contains(direBat));
	}
	
	@Test
	public void testCreatureEnvironmentTemperature() {
		ChallengeRating cr1_2 = new ChallengeRating(0, "1/2", 200, 65, 50, 35);
		crPersister.persist(cr1_2);
		
		Environment forests = new Environment(0, "Forests");
		ePersister.persist(forests);
		
		Environment underground = new Environment(0, "Underground");
		ePersister.persist(underground);
		
		Temperature temperate = new Temperature(0, "Temperate");
		tPersister.persist(temperate);
		
		Temperature warm = new Temperature(0, "Warm");
		tPersister.persist(warm);
		
		Creature bat = new Creature(0, "Bat", cr1_2.id);
		cPersister.persist(bat);
		
		cPersister.linkCreatureToEnvironment(bat.id, forests.id);
		cPersister.linkCreatureToEnvironment(bat.id, underground.id);
		cPersister.linkCreatureToTemperature(bat.id, temperate.id);
		cPersister.linkCreatureToTemperature(bat.id, warm.id);
		
		Creature viper = new Creature(0, "Viper", cr1_2.id);
		cPersister.persist(viper);
		
		cPersister.linkCreatureToEnvironment(viper.id, forests.id);
		cPersister.linkCreatureToTemperature(viper.id, warm.id);
		
		Creature dryad = new Creature(0, "Dryad", cr1_2.id);
		cPersister.persist(dryad);
		
		cPersister.linkCreatureToEnvironment(dryad.id, forests.id);
		cPersister.linkCreatureToTemperature(dryad.id, temperate.id);
		cPersister.linkCreatureToTemperature(dryad.id, warm.id);
		
		// temperate forests
		List<Creature> cFromDb = cPersister.fetchByEnvironmentAndTemperature(Arrays.asList(forests.id), 
				Arrays.asList(temperate.id));
		assertEquals(2, cFromDb.size());
		assertTrue(cFromDb.contains(bat));
		assertTrue(cFromDb.contains(dryad));
		
		// warm forests
		cFromDb = cPersister.fetchByEnvironmentAndTemperature(Arrays.asList(forests.id), 
				Arrays.asList(warm.id));
		assertEquals(3, cFromDb.size());
		assertTrue(cFromDb.contains(bat));
		assertTrue(cFromDb.contains(dryad));
		assertTrue(cFromDb.contains(viper));
		
		// any forests
		cFromDb = cPersister.fetchByEnvironmentAndTemperature(Arrays.asList(forests.id),
				Arrays.asList(temperate.id, warm.id));
		assertEquals(3, cFromDb.size());
		assertTrue(cFromDb.contains(bat));
		assertTrue(cFromDb.contains(dryad));
		assertTrue(cFromDb.contains(viper));
		
		// temperate underground
		cFromDb = cPersister.fetchByEnvironmentAndTemperature(Arrays.asList(underground.id),
				Arrays.asList(temperate.id));
		assertEquals(1, cFromDb.size());
		assertTrue(cFromDb.contains(bat));
		
		// temperate any
		cFromDb = cPersister.fetchByEnvironmentAndTemperature(Arrays.asList(forests.id, underground.id),
				Arrays.asList(temperate.id));
		assertEquals(2, cFromDb.size());
		assertTrue(cFromDb.contains(bat));
		assertTrue(cFromDb.contains(dryad));
	}
}
