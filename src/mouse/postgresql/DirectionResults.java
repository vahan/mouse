package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.DirectionResult;


public class DirectionResults extends DbDynamicTable {
	
	public DirectionResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"timestamp timestamp," +
				"direction text," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)" +
			");";

		return query;
	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"timestamp",
										"direction",
										"transponder_id",
										"box_id",
										};
		// TODO Auto-generated method stub
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableModel model) {
		DirectionResult dirResult = (DirectionResult) model;
		String[] values = new String[] {"'" + dirResult.getTimeStamp().toString() + "'",
										"'" + dirResult.getDirection().toString() + "'",
										dirResult.getTransponder().getId(),
										dirResult.getBox().getId(),
		};
		return values;
	}
	
}
