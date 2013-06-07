package mouse.dbTableModels;

import mouse.TimeStamp;


public class StayResult extends DbTableModel {
	
	private final TimeStamp start;
	private final TimeStamp stop;
	private final float duration;
	private final Transponder transponder;
	private final Box box;
	private final DirectionResult dirIn;
	private final DirectionResult dirOut;
	
	public StayResult(TimeStamp start, TimeStamp stop, float duration, String rfid,
			Transponder transponder, String boxName, Box box, 
			DirectionResult dirIn, DirectionResult dirOut) {
		super();
		this.start = start;
		this.stop = stop;
		this.duration = duration;
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
	
	

}
