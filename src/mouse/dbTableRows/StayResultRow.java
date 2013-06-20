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
	private final TransponderRow transponder;
	private final BoxRow box;
	private final DirectionResultRow dirIn;
	private final DirectionResultRow dirOut;
	
	public StayResultRow(TimeStamp start, TimeStamp stop, TransponderRow transponder, 
						BoxRow box, DirectionResultRow dirIn, DirectionResultRow dirOut) {
		super();
		this.start = start;
		this.stop = stop;
		this.duration = TimeStamp.duration(start, stop);
		this.transponder = transponder;
		this.box = box;
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

	public TransponderRow getTransponder() {
		return transponder;
	}

	public BoxRow getBox() {
		return box;
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
				+ ", transponder=" + transponder + ", box=" + box + ", dirIn="
				+ dirIn + ", dirOut=" + dirOut + "]";
	}

	@Override
	public TimeStamp timeStamp(int timeStampIndex) {
		// TODO Auto-generated method stub
		return timeStampIndex == 0 ? start : stop;
	}

	@Override
	public DbStaticTableRow staticTableRow(int staticTableRowIndex) {
		// TODO Auto-generated method stub
		return staticTableRowIndex == 0 ? transponder : box;
	}
	
	

}
