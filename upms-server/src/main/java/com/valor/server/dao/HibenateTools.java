package com.valor.server.dao;

import com.google.common.collect.ImmutableMap;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by 2015.10 on 2016/4/30.
 */
public class HibenateTools {
	private static final Logger LOG = LoggerFactory.getLogger(HibenateTools.class);

	public static String getTableName(Class<?> clazz) {
		if (clazz == null)
			return "";

		Table table = clazz.getAnnotation(Table.class);
		if (table == null)
			return "";

		return table.name();
	}

	private final static Map<String, String> KEY_STR_MAP = new HashMap<>();

	public static String getTableKeyColumnsStr(Class<?> clazz) {
		if (clazz == null)
			return "";

		String clsName = clazz.getCanonicalName();
		String keyStr = KEY_STR_MAP.get(clsName);
		if (keyStr == null) {
			synchronized (KEY_STR_MAP) {
				keyStr = KEY_STR_MAP.get(clsName);
				if (keyStr == null) {
					keyStr = "";
					List<String> keys = getTableKeyColumnNames(clazz);
					for (String key : keys) {
						keyStr = keyStr + "`" + key + "`,";
					}
					if (keyStr.endsWith(","))
						keyStr = keyStr.substring(0, keyStr.length() - 1);
					KEY_STR_MAP.put(clsName, keyStr);
				}
			}
		}

		return keyStr;
	}

	private final static Map<String, List<String>> AUTO_KEY_MAP = new HashMap<>();

	public static List<String> getTableAutoKeyColumnsName(Class<?> clazz) {
		if (clazz == null)
			return new ArrayList<>();
		String clsName = clazz.getCanonicalName();

		List<String> key = AUTO_KEY_MAP.get(clsName);
		if (key == null) {
			synchronized (AUTO_KEY_MAP) {
				key = AUTO_KEY_MAP.get(clsName);
				if (key == null) {
					key = new ArrayList<>();
					List<ColumnInfo> columnInfos = getColumnInfos(clazz);
					for (ColumnInfo columnInfo : columnInfos) {
						if (columnInfo.isId() && columnInfo.isGenerated()) {
							key.add(columnInfo.getColumnName());
						}
					}
					AUTO_KEY_MAP.put(clsName, key);
				}
			}
		}

		return key;
	}

	private final static Map<String, List<String>> KEY_CN_MAP = new HashMap<>();

	public static List<String> getTableKeyColumnNames(Class<?> clazz) {
		if (clazz == null)
			return new ArrayList<>();
		String clsName = clazz.getCanonicalName();

		List<String> keyColumnNames = KEY_CN_MAP.get(clsName);
		if (keyColumnNames == null) {
			synchronized (KEY_CN_MAP) {
				keyColumnNames = KEY_CN_MAP.get(clsName);
				if (keyColumnNames == null) {
					keyColumnNames = new ArrayList<>();
					List<ColumnInfo> columnInfos = getColumnInfos(clazz);
					for (ColumnInfo columnInfo : columnInfos) {
						if (columnInfo.isId()) {
							keyColumnNames.add(columnInfo.getColumnName());
						}
					}
					KEY_CN_MAP.put(clsName, keyColumnNames);
				}
			}
		}
		return keyColumnNames;
	}

	private final static Map<String, List<ColumnInfo>> KEY_COLUMN_MAP = new HashMap<>();

	public static List<ColumnInfo> getTableKeyColumnInfos(Class<?> clazz) {
		if (clazz == null)
			return new ArrayList<>();
		String clsName = clazz.getCanonicalName();

		List<ColumnInfo> keyColumnInfos = KEY_COLUMN_MAP.get(clsName);
		if (keyColumnInfos == null) {
			synchronized (KEY_COLUMN_MAP) {
				keyColumnInfos = KEY_COLUMN_MAP.get(clsName);
				if (keyColumnInfos == null) {
					keyColumnInfos = new ArrayList<>();
					List<ColumnInfo> columnInfos = getColumnInfos(clazz);
					for (ColumnInfo columnInfo : columnInfos) {
						if (columnInfo.isId()) {
							keyColumnInfos.add(columnInfo);
						}
					}
					KEY_COLUMN_MAP.put(clsName, keyColumnInfos);
				}
			}
		}
		return keyColumnInfos;
	}

	public interface ColumnInfo {
		Method getMethod();

		String getColumnName();

		String getAttributeName();

		int getLength();

		boolean isId();

		boolean isGenerated();
	}

	private final static Map<String, Map<String, ColumnInfo>> MAP_MAP = new HashMap<>();

	public static Map<String, ColumnInfo> getColumnNamesMap(Class<?> clazz) {
		String key = clazz.getCanonicalName();
		Map<String, ColumnInfo> map = MAP_MAP.get(key);
		if (map == null) {
			synchronized (MAP_MAP) {
				map = MAP_MAP.get(key);
				if (map == null) {
					map = new HashMap<>();
					List<ColumnInfo> columnInfos = getColumnInfos(clazz);
					for (ColumnInfo columnInfo : columnInfos) {
						map.put(columnInfo.getColumnName(), columnInfo);
					}
					MAP_MAP.put(key, map);
				}
			}
		}
		return map;
	}

	private final static Map<String, List<ColumnInfo>> LIST_MAP = new HashMap<>();

	public static List<ColumnInfo> getColumnInfos(Class<?> clazz) {
		if (clazz == null)
			return new ArrayList<>();

		String key = clazz.getCanonicalName();
		List<ColumnInfo> columnNames = LIST_MAP.get(key);
		if (columnNames == null) {
			synchronized (LIST_MAP) {
				columnNames = LIST_MAP.get(key);
				if (columnNames == null) {
					columnNames = new ArrayList<>();

					Method[] methods = clazz.getMethods();
					for (Method method : methods) {
						Transient t = method.getAnnotation(Transient.class);
						if (t != null)
							continue;

						Column c = method.getAnnotation(Column.class);
						Id id = method.getAnnotation(Id.class);
						GeneratedValue generatedValue = method.getAnnotation(GeneratedValue.class);

						if (c != null) {
							final String attibuteName = getAttributeName(method);
							ColumnInfo columnInfo = new ColumnInfo() {
								@Override
								public Method getMethod() {
									return method;
								}

								@Override
								public String getColumnName() {
									return c.name();
								}

								@Override
								public String getAttributeName() {
									return attibuteName;
								}

								@Override
								public int getLength() {
									return c.length();
								}

								@Override
								public boolean isId() {
									return id != null;
								}

								@Override
								public boolean isGenerated() {
									return generatedValue != null;
								}
							};

							columnNames.add(columnInfo);
						} else if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
							final String attibuteName = getAttributeName(method);
							ColumnInfo columnInfo = new ColumnInfo() {
								@Override
								public Method getMethod() {
									return method;
								}

								@Override
								public String getColumnName() {
									return attibuteName;
								}

								@Override
								public String getAttributeName() {
									return attibuteName;
								}

								@Override
								public int getLength() {
									return c.length();
								}

								@Override
								public boolean isId() {
									return id != null;
								}

								@Override
								public boolean isGenerated() {
									return generatedValue != null;
								}
							};

							columnNames.add(columnInfo);
						}
						// JoinColumn jc =
						// method.getAnnotation(JoinColumn.class);
					}

					Collections.sort(columnNames, new Comparator<ColumnInfo>() {
						@Override
						public int compare(ColumnInfo o1, ColumnInfo o2) {
							return o1.getColumnName().compareTo(o2.getColumnName());
						}
					});

					LIST_MAP.put(key, columnNames);
				}
			}
		}

		return columnNames;
	}

	public static void copy(Object srcObject, Class srcClass, Object dstObject, Class dstClass,
			List<String> ignoredColumns) {
		List<ColumnInfo> columnInfos = HibenateTools.getColumnInfos(srcClass);
		for (HibenateTools.ColumnInfo columnInfo : columnInfos) {
			if (ignoredColumns != null && ignoredColumns.contains(columnInfo.getColumnName()))
				continue;

			Method method = columnInfo.getMethod();
			String setMethodName = "set" + method.getName().substring(3);
			try {
				Method setMethod = dstClass.getMethod(setMethodName, method.getReturnType());
				try {
					Object value = method.invoke(srcObject);
					setMethod.invoke(dstObject, value);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					LOG.trace(e.getMessage(), e);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				LOG.trace(e.getMessage(), e);
			}
		}
	}

	private static String getAttributeName(Method method) {
		String str = method.getName().substring(3);
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	private final static Map<Class, BasicType> TYPE_MAP = ImmutableMap.<Class, BasicType> builder()
			.put(Short.class, ShortType.INSTANCE).put(Integer.class, IntegerType.INSTANCE)
			.put(Long.class, LongType.INSTANCE).put(String.class, StringType.INSTANCE)
			.put(Date.class, DateType.INSTANCE).put(Boolean.class, BooleanType.INSTANCE)
			.put(BigDecimal.class, BigDecimalType.INSTANCE).put(BigInteger.class, BigIntegerType.INSTANCE)
			.put(Double.class, DoubleType.INSTANCE).put(Float.class, FloatType.INSTANCE)
			.put(int.class, IntegerType.INSTANCE).put(long.class, LongType.INSTANCE)
			.put(double.class, DoubleType.INSTANCE).put(float.class, FloatType.INSTANCE)
			.put(boolean.class, BooleanType.INSTANCE).put(short.class, ShortType.INSTANCE)
			.put(char.class, StringType.INSTANCE).put(byte.class, ByteType.INSTANCE).build();

	public static BasicType getBasicType(Class javaType) {
		if (javaType == null)
			return null;

		return TYPE_MAP.get(javaType);
	}
}
