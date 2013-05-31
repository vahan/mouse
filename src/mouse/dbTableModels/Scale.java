package mouse.dbTableModels;

import java.util.Date;

public class Scale {
	
	private final String name;
	private final String segment;
	private final float xPoint;
	private final float yPoint;
	private final Date lastReading;
	
	public Scale(String name, String segment, float xPoint, float yPoint,
			Date lastReading) {
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

	public Date getLastReading() {
		return lastReading;
	}
	
	
	

}
