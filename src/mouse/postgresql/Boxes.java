package mouse.postgresql;

import java.util.ArrayList;

import mouse.dbTableModels.Box;

public class Boxes extends DbTable {

	private ArrayList<Box> boxes = new ArrayList<Box>();
	

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS boxes (" +
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
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
