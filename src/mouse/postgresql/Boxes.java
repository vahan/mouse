package mouse.postgresql;


/**
 * Models the boxes table
 * @author vahan
 *
 */
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;

public class Boxes extends DbStaticTable {
	
	public static final int BOX_COUNT = 44;

	public Boxes(String tableName, String[] boxNames) {
		super(tableName, boxNames, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateTables() {
		tableModels = new BoxRow[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			tableModels[i] = new BoxRow(entries[i], 0, 0); //TODO Give proper xPos and yPos
		}
		
	}

	/**
	 * Searches, finds and returns the box with the given name from the DB
	 * @param boxName	The name of the searched box
	 * @return	a Box object, with the searched name
	 */
	public static BoxRow getBoxByName(String boxName) {
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
	protected String[] insertFields() {
		String[] fields = new String[] {"name", 
				"segment", 
				"x_pos", 
				"y_pos"
				};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		BoxRow box = (BoxRow) model;
		String[] values = new String[] {"'" + box.getName() + "'", 
										"'" + box.getSegment() + "'", 
										"'" + box.getXPos() + "'", 
										"'" + box.getYPos() + "'"
										};
		return values;
	}
	
}
