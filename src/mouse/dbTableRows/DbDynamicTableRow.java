package mouse.dbTableRows;

import mouse.TimeStamp;

public abstract class DbDynamicTableRow extends DbTableRow {
	
	protected final TransponderRow transponder;

	protected final DbStaticTableRow source;
	
	protected DbDynamicTableRow(TransponderRow transponder, DbStaticTableRow source) {
		super();
		this.transponder = transponder;
		this.source = source;
	}
	
	
	public abstract TimeStamp timeStamp(int timeStampIndex);
	
	public abstract DbStaticTableRow staticTableRow(int staticTableRowIndex);


	public TransponderRow getTransponder() {
		return transponder;
	}


	public DbStaticTableRow getSource() {
		return source;
	}
	
	
	
}
