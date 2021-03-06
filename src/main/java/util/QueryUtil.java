/**
 * @ProjectName: 公安部出入境综合数据应用视频监控系统项目
 * @Copyright: 2015 中华人民共和国公安部 All Rights Reserved .
 * @address: http://www.mps.gov.cn
 * @date: 2015-8-8 上午9:53:10
 * @Description: 本内容仅限于中华人民共和国公安部内部使用，禁止转发.
 */
package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


/**
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2015-8-8
 * @modify by reason:{方法名}:{原因}
 */
public class QueryUtil {
	/**
	 * 解决Oracle中in的参数列表长度超过1000的问题
	 * 
	 * @param length
	 *            每个分段的长度，
	 * @param paramsList
	 *            待拆分的参数列表
	 * @return 拆分后的分段列表
	 */
	public static <T> List<List<T>> splitInParams(int length, List<T> paramsList) {
		if (length<1||paramsList == null || paramsList.size() == 0)
			return null;
		int size = paramsList.size();
		List<List<T>> list = new ArrayList<List<T>>();
		int d = (int) Math.ceil(size / (length+0.0));
		for (int i = 0; i < d; i++) {
			int fromIndex = length * i;
			int toIndex = Math.min(fromIndex + length, size);
			list.add(paramsList.subList(fromIndex, toIndex));
		}
		return list;
	}
	
	
	/**
	 * 解决Oracle中in的参数列表长度超过1000的问题
	 * 返回拼接的SQL语句
	 * eg:
	 * 返回的SQL语句片段：XX IN (.......) OR XX IN (......)这样的形式
	 * @param paramName  需要进行in查询的查询参数的名称
	 * @param length     每个分段的长度，对于Oracle，一般设置为800-1000
	 * @param paramsList 待拆分的参数列表
	 * @return 拼接的SQL语句片段
	 */
	public static <T> String getSQLInParamsSplit(String paramName,int length,List<T> paramsList){
		if(length<1||paramName==null||paramsList==null||paramsList.size()==0)
			return null;
		List<List<T>> list = splitInParams(length, paramsList);
		StringBuilder sb = new StringBuilder();
		String temp = list.get(0).toString();
		//由于List的toString方法返回的是[....]形式，需要去掉开头和结尾的中括号
		sb.append(paramName).append(" IN ("+temp.subSequence(1,	temp.length()-1)+") ");
		int size = list.size();
		for(int i=1;i<size;i++){
			temp = list.get(i).toString();
			sb.append(" OR "+paramName+" IN ("+temp.subSequence(1,	temp.length()-1)+") ");
		}
		return sb.toString();
	}
	
	
	
	/**
	 * 解决Oracle中in的参数列表长度超过1000的问题
	 * 返回命名参数查询的HQL语句片段和对应的参数名和值的Map
	 * eg:
	 * 返回的HQL语句片段：id IN (:id0) OR id IN (:id1) OR id IN (:id2) OR id IN (:id3)，命名参数的名称是以paramName加上序号
	 * @param paramName  需要进行in查询的查询参数的名称
	 * @param length     每个分段的长度，对于Oracle，一般设置为800-1000
	 * @param paramsList 待拆分的参数列表
	 * @return 返回一个长度为2的Object数组，
	 * 第一个元素是还有in的HQL语句片段，
	 * 第二个元素是Map<String,Object>，其中String是参数名，Object是参数值的列表
	 */
	public static <T> Object[] getHQLInParamsSplit(String paramName,int length,List<T> paramsList){
		if(length<1||paramName==null||paramsList==null||paramsList.size()==0)
			return null;
		List<List<T>> list = splitInParams(length, paramsList);
	    Map<String, Object> tempParamsValues = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		String tempParamName = paramName+0;
		sb.append(paramName).append(" IN (:").append(tempParamName).append(")");
		tempParamsValues.put(tempParamName, list.get(0));
		int size = list.size();
		for(int i=1;i<size;i++){
			tempParamName = paramName+i; 
			sb.append(" OR ").append(paramName).append(" IN (:").append(tempParamName).append(")");
			tempParamsValues.put(tempParamName, list.get(i));	
		}
		Object[] hqlAndParamsMap = new Object[2];
		hqlAndParamsMap[0]=sb.toString();
		hqlAndParamsMap[1]=tempParamsValues;
		return hqlAndParamsMap;
	}
	
	

	
	/**
	 * 解决Oracle中in的参数列表长度超过1000的问题
	 * 获取拆分in参数列表后的Criterion
	 * @param paramName  需要进行in查询的查询参数的名称
	 * @param length     每个分段的长度，对于Oracle，一般设置为800-1000
	 * @param paramsList 待拆分的参数列表
	 * @return 含有嵌套or的Criterion
	 */
	public static <T> Criterion getCriterionInParamsSplit(String paramName,int length,List<T> paramsList){
		if(length<1||paramName==null||paramsList==null||paramsList.size()==0)
			return null;
		List<List<T>> list = splitInParams(length, paramsList);
		Criterion criterion = Restrictions.in(paramName, list.get(0));
		int size = list.size();
		for(int i=1;i<size;i++)
			criterion=Restrictions.or(criterion, Restrictions.in(paramName, list.get(i)));
		return criterion;
	}
	
}
