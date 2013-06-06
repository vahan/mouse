package mouse.postgresql;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import mouse.Column;
import mouse.DataProcessor;
import mouse.dbTableModels.DbTableModel;

import zetcode.Version;


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
		tables.add(stayResults);
		tables.add(directionResults);
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
		
		ResultSet result = null;
		
		//First, drop all tables TODO remove this!
		for (DbTable table : tables) {
			String dropQuery = table.dropTableQuery();
			executePreparedStatement(dropQuery);
		}
		//create the DB tables
		for (DbTable table : tables) {
			String createQuery = table.createTableQuery();
			if (!executePreparedStatement(createQuery))
				return false;
		}
		
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
	public String executeQuery(String query) {
		conn = null;
		Statement stmt = null;
		String result = null;
		
		try {
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			stmt.execute(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = stmt.getGeneratedKeys();
			resultSet.next();
			result = resultSet.getString(1); //TODO Hard typed 1
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
				result = null;
			}
		}
		return result;
	}

	/**
	 * Wrapper to execute the given postrgeSql query
	 * @param query	The query to be executed
	 * @return		The returned result after the query execution
	 */
	public boolean executePreparedStatement(String query) {
		conn = null;
		PreparedStatement pst = null;
		boolean success = true;
		
		try {
			conn = DriverManager.getConnection(url, username, password);
			pst = conn.prepareStatement(query);
			pst.executeUpdate();
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
		for (DbTableModel model : staticTable.getTableModels()) {
			String insertQuery = staticTable.insertQuery(model);
			String id = executeQuery(insertQuery);
			if (StringUtils.isEmpty(id))
				return false;
			model.setId(id);
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
