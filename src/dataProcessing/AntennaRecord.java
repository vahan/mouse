package dataProcessing;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaRow;

/**
 * Represents antenna-recordtime connection
 * 
 * @author vahan
 * 
 */
public class AntennaRecord implements Comparable<Object> {

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

	@Override
	public int compareTo(Object o) {
		AntennaRecord antRecord = (AntennaRecord) o;
		if (antRecord == null)
			throw new IllegalArgumentException(
					"Can't compare objects of different types");
		if (recordTime.after(antRecord.getRecordTime()))
			return 1;
		else if (recordTime.before(antRecord.getRecordTime()))
			return -1;
		return 0;
	}

}
