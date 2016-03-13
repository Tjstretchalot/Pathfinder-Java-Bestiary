package me.timothy.pathfinder.bestiary.persisters.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;

public class TemperatureSqlitePersister extends SqlitePersister<Temperature>
		implements TemperaturePersister {

	public TemperatureSqlitePersister(Connection connection) {
		super(connection, "temperatures");
	}
	
	@Override
	public void persist(Temperature a) {
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
	public Temperature fetchById(int id) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id=?");
			statement.setInt(1, id);
			
			Temperature result = executeQueryForOne(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Temperature> fetchLikeName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name LIKE ?");
			statement.setString(1, name);
			
			List<Temperature> result = executeQueryForMany(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Temperature fetchByName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name=?");
			statement.setString(1, name);
			
			Temperature result = executeQueryForOne(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Temperature> fetchByCreature(int creatureId) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM "
					+ "(SELECT temperature_id FROM Temperature_Creature WHERE creature_id=?) t1 "
					+ "LEFT OUTER JOIN " + table + " ON t1.temperature_id=" + table + ".id");
			statement.setInt(1, creatureId);
			
			List<Temperature> result = executeQueryForMany(statement);
			
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Temperature fetchFromRow(ResultSet set) throws SQLException {
		return new Temperature(set.getInt("id"), set.getString("name"));
	}

}
