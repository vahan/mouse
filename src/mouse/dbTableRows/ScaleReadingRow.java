package mouse.dbTableRows;

import mouse.TimeStamp;


/**
 * Models a row for the scale_readings table
 * @author vahan
 *
 */
public class ScaleReadingRow extends DbTableRow {
	
	private final TimeStamp timeStamp;
	private final float weight;
	private final LogRow log;
	private final TransponderRow transponder;
	private final ScaleRow scale;
	
	public ScaleReadingRow(TimeStamp timeStamp, float weight, LogRow log,
			TransponderRow transponder, ScaleRow scale) {
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

	public LogRow getLog() {
		return log;
	}

	public TransponderRow getTransponder() {
		return transponder;
	}

	public ScaleRow getScale() {
		return scale;
	}

	@Override
	public String toString() {
		return "ScaleReading [timeStamp=" + timeStamp + ", weight=" + weight
				+ ", transponder=" + transponder + ", scale=" + scale + "]";
	}
	
	

}
