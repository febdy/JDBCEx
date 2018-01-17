package com.javaex.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectHR {
	public static void main(String[] args) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // Select 할 때만 필요함.
		int cnt = 0;

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection 얻어오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "hr", "hr");

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT em.employee_id, em.last_name, to_char(em.hire_date, 'YYYY-MM-DD') hire_date\r\n" + 
					"FROM employees em, employees ma\r\n" + 
					"WHERE em.manager_id = ma.employee_id\r\n" + 
					"AND em.hire_date < ma.hire_date";
			pstmt = conn.prepareStatement(query);
			pstmt.executeQuery();
			rs = pstmt.getResultSet();

			// 4.결과처리
			System.out.println(
					"  employee_id \t last_name \t hire_date");
			while (rs.next()) {
				int employeeId = rs.getInt("employee_id");
				String lastName = rs.getString("last_name");
				String hireDate = rs.getString("hire_date");

				System.out.format("%10d %15s %15s", employeeId, lastName, hireDate);
				System.out.println();
				cnt++;
			}
			
			System.out.println(cnt + "건이 발견됨.");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {

				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
	}
}
