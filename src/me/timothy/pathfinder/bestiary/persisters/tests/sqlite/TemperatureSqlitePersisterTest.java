package me.timothy.pathfinder.bestiary.persisters.tests.sqlite;

import static me.timothy.pathfinder.bestiary.persisters.tests.sqlite.SqliteTestUtil.getTestPath;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;
import me.timothy.pathfinder.bestiary.persisters.impl.sqlite.SqlitePersisterFactory;
import me.timothy.pathfinder.bestiary.persisters.tests.TemperaturePersisterTest;

public class TemperatureSqlitePersisterTest extends TemperaturePersisterTest {
	@BeforeClass
	public static void checkTestPath() throws IOException {
		SqliteTestUtil.checkTestPath();
	}
	
	protected SqlitePersisterFactory factory;
	
	@Before
	public void setupBefore() {
		factory = new SqlitePersisterFactory(getTestPath());
		persister = (TemperaturePersister) factory.getPersisterForClass(Temperature.class);
	}
	
	@After
	public void cleanupAfter() throws IOException {
		factory.close();
		factory = null;
		SqliteTestUtil.deleteTestPath();
	}
}
