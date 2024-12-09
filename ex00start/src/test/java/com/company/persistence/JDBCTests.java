package com.company.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import lombok.extern.log4j.Log4j;

@Log4j
public class JDBCTests {
	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testConnection() {
		
		try(Connection con = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:XE",
				"kmr",
				"kmr"
				)) {
			log.info(con);      /* 콘솔 로그 */
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
