package xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Character")
public class CharacterInfoXml
{
	@XmlElement(name = "Name")
	public String name;

	@XmlElement(name = "Actor")
	public String actor;

	@XmlElement(name = "PackageId")
	public String packageId;

	@XmlElement(name = "Description")
	public String description;
}
