package ikagaka;

import utils.LogWriterNew;

public class Ikagaka
{

	public static void main(String[] args)
	{
		LogWriterNew.deleteLogFile();
		LoadCharacterFile load = new LoadCharacterFile();
		new CharacterFrame(load.characterList);
	}
	
	public static void exit(String packageId)
	{
		if(packageId != null && !packageId.isEmpty())
		{
			LoadSaveFile.Instance().save(packageId);
		}
		System.exit(0);
	}

}
