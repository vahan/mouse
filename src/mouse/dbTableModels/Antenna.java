package mouse.dbTableModels;

import java.util.ArrayList;

import mouse.TimeStamp;


public class Antenna extends DbTableModel {
	
	private final String name;
	private final String position;
	private TimeStamp lastReading;
	private final Box box;
	
	private static ArrayList<Antenna> antennas = new ArrayList<Antenna>();
	
	public Antenna(String position, Box box) {
		super();
		this.position = position;
		this.box = box;
		this.name = generateName();
		
		antennas.add(this);
	}
	
	public static Antenna getAntenna(Box box, String position) {
		for (Antenna antenna : antennas) {
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

	public Box getBox() {
		return box;
	}
	

}
