package com.telus.cmb.common.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is copied from com.telus.eas.framework.utility.Utility
 * @author tongts
 *
 */
public class DatabaseUtil {

	public static void setNullable(PreparedStatement ps, int index, int sqlType, boolean value) throws SQLException {
		ps.setBoolean(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, byte value) throws SQLException {
		ps.setByte(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, short value) throws SQLException {
		ps.setShort(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, int value) throws SQLException {
		ps.setInt(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, char value) throws SQLException {
		ps.setString(index, String.valueOf(value));
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, long value) throws SQLException {
		ps.setLong(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, float value) throws SQLException {
		ps.setFloat(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, double value) throws SQLException {
		ps.setDouble(index, value);
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, String value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setString(index, value);
		}
	}
	
	public static void setNullable(PreparedStatement ps, int index, int sqlType, Double value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setDouble(index, value.doubleValue());
		}
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, java.sql.Date value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setDate(index, value);
		}
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, java.sql.Time value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setTime(index, value);
		}
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, java.sql.Timestamp value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setTimestamp(index, value);
		}
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, java.math.BigDecimal value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setBigDecimal(index, value);
		}
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, byte[] value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setBytes(index, value);
		}
	}

	public static void setNullable(PreparedStatement ps, int index, int sqlType, Object value) throws SQLException {
		if (value == null) {
			ps.setNull(index, sqlType);
		} else {
			ps.setObject(index, value);
		}
	}

	public static int compare(Object o1, Object o2) {
		if (o1 == o2) {
			return 0;
		} else if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			return o1.toString().compareToIgnoreCase(o2.toString());
		}
	}
}
