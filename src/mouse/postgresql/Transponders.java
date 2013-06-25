package mouse.postgresql;

import java.util.HashMap;

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
	public DbTableRow createModel(HashMap<String, String> columnValues) {
		// TODO Auto-generated method stub
		String rfid = columnValues.get("rfid");
		String id = columnValues.get("id");
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


	@Override
	protected void initColumns() {
		columns.put("id", new DbTableColumn("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("rfid", new DbTableColumn("rfid", ColumnTypes.text, ""));
		columns.put("sex", new DbTableColumn("sex", ColumnTypes.text, ""));
		columns.put("first_reading", new DbTableColumn("first_reading", ColumnTypes.timestamp, ""));
		columns.put("last_reading", new DbTableColumn("last_reading", ColumnTypes.timestamp, ""));
		columns.put("first_scale_reading", new DbTableColumn("first_scale_reading", ColumnTypes.timestamp, ""));
		columns.put("last_scale_reading", new DbTableColumn("last_scale_reading", ColumnTypes.timestamp, ""));
		columns.put("last_antenna_id", new DbTableColumn("last_antenna_id", ColumnTypes.integer, "references antennas(id)"));
		columns.put("last_box_id", new DbTableColumn("last_box_id", ColumnTypes.integer, "references boxes(id)"));
		columns.put("stay_count", new DbTableColumn("stay_count", ColumnTypes.integer, ""));
		columns.put("meeting_count", new DbTableColumn("meeting_count", ColumnTypes.integer, ""));
		columns.put("balade_count", new DbTableColumn("balade_count", ColumnTypes.integer, ""));
	}
	

}
