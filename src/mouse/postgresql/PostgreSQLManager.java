package mouse.postgresql;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PostgreSQLManager {
	
	private Connection db = null;
	private final String username;
	private final String password;
	
	private String url;

	private Logs logs = new Logs();
	private Scales scales = new Scales();
	private Boxes boxes = new Boxes();
	private Antennas antennas = new Antennas();
	private Transponders transponders = new Transponders();
	private ScaleReadings scaleReadings = new ScaleReadings();
	
	private AntennaReadings antennaReadings = new AntennaReadings();
	private DirectionResults directionResults = new DirectionResults();
	private StayResults stayResults = new StayResults();
	private MeetingResults meetingResults = new MeetingResults();
	
	private ArrayList<DbTable> tables = new ArrayList<DbTable>();
	
	public PostgreSQLManager(String username, String password) {
		this.username = username;
		this.password = password;

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("PostgreSQL Driver could not be found:");
			e.printStackTrace();
			return;
		}
		
		//The order must be kept!
		tables.add(logs);
		tables.add(scales);
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

	public boolean connect(String host, String port, String dbName) {
		url = generateUrl(host, port, dbName);
		
		try {
			
			db = DriverManager.getConnection(url, this.username, this.password);
		} catch (SQLException e) {
			System.out.println("Connection to DB " + dbName + " could not be established:");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates the `boxes`, `antennas` tables if not existing
	 * @return	true if succeeded without errors, false - otherwise
	 */
	public boolean initTables() {
		if (db == null) {
			return false;
		}
		
		boolean success = true;
		
		for (DbTable table : tables) {
			String query = table.createTableQuery();
			success = executeQuery(query);
			if (!success)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Wrapper to execute the given postrgeSql query
	 * @param query	The query to be executed
	 * @return		The returned result after the query execution
	 */
	private boolean executeQuery(String query) {
		Connection con = null;
		PreparedStatement pst = null;
		boolean success = true;
		
		try {
			con = DriverManager.getConnection(url, username, password);
			pst = con.prepareStatement(query);
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
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(PostgreSQLManager.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
				success = false;
			}
		}
		return success;
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
