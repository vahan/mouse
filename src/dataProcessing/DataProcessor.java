package dataProcessing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import org.apache.commons.lang3.StringUtils;

import mouse.TimeStamp;
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
import mouse.postgresql.ColumnTypes;
import mouse.postgresql.DbDynamicTable;
import mouse.postgresql.DbStaticTable;
import mouse.postgresql.DirectionResults;
import mouse.postgresql.Logs;
import mouse.postgresql.MeetingResults;
import mouse.postgresql.PostgreSQLManager;
import mouse.postgresql.Settings;
import mouse.postgresql.StayResults;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Contains methods to read, process and store the data into appropriate db tables
 * @author vahan
 *
 */
public class DataProcessor extends Observable implements Runnable {
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
	private String inputCSVFileName;
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
	
	private final HashMap<TransponderRow, MouseRecords> mouseReadings = new HashMap<TransponderRow, MouseRecords>();
	/**
	 * Provides functionality to work with postgresql data bases.
	 */
	private PostgreSQLManager psqlManager;
	
	private LogRow log;
	
	private String boxDataFileName;
	
	private String message = "";
	
	private boolean reset;
	
	private Settings settings;
	
	private static DataProcessor instance = new DataProcessor();
	
	private boolean success = false;
	
	private boolean finished = false;
	
	
	public static DataProcessor getInstance() {
		return instance;
	}
	
	
	public void init(String inputCSVFileName, String boxDataFileName, Settings settings, 
			boolean reset) {
		this.inputCSVFileName = inputCSVFileName;
		this.boxDataFileName = boxDataFileName;
		this.reset = reset;
		this.settings = settings;
		
		CSVColumn[] columns = csvColumns(inputCSVFileName, true);
		psqlManager = new PostgreSQLManager(settings, columns, reset);
	}
	
	private DataProcessor() {
		
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
	
	public String getMessage() {
		return message;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	
	/**
	 * Process the input data and write it into the according tables
	 * @return
	 */
	private boolean process() {
		finished = false;
		if (reset && !psqlManager.initTables()) {
			return false;
		}
		if (reset && !psqlManager.storeStaticTables()) {
			return false;
		}
		if (!reset && !psqlManager.getStaticTables()) {
			return false;
		}
		if (!importBoxData(boxDataFileName)) {
			return false;
		}
		log = new LogRow(inputCSVFileName);
		if (!readAntennaReadingsCSV(inputCSVFileName))
			return false;
		if (!storeLog())
			return false;
		if (!storeAntennaReadings())
			return false;
		if (!storeExtremeResults(psqlManager.getAntennaReadings(), psqlManager.getAntennas(), new String[] {"last_reading"}, 0, 0, true))
			return false;
		if (!storeExtremeResults(psqlManager.getAntennaReadings(), psqlManager.getTransponders(), new String[] {"last_reading", "last_antenna_id"}, 0, 1, true))
			return false;
		if (!storeExtremeResults(psqlManager.getAntennaReadings(), psqlManager.getTransponders(), new String[] {"first_reading"}, 0, 1, false))
			return false;
		generateDirAndStayResults();
		if (!storeDirectionResults())
			return false;
		if (!storeExtremeResults(psqlManager.getDirectionResults(), psqlManager.getBoxes(), new String[] {"last_direction_result"}, 0, 2, true))
			return false;
		if (!storeStayResults())
			return false;
		if (!generateMeetingResults())
			return false;
		if (!storeExtremeResults(psqlManager.getMeetingResults(), psqlManager.getBoxes(), new String[] {"last_meeting"}, 0, 2, true))
			return false;
		if (!addTransponderCounts())
			return false;
		finished = true;
		return true;
	}
	
	/**
	 * Reads the content of a given CSV file into antennaReadings array 
	 * 
	 * @param sourceFile	The name of the input file
	 * @return				true if successful, false - otherwise
	 */
	private boolean readAntennaReadingsCSV(String sourceFile) {
		notifyMessage("Reading input file: " + sourceFile);
		
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(sourceFile), ';', '\'', 1); //skip the first (header) line
			
			TimeStamp firstReading = null; //the earliest reading; for the Logs table
			TimeStamp lastReading = null; //the latest reading; for the Logs table
			int nbReadings = 0; //number of the readings; for the Logs table
			
			String [] nextLine;
			//Read the file line by line
			while ((nextLine = reader.readNext()) != null) {
				TimeStamp timeStamp;
				try {
					timeStamp = new TimeStamp(nextLine[DATE_TIME_STAMP_COLUMN], TimeStamp.getCsvForamt()); //read the timestamp column of the current row
				} catch (ParseException e) {
					e.printStackTrace();
					reader.close();
					return false;
				}
				//check for the first and last readings
				if (firstReading == null || timeStamp.before(firstReading))
					firstReading = timeStamp;
				if (lastReading == null || timeStamp.after(lastReading))
					lastReading = timeStamp;
				
				String boxName = nextLine[DEVICE_ID_COLUMN]; //read the box name column of the current row
				BoxRow box = BoxRow.getBoxByName(boxName); //get the box object via its read name
				String antennaPosition = nextLine[ANTENNA_ID_COLUMN]; //read the antenna position (1 or 2) column of the current row
				AntennaRow antenna = AntennaRow.getAntenna(box, antennaPosition); //get the antenna object using its corresponding box and position (id) on the box
				String rfid = nextLine[RFID_COLUMN]; //read the rfid column of the current row
				if (StringUtils.isEmpty(rfid))
					continue; //TODO: skip the line if the rfid is missing?
				TransponderRow transponder = TransponderRow.getTransponder(rfid); //get the transponder object via its rfid
				//create and the the antenna reading object to the correcponding array
				AntennaReadingRow antennaReading = new AntennaReadingRow(timeStamp, transponder, antenna, log);
				antennaReadings.add(antennaReading);
				//store into mouseReadings array as well; needed for further processing
				MouseRecords mouseRecords = mouseReadings.get(transponder);
				if (mouseRecords == null)
					mouseReadings.put(transponder, new MouseRecords(transponder));
				else
					mouseRecords.add(new AntennaRecord(antenna, timeStamp));
				
				nbReadings++; //increment the number of readings
			}
			reader.close();
			//set the data for Logs table
			log.setFirstReading(firstReading);
			log.setLastReading(lastReading);
			log.setNbReadings(nbReadings);
			log.setDuration();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Stores the antenna readings into its table in the DB
	 * @return	true if successful, false - otherwise
	 */
	private boolean storeAntennaReadings() {
		notifyMessage("Saving antenna readings into DB");
		//Set the read data into the according db table object of psqlManager
		AntennaReadings antennaReadingsTable = psqlManager.getAntennaReadings();
		antennaReadingsTable.setTableModels(
				antennaReadings.toArray(new AntennaReadingRow[antennaReadings.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = antennaReadingsTable.insertQuery(antennaReadingsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQuery(insertQueries);
		if (ids.length <= 0) {
			notifyMessage("FAILED");
			return false;
		}
		//Set the IDs to the appropriate objects
		for (int i = 0; i < antennaReadings.size(); ++i) {
			antennaReadings.get(i).setId(ids[i]);
		}
		notifyMessage("OK");
		return true;
	}
	
	/**
	 * Insert the log data into its table in the DB
	 * @return	true if successful, false - otherwise
	 */
	private boolean storeLog() {
		notifyMessage("Inserting into the Log table");
		Logs logsTable = psqlManager.getLogs();
		logsTable.getTableModels()[0] = log;
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = logsTable.insertQuery(logsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQuery(insertQueries);
		//Set the IDs to the appropriate objects
		if (ids.length != 1) {
			notifyMessage("FAILED");
			return false;
		}
		log.setId(ids[0]);
		notifyMessage("OK");
		return true;
	}
	
	/**
	 * A general method used to store extreme (earliest, latest) type of entries into various tables
	 * @return	true if successful, false - otherwise
	 */
	private boolean storeExtremeResults(DbDynamicTable dynamicTable, DbStaticTable staticTable, String[] fields, 
			int extremeResultIndex, int staticTableRowIndex, boolean last) {
		notifyMessage("Updating " + staticTable.getTableName() + "." + Arrays.toString(fields));
		dynamicTable.putExtremeReadings(extremeResultIndex, staticTableRowIndex, last);
		DbStaticTableRow[] staticTableRows = staticTable.getTableModels();
		ColumnTypes[] types = new ColumnTypes[fields.length];
		for (int i = 0; i < fields.length; ++i) {
			types[i] = staticTable.getColumn(fields[i]).getType();
		}
		String updateLastReadingsQuery = staticTable.updateLastReadingsQuery(fields, staticTableRows, extremeResultIndex, types);
		String[] ids = psqlManager.executeQuery(updateLastReadingsQuery);
		if (ids.length > 0) { //TODO: Check if ALL the rows were updated?
			notifyMessage("OK. " + ids.length + " rows were modified");
			return true;
		}
		else {
			notifyMessage("FAILED");
			return false;
		}
	}
	
	
	private void generateDirAndStayResults() {
		for (Entry<TransponderRow, MouseRecords> entry : mouseReadings.entrySet()) {
			entry.getValue().addDirectionAndStayResults(directionResults, stayResults,
					settings.getMaxTubeTime(), settings.getMaxBoxTime());
		}
	}
	
	/**
	 * Generates entry rows from antennaReadings array to directionResults array
	 * 
	 * @return	true if the successful; false - otherwise
	 */
	private boolean storeDirectionResults() {
		/*//A dictionary to efficiently handle mouse-box - antenna-record time connections.
		HashMap<MouseInBox, AntennaRecord> mouseInBoxSet = new HashMap<MouseInBox, AntennaRecord>();
		//Iterate through the antenna readings and transform the data into a direction results form
		for (AntennaReadingRow antennaReading : antennaReadings) {
			TransponderRow mouse = antennaReading.getTransponder();
			AntennaRow antenna = (AntennaRow) antennaReading.getSource();
			BoxRow box = antenna.getBox();
			TimeStamp timestamp = antennaReading.getTimeStamp();
			MouseInBox mouseInBox = new MouseInBox(mouse, box, antenna, timestamp); //represents an events of a mouse that appeared in a box
			AntennaRecord antennaRecord = mouseInBoxSet.get(mouseInBox); //get the exact antenna (and timestamp) that recorded the mouse showing up in the box
			//If the mouse-box pair is not already in the array, then add it; 
			//meaning that the mouse was not recorded in this particular box before 
			if (antennaRecord == null) {
				mouseInBoxSet.put(mouseInBox, new AntennaRecord(antenna, timestamp));
			} else if (!antennaRecord.getAntenna().equals(antenna)) {
				//Otherwise if nothing 'illegal' happened add the direction result to the array
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
				mouseInBoxSet.remove(mouseInBox); //Remove the mouse-in-box entry, when its pair is found 
			} else {
				//If the mouse entered and never left the box before entering it again,
				//or left and never entered before living again,
				//count the time from the last recorded time 
				mouseInBoxSet.put(mouseInBox, new AntennaRecord(antenna, timestamp));
			}
		}*/

		notifyMessage("Saving direction results into DB");
		//Set the read data into the according db table object of psqlManager
		DirectionResults dirResultsTable = psqlManager.getDirectionResults();
		dirResultsTable.setTableModels(
				directionResults.toArray(new DirectionResultRow[directionResults.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = dirResultsTable.insertQuery(dirResultsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQuery(insertQueries);
		//Set the IDs to the appropriate objects
		for (int i = 0; i < directionResults.size(); ++i) {
			directionResults.get(i).setId(ids[i]);
		}
		notifyMessage("OK");
		
		return true;
	}
	
	
	/**
	 * Generate entry rows from directionResults array to stayResults array
	 * 
	 * @return	true if successful; false - otherwise
	 */
	private boolean storeStayResults() {
		/*//A HashMap data structure to efficiently handle mouse-box connections.
		HashMap<MouseInBox, DirectionResultRow> mouseInBoxSet = new HashMap<MouseInBox, DirectionResultRow>();
		HashSet<TransponderRow> insideMice = new HashSet<TransponderRow>(); //Set of mice that are inside a box
		for (DirectionResultRow dirRes : directionResults) {
			TransponderRow mouse = dirRes.getTransponder();
			BoxRow box = (BoxRow) dirRes.getSource();
			TimeStamp timeStamp = dirRes.getTimeStamp();
			MouseInBox mouseInBox = new MouseInBox(mouse, box, null, timeStamp); //no need to keep the particular antenna; TODO maybe a new class?
			DirectionResultRow secondDir = mouseInBoxSet.get(mouseInBox);
			DirectionResultRow firstDir = dirRes;
			//if the mouse-box pair appears for the first time, add it to the mouseInBoxSet array
			if (secondDir == null) {
				mouseInBoxSet.put(mouseInBox, dirRes);
				insideMice.add(mouse);
			} else {
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
					//Create and store the stayResult data entry
					StayResultRow stayResult = new StayResultRow(start, stop, mouse, box, firstDir, secondDir);
					mouse.addStay(); //increment the stay count
					stayResults.add(stayResult);
					mouseInBoxSet.remove(mouseInBox);
					insideMice.remove(mouse);
				}
			}
		}*/
		
		notifyMessage("Saving stay results into DB");
		//Set the read data into the according db table object of psqlManager
		StayResults stayResultsTable = psqlManager.getStayResults();
		stayResultsTable.setTableModels(
				stayResults.toArray(new StayResultRow[stayResults.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = stayResultsTable.insertQuery(stayResultsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQuery(insertQueries);
		//Set the IDs to the appropriate objects
		for (int i = 0; i < stayResults.size(); ++i) {
			stayResults.get(i).setId(ids[i]);
		}
		notifyMessage("OK");
		
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
		for (Map.Entry<BoxRow, ArrayList<MouseInterval>> pair : boxSet.entrySet()) {
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
		}

		notifyMessage("Saving meeting results into DB");
		//Set the read data into the according db table object of psqlManager
		MeetingResults meetingResultsTable = psqlManager.getMeetingResults();
		meetingResultsTable.setTableModels(
				meetingResults.toArray(new MeetingResultRow[meetingResults.size()]));
		//Generate the INSERT query that inserts the data into the according db table
		String insertQueries = meetingResultsTable.insertQuery(meetingResultsTable.getTableModels());
		//Execute the query and return the generated serial IDs
		String[] ids = psqlManager.executeQuery(insertQueries);
		//Set the IDs to the appropriate objects
		for (int i = 0; i < meetingResults.size(); ++i) {
			meetingResults.get(i).setId(ids[i]);
		}
		notifyMessage("OK");
		
		return true;
	}
	
	
	private boolean addTransponderCounts() {
		notifyMessage("Updating transponder count columns");
		String[] ids = psqlManager.executeQuery(psqlManager.getTransponders().updateCountsQuery());
		if (ids.length > 0) {
			notifyMessage("OK. " + ids.length + " rows were updated");
			return true;
		} else {
			notifyMessage("FAILED");
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
	private CSVColumn[] csvColumns(String inputCSVFileName, boolean unique) {
		//Array of the column numbers
		int[] staticColumnNumbers = new int[] {ANTENNA_ID_COLUMN, DEVICE_ID_COLUMN, RFID_COLUMN};
		//Initializations
		CSVColumn[] columns = new CSVColumn[COLUMN_COUNT];
		for (int i = 0; i < COLUMN_COUNT; ++i) {
			columns[i] = new CSVColumn();
		}
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(inputCSVFileName), ';', '\'', 1); //skip the first (header) line
			
			String[] nextLine;
			//Read the file line by line
			while ((nextLine = reader.readNext()) != null) {
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
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return columns;
	}
	

	
	private HashMap<String, BoxData> boxDataColumns(String inputFileName) {
		//Array of the column numbers
		HashMap<String, BoxData> columns = new HashMap<String, BoxData>();
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(inputFileName), '\t', '\'', 1); //skip the first (header) line
			
			String[] nextLine;
			//Read the file line by line
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine.length <= 1) {
					continue;
				}
				String boxId = nextLine[0];
				String xPos = nextLine[1];
				String yPos = nextLine[2];
				columns.put(boxId, new BoxData(boxId, xPos, yPos));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return columns;
	}
	
	
	private boolean importBoxData(String fileName) {
		HashMap<String, BoxData> boxData = boxDataColumns(fileName);
		
		DbStaticTableRow[] boxRows = psqlManager.getBoxes().getTableModels();
		for (int i = 0; i < boxRows.length; ++i) {
			BoxRow boxRow = (BoxRow) boxRows[i];
			String boxName = boxRow.getName();
			BoxData boxDataObj = boxData.get(boxName);
			boxRow.setxPos(boxDataObj != null ? boxDataObj.getxPos() : "");
			boxRow.setyPos(boxDataObj != null ? boxDataObj.getyPos() : "");
		}
		
		String updateBoxDataQuery = psqlManager.getBoxes().updateBoxDataQuery();
		return psqlManager.executeQuery(updateBoxDataQuery).length > 0;
		
	}
	
	
	private void notifyMessage(String message) {
		synchronized (message) {
			this.message = message;
			this.message.notifyAll();
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void run() {
		success = process();
	}
}
