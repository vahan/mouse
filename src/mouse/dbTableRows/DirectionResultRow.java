package mouse.dbTableRows;

import dataProcessing.Direction;
import mouse.TimeStamp;

/**
 * Models a row for the direction_results table
 * 
 * @author vahan
 * 
 */
public class DirectionResultRow extends DbDynamicTableRow {

	private final TimeStamp timeStamp;
	private final Direction direction;

	public DirectionResultRow(TimeStamp timestamp, Direction direction,
			TransponderRow transponder, BoxRow box) {
		super(transponder, box);
		this.timeStamp = timestamp;
		this.direction = direction;
	}

	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "DirectionResult [timeStamp=" + timeStamp + ", direction="
				+ direction + ", transponder=" + transponder + ", box="
				+ source + "]";
	}

	@Override
	public TimeStamp timeStamp(int timeStampIndex) {
		return timeStamp;
	}

	@Override
	public DbStaticTableRow staticTableRow(int staticTableRowIndex) {
		return source;
	}

}
