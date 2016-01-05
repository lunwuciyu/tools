/**
 * @ProjectName: 公安部出入境综合数据应用视频监控系统项目
 * @Copyright: 2015 中华人民共和国公安部 All Rights Reserved .
 * @address: http://www.mps.gov.cn
 * @date: 2015-7-30 上午9:26:00
 * @Description: 本内容仅限于中华人民共和国公安部内部使用，禁止转发.
 */
package util;


/**
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015-7-30
 * @modify by reason:{方法名}:{原因}
 */
public class JsonCameraInfoNode {
    private String id;                 // ID
    
    private String cameraName;
    
    private String type;
    
    private String deviceName;
    
    private String channelNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }
}
