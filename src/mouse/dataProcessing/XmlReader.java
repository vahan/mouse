package mouse.dataProcessing;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import mouse.Settings;

public class XmlReader {

	public Settings importSettingsFromXml(String fileName) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(fileName));

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList listOfSettins = doc.getElementsByTagName("settings");

			for (int s = 0; s < listOfSettins.getLength(); ++s) {
				Node firstSettingNode = listOfSettins.item(s);
				if (firstSettingNode.getNodeType() != Node.ELEMENT_NODE)
					continue;
				Element settingsElement = (Element) firstSettingNode;

				NodeList usernameList = settingsElement
						.getElementsByTagName("username");
				Element usernameElement = (Element) usernameList.item(0);
				NodeList textUsernameList = usernameElement.getChildNodes();
				String username = ((Node) textUsernameList.item(0))
						.getNodeValue().trim();

				NodeList passwordList = settingsElement
						.getElementsByTagName("password");
				Element passwordElement = (Element) passwordList.item(0);
				NodeList textPasswordList = passwordElement.getChildNodes();
				String password = ((Node) textPasswordList.item(0))
						.getNodeValue().trim();

				NodeList hostList = settingsElement
						.getElementsByTagName("hostname");
				Element hostElement = (Element) hostList.item(0);
				NodeList textHostList = hostElement.getChildNodes();
				String host = ((Node) textHostList.item(0)).getNodeValue()
						.trim();

				NodeList portList = settingsElement
						.getElementsByTagName("port");
				Element portElement = (Element) portList.item(0);
				NodeList textPortList = portElement.getChildNodes();
				String port = ((Node) textPortList.item(0)).getNodeValue()
						.trim();

				NodeList dbNameList = settingsElement
						.getElementsByTagName("dbname");
				Element dbNameElement = (Element) dbNameList.item(0);
				NodeList textDbNameList = dbNameElement.getChildNodes();
				String dbName = ((Node) textDbNameList.item(0)).getNodeValue()
						.trim();

				NodeList schemaList = settingsElement
						.getElementsByTagName("schema");
				Element schemaElement = (Element) schemaList.item(0);
				NodeList textSchemaList = schemaElement.getChildNodes();
				String schema = ((Node) textSchemaList.item(0)).getNodeValue()
						.trim();
				
				NodeList inervalsList = settingsElement
						.getElementsByTagName("hist-intervals");
				Element inervalsElement = (Element) inervalsList.item(0);
				NodeList textIntervalsList = inervalsElement.getChildNodes();
				String inervals = ((Node) textIntervalsList.item(0))
						.getNodeValue().trim();
				int inervalsNumber = Integer.parseInt(inervals);

				NodeList csvDateFormatList = settingsElement
						.getElementsByTagName("date-format-csv");
				Element csvDateFormatElement = (Element) csvDateFormatList
						.item(0);
				NodeList textcsvDateFormatList = csvDateFormatElement
						.getChildNodes();
				String csvDateFormat = ((Node) textcsvDateFormatList.item(0))
						.getNodeValue().trim();

				NodeList dbDateFormatList = settingsElement
						.getElementsByTagName("date-format-db");
				Element dbDateFormatElement = (Element) dbDateFormatList
						.item(0);
				NodeList textDbDateFormatList = dbDateFormatElement
						.getChildNodes();
				String dbDateFormat = ((Node) textDbDateFormatList.item(0))
						.getNodeValue().trim();

				NodeList maxTubeTimeList = settingsElement
						.getElementsByTagName("max-tube-time");
				Element maxTubeTimeElement = (Element) maxTubeTimeList.item(0);
				NodeList textMaxTubeTimeList = maxTubeTimeElement
						.getChildNodes();
				String maxTubeTime = ((Node) textMaxTubeTimeList.item(0))
						.getNodeValue().trim();
				long maxTubeTimeNumber = Long.parseLong(maxTubeTime);

				NodeList maxBoxTimeList = settingsElement
						.getElementsByTagName("max-box-time");
				Element maxBoxTimeElement = (Element) maxBoxTimeList.item(0);
				NodeList textMaxBoxTimeList = maxBoxTimeElement.getChildNodes();
				String maxBoxTime = ((Node) textMaxBoxTimeList.item(0))
						.getNodeValue().trim();
				long maxBoxTimeNumber = Long.parseLong(maxBoxTime);

				NodeList minTubeTimeList = settingsElement
						.getElementsByTagName("min-tube-time");
				Element minTubeTimeElement = (Element) minTubeTimeList.item(0);
				NodeList textMinTubeTimeList = minTubeTimeElement
						.getChildNodes();
				String minTubeTime = ((Node) textMinTubeTimeList.item(0))
						.getNodeValue().trim();
				long minTubeTimeNumber = Long.parseLong(minTubeTime);

				NodeList minBoxTimeList = settingsElement
						.getElementsByTagName("min-box-time");
				Element minBoxTimeElement = (Element) minBoxTimeList.item(0);
				NodeList textMinBoxTimeList = minBoxTimeElement.getChildNodes();
				String minBoxTime = ((Node) textMinBoxTimeList.item(0))
						.getNodeValue().trim();
				long minBoxTimeNumber = Long.parseLong(minBoxTime);

				return new Settings(username, password, host, port, dbName,
						schema, inervalsNumber, csvDateFormat, dbDateFormat,
						maxTubeTimeNumber, maxBoxTimeNumber, minTubeTimeNumber,
						minBoxTimeNumber);
			}
			return null;
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());
			return null;
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

	}
}
