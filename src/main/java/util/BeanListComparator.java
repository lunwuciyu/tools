package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 对List<T>类型的对象进行排序的比较器<br>
 * 使用例：
 * 
 * <pre>
 * BeanListComparator&lt;Map&lt;String, String&gt;&gt; comparator = 
 *      new BeanListComparator&lt;Map&lt;String, String&gt;&gt;();
 * comparator.addSortkey(&quot;key1&quot;, SortType.ASC, 1);
 * comparator.addSortkey(&quot;key2&quot;, SortType.DESC, 2);
 * Collections.sort(testList, comparator);
 * </pre>
 * 
 * @param <T> 列表对象中元素的类型
 */
public class BeanListComparator<T> implements Comparator<T> {

	private Collator collator = Collator.getInstance(java.util.Locale.CHINA);
    /**
     * 保存排序字段名的结构体
     */
    public static class SortKey {
        /**
         * 字段名
         */
        private String name;

        /**
         * ASC:升序、DESC:降序
         */
        private SortType order;

        /**
         * 排序字段的优先顺序
         */
        private int index;

        /**
         * 构造保存排序字段名的结构体
         * 
         * @param name 字段名
         * @param order 升序、降序
         * @param index 优先顺序
         */
        SortKey(String name, SortType order, int index) {
            this.name = name;
            this.order = order;
            this.index = index;
        }

        /**
         * 获取优先顺序
         * 
         * @return 优先顺序
         */
        public int getIndex() {
            return index;
        }

        /**
         * 设置优先顺序
         * 
         * @param index 优先顺序
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * 获取字段名称
         * 
         * @return 字段名称
         */
        public String getName() {
            return name;
        }

        /**
         * 设置字段名称
         * 
         * @param name 字段名称
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取排序方式
         * 
         * @return 排序方式
         */
        public SortType getOrder() {
            return order;
        }

        /**
         * 设置排序方式
         * 
         * @param order 排序方式
         */
        public void setOrder(SortType order) {
            this.order = order;
        }
    }

    /**
     * 排序方式
     */
    public static enum SortType {
        /**
         * 升序
         */
        ASC,

        /**
         * 降序
         */
        DESC,
    }

    /**
     * 保存排序字段的列表
     */
    private List<SortKey> sortkeyList;

    /**
     * 构造比较器
     */
    public BeanListComparator() {
        // 初始化排序字段列表
        sortkeyList = new ArrayList<SortKey>();
    }

    /**
     * 添加排序字段
     * 
     * @param name 字段名称
     * @param order 排序方式
     */
    public void addSortkey(String name, SortType order) {
        int index = sortkeyList.size() + 1;
        addSortkey(name, order, index);
    }

    /**
     * 添加排序字段
     * 
     * @param name 字段名称
     * @param order 排序方式
     * @param index 优先顺序
     */
    public void addSortkey(String name, SortType order, int index) {
        sortkeyList.add(new SortKey(name, order, index));
    }
    
    /**
     * 添加排序字段
     * 
     * @param name 字段名称
     * @param order 排序方式
     * @param index 优先顺序
     */
    public void addSortkey(String name, SortType order, int index,Class<T> clazz) {
    	
		Field[] fields = clazz.getDeclaredFields();
		String orderColumn = name;
		for(Field field : fields){
			if(field.isAnnotationPresent(SortMapping.class)){
				SortMapping sortMapping = field.getAnnotation(SortMapping.class);
				if(sortMapping.name().contains(",")){
					String[] names = sortMapping.name().split(",");
					for(int i=0;i<names.length;i++){
						if(names[i].equals(name)){
							orderColumn = sortMapping.orderColumn().split(",")[i];
						}
					}
				}else{
					if(sortMapping.name().equals(name)){
						orderColumn = sortMapping.orderColumn();
					}
				}
			}
		}
    	
    	sortkeyList.add(new SortKey(orderColumn, order, index));
    }

    /*
     * (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(T row1, T row2) {

        int result = 0;

        // 根据优先顺序将排序字段进行排序
        // null作为最大(正常情况下key不应该为null)
        Collections.sort(sortkeyList, new Comparator<SortKey>() {
            public int compare(SortKey key1, SortKey key2) {
                if (key1 != null && key2 != null) {
                    return key1.getIndex() - key2.getIndex();
                } else if (key1 == null) {
                    return 1;
                } else if (key2 == null) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        // 对作为List元素的Bean对象进行比较
        for (SortKey key : sortkeyList) {
            if (key != null) {
                //根据排序字段取得两个Bean对象的对应值并进行比较
                int value = compareTo(getBeanProperty(row1, key.getName()), 
                        getBeanProperty(row2, key.getName()));
                
                if (value == 0) {
					//该字段值相等时进行下一个字段的比较
                    continue;
                } else {
                    if (key.getOrder() == SortType.ASC) {
                        // 升序时
                        result = value;
                        break;
                    } else {
                        // 降序时
                        result = value * -1;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 比较两个对象（按字符串或数值比较）
     * 
     * @param o1 对象1
     * @param o2 对象2
     * @return -1, 0, or 1
     */
    private int compareTo(Object o1, Object o2) {
        String str1 = null;
        String str2 = null;

        // null时作为空字符串处理
        if (o1 == null) {
            str1 = "";
        }
        if (o2 == null) {
            str2 = "";
        }
        str1 = String.valueOf(o1);
        str2 = String.valueOf(o2);

        if (o1 instanceof Number && o2 instanceof Number) {
            // 双方都是数值类型时
            BigDecimal num1 = new BigDecimal(str1);
            BigDecimal num2 = new BigDecimal(str2);
            return num1.compareTo(num2);
        } else {
            // 数值以外时都按字符串比较
        	// 数值以外时都按字符串比较
        	CollationKey key1 = collator.getCollationKey(str1);
        	CollationKey key2 = collator.getCollationKey(str2);
            return key1.compareTo(key2);
            //return str1.toLowerCase().compareTo(str2.toLowerCase());
        }
    }
    
    /**
     * 根据属性名获取Bean对象的属性值<br>
     * 无法获取时返回null
     * 
     * @param bean Bean对象
     * @param name 属性名
     * @return 属性值
     */
    private Object getBeanProperty(Object bean, String name) {
        Object value = null;
        try {
        	if(name.contains(".")){//对象类型
        		String[] objAttr = name.split("\\.");
        		Object obj = PropertyUtils.getProperty(bean, objAttr[0]);
        		value = PropertyUtils.getProperty(obj, objAttr[1]);
        	}else{
        		value = PropertyUtils.getProperty(bean, name);
        	}
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        }
        return value;
    }

    /**
     * 获取排序字段列表
     * 
     * @return 排序字段列表
     */
    public List<SortKey> getSortkeyList() {
        return sortkeyList;
    }

    /**
     * 设置排序字段列表
     * 
     * @param sortkeyList 排序字段列表
     */
    public void setSortkeyList(List<SortKey> sortkeyList) {
        this.sortkeyList = sortkeyList;
    }

}
