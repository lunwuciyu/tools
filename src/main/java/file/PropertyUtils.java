package file;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class PropertyUtils {
	private static final Log log = LogFactory.getLog(PropertyUtils.class);
	public PropertyUtils() {
	}
	
	public static void main(String[] args){
//		Properties p = getProperties("/conf/pro.properties");
	}
	public static Properties getProperties(String fileName) {
		Properties p = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					fileName));
			p = new Properties();
			p.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return p;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map getProcMap(String tableName, String conditionSQL, String orderSQL, int offset, int length) {
		Map map = new HashMap();
		map.put("tableName", tableName);
		map.put("conditionSQL", conditionSQL);
		map.put("orderSQL", orderSQL);
		map.put("offset", Integer.valueOf(offset));
		map.put("length", Integer.valueOf(length));
		return map;
	}

	public static String addConditionSQL(String conditionSQL, String condition) {
		if (conditionSQL!=null && !conditionSQL.trim().equals("")) {
			return " AND " + condition;
		} else {
			return condition;
		}
	}

	public static String addConditionSQLLike(String conditionSQL, String column, String key) {
		if (conditionSQL!=null && !conditionSQL.trim().equals("")) {
			return " AND " + column + " LIKE '%" + key + "%'";
		} else {
			return column + " LIKE '%" + key + "%'";
		}
	}

	@SuppressWarnings("rawtypes")
	public static void copyProperties(Object dest, HashMap orig) {

		Method[] meths = dest.getClass().getMethods();
		for (int i = 0; i < meths.length; i++) {
			try {
				Method gmeth = meths[i];
				String mName = gmeth.getName();
				if (mName.indexOf("get") == 0) {
					Method smeth = null;
					String lname = mName.substring(3, mName.length());
					for (int j = 0; j < meths.length; j++) {
						if (meths[j].getName().equals("set" + lname)) {
							smeth = meths[j];
							break;
						}
					}
					if (smeth == null) {
						continue;
					}
					String formName = lname.substring(0, 1).toLowerCase() + lname.substring(1, lname.length());
					String value = (String) orig.get(formName);
					if (value != null) {
						String retName = gmeth.getReturnType().getName();
						log.debug("retName" + retName);
						if (retName.equals("java.lang.String")) {
							smeth.invoke(dest, new Object[] { value });
						} else if (retName.equals("int")) {
							log.debug("methodName" + smeth.getName() + "value:" + value);
							smeth.invoke(dest, new Object[] { new Integer(value) });
						} else if (retName.equals("float")) {
							smeth.invoke(dest, new Object[] { new Float(value) });
						} else if (retName.equals("java.util.Date") || retName.equals("java.sql.Date")) {
							java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
							try {
								java.util.Date dat = formatter.parse(value);
								smeth.invoke(dest, new Object[] { dat });
							} catch (Exception e) {
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void copyProperties(Object dest, javax.servlet.http.HttpServletRequest orig) {
		try {
			Method[] meths = dest.getClass().getMethods();
			for (int i = 0; i < meths.length; i++) {
				Method gmeth = meths[i];
				String mName = gmeth.getName();
				if (mName.indexOf("get") == 0) {
					Method smeth = null;
					String lname = mName.substring(3, mName.length());
					for (int j = 0; j < meths.length; j++) {
						if (meths[j].getName().equals("set" + lname)) {
							smeth = meths[j];
							break;
						}
					}
					if (smeth == null) {
						continue;
					}
					String formName = lname.substring(0, 1).toLowerCase() + lname.substring(1, lname.length());
					String value = orig.getParameter(formName);
					if (value != null) {
						String retName = gmeth.getReturnType().getName();
						if (retName.equals("java.lang.String")) {
							smeth.invoke(dest, new Object[] {value});
						} else
							if (retName.equals("int")) {
								smeth.invoke(dest, new Object[] {new Integer(value)});
							} else
								if (retName.equals("float")) {
									smeth.invoke(dest, new Object[] {new Float(value)});
								} else
									if (retName.equals("java.util.Date") || retName.equals("java.sql.Date")) {
										java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
										try {
											java.util.Date dat = formatter.parse(value);
											smeth.invoke(dest, new Object[] {dat});
										} catch (Exception e) {
										}
									}
					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	static public HashMap getParametersByMap(HttpServletRequest request) {
		return getParametersByMap(request, "$");
	}

	@SuppressWarnings("rawtypes")
	static public HashMap getParametersByMap(HttpServletRequest request, String delima) {
		HashMap mm = new HashMap();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			mm.put(name, request.getParameter(name));
		}
		return getParametersByMap(mm, delima);
	}

	@SuppressWarnings("rawtypes")
	static public HashMap getParametersByMap(HashMap hm, String delima) {
		List list = new ArrayList();
		if (hm.get("recordcount") != null) {
			int recordcount = Integer.parseInt((String) hm.get("recordcount"));
			for (int i = 0; i < recordcount; i++) {
				if (hm.get("recordnumber" + delima + i) != null) {
					int recordnumber = Integer.parseInt((String) hm.get("recordnumber" + delima + i));
					if (recordnumber == 1) {
						list.add("" + i);
					}
				}
			}
		}
		HashMap value = new HashMap();
		Iterator enu = hm.keySet().iterator();
		while (enu.hasNext()) {
			String n = enu.next().toString();
			String v = hm.get(n).toString();
			if (n.equals("recordcount")) {
				value.put(n, "" + list.size());
				continue;
			}
			if (n.indexOf(delima) >= 0) {
				int inx = n.indexOf(delima);
				String sub = n.substring(inx + delima.length(), n.length());
				try {
					int num = Integer.parseInt(sub);
					if (list.indexOf("" + num) >= 0) {
						n = n.substring(0, inx + delima.length()) + list.indexOf("" + num);
					}
				} catch (Exception e) {
				}
			}
			value.put(n, v);
		}
		return value;
	}

	@SuppressWarnings("rawtypes")
	static public String getParameters(HttpServletRequest request) {
		StringBuffer value = new StringBuffer();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			if (value.toString().trim().length() > 0) {
				value.append("&");
			}
			String n = (String) enu.nextElement();
			String v = request.getParameter(n);
			value.append(n).append("=").append(v);
		}
		return value.toString();
	}

	@SuppressWarnings("rawtypes")
	static public void copyPropertiesWithoutFields(Object orig, Object dest, List exts) {
		PropertyDescriptor origDescriptors[] = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(orig);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			if (org.apache.commons.beanutils.PropertyUtils.isReadable(orig, name) && org.apache.commons.beanutils.PropertyUtils.isWriteable(dest, name)) {
				try {
					Object value = org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(orig, name);
					if (!exts.contains(name))
						org.apache.commons.beanutils.BeanUtils.copyProperty(dest, name, value);
				} catch (Exception e) {
					; // Should not happen
				}
			}
		}
	}

	static public void copyProperties(Object orig, Object dest) {
		PropertyDescriptor origDescriptors[] = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(orig);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			if (org.apache.commons.beanutils.PropertyUtils.isReadable(orig, name) && org.apache.commons.beanutils.PropertyUtils.isWriteable(dest, name)) {
				try {
					Object value = org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(orig, name);
					org.apache.commons.beanutils.BeanUtils.copyProperty(dest, name, value);
				} catch (Exception e) {
					; // Should not happen
				}
			}
		}
	}
}
