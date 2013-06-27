package mouse.postgresql;

import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.MeetingResultRow;


/**
 * Models the meeting_results table
 * @author vahan
 *
 */
public class MeetingResults extends DbDynamicTable {
	
	private long minDuration = Integer.MAX_VALUE;
	
	private long maxDuration = -1;
	
	
	public MeetingResults(String tableName) {
		super(tableName);
		
	}
	
	/**
	 * Make sure to call this method before accessing the minDuration and maxDuration members
	 * @param durationLeft
	 * @param durationRight
	 */
	private void calcMinAndMaxDurations() {
		for (DbTableRow row : tableModels) {
			MeetingResultRow meetingResult = (MeetingResultRow) row;
			long duration = meetingResult.getDuration();
			if (duration < minDuration)
				minDuration = duration;
			if (duration > maxDuration)
				maxDuration = duration;
		}
	}
	

	public long getMinDuration() {
		if (maxDuration <= 0)
			calcMinAndMaxDurations();
		return minDuration;
	}

	public long getMaxDuration() {
		if (maxDuration <= 0)
			calcMinAndMaxDurations();
		return maxDuration;
	}
	
	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"trans_from_id",
										"trans_to_id",
										"start",
										"stop",
										"duration",
										"terminated_by",
										"box_id"
		};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		MeetingResultRow meetResult = (MeetingResultRow) model;
		String[] values = new String[] {meetResult.getTransponder().getId(),
										meetResult.getTransponderTo().getId(),
										"'" + meetResult.getStart().toString() + "'",
										"'" + meetResult.getStop().toString() + "'",
										Float.toString(meetResult.getDuration()),
										Integer.toString(meetResult.getTerminatedBy()),
										meetResult.getSource().getId()
										};
		return values;
	}

	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("trans_from_id", new DbEntry("trans_from_id", ColumnTypes.integer, "references transponders(id)"));
		columns.put("trans_to_id", new DbEntry("trans_to_id", ColumnTypes.integer, "references transponders(id)"));
		columns.put("start", new DbEntry("start", ColumnTypes.timestamp, ""));
		columns.put("stop", new DbEntry("stop", ColumnTypes.timestamp, ""));
		columns.put("duration", new DbEntry("duration", ColumnTypes.real, ""));
		columns.put("terminated_by", new DbEntry("terminated_by", ColumnTypes.integer, ""));
		columns.put("box_id", new DbEntry("box_id", ColumnTypes.integer, "references boxes(id)"));
	}
	

}
