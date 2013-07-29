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

	public MouseRecords(TransponderRow mouse, AntennaRecord antennaRecord) {
		super();
		this.mouse = mouse;
		add(antennaRecord);
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

	public void addDirectionAndStayResults(
			ArrayList<DirectionResultRow> dirResults,
			ArrayList<StayResultRow> stayResults, long minTubeTime,
			long maxTubeTime, long minBoxTime, long maxBoxTime) {
		AntennaRecord[] antRecArray = antennaRecords
				.toArray(new AntennaRecord[antennaRecords.size()]);
		Arrays.sort(antRecArray);
		for (int i = 0; i < antRecArray.length;) {
			BoxRow box = antRecArray[i].getAntenna().getBox();
			if (i + 1 >= antRecArray.length)
				break;
			if (antRecArray[i + 1].getAntenna().getBox() != box) {
				i++;
				continue;
			}
			// add the direction result to the array
			DirectionResultRow inDirRes = getDirectionResultRow(antRecArray[i],
					antRecArray[i + 1], minTubeTime, maxTubeTime);
			if (inDirRes == null) {
				i++;
				continue;
			}
			dirResults.add(inDirRes);
			if (i + 2 >= antRecArray.length)
				break;
			AntennaRecord ant1 = antRecArray[i + 2];
			AntennaRecord ant2 = antRecArray[i + 2]; // For the special case
														// stayResult of form
														// A1-A2-A1
			if (antRecArray[i + 2].getAntenna().getBox() != box) {
				i += 2;
				continue;
			}
			if (i + 3 < antRecArray.length
					&& antRecArray[i + 3].getAntenna().getBox() == ant1
							.getAntenna().getBox())
				ant2 = antRecArray[i + 3];

			i += ant1 == ant2 ? 3 : 4; // For the special case stayResult of
										// form A1-A2-A1

			DirectionResultRow outDirRes = getDirectionResultRow(ant1, ant2,
					minTubeTime, maxTubeTime);
			if (outDirRes == null)
				continue;
			dirResults.add(outDirRes);
			StayResultRow stayRes = getStayResultRow(inDirRes, outDirRes,
					minBoxTime, maxBoxTime);
			if (stayRes == null)
				continue;
			stayResults.add(stayRes);
		}
	}

	private DirectionResultRow getDirectionResultRow(AntennaRecord in,
			AntennaRecord out, long minTubeTime, long maxTubeTime) {
		if (in.getAntenna().getBox() != out.getAntenna().getBox())
			return null;
		BoxRow box = in.getAntenna().getBox();
		TimeStamp timestamp = in.getRecordTime();
		if (out.getRecordTime().before(in.getRecordTime())) {
			AntennaRecord temp = in;
			in = out;
			out = temp;
		}
		long duration = TimeStamp.duration(in.getRecordTime(),
				out.getRecordTime());
		if (duration < minTubeTime || duration > maxTubeTime)
			return null;
		Direction direction = new Direction(in.getAntenna(), out.getAntenna());
		if (direction.toString() == null)
			return null;
		DirectionResultRow dirResult = new DirectionResultRow(timestamp,
				direction, mouse, box);
		return dirResult;
	}

	private StayResultRow getStayResultRow(DirectionResultRow firstDir,
			DirectionResultRow secondDir, long minBoxTime, long maxBoxTime) {
		// The first event must be before the second
		if (firstDir.getTimeStamp().after(secondDir.getTimeStamp())) {
			// notifyMessage("swapping");
			DirectionResultRow temp = firstDir;
			firstDir = secondDir;
			secondDir = temp;
		}
		// In must be before out
		if (firstDir.getDirection().getType() == Directions.In
				&& (secondDir.getDirection().getType() == Directions.Out || secondDir
						.getDirection().getType() == Directions.SpecialOut)) {
			TimeStamp start = firstDir.getTimeStamp();
			TimeStamp stop = secondDir.getTimeStamp();
			long duration = TimeStamp.duration(start, stop);
			if (duration < minBoxTime || duration > maxBoxTime)
				return null;
			BoxRow box = firstDir.getDirection().getIn().getBox();
			int type = secondDir.getDirection().getIn()
					.equals(secondDir.getDirection().getOut()) ? StayResultRow.TYPE_CORRUPT
					: StayResultRow.TYPE_NORMAL;
			// Create and store the stayResult data entry
			StayResultRow stayResult = new StayResultRow(start, stop, mouse,
					box, firstDir, secondDir, type);
			mouse.addStay(); // increment the stay count
			return stayResult;
		}
		return null;
	}

}
