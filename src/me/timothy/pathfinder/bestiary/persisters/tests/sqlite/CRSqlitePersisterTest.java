package me.timothy.pathfinder.bestiary.persisters.tests.sqlite;

import static me.timothy.pathfinder.bestiary.persisters.tests.sqlite.SqliteTestUtil.getTestPath;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;
import me.timothy.pathfinder.bestiary.persisters.impl.sqlite.SqlitePersisterFactory;
import me.timothy.pathfinder.bestiary.persisters.tests.CRPersisterTest;

public class CRSqlitePersisterTest extends CRPersisterTest {
	@BeforeClass
	public static void checkTestPath() throws IOException {
		SqliteTestUtil.checkTestPath();
	}
	
	protected SqlitePersisterFactory factory;
	
	@Before
	public void setupBefore() {
		factory = new SqlitePersisterFactory(getTestPath());
		persister = (CRPersister) factory.getPersisterForClass(ChallengeRating.class);
	}
	
	@After
	public void cleanupAfter() throws IOException {
		factory.close();
		factory = null;
		SqliteTestUtil.deleteTestPath();
	}
}
