package mouse.dbTableRows;

import java.util.ArrayList;

import mouse.TimeStamp;


/**
 * Models a row for the boxes table's 
 * @author vahan
 *
 */
public class BoxRow extends DbStaticTableRow {
	
	private final String name;
	private final String segment;
	private String xPos;
	private String yPos;
	private TimeStamp lastDirectionResult;
	private TimeStamp lastMeeting;
	
	private static ArrayList<BoxRow> boxes = new ArrayList<BoxRow>();
	
	public BoxRow(String name) {
		super();
		this.name = name;
		this.segment = findSegment();
		
		boxes.add(this);
	}
	
	public static BoxRow getBoxByName(String boxName) {
		for (BoxRow box : boxes) {
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
		this.lastMeeting = lastMeetin;
	}

	public String getxPos() {
		return xPos;
	}

	public String getyPos() {
		return yPos;
	}

	public TimeStamp getLastDirectionResult() {
		return lastDirectionResult;
	}

	public TimeStamp getLastMeetin() {
		return lastMeeting;
	}

	@Override
	public TimeStamp[] getLastResults() {
		// TODO Auto-generated method stub
		return new TimeStamp[] {lastDirectionResult, lastMeeting};
	}

	@Override
	public void setLastResult(TimeStamp result, int lastResultIndex) {
		if (lastResultIndex == 0) {
			this.lastDirectionResult = result;
		} else {
			this.lastMeeting = result;
		}
		
	}

	public static BoxRow getBoxById(String boxId) {
		for (BoxRow box : boxes) {
			if (box.getId().equals(boxId))
				return box;
		}
		return null;
	}

	public void setxPos(String xPos) {
		this.xPos = xPos;
	}

	public void setyPos(String yPos) {
		this.yPos = yPos;
	}
	
	
	

}
