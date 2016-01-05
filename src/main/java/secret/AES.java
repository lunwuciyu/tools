package secret;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//Key必须为16位！
public class AES {
	static final private String cKey = "1234567890123456";
	@SuppressWarnings("unused")
	final private static String baseStr = "679867948917059821390487120941029387490123865843021387491234";

	// 随机得到一个16位AES密匙
	public static String generateKey() {
		return keyRepair(16);
	}

	// 随机得到一个i位随机密匙
	public static String keyRepair(int i) {
		String key = "";
		Random rand = new Random();
		key += Math.abs(rand.nextInt() * i);
		if (key.length() < i) {
			key += keyRepair(i - key.length());
		} else if (key.length() > i) {
			key = key.substring(key.length() - i);
		}
		return key;
	}

	// AES加密＋16进制转码
	public static String encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());

		return byte2hex(encrypted);
	}

	// 获取Key
	public static String getAESKey() {
		return cKey;
	}

	// 将Key写入文件
	// 参数ckey为要写入的Key, path为输入的文件路径
	public static void writeKey(String ckey, String path) throws IOException {
		OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(path));
		os.write(ckey);
		os.close();
	}

	// 从一个文件中取出内容做Key用
	// 参数 path为获得文件的路径
	public static String getKeyFromFile(String path) throws IOException {
		char[] buf = new char[16];
		InputStreamReader is = new InputStreamReader(new FileInputStream(path));
		is.read(buf);
		is.close();
		return new String(buf);
	}

	// AES解密＋16进制转码
	public static String decrypt(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = hex2byte(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
//				e.printStackTrace();
//				log.error("AES解密错误."+e.getMessage());
				return null;
			}
		} catch (Exception ex) {
//			ex.printStackTrace();
//			log.error("AES解密错误."+ex.getMessage());
			return null;
		}
	}

	// 16进制转码
	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 != 0) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		for (int n = 0; n < b.length; n++) {
			StringBuffer stmp = new StringBuffer();
			stmp.append((java.lang.Integer.toHexString(b[n] & 0XFF)));
			if (stmp.toString().length() == 1) {
				hs.append("0").append(stmp.toString());
			} else {
				hs.append(stmp.toString());
			}
		}
		return hs.toString().toUpperCase();
	}

//	// AES加密+Base64转码
//	public static String encryptBase64(String sSrc, String sKey) throws Exception {
//		if (sKey == null) {
//			System.out.print("Key为空null");
//			return null;
//		}
//		// 判断Key是否为16位
//		if (sKey.length() != 16) {
//			System.out.print("Key长度不是16位");
//			return null;
//		}
//		byte[] raw = sKey.getBytes();
//		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
//		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
//		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
//
//		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
//	}
//
//	// AES解密+Base64转码
//	public static String decryptBase64(String sSrc, String sKey) throws Exception {
//		try {
//			// 判断Key是否正确
//			if (sKey == null) {
//				System.out.print("Key为空null");
//				return null;
//			}
//			// 判断Key是否为16位
//			if (sKey.length() != 16) {
//				System.out.print("Key长度不是16位");
//				return null;
//			}
//			byte[] raw = sKey.getBytes("ASCII");
//			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
//			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
//			try {
//				byte[] original = cipher.doFinal(encrypted1);
//				String originalString = new String(original);
//				return originalString;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return null;
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		}
//	}
}