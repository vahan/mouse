package mouse.dbTableRows;

import mouse.TimeStamp;

/**
 * Models a row for the stay_results table
 * @author vahan
 *
 */
public class StayResultRow extends DbDynamicTableRow {
	
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_CORRUPT = 2;
	
	private final TimeStamp start;
	private final TimeStamp stop;
	private final long duration;
	private final DirectionResultRow dirIn;
	private final DirectionResultRow dirOut;
	private final int type;
	
	public StayResultRow(TimeStamp start, TimeStamp stop, TransponderRow transponder, 
						BoxRow box, DirectionResultRow dirIn, DirectionResultRow dirOut, int type) {
		super(transponder, box);
		this.start = start;
		this.stop = stop;
		this.duration = TimeStamp.duration(start, stop);
		this.dirIn = dirIn;
		this.dirOut = dirOut;
		this.type = type;
	}

	public TimeStamp getStart() {
		return start;
	}

	public TimeStamp getStop() {
		return stop;
	}

	public long getDuration() {
		return duration;
	}

	public DirectionResultRow getDirIn() {
		return dirIn;
	}

	public DirectionResultRow getDirOut() {
		return dirOut;
	}
	
	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "StayResult [start=" + start + ", stop=" + stop
				+ ", transponder=" + transponder + ", box=" + source + ", dirIn="
				+ dirIn + ", dirOut=" + dirOut + ", type=" + type + "]";
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
