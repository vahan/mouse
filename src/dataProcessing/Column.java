package dataProcessing;

import java.util.ArrayList;

/**
 * This class serves as a data storage for columnvise representation of the input data.
 * Used to avoid ArrayList<ArrayList<String>> and simplify the implementation of the algorithms
 * @author vahan
 *
 */
public class Column {
	/**
	 * Array of the data entries. Here the actual data is stored. 
	 */
	private ArrayList<String> entries = new ArrayList<String>();

	public ArrayList<String> getEntries() {
		return entries;
	}
	
	public void addEntry(String entry) {
		entries.add(entry);
	}
	
	public String[] toArray() {
		return entries.toArray(new String[entries.size()]);
	}

}
