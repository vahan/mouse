package dataProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import mouse.TimeStamp;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DirectionResultRow;
import mouse.dbTableRows.StayResultRow;
import mouse.dbTableRows.TransponderRow;

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
	
	
	public void addDirectionAndStayResults(ArrayList<DirectionResultRow> dirResults, ArrayList<StayResultRow> stayResults,
				long maxTubeTime, long maxBoxTime) {
		AntennaRecord[] antRecArray = antennaRecords.toArray(new AntennaRecord[antennaRecords.size()]);
		Arrays.sort(antRecArray);
		for (int i = 0; i < antRecArray.length - 4; ) {
			BoxRow box = antRecArray[i + 1].getAntenna().getBox();
			if (antRecArray[i + 1].getAntenna().getBox() != box) {
				i++;
				continue;
			}
			//add the direction result to the array
			DirectionResultRow inDirRes = getDirectionResultRow(antRecArray[i], antRecArray[i + 1], maxTubeTime);
			if (inDirRes == null) {
				i++;
				continue;
			}
			dirResults.add(inDirRes);
			if (antRecArray[i + 2].getAntenna().getBox() != box || antRecArray[i + 3].getAntenna().getBox() != box) {
				i += 2;
				continue;
			}
			AntennaRecord ant1 = antRecArray[i + 2];
			AntennaRecord ant2 = (antRecArray[i + 3].getAntenna().getBox() == ant1.getAntenna().getBox())
									? antRecArray[i + 3]
									: antRecArray[i + 2]; //For the special case stayResult of form A1-A2-A1
			DirectionResultRow outDirRes = getDirectionResultRow(ant1, ant2, maxTubeTime);
			if (outDirRes == null) {
				i += ant1 == ant2 ? 1 : 2; //For the special case stayResult of form A1-A2-A1
				continue;
			}
			dirResults.add(outDirRes);
			
			if (inDirRes.getDirection().getType() != Directions.In || 
					outDirRes.getDirection().getType() != Directions.Out) {
				i += 2;
				continue;
			}
			StayResultRow stayRes = getStayResultRow(inDirRes, outDirRes, maxBoxTime);
			if (stayRes == null) {
				i += 4;
				continue;
			}
			stayResults.add(stayRes);
			i += 4;
		}
	}
	
	
	private DirectionResultRow getDirectionResultRow(AntennaRecord in, AntennaRecord out, 
				long maxTubeTime) {
		if (in.getAntenna().getBox() != out.getAntenna().getBox())
			return null;
		BoxRow box = in.getAntenna().getBox();
		TimeStamp timestamp = in.getRecordTime();
		if (out.getRecordTime().before(in.getRecordTime())) {
			AntennaRecord temp = in;
			in = out;
			out = temp;
		}
		if (TimeStamp.duration(in.getRecordTime(), out.getRecordTime()) > maxTubeTime)
			return null;
		Direction direction = new Direction(in.getAntenna(), out.getAntenna());
		if (direction.toString() == null)
			return null;
		DirectionResultRow dirResult = new DirectionResultRow(timestamp, direction, mouse, box);
		return dirResult;
	}
	
	
	private StayResultRow getStayResultRow(DirectionResultRow firstDir, DirectionResultRow secondDir,
				long maxBoxTime) {
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
			if (TimeStamp.duration(start, stop) > maxBoxTime)
				return null;
			BoxRow box = firstDir.getDirection().getIn().getBox();
			//Create and store the stayResult data entry
			StayResultRow stayResult = new StayResultRow(start, stop, mouse, box, firstDir, secondDir);
			mouse.addStay(); //increment the stay count
			return stayResult;
		}
		return null;
	}

}
