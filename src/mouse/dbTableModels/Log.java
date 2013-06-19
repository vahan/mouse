package mouse.dbTableModels;

import mouse.TimeStamp;

/**
 * Models a row for the logs table
 * @author vahan
 *
 */
public class Log extends DbTableModel {

	private final String fileName;
	private final TimeStamp firstReading;
	private final TimeStamp lastReading;
	private final float duration;
	private final TimeStamp importedAt;
	private final int nbReadings;
	private final float size;
	
	public Log(String fileName, TimeStamp firstReading, TimeStamp lastReading,
			float duration, TimeStamp importedAt, int nbReadings, float size) {
		super();
		this.fileName = fileName;
		this.firstReading = firstReading;
		this.lastReading = lastReading;
		this.duration = duration;
		this.importedAt = importedAt;
		this.nbReadings = nbReadings;
		this.size = size;
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

	public float getSize() {
		return size;
	}

	
	
	
	
	
}
