package mouse.dbTableModels;

import java.util.Comparator;

public class AntennaReadingTimeStampComparator implements Comparator<AntennaReading> {

	@Override
	public int compare(AntennaReading o1, AntennaReading o2) {
		return o1.getTimeStamp().compareTo(o2.getTimeStamp());
	}

}
