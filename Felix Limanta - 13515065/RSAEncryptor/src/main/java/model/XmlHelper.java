package org.felixlimanta.RSAEncryptor.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Static class containing XML utility functions
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-13
 */
public class XmlHelper {

  /**
   * Private constructor to prevent class instantiation.
   */
  private XmlHelper() {}

  //region XML to and from RSA key conversion
  //------------------------------------------------------------------------------------------------

  /**
   * Parses RSA key data from XML string.
   *
   * @param xml XML string containing key data
   * @param tagNames Tag names of components used in XML string
   * @return Array of {@code BigInt}s parsed from XML
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   */
  public static BigInt[] rsaKeyFromXmlString(String xml, String[] tagNames)
      throws SAXException, IOException {
    Document doc = XmlHelper.loadXml(xml);
    doc.getDocumentElement().normalize();

    BigInt[] values = new BigInt[tagNames.length];
    for (int i = 0; i < tagNames.length; ++i) {
      String val = doc.getElementsByTagName(tagNames[i]).item(0).getTextContent();
      values[i] = new BigInt(Base64.decodeBase64(val), (byte) 1);
    }

    return values;
  }

  /**
   * Generates an XML string representation of an RSA key given its tag names and values
   *
   * @param tagNames Array of strings containing tag names
   * @param values Array of {@code BigInt}s containing key values
   * @return XML string representation of key
   */
  public static String rsaKeyToXmlString(String[] tagNames, BigInt[] values) {
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

  //------------------------------------------------------------------------------------------------
  //endregion

  //region XML to and from AES key conversion
  //------------------------------------------------------------------------------------------------

  /**
   * Parses AES key data from XML string.
   *
   * @param xml XML string containing key data
   * @return Array of byte arrays parsed from XML
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   */
  public static byte[][] aesKeyFromXmlString(String xml) throws SAXException, IOException {
    Document doc = XmlHelper.loadXml(xml);
    doc.getDocumentElement().normalize();
    byte[][] values = new byte[2][];

    String keyVal = doc.getElementsByTagName("Key").item(0).getTextContent();
    values[0] = Base64.decodeBase64(keyVal);

    String ivVal = doc.getElementsByTagName("IV").item(0).getTextContent();
    values[1] = Base64.decodeBase64(ivVal);

    return values;
  }

  /**
   * Generates an XML string representation of an AES key
   *
   * @param key AES encryption key
   * @param iv AES initialization vector
   * @return XML string representation of key
   */
  public static String aesKeyToXmlString(byte[] key, byte[] iv) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // Root element
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("RSAKeyValue");
      doc.appendChild(rootElement);

      // Is encrypted
      Element isEncrypted = doc.createElement("Encrypted");
      isEncrypted.appendChild(doc.createTextNode("True"));
      rootElement.appendChild(isEncrypted);

      // Key encryption
      Element keyEncryption = doc.createElement("KeyEncryption");
      keyEncryption.setAttribute("algorithm", "RSA1024");
      rootElement.appendChild(keyEncryption);

      // Data encryption
      Element data = doc.createElement("DataEncryption");
      keyEncryption.setAttribute("algorithm", "AES128");
      rootElement.appendChild(data);

      /// AES key
      Element keyElmt = doc.createElement("Key");
      String keyVal = Base64.encodeBase64String(key);
      keyElmt.appendChild(doc.createTextNode(keyVal));
      data.appendChild(keyElmt);

      // AES Initialization Vector
      Element ivElmt = doc.createElement("IV");
      String ivVal = Base64.encodeBase64String(iv);
      ivElmt.appendChild(doc.createTextNode(ivVal));
      data.appendChild(ivElmt);

      // To XML String
      DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
      LSSerializer lsSerializer = domImplementation.createLSSerializer();
      return lsSerializer.writeToString(doc).replaceFirst("<.*?>", "");
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
      return null;
    }
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Common XML functions
  //------------------------------------------------------------------------------------------------

  /**
   * Loads an XML document from an XML string
   *
   * @param xml XML string
   * @return XML document
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   */
  private static Document loadXml(String xml) throws SAXException, IOException {
    xml = xml.trim().replaceFirst("^([\\W]+)<","<");
    ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());

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

  /**
   * Formats a raw XML string to a human-readable format with proper line breaks and indentations
   * @param xml Raw XML string
   * @param indent Number of spaces in one indentation
   * @return Formatted XML string
   */
  public static String formatXml(String xml, int indent) {
    try {
      // Turn xml string into a document
      Document document = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder()
          .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

      // Remove whitespaces outside tags
      document.normalize();
      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
          document,
          XPathConstants.NODESET);

      for (int i = 0; i < nodeList.getLength(); ++i) {
        Node node = nodeList.item(i);
        node.getParentNode().removeChild(node);
      }

      // Setup pretty print options
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", indent);
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      // Return pretty print xml string
      StringWriter stringWriter = new StringWriter();
      transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
      return stringWriter.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    //------------------------------------------------------------------------------------------------
    //endregion
  }
}
