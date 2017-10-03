package com.guru.rss.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProcessXMLUtils {

	public static String addItemGuid(String xmlStr) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new InputSource(new StringReader(xmlStr)));
		Element root = document.getDocumentElement();

		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xPath.evaluate("/rss/channel/item", root, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); ++i) {
//			System.out.println("addItemGuid() [i:" + i + "]");
			Element eItem = (Element) nodes.item(i);
			String link = eItem.getElementsByTagName("link").item(0).getTextContent();
			String guidStr = link.substring(link.lastIndexOf("%7E") + 3, link.lastIndexOf("?"));
//			System.out.println("addItemGuid()   [link:" + link + "] [guidStr:" + guidStr + "]");
			Element eGuid = document.createElement("guid");
			eGuid.setAttribute("isPermaLink", "false");
			eGuid.appendChild(document.createTextNode(guidStr));
			eItem.appendChild(eGuid);
		}

		DOMSource source = new DOMSource(document);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter outWriter = new StringWriter();
		StreamResult result = new StreamResult(outWriter);
//		StreamResult result = new StreamResult("server.xml");
		transformer.transform(source, result);
		StringBuffer sb = outWriter.getBuffer();
		String finalstring = sb.toString();
		return finalstring;
	}

}
