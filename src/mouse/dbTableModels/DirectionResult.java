package mouse.dbTableModels;

public class DirectionResult {
	
	private final String timeStamp;
	private final String direction;
	private final String rfid;
	private final Transponder transponder;
	private final Box box;
	private final StayResult stay;
	
	
	public DirectionResult(String timestamp, String direction, String rfid,
			Transponder transponder, String boxName, Box box, StayResult stay) {
		super();
		this.timeStamp = timestamp;
		this.direction = direction;
		this.rfid = rfid;
		this.transponder = transponder;
		this.box = box;
		this.stay = stay;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getDirection() {
		return direction;
	}

	public String getRfid() {
		return rfid;
	}

	public Transponder getTransponder() {
		return transponder;
	}

	public Box getBox() {
		return box;
	}

	public StayResult getStayResult() {
		return stay;
	}
	
	
	

}
