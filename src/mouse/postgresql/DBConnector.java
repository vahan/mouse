package mouse.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mouse.Column;

public class DBConnector {
	
	private Connection conn = null;
	private final String username;
	private final String password;
	private String url;

	public DBConnector(String username, String password, Column[] columns) {
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
	
	public String getUrl() {
		return url;
	}
	
	public Connection getConnection() {
		return conn;
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
