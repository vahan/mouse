package mouse.dbTableModels;

public class Antenna {
	
	private final String name;
	private final String position;
	private String lastReading;
	private final Box box;
	
	public Antenna(String position, Box box) {
		super();
		this.name = generateName();
		this.position = position;
		this.box = box;
	}

	private String generateName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public String getLastReading() {
		return lastReading;
	}

	public void setLastReading(String lastReading) {
		this.lastReading = lastReading;
	}

	public Box getBox() {
		return box;
	}
	
	
	
	

}
