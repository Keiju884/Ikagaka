package xml;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DialogList")
@XmlAccessorType(XmlAccessType.FIELD)
public class DialogListXml
{
	@XmlElement(name = "Dialog")
	private List<DialogInfoXml> dialogs;

	public List<DialogInfoXml> getDialogs()
	{
		return dialogs;
	}
}