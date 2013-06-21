package mouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.AntennaReadingRow;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbStaticTableRow;
import mouse.dbTableRows.DirectionResultRow;
import mouse.dbTableRows.LogRow;
import mouse.dbTableRows.MeetingResultRow;
import mouse.dbTableRows.StayResultRow;
import mouse.dbTableRows.TransponderRow;
import mouse.postgresql.AntennaReadings;
import mouse.postgresql.DbDynamicTable;
import mouse.postgresql.DbStaticTable;
import mouse.postgresql.DirectionResults;
import mouse.postgresql.Logs;
import mouse.postgresql.MeetingResults;
import mouse.postgresql.PostgreSQLManager;
import mouse.postgresql.StayResults;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Contains methods to read, process and store the data into appropriate db tables
 * @author vahan
 *
 */
public class DataProcessor {
	//TODO: The following constants can be changed to be read from the input file and/or from an configuration file 
	/**
	 * Constant representing the number of columns expected to be in the input CSV file
	 */
	public static final int COLUMN_COUNT = 5;
	/**
	 * The column where the timestamp column is expected
	 */
	public static final int DATE_TIME_STAMP_COLUMN = 1;
	/**
	 * The column where the device (box) column is expected
	 */
	public static final int DEVICE_ID_COLUMN = 2;
	/**
	 * The column where the antenna column is expected
	 */
	public static final int ANTENNA_ID_COLUMN = 3;
	/**
	 * The column where the rfid (mouse unique id) column is expected
	 */
	public static final int RFID_COLUMN = 4;
	
	/**
	 * The full name (can contain path as well) of the input .csv file
	 */
	private final String inputCSVFileName;
	/**
	 * An array containing the data read from the input file
	 */
	private final ArrayList<AntennaReadingRow> antennaReadings = new ArrayList<AntennaReadingRow>();
	/**
	 * An array containing data transformed into "direction results" from "antenna readings"
	 */
	private final ArrayList<DirectionResultRow> directionResults = new ArrayList<DirectionResultRow>();
	/**
	 * An array containing data transformed into "stay results" from "direction results"
	 */
	private final ArrayList<StayResultRow> stayResults = new ArrayList<StayResultRow>();
	/**
	 * An array containing data transformed into "meeting results" from "stay results"
	 */
	private final ArrayList<MeetingResultRow> meetingResults = new ArrayList<MeetingResultRow>();
	/**
	 * Provides functionality to work with postgresql data bases.
	 */
	private final PostgreSQLManager psqlManager;
	
	
	private LogRow log;
	
	
	public DataProcessor(String inputCSVFileName, String username, String password, Object host, Object port, String dbName) {
		this.inputCSVFileName = inputCSVFileName;
		
		Column[] columns = columns(inputCSVFileName, true);
		psqlManager = new PostgreSQLManager(username, password, columns);
	}
	
	public String getInputCSVFileName() {
		return inputCSVFileName;
	}

	public ArrayList<AntennaReadingRow> getAntennaReadings() {
		return antennaReadings;
	}

	public ArrayList<DirectionResultRow> getDirectionResults() {
		return directionResults;
	}

	public ArrayList<StayResultRow> getStayResults() {
		return stayResults;
	}

	public ArrayList<MeetingResultRow> getMeetingResults() {
		return meetingResults;
	}

	public PostgreSQLManager getPsqlManager() {
		return psqlManager;
	}

	/**
	 * Process the input data and write it into the according tables
	 * @return
	 */
	public boolean process() {
		if (!psqlManager.initTables())
			return false;
		if (!psqlManager.storeStaticTables()) {
			return false;
		}
		log = new LogRow(inputCSVFileName);
		if (!readAntennaReadingsCSV(inputCSVFileName))
			return false;
		if (!storeLog(inputCSVFileName))
			return false;
		if (!storeAntennaReadings())
			return false;
		if (!storeExtremeResults(psqlManager.getAntennaReadings(), psqlManager.getAntennas(), new String[] {"last_reading"}, 0, 0, true, new String[] {"timestamp"}))
			return false;
		if (!storeExtremeResults(psqlManager.getAntennaReadings(), psqlManager.getTransponders(), new String[] {"last_reading", "last_antenna_id"}, 0, 1, true, new String[] {"timestamp", "integer"}))
			return false;
		if (!storeExtremeResults(psqlManager.getAntennaReadings(), psqlManager.getTransponders(), new String[] {"first_reading"}, 0, 1, false, new String[] {"timestamp"}))
			return false;
		if (!generateDirectionResults())
			return false;
		if (!storeExtremeResults(psqlManager.getDirectionResults(), psqlManager.getBoxes(), new String[] {"last_direction_result"}, 0, 2, true, new String[] {"timestamp"}))
			return false;
		if (!generateStayResults())
			return false;
		if (!generateMeetingResults())
			return false;
		if (!storeExtremeResults(psqlManager.getMeetingResults(), psqlManager.getBoxes(), new String[] {"last_meeting"}, 0, 2, true, new String[] {"timestamp"}))
			return false;
		if (!addTransponderCounts())
			return false;
		
		return true;
	}
	
	/**
	 * Reads the content of a given CSV file into antennaReadings array 
	 * 
	 * @param sourceFile	The name of the input file
	 * @return				true if successful, false - otherwise
	 */
	private boolean readAntennaReadingsCSV(String sourceFile) {
		//TODO: change the info output from the default out to a controllable one
		System.out.println("Reading input file: " + sourceFile);
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(sourceFile), ';', '\'', 1); //skip the first (header) line
			
			TimeStamp firstReading = null;
			TimeStamp lastReading = null;
			int nbReadings = 0;
			
			String [] nextLine;
			//Read the file line by line
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
				if (firstReading == null || timeStamp.before(firstReading))
					firstReading = timeStamp;
				if (lastReading == null || timeStamp.after(lastReading))
					lastReading = timeStamp;
				
				String boxName = nextLine[2];
				BoxRow box = BoxRow.getBoxByName(boxName);
				String antennaPosition = nextLine[3];
				AntennaRow antenna = AntennaRow.getAntenna(box, antennaPosition);
				String rfid = nextLine[4];
				//TODO: Not sure what to do when the rfid is missing 
				if (StringUtils.isEmpty(rfid))
					continue;
				TransponderRow transponder = TransponderRow.getTransponder(rfid);
				
				AntennaReadingRow antennaReading = new AntennaReadingRow(timeStamp, transponder, antenna, log);
				antennaReadings.add(antennaReading);
				
				nbReadings++;
			}
			reader.close();
			
			log.setFirstReading(firstReading);
			log.setLastReading(lastReading);
			log.setNbReadings(nbReadings);
			log.setDuration();
			
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
	
	
	private boolean storeAntennaReadings() {
		System.out.println("Saving antenna readings into DB");
		
		//Set the read data into the according db table object of psqlManager
		AntennaReadings antennaReadingsTable = psqlManager.getAntennaReadings();
		antennaReadingsTable.setTableModels(
				antennaReadings.toArray(new AntennaReadingRow[antennaReadings.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = antennaReadingsTable.insertQuery(antennaReadingsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQueries(insertQueries);
		if (ids.length <= 0) {
			System.out.println("FAILED");
			return false;
		}
			
		//Set the IDs to the appropriate objects
		for (int i = 0; i < antennaReadings.size(); ++i) {
			antennaReadings.get(i).setId(ids[i]);
		}
		
		System.out.println("OK");
		return true;
	}
	
	
	private boolean storeLog(String sourceFile) {
		System.out.println("Inserting into Log table");

		Logs logsTable = psqlManager.getLogs();
		logsTable.getTableModels()[0] = log;
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = logsTable.insertQuery(logsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQueries(insertQueries);
		//Set the IDs to the appropriate objects
		if (ids.length != 1) {
			System.out.println("FAILED");
			return false;
		}
		log.setId(ids[0]);
		System.out.println("OK");
		
		return true;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private boolean storeExtremeResults(DbDynamicTable dynamicTable, DbStaticTable staticTable, String[] fields, 
			int extremeResultIndex, int staticTableRowIndex, boolean last, String[] types) {
		System.out.println("Updating " + staticTable.getTableName() + "." + Arrays.toString(fields));
		
		dynamicTable.putExtremeReadings(extremeResultIndex, staticTableRowIndex, last);
		
		DbStaticTableRow[] staticTableRows = staticTable.getTableModels();
		String updateLastReadingsQuery = staticTable.updateLastReadingsQuery(fields, staticTableRows, extremeResultIndex, types);
		String[] ids = psqlManager.executeQueries(updateLastReadingsQuery);
		
		if (ids.length > 0) { //TODO: Check if ALL the rows were updated
			System.out.println("OK. " + ids.length + " rows were modified");
			return true;
		}
		else {
			System.out.println("FAILED");
			return false;
		}
	}
	
	/**
	 * Generates entry rows from antennaReadings array to directionResults array
	 * 
	 * @return	true if the successful; false - otherwise
	 */
	private boolean generateDirectionResults() {
		//A HashMap data structure to efficiently handle mouse-box - antenna-recordtime connections.
		HashMap<MouseInBox, AntennaRecord> mouseInBoxSet = new HashMap<MouseInBox, AntennaRecord>();
		//A copy of the antenna readings array. Needed to not modify the original one.
		ArrayList<AntennaReadingRow> antennaReadingsCopy = new ArrayList<AntennaReadingRow>(antennaReadings);
		//Iterate through the antenna readings and transform the data into direction results form
		Iterator<AntennaReadingRow> it = antennaReadingsCopy.iterator();
		while (it.hasNext()) {
			AntennaReadingRow antennaReading = it.next();
			TransponderRow mouse = antennaReading.getTransponder();
			AntennaRow antenna = (AntennaRow) antennaReading.getSource();
			BoxRow box = antenna.getBox();
			TimeStamp timestamp = antennaReading.getTimeStamp();
			MouseInBox mouseInBox = new MouseInBox(mouse, box, antenna, timestamp);
			AntennaRecord antennaRecord = mouseInBoxSet.get(mouseInBox);
			//If the mouse-box pair is not already in the array, then add it
			if (!mouseInBoxSet.containsKey(mouseInBox)) {
				mouseInBoxSet.put(mouseInBox, new AntennaRecord(antenna, timestamp));
			} else if (!antennaRecord.getAntenna().equals(antenna)) {
				//Otherwise nothing 'illegal' happened add the direction result to the array
				AntennaRow in;
				AntennaRow out;
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
				TransponderRow transponder = antennaReading.getTransponder();
				DirectionResultRow dirResult = new DirectionResultRow(timestamp, direction, transponder, box);
				directionResults.add(dirResult);
			} else {
				//If the mouse entered and never left the box before entering it again,
				//or left and never entered before living again,
				//count the time from the last recorded time 
				mouseInBoxSet.put(mouseInBox, new AntennaRecord(antenna, timestamp));
			}
			it.remove();
		}

		System.out.println("Saving direction results into DB");
		//Set the read data into the according db table object of psqlManager
		DirectionResults dirResultsTable = psqlManager.getDirectionResults();
		dirResultsTable.setTableModels(
				directionResults.toArray(new DirectionResultRow[directionResults.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = dirResultsTable.insertQuery(dirResultsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQueries(insertQueries);
		//Set the IDs to the appropriate objects
		for (int i = 0; i < directionResults.size(); ++i) {
			directionResults.get(i).setId(ids[i]);
		}
		System.out.println("OK");
		
		return true;
	}
	
	
	/**
	 * Generate entry rows from directionResults array to stayResults array
	 * 
	 * @return	true if successful; false - otherwise
	 */
	private boolean generateStayResults() {
		//A HashMap data structure to efficiently handle mouse-box connections.
		HashMap<MouseInBox, DirectionResultRow> mouseInBoxSet = new HashMap<MouseInBox, DirectionResultRow>();
		//A copy of the direction results array. Needed to not modify the original one.
		ArrayList<DirectionResultRow> directionResultsCopy = new ArrayList<DirectionResultRow>(directionResults);
		//Iterate through the direction results and transform the data into stay results form
		Iterator<DirectionResultRow> it = directionResultsCopy.iterator();
		while (it.hasNext()) {
			DirectionResultRow dirRes = it.next();
			TransponderRow mouse = dirRes.getTransponder();
			BoxRow box = (BoxRow) dirRes.getSource();
			TimeStamp timeStamp = dirRes.getTimeStamp();
			MouseInBox mouseInBox = new MouseInBox(mouse, box, null, timeStamp); //TODO: perhaps a new type is needed instead of putting null for Antenna
			DirectionResultRow secondDir = mouseInBoxSet.get(mouseInBox);
			DirectionResultRow firstDir = dirRes;
			//if the mouse-box pair appears for the first time, add it to the mouseInBoxSet array
			if (secondDir == null) {
				mouseInBoxSet.put(mouseInBox, dirRes);
				it.remove();
				continue;
			} else {
				//The first event must be before the second
				if (firstDir.getTimeStamp().after(secondDir.getTimeStamp())) {
					//System.out.println("swapping");
					DirectionResultRow temp = firstDir;
					firstDir = secondDir;
					secondDir = temp;
				}
				//In must be before out
				if (firstDir.getDirection().getType() == Directions.In && 
						secondDir.getDirection().getType() == Directions.Out) {
					TimeStamp start = firstDir.getTimeStamp();
					TimeStamp stop = secondDir.getTimeStamp();
					//Create and store the stayResult data entry
					StayResultRow stayResult = new StayResultRow(start, stop, mouse, box, firstDir, secondDir);
					mouse.addStay();
					stayResults.add(stayResult);
					if (mouseInBoxSet.remove(mouseInBox) == null) {
						System.out.println("The mouseInBox could not be removed from the set after being added to the stayResults array! That's odd");
					} else {
						//System.out.println("good boy!");
					}
					it.remove();
				}
				
			}
		}
		
		System.out.println("Saving stay results into DB");
		//Set the read data into the according db table object of psqlManager
		StayResults stayResultsTable = psqlManager.getStayResults();
		stayResultsTable.setTableModels(
				stayResults.toArray(new StayResultRow[stayResults.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = stayResultsTable.insertQuery(stayResultsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQueries(insertQueries);
		//Set the IDs to the appropriate objects
		for (int i = 0; i < stayResults.size(); ++i) {
			stayResults.get(i).setId(ids[i]);
		}
		System.out.println("OK");
		
		return true;
	}
	
	/**
	 * Generates entry rows from stayResults array to meetingResults array
	 * 
	 * @return	true if successful; false - otherwise
	 */
	private boolean generateMeetingResults() {
		//First, associate with each box an array of mouse-in-out objects
		//A HashMap data structure to efficiently handle box-mouse-start-stop connections.
		HashMap<BoxRow, ArrayList<MouseInterval>> boxSet = new HashMap<BoxRow, ArrayList<MouseInterval>>();
		//iterate through all stayResults
		for (StayResultRow stayResult : stayResults) {
			BoxRow box = (BoxRow) stayResult.getSource();
			ArrayList<MouseInterval> mouseIntervals = boxSet.get(box);
			//If the boxSet doesn't contain the mouse-start-stop pair, add it
			if (mouseIntervals == null) {
				mouseIntervals = new ArrayList<MouseInterval>();
			}
			TransponderRow mouse = stayResult.getTransponder();
			TimeStamp start = stayResult.getStart();
			TimeStamp stop = stayResult.getStop();
			MouseInterval mouseInterval = new MouseInterval(mouse, start, stop);
			mouseIntervals.add(mouseInterval);
			boxSet.put(box, mouseIntervals);
		}
		//Then iterate for each box and check which mice and when met there
		Iterator<Entry<BoxRow, ArrayList<MouseInterval>>> it = boxSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<BoxRow, ArrayList<MouseInterval>> pair = 
					(Map.Entry<BoxRow, ArrayList<MouseInterval>>) it.next();
			
			BoxRow box = pair.getKey();
			//For each box iterate through the list of mouse-start-stop objects checking if there are overlapping timestamps
			ArrayList<MouseInterval> mouseIntervals = pair.getValue();
			for (MouseInterval mouseInterval : mouseIntervals) {
				TransponderRow transponderFrom = mouseInterval.getMouse();
				for (MouseInterval innerMouseInterval : mouseIntervals) {
					if (innerMouseInterval.getMouse() == mouseInterval.getMouse())
						continue;
					if (innerMouseInterval.getStart().before(mouseInterval.getStart()))
						continue; //Avoid duplicate entries
					TransponderRow transponderTo = innerMouseInterval.getMouse();
					TimeStamp start = mouseInterval.getStart().after(innerMouseInterval.getStart())
							? mouseInterval.getStart()
							: innerMouseInterval.getStart();
					TimeStamp stop = mouseInterval.getStop().before(innerMouseInterval.getStop())
							? mouseInterval.getStop()
							: innerMouseInterval.getStop();
					//Starting time must be before the stopping time to avoid duplicate entries
					if (stop.before(start))
						continue;
					TransponderRow terminatedBy = mouseInterval.getStop().before(innerMouseInterval.getStop())
							? transponderFrom
							: transponderTo;
					MeetingResultRow meetingResult = new MeetingResultRow(transponderFrom, transponderTo, start, 
									stop, terminatedBy == transponderFrom ? 0 : 1, box);
					transponderFrom.addMeeting();
					transponderTo.addMeeting();
					if (!transponderFrom.getSex().equals(transponderTo.getSex())) { //Unless they are gay (or maybe bi?)
						transponderFrom.addBalade();
						transponderTo.addBalade();
					}
					meetingResults.add(meetingResult);
				}
				
			}
			
			it.remove();
		}

		System.out.println("Saving meeting results into DB");
		//Set the read data into the according db table object of psqlManager
		MeetingResults meetingResultsTable = psqlManager.getMeetingResults();
		meetingResultsTable.setTableModels(
				meetingResults.toArray(new MeetingResultRow[meetingResults.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = meetingResultsTable.insertQuery(meetingResultsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQueries(insertQueries);
		//Set the IDs to the appropriate objects
		for (int i = 0; i < meetingResults.size(); ++i) {
			meetingResults.get(i).setId(ids[i]);
		}
		System.out.println("OK");
		
		return true;
	}
	
	
	private boolean addTransponderCounts() {
		System.out.println("Updating transponder count columns");
		String[] ids = psqlManager.executeQueries(psqlManager.getTransponders().updateCountsQuery());
		if (ids.length > 0) {
			System.out.println("OK. " + ids.length + " rows were updated");
			return true;
		} else {
			System.out.println("FAILED");
			return false;
		}
	}
	
	
	/**
	 * Scans the input file and returns Columns of antennas, boxes and transponders. 
	 * Needed for creating the static `antennas`, `boxes` and `transponders` tables.
	 * Must be called before the actual data processing starts
	 * @param inputCSVFileName	The name of the input file
	 * @param unique			Determines if the generated columns contain only unique entries
	 * @return					Columns of antennas, boxes and transponders; null if reading errors occur
	 */
	private Column[] columns(String inputCSVFileName, boolean unique) {
		//Array of the column numbers
		int[] staticColumnNumbers = new int[] {ANTENNA_ID_COLUMN, DEVICE_ID_COLUMN, RFID_COLUMN};
		//Initializations
		Column[] columns = new Column[COLUMN_COUNT];
		for (int i = 0; i < COLUMN_COUNT; ++i) {
			columns[i] = new Column();
		}
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(inputCSVFileName), ';', '\'', 1); //skip the first (header) line
			
			String [] nextLine;
			//Read the file line by line
			while ((nextLine = reader.readNext()) != null) {
				//TODO: Not sure what to do when the rfid is missing
				String rfid = nextLine[RFID_COLUMN];
				if (StringUtils.isEmpty(rfid))
					continue;
				for (int i : staticColumnNumbers) {
					//skip duplicate entries if only unique entries is required
					if (unique && columns[i].getEntries().contains(nextLine[i]))
						continue;
					columns[i].addEntry(nextLine[i]);
				}
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
	
}
