package xml;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Sprite")
public class SpriteDataXml
{
	@XmlElement(name = "TextBoxPivot")
	public int textBoxPivot;

	@XmlElementWrapper(name = "SpriteSettingList")
	@XmlElement(name = "SpriteSetting")
	public List<SpriteSettingXml> spriteSettingList;
}
