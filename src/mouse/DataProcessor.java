package mouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.AntennaReading;
import mouse.dbTableModels.AntennaRecord;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Direction;
import mouse.dbTableModels.DirectionResult;
import mouse.dbTableModels.Directions;
import mouse.dbTableModels.MeetingResult;
import mouse.dbTableModels.MouseInBox;
import mouse.dbTableModels.StayResult;
import mouse.dbTableModels.Transponder;
import mouse.postgresql.AntennaReadings;
import mouse.postgresql.DirectionResults;
import mouse.postgresql.PostgreSQLManager;
import mouse.postgresql.StayResults;

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
		if (!generateDirectionResults())
			return false;
		if (!generateStayResults())
			return false;
		if (!generateMeetingResults())
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
	 * 
	 * @param sourceFile
	 */
	private boolean readAntennaReadingsCSV(String sourceFile) {
		System.out.println("Reading input file: " + sourceFile);
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(sourceFile), ';', '\'', 1); //skip the first (header) line
			
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				TimeStamp timeStamp;
				try {
					timeStamp = new TimeStamp(nextLine[1]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					reader.close();
					return false;
				}
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
			
			System.out.println("OK\nSaving antenna readings into DB");
			
			AntennaReadings antennaReadingsTable = psqlManager.getAntennaReadings();
			antennaReadingsTable.setTableModels(
					antennaReadings.toArray(new AntennaReading[antennaReadings.size()]));
			
			String insertQueries = antennaReadingsTable.insertQuery(antennaReadingsTable.getTableModels());
			String[] ids = psqlManager.executeQueries(insertQueries);
			for (int i = 0; i < antennaReadings.size(); ++i) {
				antennaReadings.get(i).setId(ids[i]);
			}
			System.out.println("OK");
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
	 */
	private boolean generateDirectionResults() {
		HashMap<MouseInBox, AntennaRecord> mouseInBoxSet = new HashMap<MouseInBox, AntennaRecord>();
		
		for (AntennaReading antennaReading : antennaReadings) {
			Transponder mouse = antennaReading.getTransponder();
			Antenna antenna = antennaReading.getAntena();
			Box box = antenna.getBox();
			TimeStamp timestamp = antennaReading.getTimeStamp();
			MouseInBox mouseInBox = new MouseInBox(mouse, box, antenna, timestamp);
			AntennaRecord antennaRecord = mouseInBoxSet.get(mouseInBox);
			if (!mouseInBoxSet.containsKey(mouseInBox)) {
				mouseInBoxSet.put(mouseInBox, new AntennaRecord(antenna, timestamp));
			} else if (!antennaRecord.equals(antenna)) {
				Antenna in;
				Antenna out;
				if (timestamp.before(antennaRecord.getRecordTime())) {
					in = antenna;
					out = antennaRecord.getAntenna();
				} else {
					in = antennaRecord.getAntenna();
					out = antenna;
				}
				Direction direction = new Direction(in, out);
				if (direction.toString() == null)
					continue;
				Transponder transponder = antennaReading.getTransponder();
				DirectionResult dirResult = new DirectionResult(timestamp, direction, transponder, box);
				directionResults.add(dirResult);
			} else {
				//If the mouse entered and never left the box before entering it again,
				//or left and never entered before living again,
				//count the time from the last recorded time 
				mouseInBoxSet.put(mouseInBox, new AntennaRecord(antenna, timestamp));
			}
		}

		System.out.println("OK\nSaving direction results into DB");
		
		DirectionResults dirResultsTable = psqlManager.getDirectionResults();
		dirResultsTable.setTableModels(
				directionResults.toArray(new DirectionResult[directionResults.size()]));
		
		String insertQueries = dirResultsTable.insertQuery(dirResultsTable.getTableModels());
		String[] ids = psqlManager.executeQueries(insertQueries);
		for (int i = 0; i < directionResults.size(); ++i) {
			directionResults.get(i).setId(ids[i]);
		}
		System.out.println("OK");
		
		return true;
	}
	
	/**
	 * Generate entry rows from directionResults array to stayResults array
	 */
	private boolean generateStayResults() {
		HashMap<MouseInBox, DirectionResult> mouseInBoxSet = new HashMap<MouseInBox, DirectionResult>();
		
		for (DirectionResult dirRes : directionResults) {
			Transponder mouse = dirRes.getTransponder();
			Box box = dirRes.getBox();
			TimeStamp timeStamp = dirRes.getTimeStamp();
			MouseInBox mouseInBox = new MouseInBox(mouse, box, null, timeStamp); //TODO: perhaps a new type is needed instead of putting null for Antenna
			DirectionResult firstDir = mouseInBoxSet.get(mouseInBox);
			DirectionResult secondDir = dirRes;
			if (firstDir == null) {
				mouseInBoxSet.put(mouseInBox, secondDir);
				continue;
			} else {
				if (firstDir.getDirection().getType() == Directions.In && 
						secondDir.getDirection().getType() == Directions.Out) {
					TimeStamp start = firstDir.getTimeStamp();
					TimeStamp stop = secondDir.getTimeStamp();
					StayResult stayResult = new StayResult(start, stop, mouse, box, firstDir, secondDir);
					stayResults.add(stayResult);
				}
				
				mouseInBoxSet.remove(firstDir);
			}
		}

		System.out.println("OK\nSaving stay results into DB");
		
		StayResults stayResultsTable = psqlManager.getStayResults();
		stayResultsTable.setTableModels(
				stayResults.toArray(new StayResult[stayResults.size()]));
		
		String insertQueries = stayResultsTable.insertQuery(stayResultsTable.getTableModels());
		String[] ids = psqlManager.executeQueries(insertQueries);
		for (int i = 0; i < stayResults.size(); ++i) {
			stayResults.get(i).setId(ids[i]);
		}
		System.out.println("OK");
		
		return true;
	}
	
	/**
	 * Generates entry rows from stayResults array to meetingResults array
	 */
	private boolean generateMeetingResults() {
		
		
		
		return true;
	}
	

}
