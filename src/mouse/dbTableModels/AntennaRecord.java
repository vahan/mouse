package mouse.dbTableModels;

import mouse.TimeStamp;

public class AntennaRecord {
	
	private final Antenna antenna;
	
	private TimeStamp recordTime;

	public AntennaRecord(Antenna antenna, TimeStamp recordTime) {
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

	public Antenna getAntenna() {
		return antenna;
	}

	@Override
	public String toString() {
		return "AntennaRecord [antenna=" + antenna + ", recordTime="
				+ recordTime + "]";
	}
	
	
	

}
