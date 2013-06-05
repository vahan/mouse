package mouse.postgresql;


public abstract class DbTable {

	protected abstract String createTableQuery();
	
	protected abstract String insertQuery();

}
