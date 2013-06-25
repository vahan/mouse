package mouse.postgresql;


/**
 * Models the boxes table
 * @author vahan
 *
 */
import java.util.HashMap;

import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;

public class Boxes extends DbStaticTable {
	
	public static final int BOX_COUNT = 44;

	public Boxes(String tableName, String[] boxNames) {
		super(tableName, boxNames, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateTables() {
		tableModels = new BoxRow[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			tableModels[i] = new BoxRow(entries[i], 0, 0); //TODO Give proper xPos and yPos
		}
		
	}

	/**
	 * Searches, finds and returns the box with the given name from the DB
	 * @param boxName	The name of the searched box
	 * @return	a Box object, with the searched name
	 */
	public static BoxRow getBoxByName(String boxName) {
		//TODO Implement this!
		
		
		return null;
	}
	
	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"name", 
										"segment", 
										"x_pos", 
										"y_pos"
										};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		BoxRow box = (BoxRow) model;
		String[] values = new String[] {"'" + box.getName() + "'", 
										"'" + box.getSegment() + "'", 
										"'" + box.getXPos() + "'", 
										"'" + box.getYPos() + "'"
										};
		return values;
	}
	
	@Override
	public DbTableRow createModel(HashMap<String, String> columnValues) {
		// TODO Auto-generated method stub
		String name = columnValues.get("name");
		Float xPos = Float.parseFloat(columnValues.get("x_pos"));
		Float yPos = Float.parseFloat(columnValues.get("y_pos"));
		String id = columnValues.get("id");
		BoxRow box = new BoxRow(name, xPos, yPos);
		box.setId(id);
		return box;
	}
	
	@Override
	public void setTableModels(Object[] array) {
		for (int i = 0; i < array.length; ++i) {
			tableModels[i] = (BoxRow) array[i];
		}
	}

	@Override
	protected void initColumns() {
		columns.put("id", new DbTableColumn("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("name", new DbTableColumn("name", ColumnTypes.text, ""));
		columns.put("segment", new DbTableColumn("segment", ColumnTypes.text, ""));
		columns.put("x_pos", new DbTableColumn("x_pos", ColumnTypes.real, ""));
		columns.put("y_pos", new DbTableColumn("y_pos", ColumnTypes.real, ""));
		columns.put("last_direction_result", new DbTableColumn("last_direction_result", ColumnTypes.timestamp, ""));
		columns.put("last_meeting", new DbTableColumn("last_meeting", ColumnTypes.timestamp, ""));
		
		
	}
}
