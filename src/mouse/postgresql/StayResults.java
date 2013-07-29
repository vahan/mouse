package mouse.postgresql;

import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.StayResultRow;

/**
 * Models the stay_results table
 * 
 * @author vahan
 * 
 */
public class StayResults extends DbDynamicTable {

	public StayResults() {
		super("stay_results");
	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] { "start", "stop", "duration",
				"transponder_id", "box_id", "dir_in_id", "dir_out_id", "type", };
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		StayResultRow stayResult = (StayResultRow) model;
		String[] values = new String[] {
				"'" + stayResult.getStart().toString() + "'",
				"'" + stayResult.getStop().toString() + "'",
				Float.toString(stayResult.getDuration()),
				stayResult.getTransponder().getId(),
				stayResult.getSource().getId(), stayResult.getDirIn().getId(),
				stayResult.getDirOut().getId(),
				Integer.toString(stayResult.getType()), };
		return values;
	}

	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("start", new DbEntry("start", ColumnTypes.timestamp, ""));
		columns.put("stop", new DbEntry("stop", ColumnTypes.timestamp, ""));
		columns.put("duration", new DbEntry("duration", ColumnTypes.real, ""));
		columns.put("transponder_id", new DbEntry("transponder_id",
				ColumnTypes.integer, "references transponders(id)"));
		columns.put("box_id", new DbEntry("box_id", ColumnTypes.integer,
				"references boxes(id)"));
		columns.put("dir_in_id", new DbEntry("dir_in_id", ColumnTypes.integer,
				"references direction_results(id)"));
		columns.put("dir_out_id", new DbEntry("dir_out_id",
				ColumnTypes.integer, "references direction_results(id)"));
		columns.put("type", new DbEntry("type", ColumnTypes.integer, ""));

	}

}
