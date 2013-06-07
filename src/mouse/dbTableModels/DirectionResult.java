package mouse.dbTableModels;

import mouse.TimeStamp;


public class DirectionResult extends DbTableModel {
	
	private final TimeStamp timeStamp;
	private final Direction direction;
	private final Transponder transponder;
	private final Box box;
	
	
	public DirectionResult(TimeStamp timestamp, Direction direction, Transponder transponder, Box box) {
		super();
		this.timeStamp = timestamp;
		this.direction = direction;
		this.transponder = transponder;
		this.box = box;
	}

	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	public Direction getDirection() {
		return direction;
	}

	public Transponder getTransponder() {
		return transponder;
	}

	public Box getBox() {
		return box;
	}
	
	

}
