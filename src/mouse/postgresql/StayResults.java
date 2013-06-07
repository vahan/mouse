package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.StayResult;

public class StayResults extends DbTable {
	
	public StayResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"start timestamp," +
				"stop timestamp," +
				"duration real," +
				"rfid text," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)" +
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
