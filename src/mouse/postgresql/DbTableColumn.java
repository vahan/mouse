package mouse.postgresql;

public class DbTableColumn {
	
	private final String name;
	
	private final ColumnTypes type;
	/**
	 * E.g. PRIMARY KEY, references -TABLE(FIELD)-, etc
	 */
	private final String note;

	public DbTableColumn(String name, ColumnTypes type, String note) {
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
