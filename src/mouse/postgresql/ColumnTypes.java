package mouse.postgresql;

public enum ColumnTypes {
	
	text,
	timestamp,
	integer,
	real,
	serial;
	
	
	public boolean requiresQuotes() {
		return this.equals(text) || this.equals(timestamp);
	}

}
