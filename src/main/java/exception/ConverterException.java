/**
 * @ProjectName: 公安部出入境综合数据应用视频监控系统项目
 * @Copyright: 2015 中华人民共和国公安部 All Rights Reserved .
 * @address: http://www.mps.gov.cn
 * @date: 2015-7-15 上午9:01:40
 * @Description: 本内容仅限于中华人民共和国公安部内部使用，禁止转发.
 */
package exception;

/**
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015-7-15
 * @modify by reason:{方法名}:{原因}
 */
public class ConverterException extends Exception{

	/** 序列化ID*/
	private static final long	serialVersionUID	= 1L;
	public ConverterException(Throwable cause){
		super("对象转化失败!", cause);
	}
	public ConverterException(String msg){
		super(msg);
	}
	public ConverterException(String msg,Throwable cause){
		super(msg, cause);
	}
}
