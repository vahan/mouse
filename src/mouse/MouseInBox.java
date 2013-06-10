package mouse;

import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.Transponder;


public class MouseInBox {

	private final Transponder mouse;
	
	private final Box box;
	
	private Antenna antenna;
	
	private final TimeStamp timeStamp;

	public MouseInBox(Transponder mouse, Box box, Antenna antenna, TimeStamp timeStamp) {
		super();
		this.mouse = mouse;
		this.box = box;
		this.antenna = antenna;
		this.timeStamp = timeStamp;
	}

	public Transponder getMouse() {
		return mouse;
	}

	public Box getBox() {
		return box;
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	public Antenna getAntenna() {
		return antenna;
	}

	public void setAntenna(Antenna antenna) {
		this.antenna = antenna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result + ((mouse == null) ? 0 : mouse.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MouseInBox other = (MouseInBox) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (mouse == null) {
			if (other.mouse != null)
				return false;
		} else if (!mouse.equals(other.mouse))
			return false;
		return true;
	}
	
	
	
}
