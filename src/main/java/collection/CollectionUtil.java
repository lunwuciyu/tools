package collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * allIn - 判断一个集合中的所有元素是否都在另一个集合中
 * substract - 把在集合source中而不在集合target中的元素集合提取出来
 * in - 把在集合source中也在集合target中的元素集合提取出来
 * allNotIn - 判断一个集合中的所有元素是否都不在另一个集合中
 * ipMiddle - 根据起始IP地址和结束IP地址得到位于起始IP地址和结束IP地址之间的IP地址集合
 */
public abstract class CollectionUtil {

	@SuppressWarnings("rawtypes")
	public static boolean allIn(Collection source, Collection target) {
		if (target == null || target.size() == 0) {
			return false;
		}

		for (Iterator it = source.iterator(); it.hasNext();) {
			Object object = it.next();
			if (!target.contains(object)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Collection substract(Collection source, Collection target) {
		if (source == null || source.size() == 0) {
			return null;
		}
		if (target == null || target.size() == 0) {
			return source;
		}
		Collection result = new ArrayList();
		for (Iterator it = source.iterator(); it.hasNext();) {
			Object candidate = it.next();
			if (!target.contains(candidate)) {
				result.add(candidate);
			}
		}
		return result;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Collection in(Collection source, Collection target) {
		if (source == null || source.size() == 0) {
			return null;
		}
		if (target == null || target.size() == 0) {
			return null;
		}
		Collection result = new ArrayList();
		for (Iterator it = source.iterator(); it.hasNext();) {
			Object candidate = it.next();
			if (target.contains(candidate)) {
				result.add(candidate);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean allNotIn(Collection source, Collection target) {
		if (target == null || target.size() == 0) {
			return true;
		}
		for (Iterator it = source.iterator(); it.hasNext();) {
			Object candidate = it.next();
			if (target.contains(candidate)) {
				return false;
			}
		}
		return true;
	}

	public static Collection<String> ipMiddle(String startIp, String endIp) {
		// 解析起始IP地址
		String[] startIpArray = new String[4];
		int i = 0;
		StringTokenizer st = new StringTokenizer(startIp, ".");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			startIpArray[i] = token;
			i++;
		}
		int start0 = Integer.parseInt(startIpArray[0].trim());
		int start1 = Integer.parseInt(startIpArray[1].trim());
		int start2 = Integer.parseInt(startIpArray[2].trim());
		int start3 = Integer.parseInt(startIpArray[3].trim());

		// 解析结束IP地址
		String[] endIpArray = new String[4];
		int j = 0;
		st = new StringTokenizer(endIp, ".");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			endIpArray[j] = token;
			j++;
		}
		int end0 = Integer.parseInt(endIpArray[0].trim());
		int end1 = Integer.parseInt(endIpArray[1].trim());
		int end2 = Integer.parseInt(endIpArray[2].trim());
		int end3 = Integer.parseInt(endIpArray[3].trim());

		Collection<String> result = new ArrayList<String>();

		// 如果起始IP地址和结束IP地址不等
		while (!(start0 == end0 && start1 == end1 && start2 == end2 && start3 == end3)) {
			String candidate = start0 + "." + start1 + "." + start2 + "."
					+ start3;
			// 把起始地址放入集合中
			result.add(candidate);
			// 起始地址加1
			start3 = start3 + 1;
			if (start3 == 256) {
				start3 = 0;
				start2 = start2 + 1;
				if (start2 == 256) {
					start2 = 0;
					start1 = start1 + 1;
					if (start1 == 256) {
						start1 = 0;
						start0 = start0 + 1;
					}
				}
			}
		}
		// 如果退出循环，起始IP地址和结束IP地址相等
		result.add(endIp);
		return result;
	}
}
