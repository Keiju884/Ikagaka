package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogWriterNew
{

	public static SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");

	public static synchronized void writeLog(String text)
	{
		Calendar calendar = Calendar.getInstance();

		String OUTPUT_DIR = "Log";

		Date date = calendar.getTime();

		//　ログ出力
		String file_name = OUTPUT_DIR + File.separator + "appLog.log";
		File file = new File(file_name);
		FileWriter fw = null;
		String line = sdf.format(date) + "," + text;
		System.out.println(line);
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
