package mouse.postgresql;

import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Transponder;

public class Antennas extends DbStaticTable {

	public static final int ANTENNAS_PER_BOX = 2;
	
	
	public Antennas(String tableName, String[] antennaNames, String[] boxNames) {
		super(tableName, antennaNames, boxNames);
		// TODO Auto-generated constructor stub
		
		generateTables();
	}

	@Override
	protected void generateTables() {
		tableModels = new Antenna[data.length * entries.length];
		for (int j = 0, k = 0; j < data.length; ++j) {
			for (int i = 0; i < entries.length; ++i) {
				tableModels[k++] = new Antenna(entries[i], Box.getBoxByName(data[j]));
			}
		}
		
	}
	
	/**
	 * Initializes antennas
	 */
	@Deprecated
	private void generateAntennas() {
		// TODO Change to reading the names from the files. See Transponders
		
		tableModels = new Antenna[Boxes.BOX_COUNT * ANTENNAS_PER_BOX];
		
		for (int i = 0, k = 0; i < Boxes.BOX_COUNT; ++i) {
			for (int j = 0; j < ANTENNAS_PER_BOX; ++j) {
				System.out.println(k);
				tableModels[k++] = new Antenna(Integer.toString(j + 1), Box.getBoxByName(Integer.toString(i + 1)));
			}
		}
		
	}


	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"name text," +
				"position text," +
				"last_reading timestamp," +
				"box_id integer references boxes(id)" + 
			");";

		return query;
	}

	@Override
	public String insertQuery(DbTableModel model) {
		Antenna antenna = (Antenna) model;
		String[] fields = new String[] {"name", 
										"position", 
										"box_id"
										};
		String[] values = new String[] {"'" + antenna.getName() + "'", 
										"'" + antenna.getPosition() + "'", 
										"'" + antenna.getBox().getName() + "'"
										};
		String query = insertQuery(fields, values);
		
		return query;
	}
	
	
}