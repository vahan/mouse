package mouse.postgresql;

import mouse.dbTableRows.DbTableRow;


/**
 * Models the scale_readings table
 * @author vahan
 *
 */
public class ScaleReadings extends DbTable {

	public ScaleReadings(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
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
		columns.put("id",  new DbTableColumn("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("timestamp",  new DbTableColumn("timestamp", ColumnTypes.timestamp, ""));
		columns.put("weight",  new DbTableColumn("weight", ColumnTypes.real, ""));
		columns.put("log_id",  new DbTableColumn("log_id", ColumnTypes.integer, "references logs(id)"));
		columns.put("transponder_id",  new DbTableColumn("transponder_id", ColumnTypes.integer, "references transponders(id)"));
		columns.put("scale_id",  new DbTableColumn("scale_id", ColumnTypes.integer, "references scales(id)"));
		
	}
	

}
