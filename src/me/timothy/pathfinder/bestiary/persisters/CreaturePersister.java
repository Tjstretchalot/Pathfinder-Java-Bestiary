package me.timothy.pathfinder.bestiary.persisters;

import java.util.List;

import me.timothy.pathfinder.bestiary.models.Creature;

/**
 * A creature persister.
 * 
 * @author Timothy
 * @see me.timothy.pathfinder.bestiary.persisters.Persister
 */
public interface CreaturePersister extends Persister<Creature> {
	/**
	 * Fetches the creature with the specified id
	 * @param id the id 
	 * @return creature with id {@code id} or null
	 */
	public Creature fetchById(int id);
	
	/**
	 * Fetches creatures with the name using a likeness
	 * check.
	 * 
	 * @param name the name of the creature
	 * @return creatures like that name
	 */
	public List<Creature> fetchLikeName(String name);
	
	/**
	 * Fetches the creature with the name using an equality
	 * check. May or may not be case-sensitive. If multiple 
	 * creatures have the same name, the result should be treated
	 * as if it was chosen randomly.
	 * 
	 * @param name the name of the creature
	 * @return a creature with that name or null
	 */
	public Creature fetchByName(String name);
	
	/**
	 * Fetch creatures with the specified challenge rating id.
	 * 
	 * @param crId the challenge rating id
	 * @return creatures with that challenge rating id
	 */
	public List<Creature> fetchByChallengeRating(int crId);
	
	/**
	 * Links this creature with the specified environment
	 * @param creaId the creature id
	 * @param envId the environment id
	 */
	public void linkCreatureToEnvironment(int creaId, int envId);
	
	/**
	 * Links this creature with the specified temperature
	 * @param creaId the creature id
	 * @param tempId the temperature id
	 */
	public void linkCreatureToTemperature(int creaId, int tempId);
	
	/**
	 * Fetch the creatures that live in the specified environment
	 * 
	 * @param environmentId the environment id
	 * @return the creatures that can live in that environment, or an empty list
	 */
	public List<Creature> fetchByEnvironment(int environmentId);
	
	/**
	 * Fetch the creatures that live in the specified temperature
	 * 
	 * @param temperatureId the temperature id
	 * @return the creatures that can live in that temperature, or an empty list
	 */
	public List<Creature> fetchByTemperature(int temperatureId);
	
	/**
	 * Fetch the creatures that can live in some combination of the specified environment ids and temperature
	 * ids. For example, if the environments are forests and swamps, and the temperatures are temperate and tropical,
	 * the result would contain creatures that can live in temperate forests, tropical forests, temperate swamps, or
	 * tropical swamps.
	 * 
	 * @param environments the environments 
	 * @param temperatures the temperatures
	 * @return a list of creatures or an empty list
	 */
	public List<Creature> fetchByEnvironmentAndTemperature(Iterable<Integer> environments, Iterable<Integer> temperatures);
}
