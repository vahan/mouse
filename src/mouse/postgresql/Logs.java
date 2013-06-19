package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;


/**
 * Models the logs table
 * @author vahan
 *
 */
public class Logs extends DbTable {

	public Logs(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"first_reading timestamp," +
				"last_reading timestamp," +
				"duration real," +
				"imported_at timestamp," +
				"nb_readings integer," +
				"size real" +
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
