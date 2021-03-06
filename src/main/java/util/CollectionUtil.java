/**
 * @ProjectName: 公安部出入境综合数据应用视频监控系统项目
 * @Copyright: 2015 中华人民共和国公安部 All Rights Reserved .
 * @address: http://www.mps.gov.cn
 * @date: 2015-8-8 上午9:57:53
 * @Description: 本内容仅限于中华人民共和国公安部内部使用，禁止转发.
 */
package util;

import string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015-8-8
 * @modify by reason:{方法名}:{原因}
 */
public final class CollectionUtil {
    /**
     * 私有构造器，避免实例化
     */
    private CollectionUtil() {
        throw new AssertionError();
    }

    /**
     * 集合是否为null或者没有元素
     *
     * @param collection
     *            集合
     * @return 是否为null或者没有元素
     * @since 2012-8-27
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 集合转为字符串
     *
     * @param collection
     *            集合
     * @return 字符串
     */
    public static String Collection2String(Collection<?> collection) {
        return Collection2String(collection, ",");
    }

    /**
     * 集合转为字符串
     *
     * @param collection
     *            集合
     * @param seperator
     *            分隔符
     * @return 字符串
     */
    public static String Collection2String(Collection<?> collection, String seperator) {
        if (null == collection) {
            throw new IllegalArgumentException("传入的列表不能为空！");
        }

        StringBuilder sb = new StringBuilder();
        for (Object obj : collection) {
            if (sb.length() == 0) {
                sb.append(obj);
            } else {
                sb.append(seperator).append(obj);
            }
        }

        return sb.toString();
    }

    /**
     * 字符串列表转化为List<String>
     *
     * @param strs
     *            字符串列表
     * @return List<String>
     */
    public static List<String> string2List(String... strs) {
        List<String> list = new ArrayList<String>();
        for (String str : strs) {
            if (!StringUtil.isNullOrEmpty(str)) {
                list.add(str);
            }
        }
        return list;
    }

    /**
     * 数组转为字符串
     *
     * @param arr
     *            数组
     * @return 字符串
     */
    public static String Array2String(Integer[] arr) {
        return Array2String(arr, ",");
    }

    /**
     * 数组转为字符串
     *
     * @param arr
     *            数组
     * @param seperator
     *            分隔符
     * @return 字符串
     */
    public static String Array2String(Integer[] arr, String seperator) {
        if (null == arr) {
            throw new IllegalArgumentException("传入的数组不能为空！");
        }

        StringBuffer sb = new StringBuffer();
        for (Integer obj : arr) {
            sb.append(obj).append(seperator);
        }

        return "".equals(sb) ? "" : sb.substring(0, sb.length() - 1);
    }
}
