package bean;

import java.awt.Font;

public class CharacterDialog
{

	public int id;
	
	public String packageId;

	public String emotion;

	public String[] text;
	
	public Font font;
	
	public int fontSize;

	public CharacterDialog(int id, String packageId, String emotion, String text, Font font)
	{
		this.id = id;
		this.packageId = packageId;
		this.emotion = emotion;
		this.text = CreateDialogText(text);
		this.font = font;
		this.fontSize = font.getSize();
	}
	private String[] CreateDialogText(String text)
	{
		return text.split("(\\r\\n|\\r|\\n|&#xD;&#xA;)");
	}
}