package http;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2016/9/28.
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static CloseableHttpClient httpClient;
    private static RequestConfig requestConfig;
    private static String downloadFilePath = "/data/html/uploads/offical/app";

    //key为app的name，value为app的状态,0表示文件下载不成功，已经删除,1表示处理中,2表示已经下载到本地
    private static Map<String, Integer> fileStatusRecord = new ConcurrentHashMap<>();
    //用户保存文件长度,key为app的name,value为文件长度
    private static Map<String, String> fileLengthRecord = new ConcurrentHashMap<>();

    //最小线程数
    private static final int corePoolSize = 5;
    //最大线程数
    private static final int maximumPoolSize = 30;
    //线程空闲时间
    private static final long keepAliveTime = 60L;

    //该线程池用于从文件服务器下载文件到本地用
    private static ExecutorService downloadExecutorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    static {
        try {

            Setting setting = new Setting();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

            ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
            connManager.setDefaultConnectionConfig(connectionConfig);

            connManager.setMaxTotal(setting.getHttpClientMaxTotal());//最大连接数
            connManager.setDefaultMaxPerRoute(setting.getHttpClientDefaultMaxPerRoute());//路由最大连接数

            SocketConfig socketConfig = SocketConfig.custom()
                    .setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);


            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();

            httpClient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                    .build();

            requestConfig = RequestConfig.custom()
                    // 获取manager中连接 超时时间 0.5s
                    .setConnectionRequestTimeout(setting.getHttpClientConnectionRequestTimeout())
                    // 连接服务器 超时时间  1.5s
                    .setConnectTimeout(setting.getHttpClientConnectTimeout())
                    // 服务器处理 超时时间 3s
                    .setSocketTimeout(setting.getHttpClientSocketTimeout())
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("HttpUtil:创建httpClient失败", e);
            throw new RuntimeException("创建httpClient失败", e);
        }
    }

    /**
     * 用于app文件的下载
     * @param filename 文件名
     * @param path　文件远程下载路径
     * @param response
     */
    public static void downloadForApp(String filename,String path , HttpServletResponse response ){
        if(fileStatusRecord.get(filename) == null ||fileStatusRecord.get(filename) == 0){
            downloadFileFromRemoteToLocal(path,downloadFilePath,filename
                    ,fileStatusRecord, fileLengthRecord);
            setHeaderForDownload(filename, response);
            executeOutputFromRemote(path,response);
        } else if(fileStatusRecord.get(filename) == 1){
            setHeaderForDownload(filename, response);
            executeOutputFromRemote(path,response);
        } else if (fileStatusRecord.get(filename) == 2){
            setHeaderForDownload(filename, response);
            boolean isSuccess = executeOutputFromLocal(filename,response);
            if(!isSuccess){
                downloadFileFromRemoteToLocal(path,downloadFilePath,filename
                        ,fileStatusRecord, fileLengthRecord);
                executeOutputFromRemote(path,response);
            }
        } else {
            downloadFileFromRemoteToLocal(path,downloadFilePath,filename
                    , fileStatusRecord, fileLengthRecord);
            setHeaderForDownload(filename, response);
            executeOutputFromRemote(path,response);
        }
    }

    /**
     * 设置头字段
     * @param filename
     * @param response
     */
    private static void setHeaderForDownload(String filename, HttpServletResponse response) {
        log.info("final use download file name :" + filename);
        response.setHeader("Content-disposition", "attachment;filename="+filename);
        response.setContentType("application/vnd.android.package-archive");
    }


    /**
     * 从addr获取文件内容，然后输出文件
     *
     * @param path  远程文件下载路径
     * @param httpServletResponse
     */
    public static void executeOutputFromRemote(final String path, HttpServletResponse httpServletResponse) {
        HttpGet get = new HttpGet(path);
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            get.setConfig(requestConfig);
            response = httpClient.execute(get);
            Header header = response.getFirstHeader("Content-Length");
            if (header != null) {
                httpServletResponse.setHeader("Content-Length", header.getValue());
            }
            outputStream = httpServletResponse.getOutputStream();
            if (response.getStatusLine().getStatusCode() == 200) {//成功
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    inputStream = httpEntity.getContent();
                }
            }
            if (inputStream != null && outputStream != null) {
                byte[] byteArr = new byte[2048];
                int len = -1;
                while ((len = inputStream.read(byteArr)) != -1) {
                    outputStream.write(byteArr, 0, len);
                }
            }
        } catch (Exception e) {
            log.error("invoke target error", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close error", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream close error", e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("invoke target error", e);
                }
            }
        }
    }

    /**
     * 从本地服务起输出文件给用户
     * @param filename 文件名字
     * @param httpServletResponse
     * @return
     */
    public static boolean executeOutputFromLocal(String filename, HttpServletResponse httpServletResponse) {
        String filePath = downloadFilePath + File.separatorChar + filename;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
            if (!file.exists()) {
                log.error("本地文件不存在");
                return false;
            }
            long fileSize = file.length();
            if (fileSize == 0) {
                log.error("文件大小为0");
                return false;
            }
            if(fileLengthRecord.get(filename) == null || fileLengthRecord.get(filename) == "0"){
                log.error("文件长度为空");
                return false;
            }
            if(fileLengthRecord.get(filename) != null && !fileLengthRecord.get(filename).equals(file.length()+"")){
                log.error("文件长度不正确");
                return false;
            }

            httpServletResponse.setHeader("Content-Length", fileSize + "");
            outputStream = httpServletResponse.getOutputStream();

            if (inputStream != null && outputStream != null) {
                byte[] byteArr = new byte[2048];
                int len = -1;
                while ((len = inputStream.read(byteArr)) != -1) {
                    outputStream.write(byteArr, 0, len);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream close error", e);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("download from local error", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close error", e);
                }
            }
        }
        return false;
    }



    /**
     * 从远程下载文件到本地服务器
     * @param urlPath
     * @param downloadDir
     * @param fileName
     * @param fileStatusRecord
     * @param fileLengthRecord
     */
    public static void downloadFileFromRemoteToLocal(final String urlPath, final String downloadDir, final String fileName,
                                              final Map<String, Integer> fileStatusRecord ,final Map<String,String> fileLengthRecord) {
        synchronized (fileStatusRecord){
            if(fileStatusRecord.get(fileName) != null &&fileStatusRecord.get(fileName) == 1){
                return;
            }
            fileStatusRecord.put(fileName, 1);
        }
        downloadExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                downloadFileFromRemote(urlPath,downloadDir,fileName,fileStatusRecord,fileLengthRecord);
            }
        });
    }


    /**
     * 下载文件到本地服务器
     *
     * @param urlPath
     * @param downloadDir
     * @return
     */
    private static void downloadFileFromRemote(String urlPath, String downloadDir, String fileName,
                                               Map<String, Integer> fileStatusRecord ,final Map<String,String> fileLengthRecord) {
        HttpGet get = new HttpGet(urlPath);
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        String contentLength ="0" ;
        try {
            get.setConfig(requestConfig);
            response = httpClient.execute(get);
            Header contentLengthHeader = response.getFirstHeader("Content-Length");
            if (contentLengthHeader.getValue() == null || "0".equals(contentLengthHeader.getValue())) {
                return;
            }
            contentLength = contentLengthHeader.getValue();
            fileLengthRecord.put(fileName,contentLengthHeader.getValue());

            if (response.getStatusLine().getStatusCode() == 200) {//成功
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    inputStream = httpEntity.getContent();
                }
            }

            if (fileName == null) {
                log.info("get download filename fail");
                return;
            }
            if (inputStream == null) {
                log.info("get inputStream fail");
                return;
            }

            String path = downloadDir + File.separatorChar + fileName;
            file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            outputStream = new FileOutputStream(file);

            int len = -1;
            byte[] byteArr = new byte[2048];
            while ((len = inputStream.read(byteArr)) != -1) {
                outputStream.write(byteArr, 0, len);
            }
        } catch (Exception e) {
            if (file != null) {
                file.delete();
            }
            if (fileName != null) {
                fileStatusRecord.put(fileName, 0);
            }
            log.error("app download fail");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close error", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    if (!contentLength.equals(file.length()+"")) {
                        fileStatusRecord.put(fileName, 0);
                        file.delete();
                        log.error("长度不正确");
                    }else {
                        fileStatusRecord.put(fileName ,2);
                    }
                    outputStream.close();
                } catch (IOException e) {
                    if (file != null) {
                        file.delete();
                    }
                    if (fileName != null) {
                        fileStatusRecord.put(fileName, 0);
                    }
                    log.error("outputStream close error", e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("invoke target error", e);
                }
            }
        }

    }


    public static Map<String, Integer> getFileStatusRecord() {
        return fileStatusRecord;
    }

    public static Map<String, String> getFileLengthRecord() {
        return fileLengthRecord;
    }

    public static String getDownloadFilePath() {
        return downloadFilePath;
    }

    public static ExecutorService getDownloadExecutorService() {
        return downloadExecutorService;
    }
}