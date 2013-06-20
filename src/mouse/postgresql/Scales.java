package mouse.postgresql;


import mouse.TimeStamp;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.ScaleRow;


/**
 * Models the scales table
 * @author vahan
 *
 */
public class Scales extends DbTable {
	
	public Scales(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
		generateScales();
	}

	/**
	 * Generates scales
	 */
	private void generateScales() {
		// TODO Auto-generated method stub
		
		tableModels = new ScaleRow[1];
		tableModels[0] = new ScaleRow("scale1", "segment1", 0, 0, new TimeStamp());
		
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"segment text," +
				"x_pos real," +
				"y_pos real," +
				"last_reading timestamp" +
			");";
		
		return query;
	}

	@Override
	protected String[] insertFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
