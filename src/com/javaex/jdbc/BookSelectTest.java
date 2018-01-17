package com.javaex.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookSelectTest {
	public static void main(String[] args) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // Select 할 때만 필요함.

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection 얻어오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT b.book_id, b.title, b.pubs, to_char(b.pub_date, 'YY/MM/DD') pub_date,"
					+ " b.author_id, a.author_name, a.author_desc " + 
					" FROM book b, author a " + 
					" WHERE b.author_id = a.author_id" + 
					" ORDER BY b.book_id";
			pstmt = conn.prepareStatement(query);
			pstmt.executeQuery();
			rs = pstmt.getResultSet();
			
			
			// 4.결과처리
			System.out.println("  book_id \t book_title \t\t pubs \t\t pub_date \t author_id \t author_name \t author_desc");
			while(rs.next()) {
				int bookId = rs.getInt("book_id");
				String bookTitle = rs.getString("title");
				String pubs = rs.getString("pubs");
				String pubDate = rs.getString("pub_date");
				int authorId = rs.getInt("author_id");
				String authorName = rs.getString("author_name");
				String authorDesc = rs.getString("author_desc");
				
				System.out.format("%10d %15s %20s %30s %10d %10s %20s", bookId, bookTitle, pubs, pubDate, authorId, authorName, authorDesc);
				System.out.println();
			}

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
