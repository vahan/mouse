package mouse.postgresql;

import org.apache.commons.lang3.StringUtils;

import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Transponder;


/**
 * Models the transponders table
 * @author vahan
 *
 */
public class Transponders extends DbStaticTable {
	
	public Transponders(String tableName, String[] transponderNames) {
		super(tableName, transponderNames, null);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateTables() {
		tableModels = new Transponder[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			if (StringUtils.isEmpty(entries[i]))
				entries[i] = "-";	//TODO handle empty 'rfid's
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
	protected String[] insertFields() {
		String[] fields = new String[] {"rfid", 
				"sex"
				};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableModel model) {
		Transponder tr = (Transponder) model;
		String[] values = new String[] {"'" + tr.getRfid() + "'", 
										"'" + tr.getSex() + "'"
										};
		return values;
	}
	

}
