package mouse.dbTableModels;

import java.util.Date;


public class ScaleReading {
	
	private final Date timeStamp;
	private final float weight;
	private final Log log;
	private final Transponder transponder;
	private final Scale scale;
	
	public ScaleReading(Date timeStamp, float weight, Log log,
			Transponder transponder, Scale scale) {
		super();
		this.timeStamp = timeStamp;
		this.weight = weight;
		this.log = log;
		this.transponder = transponder;
		this.scale = scale;
	}

	public Date getTimeStamp() {
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
