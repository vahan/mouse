package mouse.dbTableRows;

import mouse.TimeStamp;

/**
 * Models a row for meeting_results table
 * @author vahan
 *
 */
public class MeetingResultRow extends DbDynamicTableRow {
	
	private final TransponderRow transponderTo;
	private final TimeStamp start;
	private final TimeStamp stop;
	private final float duration;
	private final int terminatedBy;
	
	public MeetingResultRow(TransponderRow transponderFrom, TransponderRow transponderTo, 
			TimeStamp start, TimeStamp stop, int terminatedBy, BoxRow box) {
		super(transponderFrom, box);
		this.transponderTo = transponderTo;
		this.start = start;
		this.stop = stop;
		this.duration = TimeStamp.duration(start, stop);
		this.terminatedBy = terminatedBy;
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

	@Override
	public String toString() {
		return "MeetingResult [transponderFrom=" + transponder
				+ ", transponderTo=" + transponderTo + ", start=" + start
				+ ", stop=" + stop + ", terminatedBy=" + terminatedBy
				+ ", box=" + source + "]";
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
			case 0:		return transponder;
			case 1:		return transponderTo;
			case 2:		return source;
			default:	return null;
		}
	}
	
	

}
