package mouse.postgresql;

import java.util.HashMap;

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
	public DbTableRow createModel(HashMap<String, String> columnValues) {
		// TODO Auto-generated method stub
		String position = columnValues.get("position");
		String boxId = columnValues.get("box_id");
		BoxRow box = BoxRow.getBoxById(boxId);
		String id = columnValues.get("id");
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


	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("name", new DbEntry("name", ColumnTypes.text, ""));
		columns.put("position", new DbEntry("position", ColumnTypes.text, ""));
		columns.put("last_reading", new DbEntry("last_reading", ColumnTypes.timestamp, ""));
		columns.put("box_id", new DbEntry("box_id", ColumnTypes.integer, "references boxes(id)"));
	}
	
	
}
