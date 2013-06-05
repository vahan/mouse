package mouse.postgresql;

import java.util.ArrayList;

import mouse.dbTableModels.Antenna;

public class Antennas extends DbTable {

	private ArrayList<Antenna> antennas = new ArrayList<Antenna>();

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS antennas (" +
				"id serial PRIMARY KEY," +
				"name text," +
				"position text," +
				"last_reading timestamp," +
				"box_id integer references boxes(id)" + 
			");";

		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
