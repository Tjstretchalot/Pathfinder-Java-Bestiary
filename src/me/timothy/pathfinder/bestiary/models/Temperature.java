package me.timothy.pathfinder.bestiary.models;

/**
 * Describes a temperature in pathfinder
 * 
 * @author Timothy
 */
public class Temperature {
	/**
	 * The id of this temperature
	 */
	public int id;
	
	/**
	 * The name of this temperature (such as Warm)
	 */
	public String name;

	/**
	 * @param id the id
	 * @param name the name
	 */
	public Temperature(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Temperature other = (Temperature) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Temperature [id=" + id + ", name=" + name + "]";
	}
	
	
}
