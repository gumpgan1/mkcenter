/**
 * @author Chnyge Lin
 */
package com.mybatis.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mktech.utils.ObjectUtil;


/**
 * @author Chnyge Lin
 * 
 */
public class MysqlCreationRun {
	// @Resource
	private JdbcTemplate jdbcTemplate;

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://10.11.112.202/SHUINI";

	// Database credentials
	static final String USER = "user";
	static final String PASS = "123456";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		// -----parameter------
		int num = 145;
		String tableName = "db_jiangyin";
		// -----------
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		for (int i = 1; i <= num; i++) {
			map.put(i, new String("L" + String.format("%04d", i)));
		}
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE IF NOT EXISTS `" + tableName.toUpperCase()
				+ "` (");
		sb.append(" `ID` int(11) NOT NULL AUTO_INCREMENT,");
		sb.append(" `TIMESTAMP` varchar(255) NOT NULL DEFAULT '',");
		for (String key : map.values()) {

			sb.append("`" + key + "` double DEFAULT NULL,");
		}
		sb.append(" PRIMARY KEY (`id`)");
		sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		System.out.println(sb.toString());
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();
			 stmt.executeUpdate(sb.toString());
			System.out.println("Created success!");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
	}

}
