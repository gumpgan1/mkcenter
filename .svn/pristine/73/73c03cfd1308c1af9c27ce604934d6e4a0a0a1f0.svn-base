/**
 * @author Chnyge Lin
 */
package com.mktech.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chnyge Lin
 * 
 */
public class ObjectUtil {
	/**
	 * 返回一个对象的属性和属性值
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getProperty(Object entityName) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Class c = entityName.getClass();
			// 获得对象属性
			Field field[] = c.getDeclaredFields();
			for (Field f : field) {
				Object v = invokeMethod(entityName, f.getName(), null);
				map.put(f.getName(), v.toString());
			}
		} catch (Exception e) {
			map = null;
		}
		return map;
	}

	/**
	 * 获得对象属性的值
	 */
	@SuppressWarnings("unchecked")
	private static Object invokeMethod(Object owner, String methodName,
			Object[] args) throws Exception {
		Class ownerClass = owner.getClass();
		methodName = methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}
		return method.invoke(owner);
	}

	/**
	 * 反射获取实体内所有属性值
	 * this,this.getclass()
	 * 
	 * @param o
	 * @param c
	 * @return
	 */
	public static String getString(Object o, Class<?> c) {
		String result = c.getSimpleName() + ":";
//		// 获取父类，判断是否为实体类
//		if (c.getSuperclass().getName().indexOf("domain") >= 0) {
//			result += "\n<" + getString(o, c.getSuperclass()) + ">,\n";
//		}
		// 获取类中的所有定义字段
		Field[] fields = c.getDeclaredFields();
		// 循环遍历字段，获取字段对应的属性值
		for (Field field : fields) {
			// 如果不为空，设置可见性，然后返回
			field.setAccessible(true);
			try {
				// 设置字段可见，即可用get方法获取属性值。
				result += field.getName() + "=" + field.get(o) + ",\n";
			} catch (Exception e) {
				// System.out.println("error--------"+methodName+".Reason is:"+e.getMessage());
			}
		}
		if (result.indexOf(",") >= 0)
			result = result.substring(0, result.length() - 2);
		return result;
	}
}
