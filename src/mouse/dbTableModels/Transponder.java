package mouse.dbTableModels;

import java.util.ArrayList;

import mouse.Gender;
import mouse.TimeStamp;


public class Transponder extends DbTableModel {
	
	private final String rfid;
	private final Gender sex;
	private TimeStamp firstReading;
	private TimeStamp lastReading;
	private TimeStamp firstScaleReading;
	private TimeStamp lastScaleReadin;
	private Antenna lastAntenna;
	private int stayCount;
	private int meetingsCount;
	private int baladeCount;
	
	private static ArrayList<Transponder> transponders = new ArrayList<Transponder>();
	
	public Transponder(String rfid) {
		super();
		this.rfid = rfid;
		this.sex = findSex();
		
		transponders.add(this);
	}
	
	public static Transponder getTransponder(String rfid) {
		for (Transponder transp : transponders) {
			if (transp.getRfid().equals(rfid))
				return transp;
		}
		return null;
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

	public void setLastScaleReadin(TimeStamp lastScaleReadin) {
		this.lastScaleReadin = lastScaleReadin;
	}

	public void setLastAntenna(Antenna lastAntenna) {
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
		return null;
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

	public TimeStamp getLastScaleReadin() {
		return lastScaleReadin;
	}

	public Antenna getLastAntenna() {
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
	
	

}
