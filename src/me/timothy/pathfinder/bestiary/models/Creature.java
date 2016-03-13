package me.timothy.pathfinder.bestiary.models;

/*
 * It should be noted that Creatures have a many-to-many relationship with
 * temperature (through Creature_Temperature) and envirnoment (through Creature_Environment).
 */
/**
 * Describes a creature in pathfinder.
 * 
 * @author Timothy
 */
public class Creature {
	/**
	 * The creatures unique id
	 */
	public int id;
	
	/**
	 * Enough identifying information that the creature can be pulled up on the
	 * paizo reference document. E.g. "Aasimar cleric 1" or "Giant Amoeba"
	 */
	public String name;
	
	/**
	 * The id of the challenge rating that identifies this creature
	 */
	public int challengeRatingId;

	/**
	 * @param id creatures id
	 * @param name creatures name
	 * @param challengeRatingId creatures challenge rating
	 */
	public Creature(int id, String name, int challengeRatingId) {
		this.id = id;
		this.name = name;
		this.challengeRatingId = challengeRatingId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + challengeRatingId;
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
		Creature other = (Creature) obj;
		if (challengeRatingId != other.challengeRatingId)
			return false;
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
		return "Creature [id=" + id + ", name=" + name + ", challengeRatingId=" + challengeRatingId + "]";
	}

	
}
