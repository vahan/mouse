package mouse.postgresql;

import java.text.ParseException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import mouse.TimeStamp;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.TransponderRow;

/**
 * Models the transponders table
 * 
 * @author vahan
 * 
 */
public class Transponders extends DbStaticTable {

	public Transponders(String[] transponderNames, boolean generate) {
		super("transponders", transponderNames, generate);

		if (generate)
			generateTables();
		if (tableModels == null)
			tableModels = new TransponderRow[names.length];
	}

	public Transponders() {
		this(new String[] { "" }, false);
	}

	public String updateCountsQuery() {
		String[] fields = new String[] { "stay_count", "meeting_count",
				"balade_count" };
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
		if (tableModels == null)
			tableModels = new TransponderRow[names.length];
		for (int i = 0; i < names.length; ++i) {
			if (StringUtils.isEmpty(names[i]))
				names[i] = "-"; // TODO handle empty 'rfid's
			tableModels[i] = TransponderRow.getTransponderRow(names[i]);
		}

	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] { "rfid", "sex" };
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		TransponderRow tr = (TransponderRow) model;
		String[] values = new String[] { "'" + tr.getRfid() + "'",
				"'" + tr.getSex() + "'" };
		return values;
	}

	@Override
	public DbTableRow createModel(HashMap<String, String> columnValues) {
		String rfid = columnValues.get("rfid");
		String id = columnValues.get("id");
		// Gender sex = Enum.valueOf(Gender.class, columnValues.get("sex"));
		// //done automatically
		TimeStamp firstReading, lastReading;
		String lastAntennaId = columnValues.get("last_antenna_id");
		if (StringUtils.isEmpty(lastAntennaId))
			lastAntennaId = "-1";
		// String lastBoxId = columnValues.get("last_box_id"); //done
		// automatically
		String stayCountStr = columnValues.get("stay_count");
		int stayCount = StringUtils.isEmpty(stayCountStr) ? 0 : Integer
				.parseInt(stayCountStr);
		String meetingCountStr = columnValues.get("meeting_count");
		int meetingCount = StringUtils.isEmpty(stayCountStr) ? 0 : Integer
				.parseInt(meetingCountStr);
		String baladeCountStr = columnValues.get("balade_count");
		int baladeCount = StringUtils.isEmpty(baladeCountStr) ? 0 : Integer
				.parseInt(baladeCountStr);
		try {
			String firstReadingStr = columnValues.get("first_reading");
			firstReading = StringUtils.isEmpty(firstReadingStr) ? null
					: new TimeStamp(firstReadingStr, TimeStamp.getDbFormat());
			String lastReadingStr = columnValues.get("last_reading");
			lastReading = StringUtils.isEmpty(lastReadingStr) ? null
					: new TimeStamp(lastReadingStr, TimeStamp.getDbFormat());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		TransponderRow tr = TransponderRow.getTransponderRow(rfid);
		tr.setId(id);
		tr.setFirstReading(firstReading);
		tr.setLastReading(lastReading);
		// tr.setFirstScaleReading(firstScaleReading);
		// tr.setLastScaleReading(lastScaleReading);
		tr.setLastAntennaId(lastAntennaId);
		tr.setStayCount(stayCount);
		tr.setMeetingCount(meetingCount);
		tr.setBaladeCount(baladeCount);

		return tr;
	}

	@Override
	public void setTableModels(Object[] array) {
		tableModels = new TransponderRow[array.length];
		for (int i = 0; i < array.length; ++i) {
			tableModels[i] = (TransponderRow) array[i];
		}
	}

	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("rfid", new DbEntry("rfid", ColumnTypes.text, ""));
		columns.put("sex", new DbEntry("sex", ColumnTypes.text, ""));
		columns.put("first_reading", new DbEntry("first_reading",
				ColumnTypes.timestamp, ""));
		columns.put("last_reading", new DbEntry("last_reading",
				ColumnTypes.timestamp, ""));
		columns.put("first_scale_reading", new DbEntry("first_scale_reading",
				ColumnTypes.timestamp, ""));
		columns.put("last_scale_reading", new DbEntry("last_scale_reading",
				ColumnTypes.timestamp, ""));
		columns.put("last_antenna_id", new DbEntry("last_antenna_id",
				ColumnTypes.integer, ""/* "references antennas(id)" */));
		columns.put("last_box_id", new DbEntry("last_box_id",
				ColumnTypes.integer, "references boxes(id)"));
		columns.put("stay_count", new DbEntry("stay_count",
				ColumnTypes.integer, ""));
		columns.put("meeting_count", new DbEntry("meeting_count",
				ColumnTypes.integer, ""));
		columns.put("balade_count", new DbEntry("balade_count",
				ColumnTypes.integer, ""));
	}

	@Override
	protected String uniqueField() {
		return "rfid";
	}

}
