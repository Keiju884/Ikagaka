package ikagaka;

public class Ikagaka
{

	public static void main(String[] args)
	{
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
