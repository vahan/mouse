package mouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.AntennaReading;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.DirectionResult;
import mouse.dbTableModels.MeetingResult;
import mouse.dbTableModels.StayResult;
import mouse.dbTableModels.Transponder;
import mouse.postgresql.PostgreSQLManager;

import au.com.bytecode.opencsv.CSVReader;

public class DataProcessor {
	
	private final String inputCSVFileName;
	
	private final ArrayList<AntennaReading> antennaReadings = new ArrayList<AntennaReading>();
	
	private final ArrayList<DirectionResult> directionResults = new ArrayList<DirectionResult>();
	
	private final ArrayList<StayResult> stayResults = new ArrayList<StayResult>();
	
	private final ArrayList<MeetingResult> meetingResults = new ArrayList<MeetingResult>();
	
	private final PostgreSQLManager psqlManager;
	
	
	public DataProcessor(String inputCSVFileName, String username, String password, Object host, Object port, String dbName) {
		this.inputCSVFileName = inputCSVFileName;
		
		psqlManager = new PostgreSQLManager(username, password);
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
		if (!readAntennaReadingsCSV(inputCSVFileName))
			return false;
		
		
		return true;
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
				Box box = new Box(boxName);
				String antennaPosition = nextLine[3];
				Antenna antenna = new Antenna(antennaPosition, box);
				String rfid = nextLine[4];
				Transponder transponder = new Transponder(rfid);
				AntennaReading antennaReading = 
						new AntennaReading(timeStamp, transponder, antenna);
				antennaReadings.add(antennaReading);
			}
			reader.close();
			
			//TODO Insert the read data into the DB table
			
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
