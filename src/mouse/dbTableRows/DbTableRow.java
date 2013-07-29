package mouse.dbTableRows;

/**
 * An abstract base class for all table row model classes.
 * 
 * @author vahan
 * 
 */
public abstract class DbTableRow {

	protected String id;

	public String getId() {
		return id;
	}

	/**
	 * The ID must be set after the according enty is inserted into the db table
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Use only the id for the hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Use only the id for comparing objects for equality
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbTableRow other = (DbTableRow) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [id=" + id + "]";
	}

}
