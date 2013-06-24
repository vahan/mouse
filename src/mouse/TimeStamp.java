package mouse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.Duration;

/**
 * Designed to read a specific date-time format and write another specified format.
 * Needed to handle the date-time format differences found in the input CSVs and supported SQL formats
 * Also adds a time duration calculation method to the Date class
 */
public class TimeStamp extends Date {
	
	private static final long serialVersionUID = 8348324320098343781L;
	
	
	public TimeStamp() {
		super();
	}
	
	public TimeStamp(long date) {
		super(date);
	}
	
	public TimeStamp(String str) throws ParseException {
		super((new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSSS", Locale.ENGLISH)).parse(str).getTime());
	}
	
	/**
	 * Static method to calculate time difference in between two times
	 * @param start	
	 * @param stop
	 * @return
	 */
	public static long duration(TimeStamp start, TimeStamp stop) {
		Duration duration = new Duration(start.getTime(), stop.getTime());
		return duration.getMillis();
		
	}
	
	/**
	 * Returns the date-time in a ready-to-write in an SQL db format
	 */
	@Override
	public String toString() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS", Locale.ENGLISH).format(this);
		return timeStamp;
	}

}
