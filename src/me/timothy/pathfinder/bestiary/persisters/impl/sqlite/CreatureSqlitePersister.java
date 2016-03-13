package me.timothy.pathfinder.bestiary.persisters.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.timothy.pathfinder.bestiary.models.Creature;
import me.timothy.pathfinder.bestiary.persisters.CreaturePersister;

/**
 * Utilizes a sqlite connection to persist creatures
 * 
 * @author Timothy
 */
public class CreatureSqlitePersister extends SqlitePersister<Creature> implements CreaturePersister {
	public CreatureSqlitePersister(Connection conn) {
		super(conn, "creatures");
	}

	@Override
	protected Creature fetchFromRow(ResultSet set) throws SQLException {
		return new Creature(set.getInt("id"), set.getString("name"), set.getInt("challenge_rating_id"));
	}
	
	@Override
	public void persist(Creature creature) {
		try {
			PreparedStatement statement;
			if(creature.id < 1) {
				statement = connection.prepareStatement("INSERT INTO " + table + " (name, challenge_rating_id) VALUES (?, ?)");
			}else {
				statement = connection.prepareStatement("UPDATE " + table + " SET name=?, challenge_rating_id=? WHERE id=?");
			}
			
			int counter = 1;
			statement.setString(counter++, creature.name);
			statement.setInt(counter++, creature.challengeRatingId);
			
			if(creature.id < 1) {
				statement.execute();
				creature.id = fetchLastInsertId();
			}else {
				statement.setInt(counter++, creature.id);
				statement.execute();
			}
			statement.close();
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Creature fetchById(int id) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id=?");
			statement.setInt(1, id);
			
			Creature result = executeQueryForOne(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Creature> fetchLikeName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name LIKE ?");
			statement.setString(1, name);
			
			List<Creature> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Creature fetchByName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name=?");
			statement.setString(1, name);
			
			Creature result = executeQueryForOne(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Creature> fetchByChallengeRating(int crId) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE challenge_rating_id=?");
			statement.setInt(1, crId);
			
			List<Creature> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Creature> fetchByEnvironment(int environmentId) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT id, name, challenge_rating_id FROM "
					+ "(SELECT creature_id, environment_id FROM Environment_Creature WHERE environment_id = ?) t1 "
					+ "LEFT OUTER JOIN creatures ON t1.creature_id = creatures.id");
			statement.setInt(1, environmentId);
			
			List<Creature> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Creature> fetchByTemperature(int temperatureId) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT id, name, challenge_rating_id FROM "
					+ "(SELECT creature_id, temperature_id FROM Temperature_Creature WHERE temperature_id = ?) t1 "
					+ "LEFT OUTER JOIN creatures ON t1.creature_id = creatures.id");
			statement.setInt(1, temperatureId);
			
			List<Creature> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void linkCreatureToEnvironment(int creaId, int envId) {
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Environment_Creature (creature_id, "
					+ "environment_id) VALUES (?, ?)");
			statement.setInt(1, creaId);
			statement.setInt(2, envId);
			
			statement.execute();
			statement.close();
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void linkCreatureToTemperature(int creaId, int tempId) {
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Temperature_Creature (creature_id, "
					+ "temperature_id) VALUES (?, ?)");
			statement.setInt(1, creaId);
			statement.setInt(2, tempId);
			
			statement.execute();
			statement.close();
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<Creature> fetchByEnvironmentAndTemperature(Iterable<Integer> environments,
			Iterable<Integer> temperatures) {
		try {
			boolean first = true;
			String temperatureWhere = "";
			for(@SuppressWarnings("unused") int t : temperatures) {
				if(!first) {
					temperatureWhere += " OR ";
				}else {
					first = false;
				}
				temperatureWhere += "temperature_id=?";
			}
			
			String environmentWhere = "";
			first = true;
			for(@SuppressWarnings("unused") int e : environments) {
				if(!first) {
					environmentWhere += " OR ";
				}else {
					first = false;
				}
				
				environmentWhere += "environment_id=?";
			}
			PreparedStatement statement = connection.prepareStatement("SELECT t2.creature_id as id, t2.name as name, t2.challenge_rating_id as challenge_rating_id FROM "
					+ "((SELECT DISTINCT creature_id FROM Temperature_Creature WHERE " + temperatureWhere + ") t1 LEFT OUTER JOIN "
					+ "creatures ON t1.creature_id=creatures.id) t2 INNER JOIN ((SELECT DISTINCT creature_id FROM Environment_Creature "
					+ "WHERE " + environmentWhere + ") t3 LEFT OUTER JOIN creatures ON t3.creature_id=creatures.id) t4 "
					+ "ON t2.creature_id = t4.creature_id");
			
			int counter = 1;
			for(int temp : temperatures) {
				statement.setInt(counter++, temp);
			}
			for(int env : environments) {
				statement.setInt(counter++, env);
			}
			
			List<Creature> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}
}
