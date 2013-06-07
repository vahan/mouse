package mouse.postgresql;


import mouse.TimeStamp;
import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Scale;


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
		
		tableModels = new Scale[1];
		tableModels[0] = new Scale("scale1", "segment1", 0, 0, new TimeStamp());
		
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
	protected String[] insertValues(DbTableModel model) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
