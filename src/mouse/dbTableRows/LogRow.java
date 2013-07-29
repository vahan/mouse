package mouse.dbTableRows;

import java.io.File;

import mouse.TimeStamp;

/**
 * Models a row for the logs table
 * 
 * @author vahan
 * 
 */
public class LogRow extends DbTableRow {

	private final String fileName;
	private TimeStamp firstReading;
	private TimeStamp lastReading;
	private float duration;
	private final TimeStamp importedAt;
	private int nbReadings;
	private final long size;

	public LogRow(String fileName) {
		super();
		this.fileName = fileName;
		this.importedAt = new TimeStamp();
		this.size = (new File(fileName)).length();
	}

	public String getFileName() {
		return fileName;
	}

	public TimeStamp getFirstReading() {
		return firstReading;
	}

	public TimeStamp getLastReading() {
		return lastReading;
	}

	public float getDuration() {
		return duration;
	}

	public TimeStamp getImportedAt() {
		return importedAt;
	}

	public int getNbReadings() {
		return nbReadings;
	}

	public long getSize() {
		return size;
	}

	public void setFirstReading(TimeStamp firstReading) {
		this.firstReading = firstReading;
	}

	public void setLastReading(TimeStamp lastReading) {
		this.lastReading = lastReading;
	}

	public void setNbReadings(int nbReadings) {
		this.nbReadings = nbReadings;
	}

	public void setDuration() {
		this.duration = TimeStamp.duration(firstReading, lastReading);
	}

}
