package me.timothy.pathfinder.bestiary.persisters.tests.sqlite;

import static me.timothy.pathfinder.bestiary.persisters.tests.sqlite.SqliteTestUtil.getTestPath;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.models.Creature;
import me.timothy.pathfinder.bestiary.models.Environment;
import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;
import me.timothy.pathfinder.bestiary.persisters.CreaturePersister;
import me.timothy.pathfinder.bestiary.persisters.EnvironmentPersister;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;
import me.timothy.pathfinder.bestiary.persisters.impl.sqlite.SqlitePersisterFactory;
import me.timothy.pathfinder.bestiary.persisters.tests.PersisterIntegrationTests;

public class SqliteIntegrationTests extends PersisterIntegrationTests {
	@BeforeClass
	public static void checkTestPath() throws IOException {
		SqliteTestUtil.checkTestPath();
	}
	
	protected SqlitePersisterFactory factory;
	
	@Before
	public void setupBefore() {
		factory = new SqlitePersisterFactory(getTestPath());
		ePersister = (EnvironmentPersister) factory.getPersisterForClass(Environment.class);
		tPersister = (TemperaturePersister) factory.getPersisterForClass(Temperature.class);
		cPersister = (CreaturePersister) factory.getPersisterForClass(Creature.class);
		crPersister = (CRPersister) factory.getPersisterForClass(ChallengeRating.class);
	}
	
	@After
	public void cleanupAfter() throws IOException {
		factory.close();
		factory = null;
		ePersister = null;
		tPersister = null;
		cPersister = null;
		crPersister = null;
		SqliteTestUtil.deleteTestPath();
	}
}
