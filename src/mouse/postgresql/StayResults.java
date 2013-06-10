package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.StayResult;

public class StayResults extends DbDynamicTable {
	
	public StayResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"start timestamp," +
				"stop timestamp," +
				"duration real," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)," +
				"dir_in_id integer references direction_results(id)," + 
				"dir_out_id integer references direction_results(id)" +
			");";

		return query;
	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"start",
										"stop",
										"duration",
										"transponder_id",
										"box_id",
										"dir_in_id",
										"dir_out_id"
										};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableModel model) {
		StayResult stayResult = (StayResult) model;
		String[] values = new String[] {"'" + stayResult.getStart().toString() + "'",
										"'" + stayResult.getStop().toString() + "'",
										Float.toString(stayResult.getDuration()),
										stayResult.getTransponder().getId(),
										stayResult.getBox().getId(),
										stayResult.getDirIn().getId(),
										stayResult.getDirOut().getId()
		};
		return values;
	}
	

}
