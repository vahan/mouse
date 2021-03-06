package mouse.postgresql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import mouse.Settings;
import mouse.TimeStamp;
import mouse.dataProcessing.CSVColumn;
import mouse.dataProcessing.DataProcessor;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;


/**
 * This class provides functionality to deal with the postgresql database
 * 
 * @author vahan
 * 
 */
public class PostgreSQLManager {
	// Connection data
	private Connection conn = null;
	private Settings settings;
	private final int version;

	// The static tables
	private Logs logs = new Logs();
	private Scales scales = new Scales();
	private Boxes boxes = new Boxes();
	private Antennas antennas = new Antennas();
	private Transponders transponders = new Transponders();
	private ScaleReadings scaleReadings = new ScaleReadings();
	// The dynamic tables
	private AntennaReadings antennaReadings = new AntennaReadings();
	private DirectionResults directionResults = new DirectionResults();
	private StayResults stayResults = new StayResults();
	private MeetingResults meetingResults = new MeetingResults();

	private CSVColumn[] columns;
	/**
	 * List of all tables in the database
	 */
	private ArrayList<DbTable> tables = new ArrayList<DbTable>();

	/**
	 * Constructor
	 * 
	 * @param username
	 * @param password
	 * @param columns
	 */
	public PostgreSQLManager(Settings settings, CSVColumn[] columns,
			boolean reset) {
		this.settings = settings;
		this.columns = columns;
		this.version = detectPsqlVersion();

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("PostgreSQL Driver could not be found:");
			e.printStackTrace();
			return;
		}

		// The order must be kept, because of the dependencies among the tables!
		tables.add(logs);
		tables.add(scales); // TODO scales is not initialized!
		tables.add(boxes);
		tables.add(antennas);
		tables.add(transponders);
		tables.add(scaleReadings);
		tables.add(directionResults);
		tables.add(stayResults);
		tables.add(antennaReadings);
		tables.add(meetingResults);

	}

	private int detectPsqlVersion() {
		// TODO Auto-generated method stub
		return 90;
	}

	public Connection getCon() {
		return conn;
	}

	public Logs getLogs() {
		return logs;
	}

	public Scales getScales() {
		return scales;
	}

	public Boxes getBoxes() {
		return boxes;
	}

	public Antennas getAntennas() {
		return antennas;
	}

	public Transponders getTransponders() {
		return transponders;
	}

	public ScaleReadings getScaleReadings() {
		return scaleReadings;
	}

	public AntennaReadings getAntennaReadings() {
		return antennaReadings;
	}

	public DirectionResults getDirectionResults() {
		return directionResults;
	}

	public StayResults getStayResults() {
		return stayResults;
	}

	public MeetingResults getMeetingResults() {
		return meetingResults;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public int getVersion() {
		return version;
	}

	public boolean connect() {
		try {
			conn = DriverManager.getConnection(settings.getUrl(),
					settings.getUsername(), settings.getPassword());
		} catch (SQLException e) {
			System.out.println("Connection to DB " + settings.getDbName()
					+ " could not be established:");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Creates the all tables (if not existing)
	 * 
	 * @return true if succeeded without errors, false - otherwise
	 */
	public boolean initTables(boolean reset) {
		if (conn == null) {
			return false;
		}
		if (reset) {
			// drop all tables
			String[] dropQueries = new String[tables.size()];
			for (int i = 0; i < tables.size(); ++i) {
				dropQueries[i] = tables.get(i).dropTableQuery();
			}
			executePreparedStatements(dropQueries);
		}
		// create the DB tables if not exist
		String[] createQueries = new String[tables.size()];
		for (int i = 0; i < tables.size(); ++i) {
			createQueries[i] = tables.get(i).createTableQuery();
		}
		String createQuery = StringUtils.join(createQueries, " ");
		if (version > 90 && !executePreparedStatements(new String[] {createQuery}))
			return false;
		else if (version <= 90)
			executeSelectQuery(createQuery, null); //TODO: check on success
		return true;
	}

	public boolean mergeStaticTables() {
		boxes = new Boxes(columns[DataProcessor.DEVICE_ID_COLUMN].toArray(),
				true);
		if (!storeStaticTable(boxes))
			return false;
		if (!getStaticTable(boxes))
			return false;
		antennas = new Antennas(
				columns[DataProcessor.ANTENNA_ID_COLUMN].toArray(),
				getBoxRows(columns[DataProcessor.DEVICE_ID_COLUMN].toArray()),
				true);
		if (!storeStaticTable(antennas))
			return false;
		if (!getStaticTable(antennas))
			return false;
		transponders = new Transponders(
				columns[DataProcessor.RFID_COLUMN].toArray(), true);
		if (!storeStaticTable(transponders))
			return false;
		if (!getStaticTable(transponders))
			return false;
		return true;
	}

	/**
	 * 
	 * @param searchObj
	 * @param condition
	 * @return
	 */
	/*
	 * public String[] searchInTable(String tableName, String condition) {
	 * Statement st = null; ResultSet rs = null; ArrayList<String> results = new
	 * ArrayList<String>();
	 * 
	 * try { conn = DriverManager.getConnection(url, username, password); st =
	 * conn.createStatement(); rs = st.executeQuery("SELECT * FROM `" +
	 * tableName + "` WHERE " + condition);
	 * 
	 * if (rs.next()) { results.add(rs.getString(1)); } } catch (SQLException
	 * ex) { Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
	 * lgr.log(Level.SEVERE, ex.getMessage(), ex); } finally { try { if (rs !=
	 * null) { rs.close(); } if (st != null) { st.close(); } if (conn != null) {
	 * conn.close(); }
	 * 
	 * } catch (SQLException ex) { Logger lgr =
	 * Logger.getLogger(PostgreSQLManager.class.getName());
	 * lgr.log(Level.WARNING, ex.getMessage(), ex); } } return
	 * results.toArray(new String[results.size()]); }
	 */

	private BoxRow[] getBoxRows(String[] boxNames) {
		BoxRow[] boxRows = new BoxRow[boxNames.length];
		for (DbTableRow row : boxes.getTableModels()) {
			BoxRow boxRow = (BoxRow) row;
			for (int i = 0; i < boxNames.length; ++i) {
				if (boxRow.getName().equals(boxNames[i])) {
					boxRows[i] = boxRow;
					break;
				}
			}
		}

		return boxRows;
	}

	/**
	 * Wrapper to execute the given postrgeSql query
	 * 
	 * @param query
	 *            The query to be executed
	 * @param returnField TODO
	 * @return The returned result after the query execution
	 */
	public String[] executeQuery(String query, String returnField) {
		conn = null;
		Statement stmt = null;
		ArrayList<String> results = new ArrayList<String>();

		try {
			conn = DriverManager.getConnection(settings.getUrl(),
					settings.getUsername(), settings.getPassword());
			stmt = conn.createStatement();
			stmt.execute(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = stmt.getGeneratedKeys();
			while (resultSet.next()) {
				results.add(resultSet.getString(returnField)); // TODO Hard typed "id",
														// assuming only ID is
														// returned
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			lgr.log(Level.SEVERE, "query: " + query);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger
						.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
				results = null;
			}
		}
		return results == null ? null : results.toArray(new String[results
				.size()]);
	}

	/**
	 * Wrapper to execute the given postrgeSql query
	 * 
	 * @param queries
	 *            The query to be executed
	 * @return The returned result after the query execution
	 */
	public boolean executePreparedStatements(String[] queries) {
		conn = null;
		PreparedStatement pst = null;
		boolean success = true;

		try {
			conn = DriverManager.getConnection(settings.getUrl(),
					settings.getUsername(), settings.getPassword());
			// TODO: join into one query
			for (int i = 0; i < queries.length; ++i) {
				String query = queries[i];
				pst = conn.prepareStatement(query);
				pst.executeUpdate();
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			success = false;
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger
						.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
				success = false;
			}
		}
		return success;
	}

	/**
	 * Wrapper to execute the given postrgeSql query
	 * 
	 * @param queries
	 *            The query to be executed. If Null, empty array will be returned
	 * @return The returned result after the query execution
	 */
	public String[] executeSelectQuery(String query, String field) {
		conn = null;
		PreparedStatement pst = null;
		ArrayList<String> results = new ArrayList<String>();

		try {
			conn = DriverManager.getConnection(settings.getUrl(),
					settings.getUsername(), settings.getPassword());
			pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			if (field != null) {
				while (rs.next()) {
					results.add(rs.getString(field));
				}
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return null;
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger
						.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
				return null;
			}
		}
		return results.toArray(new String[results.size()]);
	}

	public String[] getLogEntries() {
		String query = "SELECT * FROM " + logs.getTableName();

		return executeSelectQuery(query, "imported_at");
	}

	public boolean deleteLogEntries(TimeStamp importedAt) {
		String query = "DELETE FROM " + logs.getTableName()
				+ " WHERE imported_at='" + importedAt + "'";

		return executeQuery(query, "id").length > 0;
	}

	private boolean storeStaticTable(DbStaticTable staticTable) {
		String insertQuery = staticTable.insertQuery(staticTable
				.getTableModels());
		String[] ids = executeQuery(insertQuery, "id");
		/*
		 * for (int i = 0; i < ids.length; ++i) { if
		 * (StringUtils.isEmpty(ids[i])) return false;
		 * staticTable.getTableModels()[i].setId(ids[i]); }
		 */

		return true;
	}

	private boolean getStaticTable(DbStaticTable staticTable) {
		String selectQuery = "SELECT * FROM " + staticTable.getTableName();
		Statement stmt;
		try {
			conn = DriverManager.getConnection(settings.getUrl(),
					settings.getUsername(), settings.getPassword());
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(selectQuery);
			Set<String> columnNames = staticTable.getColumnNames();
			ArrayList<DbTableRow> models = new ArrayList<DbTableRow>();
			while (rs.next()) {
				HashMap<String, String> columnValues = new HashMap<String, String>();
				for (String colName : columnNames) {
					columnValues.put(colName, rs.getString(colName));
				}
				models.add(staticTable.createModel(columnValues));
			}
			staticTable.setTableModels(models.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

}
