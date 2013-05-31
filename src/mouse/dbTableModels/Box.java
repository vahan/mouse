package mouse.dbTableModels;

import java.util.Date;

public class Box {
	
	private final String name;
	private final String segment;
	private float xPos;
	private float yPos;
	private Date lastDirectionResult;
	private Date lastMeetin;
	
	public Box(String name) {
		super();
		this.name = name;
		this.segment = findSegment();
	}

	private String findSegment() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return name;
	}

	public String getSegment() {
		return segment;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	public void setLastDirectionResult(Date lastDirectionResult) {
		this.lastDirectionResult = lastDirectionResult;
	}

	public void setLastMeetin(Date lastMeetin) {
		this.lastMeetin = lastMeetin;
	}

	public float getxPos() {
		return xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public Date getLastDirectionResult() {
		return lastDirectionResult;
	}

	public Date getLastMeetin() {
		return lastMeetin;
	}
	
	
	
	
	

}
