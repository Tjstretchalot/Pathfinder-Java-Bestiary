package me.timothy.pathfinder.bestiary.persisters.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;

/**
 * A challenge rating sqlite persister
 * 
 * @author Timothy
 */
public class CRSqlitePersister extends SqlitePersister<ChallengeRating> implements CRPersister {

	protected CRSqlitePersister(Connection connection) {
		super(connection, "challenge_ratings");
	}

	@Override
	public void persist(ChallengeRating a) {
		try {
			PreparedStatement statement;
			if(a.id < 1) {
				statement = connection.prepareStatement("INSERT INTO " + table + " (name, total_xp, sm_group_xp, "
						+ "md_group_xp, lg_group_xp) VALUES (?, ?, ?, ?, ?)");
			}else {
				statement = connection.prepareStatement("UPDATE " + table + " SET name=?, total_xp=?, sm_group_xp=?, "
						+ "md_group_xp=?, lg_group_xp=? WHERE id=?");
			}
			
			int counter = 1;
			statement.setString(counter++, a.name);
			statement.setInt(counter++, a.totalExperience);
			statement.setInt(counter++, a.smallGroupIndividualExperience);
			statement.setInt(counter++, a.mediumGroupIndividualExperience);
			statement.setInt(counter++, a.largeGroupIndividualExperience);
			
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
	public ChallengeRating fetchByID(int id) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id=?");
			statement.setInt(1, id);
			
			ChallengeRating result = executeQueryForOne(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ChallengeRating> fetchLikeName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name LIKE ?");
			statement.setString(1, name);
			
			List<ChallengeRating> result = executeQueryForMany(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public ChallengeRating fetchByName(String name) {
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE name=?");
			statement.setString(1, name);
			
			ChallengeRating result = executeQueryForOne(statement);
			statement.close();
			return result;
		}catch(SQLException e) {
			logger.throwing(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected ChallengeRating fetchFromRow(ResultSet set) throws SQLException {
		return new ChallengeRating(set.getInt("id"), set.getString("name"), set.getInt("total_xp"),
				set.getInt("sm_group_xp"), set.getInt("md_group_xp"), set.getInt("lg_group_xp"));
	}

}
