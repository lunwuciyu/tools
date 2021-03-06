package xml;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlUtil {

	public static boolean enableJCBK = false;
	/**
	 * 由一个xml格式的字符串生成一个表示xml配置文件的Document对象
	 */
	public static Document stringToDocument(String str) {
		Document doc = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(str)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;
	}

	/**
	 * 由一个Document对象生成一个xml格式的字符串
	 */
	public static String documentToString(Document doc) {
		DOMBuilder builder = new DOMBuilder();
		XMLOutputter xml = new XMLOutputter();
		return xml.outputString(builder.build(doc));
	}

	/**
	 * 由文件名所代表的文件生成一个Document对象
	 */
	public static Document fileNameToDocument(String fileName) {
		Document doc = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.parse(new File(fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;
	}

}
