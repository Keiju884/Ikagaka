package xml;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class DialogInfoXml
{

	@XmlAttribute(name = "ID")
	private int id;

	@XmlElement(name = "Emotion")
	private String emotion;

	@XmlElement(name = "Text")
	private String text;

	public int getId()
	{
		return id;
	}

	public String getEmotion()
	{
		return emotion;
	}

	public String getText()
	{
		return text;
	}
}
