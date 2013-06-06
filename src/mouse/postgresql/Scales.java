package mouse.postgresql;

import java.util.ArrayList;
import java.util.Date;

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
		tableModels[0] = new Scale("scale1", "segment1", 0, 0, new Date());
		
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
	

}
