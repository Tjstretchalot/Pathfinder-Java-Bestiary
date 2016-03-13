package me.timothy.pathfinder.bestiary.persisters.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.timothy.pathfinder.bestiary.persisters.Persister;

/**
 * Contains some helpful methods for sqlite persisters
 * 
 * @author Timothy
 */
public abstract class SqlitePersister<A> implements Persister<A> {
	protected Connection connection;
	protected Logger logger;
	protected String table;
	
	/**
	 * Fetches the current object in the row.
	 * 
	 * @param set the set
	 * @return the object in the row
	 * @throws SQLException if one occurs
	 */
	protected abstract A fetchFromRow(ResultSet set) throws SQLException;
	
	/**
	 * Creates the sqlite persister connected to the specified table
	 * 
	 * @param connection the sqlite connection to use
	 * @param table the table
	 */
	protected SqlitePersister(Connection connection, String table) {
		this.connection = connection;
		this.table = table;
		this.logger = LogManager.getLogger(getClass());
	}

	/**
	 * Executes the specified statement, which is assumed to find exactly
	 * one or zero objects. Does not close the statement.
	 * 
	 * @param statement the statement to execute
	 * @return object in first row, or null
	 * @throws SQLException if one occurs
	 */
	protected A executeQueryForOne(PreparedStatement statement) throws SQLException {
		ResultSet results = statement.executeQuery();
		
		A result = null;
		if(results.next()) {
			result = fetchFromRow(results);
		}
		
		results.close();
		return result;
	}
	
	/**
	 * Executes the specified statement, and returns a list of all the objects
	 * in it. Does not close the statement.
	 * 
	 * @param statement the statement to execute
	 * @return objects or an empty list
	 * @throws SQLException if one occurs
	 */
	protected List<A> executeQueryForMany(PreparedStatement statement) throws SQLException {
		ResultSet results = statement.executeQuery();
		
		List<A> result = new ArrayList<>();
		while(results.next()) {
			result.add(fetchFromRow(results));
		}
		
		results.close();
		return result;
	}
	
	/**
	 * Fetches the last insert id from the connection
	 * @return the last insert id
	 * @throws SQLException if one occurs
	 */
	protected int fetchLastInsertId() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT last_insert_rowid()");
		
		ResultSet results = statement.executeQuery();
		results.next();
		int id = results.getInt(1);
		results.close();
		
		statement.close();
		
		return id;
	}
	
	@Override
	public List<A> fetchAll() {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
			List<A> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}
}
