package string;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * IsEmpty/IsBlank – 检查字符串是否有内容。 
 * Trim/Strip – 删除字符串开始和结尾的空白符。
 * Equals – 比较两个字符串null安全。
 * IndexOf/LastIndexOf/Contains – null安全的索引检查。  
 * IndexOfAny/LastIndexOfAny/IndexOfAnyBut/LastIndexOfAnyBut – 字符串集合索引检查。
 * ContainsOnly/ContainsNone – 字符在字符串中出现一次或一次也没有出现。
 * Substring/Left/Right/Mid – null安全子串的提取。  
 * SubstringBefore/SubstringAfter/SubstringBetween – 子串提取依赖其它字符串。 
 * Split/Join – 字符串拆分为子串的字符串数组，反之亦然。 
 * Remove/Delete – 删除部分字符串。 
 * Replace/Overlay – 替换字符串的部分字符。 
 * Chomp/Chop – 删除字符串最后的字符。 
 * LeftPad/RightPad/Center/Repeat – 补字符串。
 * UpperCase/LowerCase/SwapCase/Capitalize/Uncapitalize – 改变字符串的大小写。 
 * CountMatches – 计算一个字符或字符串在另外一个字符串出现的次数。 
 * IsAlpha/IsNumeric/IsWhitespace/IsAsciiPrintable – 判断字符是否在字符串中。
 * DefaultString –null安全，null转换为字符串。 
 * Reverse/ReverseDelimited – 反转字符串。 
 * Abbreviate – 缩写字符串用省略符。
 * Difference – 比较两个字符串并且返回不同。 
 * LevensteinDistance – 一个字符串改变为另一个字符串需要改变的数量。
 */
public class StringUtil extends StringUtils{
	
	private static final Log log = LogFactory.getLog(StringUtil.class);

	/**
	 * 得到在数组source中而不在数组target中的数组
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            目标
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static String[] inSourceNotInTarget(String[] source, String[] target) {
		List resultList = new ArrayList();
		List sourceList = Arrays.asList(source);
		List targetList = Arrays.asList(target);
		for (Iterator it = sourceList.iterator(); it.hasNext();) {
			Object candidate = it.next();
			if (!targetList.contains(candidate)) {
				resultList.add(candidate);
			}
		}
		return (String[]) resultList.toArray(new String[resultList.size()]);
	}

	/**
	 * 把数组的顺序颠倒过来
	 * 
	 * @param source
	 * @return
	 */
	public static String[] reverse(String[] source) {
		String[] result = new String[source.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = source[result.length - i - 1];
		}
		return result;
	}

	public static void main(String[] args) {
		String[] a = new String[] { "1", "2", "3", "4" };
		String[] b = new String[] { "1", "2", "7", "8" };
		String[] r = inSourceNotInTarget(b, a);
		for (int i = 0; i < r.length; i++) {
			log.debug(r[i]);
		}
	}

	/**
	 * 
	 * @Date : 2010-5-12 上午11:06:55
	 * @Description : 判断字符串对象是否为null或者字符串为空窜
	 * @param str
	 *            字符串对象
	 * @return
	 * @ModificationHistory ===================================
	 * @Modifiy by :modifiy_name
	 * @Date :2010-5-12上午11:06:55
	 * @Modifiy reason :
	 * 
	 */
	public static boolean isNotNullOrEmpty(String str) {
		return !isNullOrEmpty(str);
	}
	
	/**
	 * 是否为null或空字符串
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str){
		return str == null || str.isEmpty();
	}
	
	/**
	 * 读取文件内容为文本，对UTF格式的编码，将试探有无BOM编码
	 * 
	 * @param filename
	 * @param encode
	 * @return
	 */
	public static String readFromFile(File filename, String encode) {
		StringBuilder result = new StringBuilder("");
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStream is = fis;
			if (encode.toUpperCase().contains("UTF")) {
				UnicodeInputStream uin = new UnicodeInputStream(fis, encode);
				encode = uin.getEncoding();
				is = uin;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encode));
			String buffer = "";
			while ((buffer = br.readLine()) != null)
				result.append(buffer).append("\n");
			br.close();
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
		return result.toString();
	}
	
	/**
	 * 将文本写入文件
	 * 
	 * @param fout
	 * @param value
	 * @param encode
	 */
	public static void writeToFile(File fout, String value, String encode) {
		BufferedWriter bw = null;
		try {
			createNewFile(fout, false);
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fout), encode));
			bw.write(value);
			bw.flush();
			bw.close();
			log.info("写入文件：" + fout.getAbsolutePath());
		} catch (IOException ex) {
			log.error(null, ex);
		}
	}
	
	public static boolean pkiLogin(String path) {
		boolean state = false;
		if (StringUtil.isNotNullOrEmpty(path) && path.indexOf(":") != -1) {
			state = new File(path).exists();
		}
		return state;
	}
	
	/**
	 * 新建文件，如果以存在是否删除
	 * @param filename
	 * @param delete
	 */
	public static void createNewFile(File filename, boolean delete) {
		filename = filename.getAbsoluteFile();
		try {
			if (filename.exists())
				if (delete || filename.length() == 0) {
					if(filename.delete()){
						if(!filename.createNewFile()){
							log.info("create new file failed.");
						}
					}else{
						log.info("delete file failed.");
					}
				} 
				else
					return; 
			else {
				File path = filename.getParentFile();
				if (!path.exists()){
					if(!path.mkdirs()){
						log.info("create path failed.");
					}
				}
				if(path.exists()){
					if(!filename.createNewFile()){
						log.info("create new file failed.");
					}
				}
			}
		} catch (IOException ex) {
			log.error("File: " + filename.toString() + " create failed.", ex);
		}
	}
	
	/**
	 * version: 1.1 / 2007-01-25 - changed BOM recognition ordering (longer boms
	 * first)
	 * 
	 * Original pseudocode : Thomas Weidenfeller Implementation tweaked: Aki
	 * Nieminen
	 * 
	 * http://www.unicode.org/unicode/faq/utf_bom.html BOMs in byte length
	 * ordering: 00 00 FE FF = UTF-32, big-endian FF FE 00 00 = UTF-32,
	 * little-endian EF BB BF = UTF-8, FE FF = UTF-16, big-endian FF FE =
	 * UTF-16, little-endian
	 * 
	 * Win2k Notepad: Unicode format = UTF-16LE
	 ***/

	/**
	 * This inputstream will recognize unicode BOM marks and will skip bytes if
	 * getEncoding() method is called before any of the read(...) methods.
	 * 
	 * Usage pattern: String enc = "ISO-8859-1"; // or NULL to use systemdefault
	 * FileInputStream fis = new FileInputStream(file); UnicodeInputStream uin =
	 * new UnicodeInputStream(fis, enc); enc = uin.getEncoding(); // check and
	 * skip possible BOM bytes InputStreamReader in; if (enc == null) in = new
	 * InputStreamReader(uin); else in = new InputStreamReader(uin, enc);
	 */
	public static class UnicodeInputStream extends InputStream {
		PushbackInputStream internalIn;
		boolean isInited = false;
		String defaultEnc;
		String encoding;

		private static final int BOM_SIZE = 4;

		UnicodeInputStream(InputStream in, String defaultEnc) {
			internalIn = new PushbackInputStream(in, BOM_SIZE);
			this.defaultEnc = defaultEnc;
		}

		public String getDefaultEncoding() {
			return defaultEnc;
		}

		public String getEncoding() {
			if (!isInited) {
				try {
					init();
				} catch (IOException ex) {
					IllegalStateException ise = new IllegalStateException(
							"Init method failed.");
					ise.initCause(ise);
					throw ise;
				}
			}
			return encoding;
		}
		
		/**
		 * Read-ahead four bytes and check for BOM marks. Extra bytes are unread
		 * back to the stream, only BOM bytes are skipped.
		 */
		protected void init() throws IOException {
			if (isInited)
				return;

			byte bom[] = new byte[BOM_SIZE];
			int n, unread;
			n = internalIn.read(bom, 0, bom.length);

			if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00)
					&& (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
				encoding = "UTF-32BE";
				unread = n - 4;
			} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)
					&& (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
				encoding = "UTF-32LE";
				unread = n - 4;
			} else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB)
					&& (bom[2] == (byte) 0xBF)) {
				encoding = "UTF-8";
				unread = n - 3;
			} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
				encoding = "UTF-16BE";
				unread = n - 2;
			} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
				encoding = "UTF-16LE";
				unread = n - 2;
			} else {
				// Unicode BOM mark not found, unread all bytes
				encoding = defaultEnc;
				unread = n;
			}
			// System.out.println("read=" + n + ", unread=" + unread);

			if (unread > 0)
				internalIn.unread(bom, (n - unread), unread);

			isInited = true;
		}

		public void close() throws IOException {
			// init();
			isInited = true;
			internalIn.close();
		}

		public int read() throws IOException {
			// init();
			isInited = true;
			return internalIn.read();
		}
		
	}
	
	public static <T> String convertListToString(List<T> list,boolean isString){
		StringBuffer sb=new StringBuffer();
		for(Object obj:list){
			if(obj!=null&&!"".equals(obj)){
				if(isString){
					sb.append("'").append(obj).append("'").append(",");
				}else{
					sb.append(obj).append(",");
				}
			}
		}
		if(!"".equals(sb.toString())){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public static String convertArrayToString(Object[] objArray,boolean isString){
		StringBuffer sb=new StringBuffer();
		for(Object obj:objArray){
			if(obj!=null&&!"".equals(obj)){
				if(isString){
					sb.append("'").append(obj).append("'").append(",");
				}else{
					sb.append(obj).append(",");
				}
			}
		}
		if(!"".equals(sb.toString())){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
    public static String combinePath(String... paths) {
        final String windowsSeparator = "\\";
        final String unixSeparator = "/";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < paths.length; i++) {
            String path = trim(paths[i]);
            if (buf.length() > 0
                    && (path.startsWith(windowsSeparator) || path.startsWith(unixSeparator))) {
                path = path.substring(1);
            }
            for (int j = 0; j < path.length(); j++) {
                String c = path.substring(j, j + 1);
                if (windowsSeparator.equals(c) || unixSeparator.equals(c)) {
                    buf.append(File.separator);
                } else {
                    buf.append(c);
                }
            }
            String endChar = buf.substring(buf.length() - 1, buf.length());
            if (!endChar.equals(File.separator)) {
                buf.append(File.separator);
            }
        }
        String endChar = String.valueOf(buf.charAt(buf.length() - 1));
        if (endChar.equals(File.separator)) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }
	
}
