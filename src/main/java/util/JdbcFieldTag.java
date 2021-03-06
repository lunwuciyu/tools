/**
 * @ProjectName: 公安部出入境综合数据应用视频监控系统项目
 * @Copyright: 2015 中华人民共和国公安部 All Rights Reserved .
 * @address: http://www.mps.gov.cn
 * @date: 2015-12-23 下午7:01:03
 * @Description: 本内容仅限于中华人民共和国公安部内部使用，禁止转发.
 */
package util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015-12-23
 * @modify by reason:{方法名}:{原因}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcFieldTag {
	public String name();
}
