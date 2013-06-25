package mouse.postgresql;

public class DbEntry {
	
	private final String name;
	
	private final ColumnTypes type;
	/**
	 * E.g. PRIMARY KEY, references -TABLE(FIELD)-, etc
	 */
	private final String note;

	public DbEntry(String name, ColumnTypes type, String note) {
		super();
		this.name = name;
		this.type = type;
		this.note = note;
	}

	public String getName() {
		return name;
	}

	public ColumnTypes getType() {
		return type;
	}

	public String getNote() {
		return note;
	}
	
	
	

}
