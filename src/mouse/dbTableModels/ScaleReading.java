package mouse.dbTableModels;

import mouse.TimeStamp;



public class ScaleReading extends DbTableModel {
	
	private final TimeStamp timeStamp;
	private final float weight;
	private final Log log;
	private final Transponder transponder;
	private final Scale scale;
	
	public ScaleReading(TimeStamp timeStamp, float weight, Log log,
			Transponder transponder, Scale scale) {
		super();
		this.timeStamp = timeStamp;
		this.weight = weight;
		this.log = log;
		this.transponder = transponder;
		this.scale = scale;
	}

	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	public float getWeight() {
		return weight;
	}

	public Log getLog() {
		return log;
	}

	public Transponder getTransponder() {
		return transponder;
	}

	public Scale getScale() {
		return scale;
	}
	

}
