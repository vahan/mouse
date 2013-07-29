package mouse.postgresql;

import mouse.TimeStamp;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.ScaleRow;

/**
 * Models the scales table
 * 
 * @author vahan
 * 
 */
public class Scales extends DbTable {

	public Scales() {
		super("scales");

		generateScales();
	}

	/**
	 * Generates scales
	 */
	private void generateScales() {
		tableModels = new ScaleRow[1];
		tableModels[0] = new ScaleRow("scale1", "segment1", 0, 0,
				new TimeStamp());

	}

	@Override
	protected String[] insertFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("segment", new DbEntry("segment", ColumnTypes.text, ""));
		columns.put("x_pos", new DbEntry("x_pos", ColumnTypes.real, ""));
		columns.put("y_pos", new DbEntry("y_pos", ColumnTypes.real, ""));
		columns.put("last_reading", new DbEntry("last_reading",
				ColumnTypes.timestamp, ""));

	}

}
