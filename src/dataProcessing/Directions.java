package dataProcessing;

/**
 * Possible direction types
 * @author vahan
 *
 */
public enum Directions {
	
	In,	//The mouse went into the box
	Out,	//The mouse left the box
	SpecialOut,	//For the 2-1-[2] special case
	Undefined	//The mouse made an unclear movement

}
