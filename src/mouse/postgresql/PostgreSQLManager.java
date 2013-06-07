package mouse.postgresql;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import mouse.Column;
import mouse.DataProcessor;


public class PostgreSQLManager {
	
	private Connection conn = null;
	private final String username;
	private final String password;
	
	private String url;

	private Logs logs = new Logs("logs");
	private Scales scales = new Scales("scales");
	private Boxes boxes;
	private Antennas antennas;
	private Transponders transponders;
	private ScaleReadings scaleReadings = new ScaleReadings("scaleReadings");
	
	private AntennaReadings antennaReadings = new AntennaReadings("antenna_readings");
	private DirectionResults directionResults = new DirectionResults("direction_results");
	private StayResults stayResults = new StayResults("stay_results");
	private MeetingResults meetingResults = new MeetingResults("meeting_results");
	
	private ArrayList<DbTable> tables = new ArrayList<DbTable>();
	
	public PostgreSQLManager(String username, String password, Column[] columns) {
		this.username = username;
		this.password = password;

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("PostgreSQL Driver could not be found:");
			e.printStackTrace();
			return;
		}
		
		this.boxes = new Boxes("boxes", columns[DataProcessor.DEVICE_ID_COLUMN].toArray());
		this.antennas = new Antennas("antennas", columns[DataProcessor.ANTENNA_ID_COLUMN].toArray(), 
												columns[DataProcessor.DEVICE_ID_COLUMN].toArray());
		this.transponders = new Transponders("transponders", columns[DataProcessor.RFID_COLUMN].toArray());
		
		//The order must be kept!
		tables.add(logs);
		tables.add(scales); //TODO scales is not initialized!
		tables.add(boxes);
		tables.add(antennas);
		tables.add(transponders);
		tables.add(scaleReadings);
		tables.add(directionResults);
		tables.add(stayResults);
		tables.add(antennaReadings);
		tables.add(meetingResults);
		
	}

	public String getUrl() {
		return url;
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

	public boolean connect(String host, String port, String dbName) {
		url = generateUrl(host, port, dbName);
		
		try {
			conn = DriverManager.getConnection(url, this.username, this.password);
		} catch (SQLException e) {
			System.out.println("Connection to DB " + dbName + " could not be established:");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates the all tables (if not existing)
	 * @return	true if succeeded without errors, false - otherwise
	 */
	public boolean initTables() {
		if (conn == null) {
			return false;
		}
		
		//First, drop all tables TODO remove this!
		String[] dropQueries = new String[tables.size()];
		for (int i = 0; i < tables.size(); ++i) {
			dropQueries[i] = tables.get(i).dropTableQuery();
		}
		executePreparedStatements(dropQueries);
		//create the DB tables
		String[] createQueries = new String[tables.size()];
		for (int i = 0; i < tables.size(); ++i) {
			createQueries[i] = tables.get(i).createTableQuery();
		}
		if (!executePreparedStatements(createQueries))
			return false;
		
		return true;
	}
	

	public boolean storeStaticTables() {
		if (!storeStaticTable(this.boxes))
			return false;
		if (!storeStaticTable(this.antennas))
			return false;
		if (!storeStaticTable(this.transponders))
			return false;
		return true;
	}
	
	
	/**
	 * 
	 * @param searchObj
	 * @param condition
	 * @return
	 */
	public String[] searchInTable(String tableName, String condition) {
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String> results = new ArrayList<String>();
		
		try {
			conn = DriverManager.getConnection(url, username, password);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM `" + tableName + "` WHERE " + condition);
			
			if (rs.next()) {
				results.add(rs.getString(1));
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return results.toArray(new String[results.size()]);
	}
	
	/**
	 * Wrapper to execute the given postrgeSql query
	 * @param query	The query to be executed
	 * @return		The returned result after the query execution
	 */
	public String[] executeQueries(String query) {
		conn = null;
		Statement stmt = null;
		ArrayList<String> results = new ArrayList<String>();
		
		try {
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			stmt.execute(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = stmt.getGeneratedKeys();
			while (resultSet.next()) {
				results.add(resultSet.getString(1)); //TODO Hard typed 1, assuming only ID is returned
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
				results = null;
			}
		}
		return results.toArray(new String[results.size()]);
	}

	/**
	 * Wrapper to execute the given postrgeSql query
	 * @param queries	The query to be executed
	 * @return		The returned result after the query execution
	 */
	public boolean executePreparedStatements(String[] queries) {
		conn = null;
		PreparedStatement pst = null;
		boolean success = true;
		
		try {
			conn = DriverManager.getConnection(url, username, password);
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
				Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
				success = false;
			}
		}
		return success;
	}

	
	private boolean storeStaticTable(DbStaticTable staticTable) {
		String insertQuery = staticTable.insertQuery(staticTable.getTableModels());
		String[] ids = executeQueries(insertQuery);
		for (int i = 0; i < ids.length; ++i) {
			if (StringUtils.isEmpty(ids[i]))
				return false;
			staticTable.getTableModels()[i].setId(ids[i]);
		}
		
		return true;
	}


	private String generateUrl(String host, String port, String dbName) {
		String url = "jdbc:postgresql:";
		if (!host.isEmpty()) {
			url += "//" + host;
			if (!port.isEmpty())
				url +=  ":" + port + "/";
			else
				url += "/";
		}
		url += dbName;
		return url;
	}

}
