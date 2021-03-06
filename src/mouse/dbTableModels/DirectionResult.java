package mouse.dbTableModels;

import mouse.Direction;
import mouse.TimeStamp;

/**
 * Models a row for the direction_results table
 * @author vahan
 *
 */
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

	@Override
	public String toString() {
		return "DirectionResult [timeStamp=" + timeStamp + ", direction="
				+ direction + ", transponder=" + transponder + ", box=" + box
				+ "]";
	}
	
	

}
