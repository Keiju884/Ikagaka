package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogWriterNew
{
	private static final String OUTPUT_DIR = "Log";

	private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
	
	public static synchronized void deleteLogFile()
	{
		String file_name = OUTPUT_DIR + File.separator + "appLog.log";
		File file = new File(file_name);
		if(file.exists())
		{
			file.delete();
		}
	}

	public static synchronized void writeLog(String text,boolean isError)
	{
		Calendar calendar = Calendar.getInstance();

		Date date = calendar.getTime();

		//　ログ出力
		String file_name = OUTPUT_DIR + File.separator + "appLog.log";
		File file = new File(file_name);
		FileWriter fw = null;
		String line = sdf.format(date) + "," + text;
		if(isError)
		{
			System.err.println(text);
		}
		else
		{
			System.out.println(text);
		}
		try
		{
			fw = new FileWriter(file, true);
			fw.write(line + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(fw != null)
			{
				try
				{
					fw.close();
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		}
	}
}
