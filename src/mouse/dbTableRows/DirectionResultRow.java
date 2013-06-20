package mouse.dbTableRows;

import mouse.Direction;
import mouse.TimeStamp;

/**
 * Models a row for the direction_results table
 * @author vahan
 *
 */
public class DirectionResultRow extends DbDynamicTableRow {
	
	private final TimeStamp timeStamp;
	private final Direction direction;
	private final TransponderRow transponder;
	private final BoxRow box;
	
	
	public DirectionResultRow(TimeStamp timestamp, Direction direction, TransponderRow transponder, BoxRow box) {
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

	public TransponderRow getTransponder() {
		return transponder;
	}

	public BoxRow getBox() {
		return box;
	}

	@Override
	public String toString() {
		return "DirectionResult [timeStamp=" + timeStamp + ", direction="
				+ direction + ", transponder=" + transponder + ", box=" + box
				+ "]";
	}

	@Override
	public TimeStamp timeStamp(int timeStampIndex) {
		// TODO Auto-generated method stub
		return timeStamp;
	}

	@Override
	public DbStaticTableRow staticTableRow(int staticTableRowIndex) {
		// TODO Auto-generated method stub
		return box;
	}
	
	

}
