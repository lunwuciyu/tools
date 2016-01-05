package file;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SimpleIniUtil implements Serializable {

	/** 序列化ID*/
	private static final long	serialVersionUID	= 1L;

	/*
	 * log for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(SimpleIniUtil.class);

	private String iniFile = null;

	private Map<String, String> propContent;

	private String defaultEncode = "GBK";

	private boolean isReloadOnchange = false;

	private long fileTime;

	/**
	 * default constructor method
	 */
	public SimpleIniUtil() {
		propContent = new LinkedHashMap<String, String>();
	}

	/**
	 * set the load file encode
	 * 
	 * @param encode
	 */
	public void setEncode(String encode) {
		this.defaultEncode = encode;
	}

	/**
	 * constructor method
	 * 
	 * @param iniFile
	 *            full properties file path
	 */
	public SimpleIniUtil(String iniFile) {
		this();
		this.setIniFile(iniFile);
	}

	/**
	 * get the full properties file path
	 * 
	 * @return file path
	 */
	public String getIniFile() {
		return iniFile;
	}

	/**
	 * set the properties file path
	 * 
	 * @param iniFile
	 *            full properties file path
	 */
	public void setIniFile(String iniFile) {
		this.iniFile = iniFile;
	}

	/**
	 * load the properties content from the file
	 * 
	 * @return properteis content from the file
	 * @throws IOException
	 *             IO about exception will throw out
	 */
	public Map<String, String> loadFile() throws IOException {
		return this.loadFile(this.iniFile);
	}

	/**
	 * load the propeties file by specified file path
	 * 
	 * @param iniFile
	 *            full file path
	 * @return properteis content from the file
	 * @throws IOException
	 *             IO about exception will throw out
	 */
	public Map<String, String> loadFile(String iniFile) throws IOException {
		return this.loadFile(iniFile, this.defaultEncode);
	}

	/**
	 * load the propeties file by specified file path and encodign
	 * 
	 * @param instream
	 *            input stream object
	 * @param encode
	 *            specified encoding
	 * @return map of properteis content from the file
	 * @throws IOException
	 *             IO about exception will throw out
	 */
	public Map<String, String> loadFile(InputStream instream, String encode)
			throws IOException {

		this.defaultEncode = encode;

		BufferedReader in = new BufferedReader(new InputStreamReader(instream,
				encode));
		String lineStr = null;
		int commentPos = -1;
		int valueBreakPos = -1;
		String key = null;
		String value = "";
		int variabesNum = 0;
		String variableKey = null;

		while ((lineStr = in.readLine()) != null) {
			lineStr = lineStr.trim();
			if (lineStr.equals("")) {
				continue;
			}
			if (lineStr.startsWith("#")) {
				continue;
			}

			commentPos = lineStr.indexOf("#");
			if (commentPos != -1) {
				lineStr = lineStr.substring(0, commentPos);
			}
			valueBreakPos = lineStr.indexOf("=");
			if (valueBreakPos == -1) {
				setPropertiseValue(lineStr, "");
			} else {
				key = lineStr.substring(0, valueBreakPos).trim();
				value = lineStr.substring(++valueBreakPos, lineStr.length())
						.trim();
				if (value.indexOf("${") != -1) {

					List<String> variables = parseVaraibles(value);
					if (variables != null) {
						variabesNum = variables.size();
						for (int i = 0; i < variabesNum; i++) {
							variableKey = variables.get(i);
							if (variableKey != null && !variableKey.equals(key)) {
								value = StringUtils.replace(value, "${"
										+ variableKey + "}",
										getPropertiesValue(variableKey, ""));
							}
						}
					}
				}
				setPropertiseValue(key, value);
			}
		}
		in.close();

		return this.propContent;
	}

	/**
	 * load the propeties file by specified file path and encodign
	 * 
	 * @param iniFile
	 *            full file path
	 * @param encode
	 *            specified encoding
	 * @return map of properteis content from the file
	 * @throws IOException
	 *             IO about exception will throw out
	 */
	public Map<String, String> loadFile(String iniFile, String encode)
			throws IOException {
		if (iniFile == null) {
			LOGGER.warn("null pointer of iniFile");
			throw new NullPointerException("iniFile is set to be value of null");
		}
		if (iniFile.equals("")) {
			throw new IOException("filename should not be null");
		}

		setIniFile(iniFile);

		File file = new File(iniFile);
		if (!file.exists()) {
			if(!file.createNewFile()){
				LOGGER.info("crate new file failed.");
			}
		}
		fileTime = file.lastModified();
		InputStream inFile = new FileInputStream(file);
		Map<String, String> result = loadFile(inFile, encode);
		inFile.close();
		return result;
	}

	/**
	 * parse the format as ${..} variables
	 * 
	 * @param field
	 *            string to be parsed
	 * @return list object contains parsed variables if field is null or blank
	 *         null value will be return
	 */
	private List<String> parseVaraibles(String field) {
		List<String> variables = new ArrayList<String>();

		if (StringUtils.isBlank(field)) {
			return null;
		}

		int len = field.length();
		char keyChar;
		boolean iskey = false;
		StringBuffer tempStr = new StringBuffer();
		for (int i = 0; i < len; i++) {
			keyChar = field.charAt(i);

			if (keyChar == '$') {
				if ((++i < len) && (field.charAt(i) == '{')) {
					iskey = true;
					tempStr.setLength(0);
				}
			} else if (keyChar == '}') {
				if (tempStr.length() > 0) {
					variables.add(tempStr.toString());
				}
				iskey = false;
			} else {
				if (iskey) {
					tempStr.append(keyChar);
				}
			}
		}

		return variables;
	}

	/**
	 * store the properties content to the opened file
	 * 
	 * @throws IOException
	 *             IO about exception will throw out
	 */
	public void store() throws IOException {
		this.store(this.iniFile);
	}

	/**
	 * store the properties content to the specified file
	 * 
	 * @param iniFile
	 *            full file path
	 * @throws IOException
	 *             IO about exception will throw out
	 */
	public void store(String iniFile) throws IOException {
		this.store(iniFile, "");
	}

	/**
	 * store the properties content to the specified file and support to add
	 * comment if call this method as follow: store("c:\\test.ini",
	 * "Matthew update it"); the comment will out like this: #Matthew update it
	 * #update time:-- ::
	 * 
	 * @param iniFile
	 *            full file path
	 * @param comment
	 *            comment for this updating
	 * @throws IOException
	 *             IOException IO about exception will throw out
	 */
	public void store(String iniFile, String comment) throws IOException {
		if (iniFile == null) {
			LOGGER.warn("null pointer of iniFile");
			throw new NullPointerException("iniFile is set to be value of null");
		}
		if (iniFile.equals("")) {
			throw new IOException("filename should not be null");
		}

		if (this.propContent == null) {
			return;
		}

		StringBuffer sbuff = new StringBuffer();
		if (comment != null && (!comment.equals(""))) {
			sbuff.append("#");
			sbuff.append(comment);
			sbuff.append("\r\n");
			DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sbuff.append("#update time:").append(dformat.format(new Date()));
			sbuff.append("\r\n");
		}

		Iterator<Entry<String, String>> iter = this.propContent.entrySet().iterator();
		String key, value;
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			key = (String) entry.getKey();
			value = (String) entry.getValue();

			sbuff.append(key);
			sbuff.append("=");
			sbuff.append(value);
			sbuff.append("\r\n");
		}

		OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(iniFile),"UTF-8");
		out.write(sbuff.toString());
		out.flush();
		out.close();

	}
	
	/**
	 * store the properties content to the specified file and support to add
	 * comment if call this method as follow: store("c:\\test.ini",
	 * "Matthew update it"); the comment will out like this: #Matthew update it
	 * #update time:-- ::
	 * 
	 * @param iniFile
	 *            full file path
	 * @param propContent
	 *            comment for this updating
	 * @throws IOException
	 *             IOException IO about exception will throw out
	 */
	public void store(String iniFile, Map<String, String> propContent) throws IOException {
		if (iniFile == null) {
			LOGGER.warn("null pointer of iniFile");
			throw new NullPointerException("iniFile is set to be value of null");
		}
		if (iniFile.equals("")) {
			throw new IOException("filename should not be null");
		}

		if (this.propContent == null) {
			return;
		}

		StringBuffer sbuff = new StringBuffer();
		
		Iterator<Entry<String, String>>iter = this.propContent.entrySet().iterator();
		String key, value;
		while (iter.hasNext()) {
			Entry<String, String>  entry = iter.next();
			key = (String) entry.getKey();
			value = (String) entry.getValue();

			sbuff.append(key);
			sbuff.append("=");
			sbuff.append(value);
			sbuff.append("\r\n");
		}

		OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(iniFile, false),"UTF-8");
		out.write(sbuff.toString());
		out.flush();
		out.close();
	}

	/**
	 * get string properties value if the properties name not found null will be
	 * return
	 * 
	 * @param key
	 *            properties key
	 * @return properties value
	 */
	public String getPropertiesValue(String key) {

		return getPropertiesValue(key, null);
	}

	/**
	 * get string properties value if the properties name not found default
	 * value will be return.
	 * 
	 * @param key
	 *            properties key
	 * @param defValue
	 *            properties value
	 * @return properties value
	 */
	public String getPropertiesValue(String key, String defValue) {

		if (isReloadOnchange) {
			try {
				reloadOnChange();
			} catch (IOException e) {
				LOGGER.warn("reload propeties file failed: reason:"
						+ e.getMessage());
			}
		}

		String strRet = defValue;
		if (key != null && (!key.equals(""))) {
			strRet = this.propContent.get(key);
			if (strRet == null) {
				return defValue;
			}
		}
		return strRet;
	}

	/**
	 * reload the properties file.
	 * 
	 * @throws IOException
	 */
	private void reloadOnChange() throws IOException {
		if (StringUtils.isBlank(iniFile)) {
			return;
		}

		File file = new File(iniFile);
		if (!file.exists()) {
			propContent.clear();
			return;
		}

		long fileLastModifytime = file.lastModified();
		if (fileLastModifytime != fileTime) {
			if (propContent != null) {
				propContent.clear();
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("reloadOnChange() - reload ini file:" + iniFile);
			}
			loadFile(iniFile, defaultEncode);
		}

	}

	/**
	 * get Integer properties value if the properties name not found default
	 * value will be return.
	 * 
	 * @param key
	 *            properties key
	 * @param defValue
	 *            properties value
	 * @return properties value
	 */
	public Integer getPropertiesInteger(String key, Integer defValue) {
		int intRet = defValue;
		String strValue = getPropertiesValue(key);
		if (strValue != null) {
			try {
				intRet = Integer.parseInt(strValue);
			} catch (Exception e) {
				LOGGER.warn("parse String to Integer failed.");
			}
		}
		return intRet;
	}

	/**
	 * get Float properties value if the properties name not found default value
	 * will be return.
	 * 
	 * @param key
	 *            properties key
	 * @param defValue
	 *            properties value
	 * @return properties value
	 */
	public Float getPropertiesFloat(String key, Float defValue) {
		float floatRet = defValue;
		String strValue = getPropertiesValue(key);
		if (strValue != null) {
			try {
				floatRet = Float.parseFloat(strValue);
			} catch (Exception e) {
				LOGGER.warn("parse String to Float failed.");
			}
		}
		return floatRet;
	}

	/**
	 * <p>
	 * set properties value. <code>null</code> value and key is not permitted.
	 * </p>
	 * 
	 * @param key
	 *            properties key
	 * @param value
	 *            properties value
	 * @return return the previous value of the specified key in this property
	 *         list, or null if it did not have one.
	 */
	public Object setPropertiseValue(String key, String value) {
		return this.propContent.put(key, value);
	}

	/**
	 * set integer properties value
	 * 
	 * @param key
	 *            properties key
	 * @param value
	 *            properties value
	 * @return return the previous value of the specified key in this property
	 *         list, or null if it did not have one.
	 */
	public Object setPropertiseValue(String key, Integer value) {
		return this.propContent.put(key, value.toString());
	}

	/**
	 * set float properties value
	 * 
	 * @param key
	 *            properties key
	 * @param value
	 *            properties value
	 * @return return the previous value of the specified key in this property
	 *         list, or null if it did not have one.
	 */
	public Object setPropertiseValue(String key, Float value) {
		return this.propContent.put(key, value.toString());
	}

	/**
	 * <p>
	 * get all properties keys.<br>
	 * <code>null</code> will be return if the properties is <code>null</code>
	 * </p>
	 * 
	 * @return list type properties keys
	 */
	public List<String> getAllKeys() {
		if (propContent == null) {
			return null;
		}

		Set<String> keys = propContent.keySet();
		List<String> list = new LinkedList<String>(keys);
		return list;
	}

	/**
	 * <p>
	 * get all peroperties keys and values<br>
	 * <code>null</code> will be return if the properties is <code>null</code>
	 * </p>
	 * 
	 * @return map type properties keys and values
	 */
	public Map<String, String> getAllKeyValues() {
		return propContent;
	}

	/**
	 * @return the isReloadOnchange
	 */
	public boolean isReloadOnchange() {
		return isReloadOnchange;
	}

	/**
	 * @param isReloadOnchange
	 *            the isReloadOnchange to set
	 */
	public void setReloadOnchange(boolean isReloadOnchange) {
		this.isReloadOnchange = isReloadOnchange;
	}

	/**
	 * @return the propContent
	 */
	public Map<String, String> getPropContent() {
		return propContent;
	}

	/**
	 * @param propContent
	 *            the propContent to set
	 */
	public void setPropContent(Map<String, String> propContent) {
		this.propContent = propContent;
	}

	/**
	 * get the properties keys and values
	 * 
	 * @return override toString method.
	 */
	@Override
	public String toString() {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("Properties values{");
		if (this.propContent != null) {
			Iterator<Entry<String, String>> iter = this.propContent.entrySet().iterator();
			String key, value;
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				key = (String) entry.getKey();
				value = (String) entry.getValue();

				sbuff.append(key);
				sbuff.append("=");
				sbuff.append(value);
				sbuff.append("\r\n");
			}
		}
		sbuff.append("}");
		return sbuff.toString();
	}

}
