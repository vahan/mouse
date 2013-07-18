package mouse.postgresql;


/**
 * Models the boxes table
 * @author vahan
 *
 */
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;

public class Boxes extends DbStaticTable {
	

	public Boxes(String tableName, String[] boxNames, boolean generate) {
		super(tableName, boxNames, null, generate);

		if (tableModels == null)
			tableModels = new BoxRow[entries.length];
	}
	
	public String updateBoxDataQuery() {
		String[] fields = new String[] {"x_pos", "y_pos"};
		String[][] values = new String[tableModels.length][fields.length];
		
		for (int i = 0; i < tableModels.length; ++i) {
			values[i][0] = ((BoxRow) tableModels[i]).getxPos();
			values[i][1] = ((BoxRow) tableModels[i]).getyPos();
		}
		return updateQueryByName(fields, values, "name", ColumnTypes.text);
	}
	

	@Override
	protected void generateTables() {
		if (tableModels == null)
			tableModels = new BoxRow[entries.length];
		for (int i = 0; i < entries.length; ++i) {
			tableModels[i] = new BoxRow(entries[i]);
		}
		
	}
	
	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"name", 
										"segment", 
										};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		BoxRow box = (BoxRow) model;
		String[] values = new String[] {"'" + box.getName() + "'", 
										"'" + box.getSegment() + "'", 
										};
		return values;
	}
	
	@Override
	public DbTableRow createModel(HashMap<String, String> columnValues) {
		String name = columnValues.get("name");
		String xPos = columnValues.get("x_pos");
		String yPos = columnValues.get("y_pos");
		String id = columnValues.get("id");
		BoxRow box = new BoxRow(name);
		box.setxPos(xPos);
		box.setyPos(yPos);
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
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("name", new DbEntry("name", ColumnTypes.text, ""));
		columns.put("segment", new DbEntry("segment", ColumnTypes.text, ""));
		columns.put("x_pos", new DbEntry("x_pos", ColumnTypes.real, ""));
		columns.put("y_pos", new DbEntry("y_pos", ColumnTypes.real, ""));
		columns.put("last_direction_result", new DbEntry("last_direction_result", ColumnTypes.timestamp, ""));
		columns.put("last_meeting", new DbEntry("last_meeting", ColumnTypes.timestamp, ""));
		
		
	}
	
	
	private String updateQueryByName(String[] fields, String[][] values, String field, ColumnTypes type) {
		String query = "UPDATE " + tableName + " SET ";
		String quote = type.requiresQuotes() ? "'" : "";
		for (int i = 0; i < fields.length; ++i) {
			query += fields[i] + " = CASE name";
			for (int j = 0; j < tableModels.length; ++j) {
				if (StringUtils.isEmpty(values[j][i]))
					continue;
				BoxRow row = (BoxRow) tableModels[j];
				query += " WHEN " + quote + row.getName() + quote + " THEN " + values[j][i];
			}
			query += " END";
			query += i == fields.length - 1 ? " " : ", ";
		}
		String[] allIds = new String[tableModels.length];
		for (int j = 0; j < tableModels.length; ++j) {
			BoxRow row = (BoxRow) tableModels[j];
			allIds[j] = row.getName();
		}
		query += "WHERE " + field + " IN (" + quote + StringUtils.join(allIds, quote + ", " + quote) + quote + " )";
		
		return query;
	}
	
}
