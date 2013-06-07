package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.MeetingResult;


public class MeetingResults extends DbDynamicTable {
	
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
		String[] fields = new String[] {"rfid_from",
										"rfid_to",
										"start",
										"stop",
										"duration",
										"terminated_by",
										"box_id"
		};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableModel model) {
		MeetingResult meetResult = (MeetingResult) model;
		String[] values = new String[] {meetResult.getRfidFrom(),
										meetResult.getRfidTo(),
										meetResult.getStart().toString(),
										meetResult.getStop().toString(),
										Float.toString(meetResult.getDuration()),
										Integer.toString(meetResult.getTerminatedBy()),
										meetResult.getBox().getId()
										};
		return values;
	}
	

}
