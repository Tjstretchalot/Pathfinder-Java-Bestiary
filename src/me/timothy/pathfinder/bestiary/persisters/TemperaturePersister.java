package me.timothy.pathfinder.bestiary.persisters;

import java.util.List;

import me.timothy.pathfinder.bestiary.models.Temperature;

public interface TemperaturePersister extends Persister<Temperature> {
	/**
	 * Fetch an temperature with the specified id.
	 * 
	 * @param id the id of the temperature
	 * @return an temperature with that id, or null
	 */
	public Temperature fetchById(int id);
	
	/**
	 * Fetch temperatures with the specified name using a likeness
	 * check.
	 * 
	 * @param name the name of the temperature
	 * @return temperatures with a name like {@code name} or an empty list
	 */
	public List<Temperature> fetchLikeName(String name);
	
	/**
	 * Fetch an temperature with the specified name using an equality
	 * check. May or may not be case sensitive.
	 * 
	 * @param name the name of the temperature
	 * @return an temperature with that name or null
	 */
	public Temperature fetchByName(String name);
	
	/**
	 * Fetch the temperatures that the specified creature id
	 * can live in.
	 * 
	 * @param creatureId the creature id
	 * @return the list of temperatures for that creature, or an empty list
	 */
	public List<Temperature> fetchByCreature(int creatureId);
}
