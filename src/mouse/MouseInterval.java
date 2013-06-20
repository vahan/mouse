package mouse;

/**
 * Represents mouse-start-stop connections. Used to simplify data proccessing
 */
import mouse.dbTableRows.TransponderRow;

public class MouseInterval {

	private final TransponderRow mouse;
	
	private final TimeStamp start;
	
	private final TimeStamp stop;

	public MouseInterval(TransponderRow mouse, TimeStamp start, TimeStamp stop) {
		super();
		this.mouse = mouse;
		this.start = start;
		this.stop = stop;
	}

	public TransponderRow getMouse() {
		return mouse;
	}

	public TimeStamp getStart() {
		return start;
	}

	public TimeStamp getStop() {
		return stop;
	}
	
	
	
	
}
