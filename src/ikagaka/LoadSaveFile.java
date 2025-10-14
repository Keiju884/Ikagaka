package ikagaka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import utils.LogWriterNew;

public class LoadSaveFile
{

	private static LoadSaveFile load;

	private LoadSaveFile()
	{

	}

	public static LoadSaveFile Instance()
	{
		if(load == null)
		{
			load = new LoadSaveFile();
		}
		return load;
	}

	public void save(String packageId)
	{
		saveData("save", packageId);
	}

	public String load()
	{
		String s = loadData("save");
		if(s == null || s.isEmpty())
		{
			return null;
		}
		return s;
	}

	private static File getSaveDirectory()
	{
		String localAppData = System.getenv("LOCALAPPDATA");

		// もし環境変数が取得できなければ user.home にフォールバック
		if(localAppData == null || localAppData.isEmpty())
		{
			localAppData = System.getProperty("user.home");
		}

		// Ikagakaフォルダを指定
		File saveDir = new File(localAppData, "Ikagaka");

		// フォルダが存在しなければ作成
		if(!saveDir.exists())
		{
			boolean created = saveDir.mkdirs();
			if(!created)
			{
				LogWriterNew.writeLog("保存フォルダの作成に失敗しました: " + saveDir.getAbsolutePath());
			}
		}
		return saveDir;
	}

	private static void saveData(String fileName, String packageId)
	{
		File saveDir = getSaveDirectory();
		File saveFile = new File(saveDir, fileName);

		try (FileWriter writer = new FileWriter(saveFile))
		{
			writer.write(packageId);
			LogWriterNew.writeLog("packageIdを保存しました: " + packageId +":" + saveFile.getAbsolutePath());
		}
		catch (IOException e)
		{
			LogWriterNew.writeLog(e.toString());
		}
	}

	private static String loadData(String fileName)
	{
		File saveDir = getSaveDirectory();
		File saveFile = new File(saveDir, fileName);

		if(!saveFile.exists())
			return null;

		try
		{
			String s = Files.readString(saveFile.toPath());
			LogWriterNew.writeLog("packageIdを読み込みました:"+ s);
			return s;
		}
		catch (IOException e)
		{
			LogWriterNew.writeLog(e.toString());
			return null;
		}
	}
}
