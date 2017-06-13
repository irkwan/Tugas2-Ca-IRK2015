package org.felixlimanta.RSAEncryptor.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

/**
 * Created by ASUS on 13/06/17.
 */
class RsaXmlHelper {
  static BigInt[] fromXmlString(String xml, String[] tagNames) {
    try {
      Document doc = RsaXmlHelper.loadXml(xml);
      doc.getDocumentElement().normalize();

      BigInt[] values = new BigInt[tagNames.length];
      for (int i = 0; i < tagNames.length; ++i) {
        String val = doc.getElementsByTagName(tagNames[i]).item(0).getTextContent();
        values[i] = new BigInt(Base64.decodeBase64(val), (byte) 1);
      }

      return values;
    } catch (SAXException saxe) {
      saxe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return null;
  }

  private static Document loadXml(String xml) throws SAXException, IOException {
    xml = xml.trim().replaceFirst("^([\\W]+)<","<");
    return loadXml(new ByteArrayInputStream(xml.getBytes()));
  }

  private static Document loadXml(InputStream is) throws SAXException, IOException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    docFactory.setNamespaceAware(true);

    try {
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(is);

      is.close();
      return doc;
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
      return null;
    }
  }

  static String toXmlString(String[] tagNames, BigInt[] values) {
    assert (tagNames.length == values.length);
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // Root element
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("RSAKeyValue");
      doc.appendChild(rootElement);

      for (int i = 0; i < tagNames.length; ++i) {
        Element e = doc.createElement(tagNames[i]);
        String val = Base64.encodeBase64String(values[i].toByteArray());
        e.appendChild(doc.createTextNode(val));
        rootElement.appendChild(e);
      }

      // To XML String
      DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
      LSSerializer lsSerializer = domImplementation.createLSSerializer();
      return lsSerializer.writeToString(doc).replaceFirst("<.*?>", "");
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
      return null;
    }
  }
}
