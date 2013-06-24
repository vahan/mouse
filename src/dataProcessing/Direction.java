package dataProcessing;

import mouse.dbTableRows.AntennaRow;


/**
 * Models directions, i.e. the in and out antennas.
 * @author vahan
 *
 */
public class Direction {
	
	private final AntennaRow in;
	
	private final AntennaRow out;
	
	private Directions type;

	public Direction(AntennaRow in, AntennaRow out) {
		super();
		
		this.in = in;
		this.out = out;
		
		init();
	}

	public AntennaRow getIn() {
		return in;
	}

	public AntennaRow getOut() {
		return out;
	}
	
	public Directions getType() {
		return type;
	}

	@Override
	public String toString() {
		return type.toString();
	}
	
	/**
	 * Use the in, out and type members for calculating the hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((in == null) ? 0 : in.hashCode());
		result = prime * result + ((out == null) ? 0 : out.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Use the in, out and type members to compare objects for equality
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Direction other = (Direction) obj;
		if (in == null) {
			if (other.in != null)
				return false;
		} else if (!in.equals(other.in))
			return false;
		if (out == null) {
			if (other.out != null)
				return false;
		} else if (!out.equals(other.out))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Reveal the type of the direction based on the in and out antennas
	 */
	private void init() {
		int inPos = Integer.parseInt(in.getPosition());
		int outPos = Integer.parseInt(out.getPosition());
		if (inPos == outPos) {
			type = Directions.Undefined;
		} else {
			type = inPos > outPos ? Directions.In : Directions.Out;
		}
	}
	
	

}
