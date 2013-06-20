package mouse.dbTableRows;

import java.util.ArrayList;

import mouse.TimeStamp;

/**
 * Models a row for the antennas table's
 * @author vahan
 *
 */
public class AntennaRow extends DbStaticTableRow {
	
	private final String name;
	private final String position;
	private TimeStamp lastReading;
	private final BoxRow box;
	
	private static ArrayList<AntennaRow> antennas = new ArrayList<AntennaRow>();
	
	public AntennaRow(String position, BoxRow box) {
		super();
		this.position = position;
		this.box = box;
		this.name = generateName();
		
		antennas.add(this);
	}
	
	public static AntennaRow getAntenna(BoxRow box, String position) {
		for (AntennaRow antenna : antennas) {
			if (antenna.getBox() == box && antenna.getPosition().equals(position))
				return antenna;
		}
		return null;
	}

	private String generateName() {
		String name = box.getName() + "-" + position; 
		return name;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public TimeStamp getLastReading() {
		return lastReading;
	}

	public void setLastReading(TimeStamp lastReading) {
		this.lastReading = lastReading;
	}

	public BoxRow getBox() {
		return box;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AntennaRow other = (AntennaRow) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public TimeStamp[] getLastResults() {
		return new TimeStamp[] {lastReading};
	}

	@Override
	public void setLastResult(TimeStamp result, int lastResultIndex) {
		// TODO Auto-generated method stub
		lastReading = result;
	}
	

}
