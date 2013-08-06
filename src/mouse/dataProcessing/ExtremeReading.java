package mouse.dataProcessing;

import mouse.TimeStamp;
import mouse.dbTableRows.DbStaticTableRow;
import mouse.dbTableRows.TransponderRow;

public class ExtremeReading {

	private TimeStamp timeStamp;

	private TransponderRow transponder;

	private DbStaticTableRow source;

	public ExtremeReading(TimeStamp timeStamp, TransponderRow transponder,
			DbStaticTableRow source) {
		super();
		this.timeStamp = timeStamp;
		this.transponder = transponder;
		this.source = source;
	}

	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(TimeStamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public TransponderRow getTransponder() {
		return transponder;
	}

	public void setTransponder(TransponderRow antenna) {
		this.transponder = antenna;
	}

	public DbStaticTableRow getSource() {
		return source;
	}

	public void setAntenna(DbStaticTableRow source) {
		this.source = source;
	}

}
