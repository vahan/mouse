package mouse.postgresql;

public class Settings {
	
	private String username;
	
	private String password;
	
	private String hostName;
	
	private String port;
	
	private String dbName;
	
	private String url;

	private int intervalsNumber;

	private String csvDateFormat;

	private String dbDateFormat;
	
	private long maxTubeTime;
	
	private long maxBoxTime;
	

	public Settings(String username, String password, String hostName,
			String port, String dbName, int intervalsNumber,
			String csvDateFormat, String dbDateFormat, 
			long maxTubeTime, long maxBoxTime) {
		super();
		this.username = username;
		this.password = password;
		this.hostName = hostName;
		this.port = port;
		this.dbName = dbName;
		url = generateUrl(hostName, port, dbName);
		this.intervalsNumber = intervalsNumber;
		this.csvDateFormat = csvDateFormat;
		this.dbDateFormat = dbDateFormat;
		this.maxTubeTime = maxTubeTime;
		this.maxBoxTime = maxBoxTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
		url = generateUrl(hostName, port, dbName);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
		url = generateUrl(hostName, port, dbName);
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
		url = generateUrl(hostName, port, dbName);
	}
	
	public long getMaxTubeTime() {
		return maxTubeTime;
	}

	public long getMaxBoxTime() {
		return maxBoxTime;
	}

	public String getUrl() {
		return url;
	}


	/**
	 * Generate the appropriate url from the given host name, port and db name.
	 * Note that host and port can be empty
	 * @param host
	 * @param port
	 * @param dbName
	 * @return
	 */
	public String generateUrl(String host, String port, String dbName) {
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

	public int getIntervalsNumber() {
		return intervalsNumber;
	}

	public String getCsvDateFormat() {
		return csvDateFormat;
	}

	public String getDbDateFormat() {
		return dbDateFormat;
	}

}
