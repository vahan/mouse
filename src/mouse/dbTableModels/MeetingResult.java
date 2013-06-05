package mouse.dbTableModels;

import java.util.Date;


public class MeetingResult {
	
	private final String rfidFrom;
	private final String rfidTo;
	private final Date start;
	private final Date stop;
	private final float duration;
	private final int terminatedBy;
	private final Box box;
	
	public MeetingResult(String rfidFrom, String rfidTo, Date start, Date stop,
			float duration, int terminatedBy, Box box) {
		super();
		this.rfidFrom = rfidFrom;
		this.rfidTo = rfidTo;
		this.start = start;
		this.stop = stop;
		this.duration = duration;
		this.terminatedBy = terminatedBy;
		this.box = box;
	}

	public String getRfidFrom() {
		return rfidFrom;
	}

	public String getRfidTo() {
		return rfidTo;
	}

	public Date getStart() {
		return start;
	}

	public Date getStop() {
		return stop;
	}

	public float getDuration() {
		return duration;
	}

	public int getTerminatedBy() {
		return terminatedBy;
	}

	public Box getBox() {
		return box;
	}
	

}
