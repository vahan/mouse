package mouse.postgresql;


import mouse.dbTableModels.Box;
import mouse.dbTableModels.DbTableModel;

public class Boxes extends DbStaticTable {
	
	public static final int BOX_COUNT = 44;

	public Boxes(String tableName, String[] boxNames) {
		super(tableName, boxNames, null);
		// TODO Auto-generated constructor stub
		generateTables();
	}

	@Override
	protected void generateTables() {
		tableModels = new Box[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			tableModels[i] = new Box(entries[i], 0, 0); //TODO Give proper xPos and yPos
		}
		
	}
	
	/**
	 * Initializes boxes
	 */
	@Deprecated
	private void generateBoxes() {
		// TODO Change to reading the names from the files. See Transponders
		
		tableModels = new Box[BOX_COUNT];
		
		for (int i = 0; i < BOX_COUNT; ++i) {
			tableModels[i] = new Box(Integer.toString(i + 1), 0, 0); //TODO: set proper x_pos and y_pos attributes
		}
		
	}

	/**
	 * Searches, finds and returns the box with the given name from the DB
	 * @param boxName	The name of the searched box
	 * @return	a Box object, with the searched name
	 */
	public static Box getBoxByName(String boxName) {
		//TODO Implement this!
		
		
		return null;
	}
	

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"name text," +
				"segment text," +
				"x_pos real," +
				"y_pos real," +
				"last_direction_result timestamp," +
				"last_meeting timestamp" +
			");";
		
		return query;
	}

	@Override
	public String insertQuery(DbTableModel model) {
		Box box = (Box) model;
		String[] fields = new String[] {"name", 
										"segment", 
										"x_pos", 
										"y_pos"
										};
		String[] values = new String[] {"'" + box.getName() + "'", 
										"'" + box.getSegment() + "'", 
										"'" + box.getXPos() + "'", 
										"'" + box.getYPos() + "'"
										};
		String query = insertQuery(fields, values);
		
		return query;
	}
	
}
