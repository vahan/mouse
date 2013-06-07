package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;


public class MeetingResults extends DbTable {
	
	public MeetingResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"rfid_from text," +
				"rfid_to text," +
				"start timestamp," +
				"stop timestamp," +
				"duration real," +
				"terminated_by integer," +
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
