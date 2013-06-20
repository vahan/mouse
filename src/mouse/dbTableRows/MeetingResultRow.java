package mouse.dbTableRows;

import mouse.TimeStamp;

/**
 * Models a row for meeting_results table
 * @author vahan
 *
 */
public class MeetingResultRow extends DbDynamicTableRow {
	
	private final TransponderRow transponderFrom;
	private final TransponderRow transponderTo;
	private final TimeStamp start;
	private final TimeStamp stop;
	private final float duration;
	private final int terminatedBy;
	private final BoxRow box;
	
	public MeetingResultRow(TransponderRow transponderFrom, TransponderRow transponderTo, 
			TimeStamp start, TimeStamp stop, int terminatedBy, BoxRow box) {
		super();
		this.transponderFrom = transponderFrom;
		this.transponderTo = transponderTo;
		this.start = start;
		this.stop = stop;
		this.duration = TimeStamp.duration(start, stop);
		this.terminatedBy = terminatedBy;
		this.box = box;
	}

	public TransponderRow getTransponderFrom() {
		return transponderFrom;
	}

	public TransponderRow getTransponderTo() {
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

	public BoxRow getBox() {
		return box;
	}

	@Override
	public String toString() {
		return "MeetingResult [transponderFrom=" + transponderFrom
				+ ", transponderTo=" + transponderTo + ", start=" + start
				+ ", stop=" + stop + ", terminatedBy=" + terminatedBy
				+ ", box=" + box + "]";
	}

	@Override
	public TimeStamp timeStamp(int timeStampIndex) {
		// TODO Auto-generated method stub
		return timeStampIndex == 0 ? start : stop;
	}

	@Override
	public DbStaticTableRow staticTableRow(int staticTableRowIndex) {
		// TODO Auto-generated method stub
		switch (staticTableRowIndex) {
			case 0:		return transponderFrom;
			case 1:		return transponderTo;
			case 2:		return box;
			default:	return null;
		}
	}
	
	

}
