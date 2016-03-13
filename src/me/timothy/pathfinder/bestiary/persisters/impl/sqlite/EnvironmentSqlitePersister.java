package me.timothy.pathfinder.bestiary.persisters.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.timothy.pathfinder.bestiary.models.Environment;
import me.timothy.pathfinder.bestiary.persisters.EnvironmentPersister;

public class EnvironmentSqlitePersister extends SqlitePersister<Environment> implements EnvironmentPersister {

	public EnvironmentSqlitePersister(Connection connection) {
		super(connection, "environments");
	}

	@Override
	public void persist(Environment a) {
		try {
			PreparedStatement statement;
			if(a.id < 1) {
				statement = connection.prepareStatement("INSERT INTO " + table + " (name) VALUES (?)");
			}else {
				statement = connection.prepareStatement("UPDATE " + table + " SET name=? WHERE id=?");
			}
			
			int counter = 1;
			statement.setString(counter++, a.name);
			
			if(a.id < 1) {
				statement.execute();
				a.id = fetchLastInsertId();
			}else {
				statement.setInt(counter++, a.id);
				statement.execute();
			}
			statement.close();
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Environment fetchById(int id) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id=?");
			statement.setInt(1, id);
			
			Environment result = executeQueryForOne(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Environment> fetchLikeName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name LIKE ?");
			statement.setString(1, name);
			
			List<Environment> result = executeQueryForMany(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Environment fetchByName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name=?");
			statement.setString(1, name);
			
			Environment result = executeQueryForOne(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Environment> fetchByCreature(int creatureId) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM "
					+ "(SELECT environment_id FROM Environment_Creature WHERE creature_id=?) t1 "
					+ "LEFT OUTER JOIN " + table + " ON t1.environment_id=" + table + ".id");
			statement.setInt(1, creatureId);
			
			List<Environment> result = executeQueryForMany(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Environment fetchFromRow(ResultSet set) throws SQLException {
		return new Environment(set.getInt("id"), set.getString("name"));
	}
	

}
