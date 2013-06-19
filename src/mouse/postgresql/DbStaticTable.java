package mouse.postgresql;

/**
 * Abstract base class to model those tables that are filled before process the input data
 * @author vahan
 *
 */
public abstract class DbStaticTable extends DbTable {

	/**
	 * Entry values to be stored. For example, antenna, box or transponder names
	 */
	protected String[] entries;
	/**
	 * Additional data to be used. Particularly, used by antennas, as corresponding box names
	 */
	protected String[] data;
	
	protected DbStaticTable(String tableName, String[] entries, String[] data) {
		super(tableName);
		// TODO Auto-generated constructor stub

		this.entries = entries;
		this.data = data;
		
		generateTables();
	}
	

	/**
	 * Each implementation of this abstract method generates the corresponding entries (possibly used data)
	 */
	protected abstract void generateTables();



}
