package mouse.dbTableModels;

import java.util.ArrayList;

import mouse.TimeStamp;


/**
 * Models a row for the boxes table's 
 * @author vahan
 *
 */
public class Box extends DbTableModel {
	
	private final String name;
	private final String segment;
	private final float xPos;
	private final float yPos;
	private TimeStamp lastDirectionResult;
	private TimeStamp lastMeetin;
	
	private static ArrayList<Box> boxes = new ArrayList<Box>();
	
	public Box(String name, float xPos, float yPos) {
		super();
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.segment = findSegment();
		
		boxes.add(this);
	}
	
	public static Box getBoxByName(String boxName) {
		for (Box box : boxes) {
			if (box.getName().equals(boxName))
				return box;
		}
		return null;
	}

	private String findSegment() {
		// TODO Auto-generated method stub
		return "A";
	}

	public String getName() {
		return name;
	}

	public String getSegment() {
		return segment;
	}

	public void setLastDirectionResult(TimeStamp lastDirectionResult) {
		this.lastDirectionResult = lastDirectionResult;
	}

	public void setLastMeetin(TimeStamp lastMeetin) {
		this.lastMeetin = lastMeetin;
	}

	public float getXPos() {
		return xPos;
	}

	public float getYPos() {
		return yPos;
	}

	public TimeStamp getLastDirectionResult() {
		return lastDirectionResult;
	}

	public TimeStamp getLastMeetin() {
		return lastMeetin;
	}

	
	
	
	

}
