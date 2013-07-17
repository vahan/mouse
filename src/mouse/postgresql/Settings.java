package mouse.postgresql;

public class Settings {
	
	private final String username;
	
	private final String password;
	
	private final String hostName;
	
	private final String port;
	
	private final String dbName;
	
	private final String url;

	private final int intervalsNumber;

	private final String csvDateFormat;

	private final String dbDateFormat;
	
	private final long maxTubeTime;
	
	private final long maxBoxTime;
	
	private final long minTubeTime;
	
	private final long minBoxTime;

	public Settings(String username, String password, String hostName,
			String port, String dbName, int intervalsNumber,
			String csvDateFormat, String dbDateFormat, 
			long maxTubeTime, long maxBoxTime, long minTubeTime, long minBoxTime) {
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
		this.minTubeTime = minTubeTime;
		this.minBoxTime = minBoxTime;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getHostName() {
		return hostName;
	}

	public String getPort() {
		return port;
	}

	public String getDbName() {
		return dbName;
	}
	
	public long getMaxTubeTime() {
		return maxTubeTime;
	}

	public long getMaxBoxTime() {
		return maxBoxTime;
	}
	
	public long getMinTubeTime() {
		return minTubeTime;
	}

	public long getMinBoxTime() {
		return minBoxTime;
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
