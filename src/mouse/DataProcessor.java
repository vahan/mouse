package mouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.AntennaReading;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.DirectionResult;
import mouse.dbTableModels.MeetingResult;
import mouse.dbTableModels.StayResult;
import mouse.dbTableModels.Transponder;
import mouse.postgresql.AntennaReadings;
import mouse.postgresql.PostgreSQLManager;

import au.com.bytecode.opencsv.CSVReader;

public class DataProcessor {
	
	public static final int COLUMN_COUNT = 5;
	public static final int DATE_TIME_STAMP_COLUMN = 1;
	public static final int DEVICE_ID_COLUMN = 2;
	public static final int ANTENNA_ID_COLUMN = 3;
	public static final int RFID_COLUMN = 4;
	
	
	private final String inputCSVFileName;
	
	private final ArrayList<AntennaReading> antennaReadings = new ArrayList<AntennaReading>();
	
	private final ArrayList<DirectionResult> directionResults = new ArrayList<DirectionResult>();
	
	private final ArrayList<StayResult> stayResults = new ArrayList<StayResult>();
	
	private final ArrayList<MeetingResult> meetingResults = new ArrayList<MeetingResult>();
	
	private final PostgreSQLManager psqlManager;
	
	
	public DataProcessor(String inputCSVFileName, String username, String password, Object host, Object port, String dbName) {
		this.inputCSVFileName = inputCSVFileName;
		
		Column[] columns = columns(inputCSVFileName, true);
		psqlManager = new PostgreSQLManager(username, password, columns);
	}
	
	public String getInputCSVFileName() {
		return inputCSVFileName;
	}

	public ArrayList<AntennaReading> getAntennaReadings() {
		return antennaReadings;
	}

	public ArrayList<DirectionResult> getDirectionResults() {
		return directionResults;
	}

	public ArrayList<StayResult> getStayResults() {
		return stayResults;
	}

	public ArrayList<MeetingResult> getMeetingResults() {
		return meetingResults;
	}

	public PostgreSQLManager getPsqlManager() {
		return psqlManager;
	}

	/**
	 * Processes the input data and writes into the according tables
	 * @return
	 */
	public boolean process() {
		if (!psqlManager.initTables())
			return false;
		if (!psqlManager.storeStaticTables()) {
			return false;
		}
		if (!readAntennaReadingsCSV(inputCSVFileName))
			return false;
		
		
		return true;
	}
	
	/**
	 * Scans the input file and returns Columns of antennas, boxes and transponders
	 * @param inputCSVFileName
	 * @param unique
	 * @return
	 */
	private Column[] columns(String inputCSVFileName, boolean unique) {
		int[] staticColumnNumbers = new int[] {ANTENNA_ID_COLUMN, DEVICE_ID_COLUMN, RFID_COLUMN};
		Column[] columns = new Column[COLUMN_COUNT];
		for (int i = 0; i < COLUMN_COUNT; ++i) {
			columns[i] = new Column();
		}
		
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(inputCSVFileName), ';', '\'', 1); //skip the first (header) line
			
			String [] nextLine;
			int lineNumber = 0;
			while ((nextLine = reader.readNext()) != null) {
				for (int i : staticColumnNumbers) {
					if (unique && columns[i].getEntries().contains(nextLine[i]))
						continue;
					columns[i].addEntry(nextLine[i]);
				}
				System.out.println(lineNumber++);
			}
			reader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return columns;
	}
	
	
	/**
	 * Reads the content of a given CSV file into antennaReadings array 
	 * and inserts the data into the antenna_readings table
	 * 
	 * @param sourceFile
	 */
	private boolean readAntennaReadingsCSV(String sourceFile) {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(sourceFile), ';', '\'', 1); //skip the first (header) line
			
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				String timeStamp = nextLine[1];
				String boxName = nextLine[2];
				Box box = Box.getBoxByName(boxName);
				String antennaPosition = nextLine[3];
				Antenna antenna = Antenna.getAntenna(box, antennaPosition);
				String rfid = nextLine[4];
				if (StringUtils.isEmpty(rfid))
					continue;
				Transponder transponder = Transponder.getTransponder(rfid);
				AntennaReading antennaReading = new AntennaReading(timeStamp, transponder, antenna);
				antennaReadings.add(antennaReading);
			}
			reader.close();
			
			AntennaReadings antennaReadingsTable = psqlManager.getAntennaReadings();
			antennaReadingsTable.setAntennaReadings(
					antennaReadings.toArray(new AntennaReading[antennaReadings.size()]));
			
			//TODO Use ONE query!
			String[] insertQueries = new String[antennaReadingsTable.getTableModels().length];
			for (int i = 0; i < antennaReadingsTable.getTableModels().length; ++i) {
				insertQueries[i] = antennaReadingsTable.insertQuery(antennaReadingsTable.getTableModels()[i]);
			}
			psqlManager.executePreparedStatements(insertQueries);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Generates entry rows from antennaReadings array to directionResults array 
	 * and inserts the data into the direction_results table
	 */
	private void generateDirectionResults() {
		
	}
	
	/**
	 * Generate entry rows from directionResults array to stayResults array
	 * and inserts the data into the stay_results table
	 */
	private void generateStayResults() {
		
	}
	
	/**
	 * Generates entry rows from stayResults array to meetingResults array
	 * and inserts the data into the meeting_results table
	 */
	private void generateMeetingResults() {
		
	}
	

}
