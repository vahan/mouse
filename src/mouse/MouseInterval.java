package mouse;

/**
 * Represents mouse-start-stop connections. Used to simplify data proccessing
 */
import mouse.dbTableModels.Transponder;

public class MouseInterval {

	private final Transponder mouse;
	
	private final TimeStamp start;
	
	private final TimeStamp stop;

	public MouseInterval(Transponder mouse, TimeStamp start, TimeStamp stop) {
		super();
		this.mouse = mouse;
		this.start = start;
		this.stop = stop;
	}

	public Transponder getMouse() {
		return mouse;
	}

	public TimeStamp getStart() {
		return start;
	}

	public TimeStamp getStop() {
		return stop;
	}
	
	
	
	
}
