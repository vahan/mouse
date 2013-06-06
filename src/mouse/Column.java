package mouse;

import java.util.ArrayList;

public class Column {
	
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
