package secret;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5HashUtil {
	private static final Log log = LogFactory.getLog(MD5HashUtil.class);
	private MessageDigest md = null;
	private static MD5HashUtil md5 = null;
	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Constructor is private so you must use the getInstance method
	 */
	private MD5HashUtil() throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("MD5");
	}

	/**
	 * This returns the singleton instance
	 */
	public static MD5HashUtil getInstance() throws NoSuchAlgorithmException {
		if (md5 == null) {
			md5 = new MD5HashUtil();
		}
		return (md5);
	}

	public static String hashCode(String dataToHash) throws NoSuchAlgorithmException {
		return getInstance().hashData(dataToHash.getBytes());
	}

	public static String hashCode(byte[] dataToHash) throws NoSuchAlgorithmException {
		return getInstance().hashData(dataToHash);
	}

	public String hashData(byte[] dataToHash) {
		return hexStringFromBytes((calculateHash(dataToHash)));
	}

	private byte[] calculateHash(byte[] dataToHash) {
		md.update(dataToHash, 0, dataToHash.length);
		return (md.digest());
	}

	public String hexStringFromBytes(byte[] b) {
		StringBuffer hex = new StringBuffer();
		int msb;
		int lsb = 0;
		int i;
		// MSB maps to idx 0
		for (i = 0; i < b.length; i++) {
			msb = ((int) b[i] & 0x000000FF) / 16;
			lsb = ((int) b[i] & 0x000000FF) % 16;
			hex.append(hexChars[msb]).append(hexChars[lsb]);
		}
		return (hex.toString());
	}

	public static void main(String args[]) throws NoSuchAlgorithmException {
		String string = "734348";
		log.debug(string);
		log.debug(hashCode(string));
	}
}