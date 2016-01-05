package ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpUtil {

	private static final Logger logger = Logger.getLogger(FtpUtil.class);
	private static FtpUtil instance;
	private FTPClient ftpClient = null;

	public synchronized static FtpUtil getInstance() {
		if (instance == null) {
			instance = new FtpUtil();
		}
		return instance;
	}

	public void login(String url, String username, String pwd) {
		ftpClient = new FTPClient();
		try {
			// 连接
			ftpClient.connect(url);
			ftpClient.login(username, pwd);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.close();
				logger.error("FTP server refused connection.");
			}
		} catch (Exception e) {
			logger.error("login FTP server error:", e);
			this.close();
		}
	}
	
	public boolean deleteFile(String fileName){
		boolean flag = false;
		if (ftpClient != null) {
			try {
				flag = ftpClient.deleteFile(fileName);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				this.close();
			}
		}
		return flag;
	}

	public boolean deleteFile(String url, String userName, String pwd, String fileName) {
		login(url, userName, pwd);
		return deleteFile(fileName);
	}

	public void close() {
		if (ftpClient != null) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println("start delete file");
		getInstance().deleteFile("100.10.3.41", "test", "12345", "/test/test.txt");
		System.out.println("delete file complete");
	}
}
