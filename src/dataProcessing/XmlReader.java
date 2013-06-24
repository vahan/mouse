package dataProcessing;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import mouse.postgresql.Settings;

public class XmlReader {

	public Settings importSettingsFromXml(String fileName) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(fileName));

			// normalize text representation
			doc.getDocumentElement ().normalize ();

			NodeList listOfSettins = doc.getElementsByTagName("settings");

			for (int s = 0; s < listOfSettins.getLength(); ++s) {
				Node firstSettingNode = listOfSettins.item(s);
				if(firstSettingNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element)firstSettingNode;
	
					NodeList usernameList = firstPersonElement.getElementsByTagName("username");
					Element usernameElement = (Element)usernameList.item(0);
					NodeList textUsernameList = usernameElement.getChildNodes();
					String username = ((Node)textUsernameList.item(0)).getNodeValue().trim();
	
					NodeList passwordList = firstPersonElement.getElementsByTagName("password");
					Element passwordElement = (Element)passwordList.item(0);
					NodeList textPasswordList = passwordElement.getChildNodes();
					String password = ((Node)textPasswordList.item(0)).getNodeValue().trim();
	
					NodeList hostList = firstPersonElement.getElementsByTagName("hostname");
					Element hostElement = (Element)hostList.item(0);
					NodeList textHostList = hostElement.getChildNodes();
					String host = ((Node)textHostList.item(0)).getNodeValue().trim();
					
					NodeList portList = firstPersonElement.getElementsByTagName("port");
					Element portElement = (Element)portList.item(0);
					NodeList textPortList = portElement.getChildNodes();
					String port = ((Node)textPortList.item(0)).getNodeValue().trim();
	
					NodeList dbNameList = firstPersonElement.getElementsByTagName("dbname");
					Element dbNameElement = (Element)dbNameList.item(0);
					NodeList textDbNameList = dbNameElement.getChildNodes();
					String dbName = ((Node)textDbNameList.item(0)).getNodeValue().trim();
					
					return new Settings(username, password, host, port, dbName);
				}
			}
			return null;
		} catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " 
					+ err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());
			return null;
		} catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
			return null;
		} catch (Throwable t) {
			t.printStackTrace ();
			return null;
		}
		
	}
}
