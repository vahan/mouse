package mouse.dbTableModels;

import java.util.Date;


public class Log extends DbTableModel {

	private final String fileName;
	private final Date firstReading;
	private final Date lastReading;
	private final float duration;
	private final Date importedAt;
	private final int nbReadings;
	private final float size;
	
	public Log(String fileName, Date firstReading, Date lastReading,
			float duration, Date importedAt, int nbReadings, float size) {
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

	public Date getFirstReading() {
		return firstReading;
	}

	public Date getLastReading() {
		return lastReading;
	}

	public float getDuration() {
		return duration;
	}

	public Date getImportedAt() {
		return importedAt;
	}

	public int getNbReadings() {
		return nbReadings;
	}

	public float getSize() {
		return size;
	}

	
	
	
	
	
}
