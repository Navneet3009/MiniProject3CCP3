package com.masai;

import java.sql.*;
import java.util.Scanner;

public class BorrowService {
	public void borrowBook(Scanner sc) throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			System.out.print("User ID: ");
			int userId = Integer.parseInt(sc.nextLine().trim());
			System.out.print("Book ID: ");
			int bookId = Integer.parseInt(sc.nextLine().trim());

			String checkSql = "SELECT available_copies FROM books WHERE book_id=?";
			PreparedStatement checkStmt = conn.prepareStatement(checkSql);
			checkStmt.setInt(1, bookId);
			ResultSet rs = checkStmt.executeQuery();

			if (rs.next() && rs.getInt("available_copies") > 0) {
				String borrowSql = "INSERT INTO borrowed_books (user_id, book_id, borrow_date) VALUES (?, ?, CURDATE())";
				PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
				borrowStmt.setInt(1, userId);
				borrowStmt.setInt(2, bookId);
				borrowStmt.executeUpdate();

				String updateSql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id=?";
				PreparedStatement updateStmt = conn.prepareStatement(updateSql);
				updateStmt.setInt(1, bookId);
				updateStmt.executeUpdate();
			} else {
				System.out.println("Book not available or doesn't exist.");
			}
		}
	}
	
	public void returnBook(Scanner sc) throws SQLException {
	    try (Connection conn = DBUtils.getConnection()) {
	        System.out.print("Borrow ID: ");
	        int borrowId = Integer.parseInt(sc.nextLine().trim());

	        String getBookSql = "SELECT book_id FROM borrowed_books WHERE borrow_id=? AND return_date IS NULL";
	        PreparedStatement getBookStmt = conn.prepareStatement(getBookSql);
	        getBookStmt.setInt(1, borrowId);
	        ResultSet rs = getBookStmt.executeQuery();

	        if (rs.next()) {
	            int bookId = rs.getInt("book_id");

	            System.out.println("Valid borrow ID found. Returning book...");

	            String updateBorrowSql = "UPDATE borrowed_books SET return_date = CURDATE() WHERE borrow_id=?";
	            PreparedStatement updateBorrowStmt = conn.prepareStatement(updateBorrowSql);
	            updateBorrowStmt.setInt(1, borrowId);
	            updateBorrowStmt.executeUpdate();

	            String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id=?";
	            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
	            updateBookStmt.setInt(1, bookId);
	            updateBookStmt.executeUpdate();

	            System.out.println("Book returned successfully!");
	        } else {
	            System.out.println("❌ No record found with this Borrow ID and NULL return_date.");
	        }
	    }
	}




	public void listBorrowedBooksByUser(Scanner sc) throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			System.out.print("User ID: ");
			int userId = sc.nextInt();
			String sql = "SELECT b.title, bb.borrow_date, bb.return_date FROM borrowed_books bb JOIN books b ON bb.book_id = b.book_id WHERE bb.user_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(
						rs.getString("title") + " | " + rs.getDate("borrow_date") + " | " + rs.getDate("return_date"));
			}
		}
	}

	public void listUnreturnedBooks() throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			String sql = "SELECT name, email FROM users WHERE user_id IN (SELECT user_id FROM borrowed_books WHERE return_date IS NULL)";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("name") + " | " + rs.getString("email"));
			}
		}
	}

	public void borrowHistory() throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			String sql = "SELECT u.name, b.title, bb.borrow_date, bb.return_date FROM borrowed_books bb JOIN users u ON bb.user_id = u.user_id JOIN books b ON bb.book_id = b.book_id";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("name") + " | " + rs.getString("title") + " | "
						+ rs.getDate("borrow_date") + " | " + rs.getDate("return_date"));
			}
		}
	}
}
