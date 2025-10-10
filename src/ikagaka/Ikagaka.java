package ikagaka;

public class Ikagaka
{

	public static void main(String[] args)
	{
		LoadCharacterFile load = new LoadCharacterFile();
		new CharacterFrame(load.characterList);
	}

}
