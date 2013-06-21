package mouse.postgresql;

import java.util.HashMap;

import mouse.TimeStamp;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.DirectionResultRow;


/**
 * Models the direction_results table
 * @author vahan
 *
 */
public class DirectionResults extends DbDynamicTable {
	
	public DirectionResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}

	
	/**
	 * Gives the last reading of a given antenna
	 * 
	 * @param antenna	
	 * @return
	 */
	public HashMap<BoxRow, TimeStamp> lastReadings() {
		HashMap<BoxRow, TimeStamp> lastReadings = new HashMap<BoxRow, TimeStamp>();
		for (DbTableRow model : tableModels) {
			DirectionResultRow directionResult = (DirectionResultRow) model;
			BoxRow box = (BoxRow) directionResult.getSource();
			TimeStamp timeStamp = directionResult.getTimeStamp();
			if (lastReadings.containsKey(box)) {
				TimeStamp lastReading = lastReadings.get(box);
				if (timeStamp.after(lastReading)) {
					lastReadings.put(box, timeStamp);
				}
			} else {
				lastReadings.put(box, timeStamp);
			}
		}
		return lastReadings;
	}
	
	
	public void putLastReadings() {
		HashMap<BoxRow, TimeStamp> lastReadings = lastReadings();
		for (BoxRow box : lastReadings.keySet()) {
			box.setLastDirectionResult(lastReadings.get(box));
		}
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
	protected String[] insertValues(DbTableRow model) {
		DirectionResultRow dirResult = (DirectionResultRow) model;
		String[] values = new String[] {"'" + dirResult.getTimeStamp().toString() + "'",
										"'" + dirResult.getDirection().toString() + "'",
										dirResult.getTransponder().getId(),
										dirResult.getSource().getId(),
		};
		return values;
	}
	
}
