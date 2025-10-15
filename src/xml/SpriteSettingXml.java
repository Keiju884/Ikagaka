package xml;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class SpriteSettingXml
{
	@XmlAttribute(name = "Sprite")
	public String spriteName;

	@XmlElement(name = "Setting")
	public Setting setting;

	public static class Setting
	{
		@XmlAttribute(name = "Scale")
		public double scale;

		@XmlAttribute(name = "Pivot_X")
		public int pivotX;

		@XmlAttribute(name = "Pivot_Y")
		public int pivotY;
	}
}
