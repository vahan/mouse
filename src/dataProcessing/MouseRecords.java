package dataProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaReadingRow;
import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DirectionResultRow;
import mouse.dbTableRows.StayResultRow;
import mouse.dbTableRows.TransponderRow;
import mouse.postgresql.StayResults;

public class MouseRecords {
	
	private final TransponderRow mouse;
	
	private final HashSet<AntennaRecord> antennaRecords = new HashSet<AntennaRecord>();

	public MouseRecords(TransponderRow mouse) {
		super();
		this.mouse = mouse;
	}

	public TransponderRow getMouse() {
		return mouse;
	}
	
	public void add(AntennaRecord antennaRecord) {
		antennaRecords.add(antennaRecord);
	}
	
	public boolean contains(AntennaRecord antennaRecord) {
		return antennaRecords.contains(antennaRecord);
	}
	
	
	public void addDirectionAndStayResults(ArrayList<DirectionResultRow> dirResults, ArrayList<StayResultRow> stayResults) {
		AntennaRecord[] antRecArray = antennaRecords.toArray(new AntennaRecord[antennaRecords.size()]);
		Arrays.sort(antRecArray);
		for (int i = 0; i < antRecArray.length - 4; ) {
			BoxRow box = antRecArray[i + 1].getAntenna().getBox();
			if (antRecArray[i + 1].getAntenna().getBox() != box) {
				i++;
				continue;
			}
			//add the direction result to the array
			DirectionResultRow inDirRes = getDirectionResultRow(antRecArray[i], antRecArray[i + 1]);
			if (inDirRes == null) {
				i++;
				continue;
			}
			dirResults.add(inDirRes);
			if (antRecArray[i + 2].getAntenna().getBox() != box || antRecArray[i + 3].getAntenna().getBox() != box) {
				i += 2;
				continue;
			}
			DirectionResultRow outDirRes = getDirectionResultRow(antRecArray[i + 2], antRecArray[i + 3]);
			if (outDirRes == null) {
				i += 2;
				continue;
			}
			dirResults.add(outDirRes);
			
			if (inDirRes.getDirection().getType() != Directions.In || 
					outDirRes.getDirection().getType() != Directions.Out) {
				i += 2;
				continue;
			}
			StayResultRow stayRes = getStayResultRow(inDirRes, outDirRes);
			stayResults.add(stayRes);
			i += 4;
		}
	}
	
	
	private DirectionResultRow getDirectionResultRow(AntennaRecord in, AntennaRecord out) {
		if (in.getAntenna().getBox() != out.getAntenna().getBox())
			return null;
		BoxRow box = in.getAntenna().getBox();
		TimeStamp timestamp = in.getRecordTime();
		if (out.getRecordTime().before(in.getRecordTime())) {
			AntennaRecord temp = in;
			in = out;
			out = temp;
		}
		Direction direction = new Direction(in.getAntenna(), out.getAntenna());
		if (direction.toString() == null)
			return null;
		DirectionResultRow dirResult = new DirectionResultRow(timestamp, direction, mouse, box);
		return dirResult;
	}
	
	
	private StayResultRow getStayResultRow(DirectionResultRow firstDir, DirectionResultRow secondDir) {
		//The first event must be before the second
		if (firstDir.getTimeStamp().after(secondDir.getTimeStamp())) {
			//notifyMessage("swapping");
			DirectionResultRow temp = firstDir;
			firstDir = secondDir;
			secondDir = temp;
		}
		//In must be before out
		if (firstDir.getDirection().getType() == Directions.In && 
				secondDir.getDirection().getType() == Directions.Out) {
			TimeStamp start = firstDir.getTimeStamp();
			TimeStamp stop = secondDir.getTimeStamp();
			BoxRow box = firstDir.getDirection().getIn().getBox();
			//Create and store the stayResult data entry
			StayResultRow stayResult = new StayResultRow(start, stop, mouse, box, firstDir, secondDir);
			mouse.addStay(); //increment the stay count
			return stayResult;
		}
		return null;
	}

}
