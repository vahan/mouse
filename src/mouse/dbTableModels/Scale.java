package mouse.dbTableModels;

import mouse.TimeStamp;


public class Scale extends DbTableModel {
	
	private final String name;
	private final String segment;
	private final float xPoint;
	private final float yPoint;
	private final TimeStamp lastReading;
	
	public Scale(String name, String segment, float xPoint, float yPoint,
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
