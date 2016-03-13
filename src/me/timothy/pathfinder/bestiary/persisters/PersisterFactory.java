package me.timothy.pathfinder.bestiary.persisters;

/**
 * A standard factory-pattern for persisters. It is expected that
 * many database-connection orientied factories will utilize caching
 * where appropriate.
 * 
 * @author Timothy
 * @see <a href="https://en.wikipedia.org/wiki/Factory_method_pattern">Factory method pattern</a>
 */
public interface PersisterFactory {
	/**
	 * Get the appropriate persister for the specified
	 * object. Should be identical to:
	 * <code>
	 *   getPersisterForClass(object.getClass());
	 * </code>
	 * @param <A> the class the persister persists
	 * @param object the object
	 * @return the persister for the object
	 * @throws IllegalArgumentException if no persister for that object exists
	 */
	public <A> Persister<A> getPersisterFor(A object);
	
	/**
	 * Get the appropriate persister for the specified class.
	 * 
	 * @param <A> the class the persister persists
	 * @param cl the class
	 * @return the persister for that class
	 * @throws IllegalArgumentException if no persister for that object exists
	 */
	public <A> Persister<A> getPersisterForClass(Class<A> cl);
	
}
