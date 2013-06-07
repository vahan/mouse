package mouse.dbTableModels;


public class Direction {
	
	private final Antenna in;
	
	private final Antenna out;

	public Direction(Antenna in, Antenna out) {
		super();
		
		this.in = in;
		this.out = out;
	}

	public Antenna getIn() {
		return in;
	}

	public Antenna getOut() {
		return out;
	}

	@Override
	public String toString() {
		int inPos = Integer.parseInt(in.getPosition());
		int outPos = Integer.parseInt(out.getPosition());
		if (inPos == outPos)
			return null;
		return inPos < outPos ? "IN" : "OUT";
	}
	
	

}
