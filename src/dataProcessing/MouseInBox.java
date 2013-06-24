package dataProcessing;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.TransponderRow;

/**
 * Represents mouse-box connections to simplify data processing
 * @author vahan
 *
 */
public class MouseInBox {

	private final TransponderRow mouse;
	
	private final BoxRow box;
	
	private AntennaRow antenna;
	
	private final TimeStamp timeStamp;

	public MouseInBox(TransponderRow mouse, BoxRow box, AntennaRow antenna, TimeStamp timeStamp) {
		super();
		this.mouse = mouse;
		this.box = box;
		this.antenna = antenna;
		this.timeStamp = timeStamp;
	}

	public TransponderRow getMouse() {
		return mouse;
	}

	public BoxRow getBox() {
		return box;
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	public AntennaRow getAntenna() {
		return antenna;
	}

	public void setAntenna(AntennaRow antenna) {
		this.antenna = antenna;
	}

	/**
	 * Use only box and mouse members for hashcode generation
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result + ((mouse == null) ? 0 : mouse.hashCode());
		return result;
	}

	/**
	 * Use only box and mouse members for comparing object equality
	 */
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
