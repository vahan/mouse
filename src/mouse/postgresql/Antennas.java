package mouse.postgresql;

import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;

/**
 * Models the antennas table
 * @author vahan
 *
 */
public class Antennas extends DbStaticTable {

	public static final int ANTENNAS_PER_BOX = 2;
	
	public Antennas(String tableName, String[] antennaNames, String[] boxNames) {
		super(tableName, antennaNames, boxNames);
		// TODO Auto-generated constructor stub
		
	}
	
	
	@Override
	protected void generateTables() {
		tableModels = new AntennaRow[data.length * entries.length];
		for (int j = 0, k = 0; j < data.length; ++j) {
			for (int i = 0; i < entries.length; ++i) {
				tableModels[k++] = new AntennaRow(entries[i], BoxRow.getBoxByName(data[j]));
			}
		}
	}
	
	
	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"name text," +
				"position text," +
				"last_reading timestamp," +
				"box_id integer references boxes(id)" + 
			");";

		return query;
	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"name", 
										"position", 
										"box_id"
										};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		AntennaRow antenna = (AntennaRow) model;
		String[] values = new String[] {"'" + antenna.getName() + "'", 
										"'" + antenna.getPosition() + "'", 
										"'" + antenna.getBox().getId() + "'"
										};
		return values;
	}
	
	@Override
	public String[] getColumnNames() {
		String[] fields = new String[] {"position", 
										"box_id",
										"id"
										};
		return fields;
	}

	@Override
	public DbTableRow createModel(String[] columnValues) {
		// TODO Auto-generated method stub
		String position = columnValues[0];
		String boxId = columnValues[1];
		BoxRow box = BoxRow.getBoxById(boxId);
		String id = columnValues[2];
		AntennaRow row = new AntennaRow(position, box);
		row.setId(id);
		return row;
	}
	
	@Override
	public void setTableModels(Object[] array) {
		for (int i = 0; i < array.length; ++i) {
			tableModels[i] = (AntennaRow) array[i];
		}
	}
	
	
}
