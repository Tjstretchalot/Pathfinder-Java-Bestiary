package me.timothy.pathfinder.bestiary.persisters;

import java.util.List;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;

/**
 * A challenge rating persister
 * 
 * @author Timothy
 * @see me.timothy.pathfinder.bestiary.persisters.Persister
 */
public interface CRPersister extends Persister<ChallengeRating> {
	/**
	 * Fetch a challenge rating by its id
	 * @param id the id of the object
	 * @return challenge rating with id {@code id} or null if none is found
	 */
	public ChallengeRating fetchByID(int id);
	
	/**
	 * Fetch a challenge rating by using a likeness check
	 * on its name.
	 * @param name the name
	 * @return challenge ratings which have a name like {@code name} (may be empty, not null)
	 */
	public List<ChallengeRating> fetchLikeName(String name);
	
	/**
	 * Fetch a challenge rating with the specified name using a database equality
	 * check. May or may not be case-dependent.
	 * @param name the name of the challenge rating
	 * @return a challenge rating with the specified name, or null.
	 */
	public ChallengeRating fetchByName(String name);
}
