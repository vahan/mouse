package mouse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeStamp extends Date {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8348324320098343781L;
	
	
	public TimeStamp() {
		super();
	}
	
	public TimeStamp(String str) throws ParseException {
		super((new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSSS", Locale.ENGLISH)).parse(str).getTime());
	}
	
	
	@Override
	public String toString() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS", Locale.ENGLISH).format(this);
		return timeStamp;
	}

}
