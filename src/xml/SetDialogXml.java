package xml;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SetDialog")
public class SetDialogXml
{
	@XmlElement(name = "Normal")
	public DialogGroup normal;

	@XmlElement(name = "Moning")
	public DialogGroup moning;

	@XmlElement(name = "Noon")
	public DialogGroup noon;

	@XmlElement(name = "Evening")
	public DialogGroup evening;

	@XmlElement(name = "Night")
	public DialogGroup night;

	@XmlElement(name = "Midnight")
	public DialogGroup midnight;

	public static class DialogGroup
	{
		@XmlElement(name = "Dialog")
		public List<Integer> dialogIds;
	}
}