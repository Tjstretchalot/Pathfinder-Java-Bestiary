package me.timothy.pathfinder.bestiary.persisters;

import java.util.List;

/**
 * <p>Similar to serialization, something is a persister
 * if it is capable of storing a particular object.</p>
 * 
 * <p>The aim of this persister model is to maintain separation
 * of concerns, and thus the ability to support multiple database
 * models, while keeping most of the convenience of active records.
 * The main disadvantage is having to pass more objects (persisters)
 * around, but this can be alleviated using a factory pattern</p>
 * 
 * <p>For example, you could have a Cat, which you might
 * want to save using Json, Mysql, or SQLite, then you would
 * have a CatJsonPersister, CatMysqlPersister, and CatSqlitePersister.
 * The configuration code would decide which persister to use, which 
 * would then be passed where necessary.</p>
 * 
 * <p>Often times it is helpful to have hard-typed information about
 * an object, for example if you wanted to fetch cats by fur type. In
 * this case, an interface extending Persister should be used, e.g.
 * CatPersister, defining any additional functions a cat persister
 * should use.</p>
 * 
 * <p>Despite its name, persisters handle both saving and acquiring objects.
 * Persisters should <i>never</i> return a different object then the persisters
 * object. For example, if FurType is an object, a CatPersister should <i>not</i>
 * have a function to get it directly. If its a many-to-one or one-to-one, the CatPersister
 * may return the fur types id. The FurTypePersister may then convert that id into
 * a FurType.</p>
 * 
 * <p>Function names should start with either persist, fetch, or link.</p>
 * 
 * @author Timothy
 * @see <a href="https://en.wikipedia.org/wiki/Active_record_pattern">Active record pattern</a>
 */
public interface Persister<A> {
	/**
	 * This ensures the object matches its database entry, creating
	 * the entry if it does not exist.
	 * 
	 * @param a the object to persist
	 */
	public void persist(A a);
	
	/**
	 * Fetch all of this persisters object type
	 * 
	 * @return all A
	 */
	public List<A> fetchAll();
}
