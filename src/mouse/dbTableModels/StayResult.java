package mouse.dbTableModels;

public class StayResult {
	
	private final String start;
	private final String stop;
	private final float duration;
	private final String rfid;
	private final Transponder transponder;
	private final Box box;
	
	public StayResult(String start, String stop, float duration, String rfid,
			Transponder transponder, String boxName, Box box) {
		super();
		this.start = start;
		this.stop = stop;
		this.duration = duration;
		this.rfid = rfid;
		this.transponder = transponder;
		this.box = box;
	}

	public String getStart() {
		return start;
	}

	public String getStop() {
		return stop;
	}

	public float getDuration() {
		return duration;
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
	
	
	

}
