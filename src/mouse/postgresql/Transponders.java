package mouse.postgresql;

import java.text.ParseException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import mouse.TimeStamp;
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
		
	}
	
	
	public String updateCountsQuery() {
		String[] fields = new String[] {"stay_count", "meeting_count", "balade_count"};
		String[][] values = new String[tableModels.length][fields.length];
		
		for (int i = 0; i < tableModels.length; ++i) {
			TransponderRow tr = (TransponderRow) tableModels[i];
			values[i][0] = Integer.toString(tr.getStayCount());
			values[i][1] = Integer.toString(tr.getMeetingCount());
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
		String rfid = columnValues.get("rfid");
		String id = columnValues.get("id");
		//Gender sex = Enum.valueOf(Gender.class, columnValues.get("sex")); //done automatically
		TimeStamp firstReading, lastReading, firstScaleReading, lastScaleReading;
		String lastAntennaId = columnValues.get("last_antenna_id");
		//String lastBoxId = columnValues.get("last_box_id"); //done automatically
		int stayCount = Integer.parseInt(columnValues.get("stay_count"));
		int meetingCount = Integer.parseInt(columnValues.get("meeting_count"));
		int baladeCount = Integer.parseInt(columnValues.get("balade_count"));
		try {
			firstReading = new TimeStamp(columnValues.get("first_reading"), TimeStamp.dbFormat);
			lastReading = new TimeStamp(columnValues.get("last_reading"), TimeStamp.dbFormat);
			//firstScaleReading = new TimeStamp(columnValues.get("first_scale_reading"), TimeStamp.dbFormat);
			//lastScaleReading = new TimeStamp(columnValues.get("last_scale_reading"), TimeStamp.dbFormat);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		TransponderRow tr = new TransponderRow(rfid);
		tr.setId(id);
		tr.setFirstReading(firstReading);
		tr.setLastReading(lastReading);
		//tr.setFirstScaleReading(firstScaleReading);
		//tr.setLastScaleReading(lastScaleReading);
		tr.setLastAntennaId(lastAntennaId);
		tr.setStayCount(stayCount);
		tr.setMeetingCount(meetingCount);
		tr.setBaladeCount(baladeCount);
		
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
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("rfid", new DbEntry("rfid", ColumnTypes.text, ""));
		columns.put("sex", new DbEntry("sex", ColumnTypes.text, ""));
		columns.put("first_reading", new DbEntry("first_reading", ColumnTypes.timestamp, ""));
		columns.put("last_reading", new DbEntry("last_reading", ColumnTypes.timestamp, ""));
		columns.put("first_scale_reading", new DbEntry("first_scale_reading", ColumnTypes.timestamp, ""));
		columns.put("last_scale_reading", new DbEntry("last_scale_reading", ColumnTypes.timestamp, ""));
		columns.put("last_antenna_id", new DbEntry("last_antenna_id", ColumnTypes.integer, "references antennas(id)"));
		columns.put("last_box_id", new DbEntry("last_box_id", ColumnTypes.integer, "references boxes(id)"));
		columns.put("stay_count", new DbEntry("stay_count", ColumnTypes.integer, ""));
		columns.put("meeting_count", new DbEntry("meeting_count", ColumnTypes.integer, ""));
		columns.put("balade_count", new DbEntry("balade_count", ColumnTypes.integer, ""));
	}
	

}
