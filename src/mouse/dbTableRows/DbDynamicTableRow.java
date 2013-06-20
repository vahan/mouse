package mouse.dbTableRows;

import mouse.TimeStamp;

public abstract class DbDynamicTableRow extends DbTableRow {
	
	public abstract TimeStamp timeStamp(int timeStampIndex);
	
	public abstract DbStaticTableRow staticTableRow(int staticTableRowIndex);

}
