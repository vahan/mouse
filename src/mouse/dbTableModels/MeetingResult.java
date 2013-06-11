package mouse.dbTableModels;

import mouse.TimeStamp;


public class MeetingResult extends DbTableModel {
	
	private final Transponder transponderFrom;
	private final Transponder transponderTo;
	private final TimeStamp start;
	private final TimeStamp stop;
	private final float duration;
	private final int terminatedBy;
	private final Box box;
	
	public MeetingResult(Transponder transponderFrom, Transponder transponderTo, TimeStamp start, TimeStamp stop,
			float duration, int terminatedBy, Box box) {
		super();
		this.transponderFrom = transponderFrom;
		this.transponderTo = transponderTo;
		this.start = start;
		this.stop = stop;
		this.duration = duration;
		this.terminatedBy = terminatedBy;
		this.box = box;
	}

	public Transponder getTransponderFrom() {
		return transponderFrom;
	}

	public Transponder getTransponderTo() {
		return transponderTo;
	}

	public TimeStamp getStart() {
		return start;
	}

	public TimeStamp getStop() {
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

	@Override
	public String toString() {
		return "MeetingResult [transponderFrom=" + transponderFrom
				+ ", transponderTo=" + transponderTo + ", start=" + start
				+ ", stop=" + stop + ", terminatedBy=" + terminatedBy
				+ ", box=" + box + "]";
	}
	
	

}
