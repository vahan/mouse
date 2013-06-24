package dataProcessing;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaRow;

/**
 * Represents antenna-recordtime connection
 * @author vahan
 *
 */
public class AntennaRecord {
	
	private final AntennaRow antenna;
	
	private TimeStamp recordTime;

	public AntennaRecord(AntennaRow antenna, TimeStamp recordTime) {
		super();
		this.antenna = antenna;
		this.recordTime = recordTime;
	}

	public TimeStamp getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(TimeStamp recordTime) {
		this.recordTime = recordTime;
	}

	public AntennaRow getAntenna() {
		return antenna;
	}

	@Override
	public String toString() {
		return "AntennaRecord [antenna=" + antenna + ", recordTime="
				+ recordTime + "]";
	}
	
	
	

}
