package mouse.postgresql;

import java.util.HashMap;

import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;

/**
 * Models the antennas table
 * 
 * @author vahan
 * 
 */
public class Antennas extends DbStaticTable {

	public static final int ANTENNAS_PER_BOX = 2;

	private final BoxRow[] boxRows;

	public Antennas(String[] antennaNames, BoxRow[] boxRows, boolean generate) {
		super("antennas", antennaNames, generate);
		this.boxRows = boxRows;
		if (generate)
			generateTables();
		if (tableModels == null)
			tableModels = new AntennaRow[boxRows.length * names.length];

	}

	public Antennas() {
		this(new String[] { "" }, new BoxRow[0], false);
	}

	@Override
	protected void generateTables() {
		if (tableModels == null)
			tableModels = new AntennaRow[boxRows.length * names.length];
		for (int j = 0, k = 0; j < boxRows.length; ++j) {
			for (int i = 0; i < names.length; ++i) {
				tableModels[k++] = AntennaRow.getAntennaRow(names[i],
						boxRows[j]);
			}
		}
	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] { "name", "position", "box_id" };
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		AntennaRow antenna = (AntennaRow) model;
		String[] values = new String[] { "'" + antenna.getName() + "'",
				"'" + antenna.getPosition() + "'", antenna.getBox().getId() };
		return values;
	}

	@Override
	public DbTableRow createModel(HashMap<String, String> columnValues) {
		String position = columnValues.get("position");
		String boxId = columnValues.get("box_id");
		BoxRow box = BoxRow.getBoxById(boxId);
		String id = columnValues.get("id");
		AntennaRow row = AntennaRow.getAntennaRow(position, box);
		row.setId(id);
		return row;
	}

	@Override
	public void setTableModels(Object[] array) {
		tableModels = new AntennaRow[array.length];
		for (int i = 0; i < array.length; ++i) {
			tableModels[i] = (AntennaRow) array[i];
		}
	}

	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("name", new DbEntry("name", ColumnTypes.text, ""));
		columns.put("position", new DbEntry("position", ColumnTypes.text, ""));
		columns.put("last_reading", new DbEntry("last_reading",
				ColumnTypes.timestamp, ""));
		columns.put("box_id", new DbEntry("box_id", ColumnTypes.integer,
				"references boxes(id)"));
	}

	@Override
	protected String uniqueField() {
		return "name";
	}

}
