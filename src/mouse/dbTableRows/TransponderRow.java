package mouse.dbTableRows;

import java.util.ArrayList;

import mouse.Gender;
import mouse.TimeStamp;

/**
 * Models a row for the transponders table
 * @author vahan
 *
 */
public class TransponderRow extends DbStaticTableRow {
	
	private final String rfid;
	private final Gender sex;
	private TimeStamp firstReading;
	private TimeStamp lastReading;
	private TimeStamp firstScaleReading;
	private TimeStamp lastScaleReading;
	private AntennaRow lastAntenna;
	private int stayCount;
	private int meetingsCount;
	private int baladeCount;
	
	/**
	 * Keeps the list of all existing transponders.
	 * A new item to this list should be added only on the last line of the constructor 
	 */
	private static ArrayList<TransponderRow> transponders = new ArrayList<TransponderRow>();
	
	public TransponderRow(String rfid) {
		super();
		this.rfid = rfid;
		this.sex = findSex();
		
		transponders.add(this);
	}
	
	public static TransponderRow getTransponder(String rfid) {
		for (TransponderRow transp : transponders) {
			if (transp.getRfid().equals(rfid))
				return transp;
		}
		return new TransponderRow("NaT");
	}

	public void setFirstReading(TimeStamp firstReading) {
		this.firstReading = firstReading;
	}

	public void setLastReading(TimeStamp lastReading) {
		this.lastReading = lastReading;
	}

	public void setFirstScaleReading(TimeStamp firstScaleReading) {
		this.firstScaleReading = firstScaleReading;
	}

	public void setLastScaleReading(TimeStamp lastScaleReadin) {
		this.lastScaleReading = lastScaleReadin;
	}

	public void setLastAntenna(AntennaRow lastAntenna) {
		this.lastAntenna = lastAntenna;
	}

	public void setStayCount(int stayCount) {
		this.stayCount = stayCount;
	}

	public void setMeetingsCount(int meetingsCount) {
		this.meetingsCount = meetingsCount;
	}

	public void setBaladeCount(int baladeCount) {
		this.baladeCount = baladeCount;
	}

	private Gender findSex() {
		// TODO Auto-generated method stub
		return Gender.ItsComplicated;
	}

	public String getRfid() {
		return rfid;
	}

	public Gender getSex() {
		return sex;
	}

	public TimeStamp getFirstReading() {
		return firstReading;
	}

	public TimeStamp getLastReading() {
		return lastReading;
	}

	public TimeStamp getFirstScaleReading() {
		return firstScaleReading;
	}

	public TimeStamp getLastScaleReading() {
		return lastScaleReading;
	}

	public AntennaRow getLastAntenna() {
		return lastAntenna;
	}

	public int getStayCount() {
		return stayCount;
	}

	public int getMeetingsCount() {
		return meetingsCount;
	}

	public int getBaladeCount() {
		return baladeCount;
	}
	
	@Override
	public TimeStamp[] getLastResults() {
		// TODO Auto-generated method stub
		return new TimeStamp[] {lastReading, lastScaleReading};
	}

	@Override
	public void setLastResult(TimeStamp result, int lastResultIndex) {
		// TODO Auto-generated method stub
		if (lastResultIndex == 0) {
			lastReading = result;
		} else {
			firstReading = result;
		}
	}
	
	

}
