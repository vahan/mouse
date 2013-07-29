package mouse.dbTableRows;

import mouse.TimeStamp;

/**
 * Models a row for the scales table's model
 * 
 * @author vahan
 * 
 */
public class ScaleRow extends DbTableRow {

	private final String name;
	private final String segment;
	private final float xPoint;
	private final float yPoint;
	private final TimeStamp lastReading;

	public ScaleRow(String name, String segment, float xPoint, float yPoint,
			TimeStamp lastReading) {
		super();
		this.name = name;
		this.segment = segment;
		this.xPoint = xPoint;
		this.yPoint = yPoint;
		this.lastReading = lastReading;
	}

	public String getName() {
		return name;
	}

	public String getSegment() {
		return segment;
	}

	public float getxPoint() {
		return xPoint;
	}

	public float getyPoint() {
		return yPoint;
	}

	public TimeStamp getLastReading() {
		return lastReading;
	}

}
