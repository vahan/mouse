package mouse.dbTableRows;


import mouse.TimeStamp;


/**
 * Models a row for the antenna_readings table
 * @author vahan
 *
 */
public class AntennaReadingRow extends DbDynamicTableRow {
	
	private final TimeStamp timeStamp;
	private LogRow log;
	private DirectionResultRow directionResult;
	
	public AntennaReadingRow(TimeStamp timeStamp, TransponderRow transponder,
			AntennaRow antena) {
		super(transponder, antena);
		this.timeStamp = timeStamp;
	}

	public void setDirectionResult(DirectionResultRow directionResult) {
		this.directionResult = directionResult;
	}

	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	public LogRow getLog() {
		return log;
	}

	public void setLog(LogRow log) {
		this.log = log;
	}

	public DirectionResultRow getDirectionResult() {
		return directionResult;
	}

	@Override
	public String toString() {
		return "AntennaReading [timeStamp=" + timeStamp + ", log=" + log
				+ ", transponder=" + transponder + ", antena=" + source
				+ ", directionResult=" + directionResult + "]";
	}

	@Override
	public TimeStamp timeStamp(int timeStampIndex) {
		// TODO Auto-generated method stub
		return timeStamp;
	}

	@Override
	public DbStaticTableRow staticTableRow(int staticTableRowIndex) {
		// TODO Auto-generated method stub
		return staticTableRowIndex == 0 ? source : transponder;
	}

	
	

}
