package mouse.dbTableRows;

import mouse.TimeStamp;

/**
 * Models a row for the stay_results table
 * @author vahan
 *
 */
public class StayResultRow extends DbDynamicTableRow {
	
	private final TimeStamp start;
	private final TimeStamp stop;
	private final float duration;
	private final DirectionResultRow dirIn;
	private final DirectionResultRow dirOut;
	
	public StayResultRow(TimeStamp start, TimeStamp stop, TransponderRow transponder, 
						BoxRow box, DirectionResultRow dirIn, DirectionResultRow dirOut) {
		super(transponder, box);
		this.start = start;
		this.stop = stop;
		this.duration = TimeStamp.duration(start, stop);
		this.dirIn = dirIn;
		this.dirOut = dirOut;
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

	public DirectionResultRow getDirIn() {
		return dirIn;
	}

	public DirectionResultRow getDirOut() {
		return dirOut;
	}

	@Override
	public String toString() {
		return "StayResult [start=" + start + ", stop=" + stop
				+ ", transponder=" + transponder + ", box=" + source + ", dirIn="
				+ dirIn + ", dirOut=" + dirOut + "]";
	}

	@Override
	public TimeStamp timeStamp(int timeStampIndex) {
		return timeStampIndex == 0 ? start : stop;
	}

	@Override
	public DbStaticTableRow staticTableRow(int staticTableRowIndex) {
		return staticTableRowIndex == 0 ? transponder : source;
	}
	
	

}
