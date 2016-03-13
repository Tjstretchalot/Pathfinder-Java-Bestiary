package me.timothy.pathfinder.bestiary.persisters;

import java.util.List;

import me.timothy.pathfinder.bestiary.models.Environment;

/**
 * An environment persister
 * 
 * @author Timothy
 * @see me.timothy.pathfinder.bestiary.persisters.Persister
 */
public interface EnvironmentPersister extends Persister<Environment> {
	/**
	 * Fetch an environment with the specified id.
	 * 
	 * @param id the id of the environment
	 * @return an environment with that id, or null
	 */
	public Environment fetchById(int id);
	
	/**
	 * Fetch environments with the specified name using a likeness
	 * check.
	 * 
	 * @param name the name of the environment
	 * @return environments with a name like {@code name} or an empty list
	 */
	public List<Environment> fetchLikeName(String name);
	
	/**
	 * Fetch an environment with the specified name using an equality
	 * check. May or may not be case sensitive.
	 * 
	 * @param name the name of the environment
	 * @return an environment with that name or null
	 */
	public Environment fetchByName(String name);
	
	/**
	 * Fetches the environments the specified creature can live in.
	 * 
	 * @param creatureId the creature id
	 * @return the list of environments for that creature, or an empty list
	 */
	public List<Environment> fetchByCreature(int creatureId);
}
