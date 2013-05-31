package mouse.postgresql;

import java.sql.*;

import org.postgresql.*;


public class PostgreSQLManager {
	
	private Connection db = null;
	private final String username;
	private final String password;
	
	private String url;
	
	public String getUrl() {
		return url;
	}

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
	 * Creates the tables if not existing
	 * @return	true if succeeded without errors, false - otherwise
	 */
	public boolean createTables() {
		if (db == null) {
			return false;
		}
		
		//TODO: create the not existing tables
		
		
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
