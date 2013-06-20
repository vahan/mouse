package mouse.postgresql;

import mouse.dbTableRows.DbTableRow;


/**
 * Models the scale_readings table
 * @author vahan
 *
 */
public class ScaleReadings extends DbTable {

	public ScaleReadings(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"timestamp timestamp," +
				"weight real," +
				"log_id integer references logs(id)," +
				"transponder_id integer references transponders(id)," +
				"scale_id integer references scales(id)" +
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
