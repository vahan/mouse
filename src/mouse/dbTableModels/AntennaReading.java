package mouse.dbTableModels;


public class AntennaReading {
	
	private final String timeStamp;
	private Log log;
	private final Transponder transponder;
	private final Antenna antena;
	private DirectionResult directionResult;
	
	public AntennaReading(String timeStamp, Transponder transponder,
			Antenna antena) {
		super();
		this.timeStamp = timeStamp;
		this.transponder = transponder;
		this.antena = antena;
	}

	public void setDirectionResult(DirectionResult directionResult) {
		this.directionResult = directionResult;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Transponder getTransponder() {
		return transponder;
	}

	public Antenna getAntena() {
		return antena;
	}

	public DirectionResult getDirectionResult() {
		return directionResult;
	}
	
	
	

}
