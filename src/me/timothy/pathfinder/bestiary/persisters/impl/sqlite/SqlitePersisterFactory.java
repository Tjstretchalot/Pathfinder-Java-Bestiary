package me.timothy.pathfinder.bestiary.persisters.impl.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.models.Creature;
import me.timothy.pathfinder.bestiary.models.Environment;
import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;
import me.timothy.pathfinder.bestiary.persisters.CreaturePersister;
import me.timothy.pathfinder.bestiary.persisters.EnvironmentPersister;
import me.timothy.pathfinder.bestiary.persisters.Persister;
import me.timothy.pathfinder.bestiary.persisters.PersisterFactory;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;

public class SqlitePersisterFactory implements PersisterFactory {
	private static final Logger logger = LogManager.getLogger();
	
	private Connection connection;
	private Path databaseFile;
	private CreaturePersister creaturePersister;
	private CRPersister crPersister;
	private EnvironmentPersister environmentPersister;
	private TemperaturePersister temperaturePersister;
	
	public SqlitePersisterFactory() {
		this(Paths.get(System.getProperty("user.home"), "Documents", "My Games", "Pathfinder", "pathfinder.db"));
	}
	public SqlitePersisterFactory(Path db) {
		Path folder = db.getParent();
		
		try {
			Files.createDirectories(folder);
		} catch (IOException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + db.toAbsolutePath().toString());
		}catch(SQLException ex) {
			logger.throwing(ex);
			throw new RuntimeException(ex);
		}
		
		databaseFile = db;
		
		try {
			PreparedStatement statement;
			
			// Core tables
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS challenge_ratings ("
					+ "id INTEGER PRIMARY KEY, "
					+ "name TEXT NOT NULL, "
					+ "total_xp INTEGER NOT NULL, "
					+ "sm_group_xp INTEGER NOT NULL, "
					+ "md_group_xp INTEGER NOT NULL, "
					+ "lg_group_xp INTEGER NOT NULL)");
			statement.execute();
			statement.close();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS creatures ("
					+ "id INTEGER PRIMARY KEY, "
					+ "name TEXT NOT NULL, "
					+ "challenge_rating_id INTEGER NOT NULL,"
					+ "FOREIGN KEY(challenge_rating_id) REFERENCES challenge_ratings(id))");
			statement.execute();
			statement.close();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS environments ("
					+ "id INTEGER PRIMARY KEY, "
					+ "name TEXT NOT NULL)");
			statement.execute();
			statement.close();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS temperatures ("
					+ "id INTEGER PRIMARY KEY,"
					+ "name TEXT NOT NULL)");
			statement.execute();
			statement.close();
			
			// Technical tables
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Environment_Creature ("
					+ "environment_id INTEGER, "
					+ "creature_id INTEGER, "
					+ "FOREIGN KEY(environment_id) REFERENCES environments(id), "
					+ "FOREIGN KEY(creature_id) REFERENCES creatures(id))");
			statement.execute();
			statement.close();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Temperature_Creature ("
					+ "temperature_id INTEGER, "
					+ "creature_id INTEGER, "
					+ "FOREIGN KEY(temperature_id) REFERENCES temperatures(id), "
					+ "FOREIGN KEY(creature_id) REFERENCES creatures(id))");
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
		
		creaturePersister = new CreatureSqlitePersister(connection);
		crPersister = new CRSqlitePersister(connection);
		environmentPersister = new EnvironmentSqlitePersister(connection);
		temperaturePersister = new TemperatureSqlitePersister(connection);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <A> Persister<A> getPersisterFor(A object) {
		if(object instanceof Creature) {
			return (Persister<A>) creaturePersister;
		}else if(object instanceof ChallengeRating) {
			return (Persister<A>) crPersister;
		}else if(object instanceof Environment) {
			return (Persister<A>) environmentPersister;
		}else if(object instanceof Temperature) {
			return (Persister<A>) temperaturePersister;
		}
		throw new IllegalArgumentException("Unknown object " + object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A> Persister<A> getPersisterForClass(Class<A> cl) {
		if(cl == Creature.class) {
			return (Persister<A>) creaturePersister;
		}else if(cl == ChallengeRating.class) {
			return (Persister<A>) crPersister;
		}else if(cl == Environment.class) {
			return (Persister<A>) environmentPersister;
		}else if(cl == Temperature.class) {
			return (Persister<A>) temperaturePersister;
		}
		throw new IllegalArgumentException("Unknown class " + cl);
	}

	public void close() {
		if(connection == null)
			return;
		
		try {
			connection.close();
			connection = null;
			creaturePersister = null;
			crPersister = null;
			environmentPersister = null;
			temperaturePersister = null;
		} catch (SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}
	
	public Path getDatabaseFile() {
		return databaseFile;
	}
}
