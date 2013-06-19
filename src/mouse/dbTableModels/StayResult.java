package mouse.dbTableModels;

import mouse.TimeStamp;

/**
 * Models a row for the stay_results table
 * @author vahan
 *
 */
public class StayResult extends DbTableModel {
	
	private final TimeStamp start;
	private final TimeStamp stop;
	private final float duration;
	private final Transponder transponder;
	private final Box box;
	private final DirectionResult dirIn;
	private final DirectionResult dirOut;
	
	public StayResult(TimeStamp start, TimeStamp stop, Transponder transponder, 
						Box box, DirectionResult dirIn, DirectionResult dirOut) {
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

	public Transponder getTransponder() {
		return transponder;
	}

	public Box getBox() {
		return box;
	}

	public DirectionResult getDirIn() {
		return dirIn;
	}

	public DirectionResult getDirOut() {
		return dirOut;
	}

	@Override
	public String toString() {
		return "StayResult [start=" + start + ", stop=" + stop
				+ ", transponder=" + transponder + ", box=" + box + ", dirIn="
				+ dirIn + ", dirOut=" + dirOut + "]";
	}
	
	

}
