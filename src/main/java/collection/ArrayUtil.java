package collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class ArrayUtil extends ArrayUtils{
    /**
     * 字符串数组转整型列表
     *
     * @param array 字符串数组
     * @return 整型列表
     */
    public static List<Integer> arrayToList(String[] array) {
        List<Integer> list = new ArrayList<Integer>();
        for (String str : array) {
            list.add(Integer.valueOf(str));
        }
        return list;
    }

    /**
     * 整型串转整型列表
     *
     * @param integers 整型串
     * @return 整型列表
     */
    public static List<Integer> arrayToList(Integer... integers) {
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, integers);
        return list;
    }

    /**
     * 私有构造器，避免实例化
     */
    private ArrayUtil() {
        throw new AssertionError();
    }

	
}
