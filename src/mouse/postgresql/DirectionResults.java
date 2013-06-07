package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;


public class DirectionResults extends DbTable {
	
	public DirectionResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"direction text," +
				"rfid text," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)," +
				"stay_id integer references stay_results(id)" +
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
