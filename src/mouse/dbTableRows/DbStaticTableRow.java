package mouse.dbTableRows;

import mouse.TimeStamp;

public abstract class DbStaticTableRow extends DbTableRow {

	
	public abstract TimeStamp[] getLastResults();
	
	
	public abstract void setLastResult(TimeStamp result, int lastResultIndex);
	
	
}
