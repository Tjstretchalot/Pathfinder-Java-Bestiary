package me.timothy.pathfinder.bestiary.models;

/**
 * Describes a challenge rating that an encounter or
 * creature might have.
 * 
 * @author Timothy
 */
public class ChallengeRating {
	/**
	 * The id of this challenge rating
	 */
	public int id;
	
	/**
	 * The name of the challenge rating. Numerical, such as:
	 * <ul>
	 *   <li>1/4</li>
	 *   <li>1/2</li>
	 *   <li>1</li>
	 * </ul>
	 */
	public String name;
	
	/**
	 * Roughly equal to the total experience that is given to an average
	 * group. Generally there is some rounding error when the experience
	 * is actually dished out to make play easier.
	 */
	public int totalExperience;
	
	/**
	 * How much experience is given to each individual in a small group (1-3 players)
	 */
	public int smallGroupIndividualExperience;
	
	/**
	 * How much experience is given to each individual in a medium group (4-5 players)
	 */
	public int mediumGroupIndividualExperience;
	
	/**
	 * How much experience is given to each individual in a large group (6+ players)
	 */
	public int largeGroupIndividualExperience;

	public ChallengeRating(int id, String name, int totalExperience, int smallGroupIndividualExperience,
			int mediumGroupIndividualExperience, int largeGroupIndividualExperience) {
		super();
		this.id = id;
		this.name = name;
		this.totalExperience = totalExperience;
		this.smallGroupIndividualExperience = smallGroupIndividualExperience;
		this.mediumGroupIndividualExperience = mediumGroupIndividualExperience;
		this.largeGroupIndividualExperience = largeGroupIndividualExperience;
	}
	
	@Override
	public String toString() {
		return "ChallengeRating [name=" + name + ", totalExperience=" + totalExperience
				+ ", smallGroupIndividualExperience=" + smallGroupIndividualExperience
				+ ", mediumGroupIndividualExperience=" + mediumGroupIndividualExperience
				+ ", largeGroupIndividualExperience=" + largeGroupIndividualExperience + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + largeGroupIndividualExperience;
		result = prime * result + mediumGroupIndividualExperience;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + smallGroupIndividualExperience;
		result = prime * result + totalExperience;
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
		ChallengeRating other = (ChallengeRating) obj;
		if (id != other.id)
			return false;
		if (largeGroupIndividualExperience != other.largeGroupIndividualExperience)
			return false;
		if (mediumGroupIndividualExperience != other.mediumGroupIndividualExperience)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (smallGroupIndividualExperience != other.smallGroupIndividualExperience)
			return false;
		if (totalExperience != other.totalExperience)
			return false;
		return true;
	}
}
