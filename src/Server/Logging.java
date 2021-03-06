package Server;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging 
{
	private ServerFrame sf;
	private Logger myLogger;
	private String path;
	private int getMonth;
	private int getYear;
	private Calendar cal = Calendar.getInstance();
	private FileHandler fh;

	
	/**
	 * Logger constructor
	 */
	public void createLogger() 
	{
		myLogger = Logger.getLogger("Logger");
		getMonth = cal.get(cal.MONTH)+1;
		getYear = cal.get(cal.YEAR);

		File f = new File("Logger\\logs-" + getMonth +"-"+getYear + ".txt");
		path = "Logger/"+f.getName();

		try 
		{
			if (!f.getParentFile().exists())
				f.getParentFile().mkdir();

			if (!f.exists()) 
			{
				f.createNewFile();
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try 
		{
			fh = new FileHandler(path,true);
		} 
		catch (SecurityException | IOException e) 
		{
			e.printStackTrace();
		}
		
		myLogger.addHandler(fh);
		SimpleFormatter formater = new SimpleFormatter();
		fh.setFormatter(formater);
	}

	public void write(String text, String severity) 
	{		
		if (severity.equals("info")) 
		{
			myLogger.setLevel(Level.INFO);
			myLogger.info(text);
			ServerFrame.createLabel(text);
		} 
		else if (severity.equals("warning")) 
		{
			myLogger.setLevel(Level.WARNING);
			myLogger.warning(text);
			ServerFrame.createLabel(text);
		} 
		else if (severity.equals("severe")) 
		{
			myLogger.setLevel(Level.SEVERE);
			myLogger.severe(text);
			ServerFrame.createLabel(text);
		}
	} 
}
