package com.masai;

import java.sql.*;
import java.util.Scanner;

public class BookService {
	public void addBook(Scanner sc) throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			String sql = "INSERT INTO books (title, author, available_copies) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.print("Title: ");
			String title = sc.nextLine();
			System.out.print("Author: ");
			String author = sc.nextLine();
			System.out.print("Available Copies: ");
			String copiesStr = sc.nextLine();
			int copies = Integer.parseInt(copiesStr);
			ps.setString(1, title);
			ps.setString(2, author);
			ps.setInt(3, copies);
			int a = ps.executeUpdate();
			if (a == 1) {
				System.out.println("Book added successfully.");
			}
		}
	}

	public void listBooks() throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM books");
			while (rs.next()) {
				System.out.println(rs.getInt("book_id") + " | " + rs.getString("title") + " | " + rs.getString("author")
						+ " | " + rs.getInt("available_copies"));
			}
		}
	}
}
