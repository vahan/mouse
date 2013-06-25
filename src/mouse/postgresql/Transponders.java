package mouse.postgresql;

import org.apache.commons.lang3.StringUtils;

import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.TransponderRow;


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
	
	
	public String updateCountsQuery() {
		String[] fields = new String[] {"stay_count", "meeting_count", "balade_count"};
		String[][] values = new String[tableModels.length][fields.length];
		
		for (int i = 0; i < tableModels.length; ++i) {
			TransponderRow tr = (TransponderRow) tableModels[i];
			values[i][0] = Integer.toString(tr.getStayCount());
			values[i][1] = Integer.toString(tr.getMeetingsCount());
			values[i][2] = Integer.toString(tr.getBaladeCount());
		}
		
		return updateQuery(fields, values);
	}

	@Override
	protected void generateTables() {
		tableModels = new TransponderRow[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			if (StringUtils.isEmpty(entries[i]))
				entries[i] = "-";	//TODO handle empty 'rfid's
			tableModels[i] = new TransponderRow(entries[i]);
		}
		
	}
	
	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"rfid text," +
				"sex text," +
				"first_reading timestamp," +
				"last_reading timestamp," +
				"first_scale_reading timestamp," +
				"last_scale_reading timestamp," +
				"last_antenna_id integer references antennas(id)," +
				"last_box_id integer references boxes(id)," +
				"stay_count integer," +
				"meeting_count integer," +
				"balade_count integer" +
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
	protected String[] insertValues(DbTableRow model) {
		TransponderRow tr = (TransponderRow) model;
		String[] values = new String[] {"'" + tr.getRfid() + "'", 
										"'" + tr.getSex() + "'"
										};
		return values;
	}
	
	@Override
	public String[] getColumnNames() {
		String[] fields = new String[] {"rfid", 
										"id"
										};
		return fields;
	}

	@Override
	public DbTableRow createModel(String[] columnValues) {
		// TODO Auto-generated method stub
		String rfid = columnValues[0];
		String id = columnValues[1];
		TransponderRow tr = new TransponderRow(rfid);
		tr.setId(id);
		return tr;
	}

	@Override
	public void setTableModels(Object[] array) {
		for (int i = 0; i < array.length; ++i) {
			tableModels[i] = (TransponderRow) array[i];
		}
	}
	

}
