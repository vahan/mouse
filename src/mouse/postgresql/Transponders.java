package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Transponder;


public class Transponders extends DbStaticTable {
	
	public Transponders(String tableName, String[] transponderNames) {
		super(tableName, transponderNames, null);
		
		// TODO Auto-generated constructor stub
		generateTables();
	}

	@Override
	protected void generateTables() {
		tableModels = new Transponder[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			tableModels[i] = new Transponder(entries[i]);
		}
		
	}
	
	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"rfid text," +
				"sex text," +
				"first_reading timestamp," +
				"last_readin timestamp," +
				"first_scale_reading timestamp," +
				"last_scale_reading timestamp," +
				"last_antenna text," +
				"last_box text," +
				"stay_count integer," +
				"meeting_count integer," +
				"balaade_count integer" +
			");";
		
		return query;
	}

	
	@Override
	public String insertQuery(DbTableModel model) {
		Transponder tr = (Transponder) model;
		String[] fields = new String[] {"rfid", 
										"sex"
										};
		String[] values = new String[] {"'" + tr.getRfid() + "'", 
										"'" + tr.getSex() + "'"
										};
		String query = insertQuery(fields, values);
		
		return query;
	}
	

}
