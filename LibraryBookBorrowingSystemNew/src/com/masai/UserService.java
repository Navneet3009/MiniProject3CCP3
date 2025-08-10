package com.masai;

import java.sql.*;
import java.util.Scanner;

public class UserService {
	public void addUser(Scanner sc) throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.print("Name: ");
			ps.setString(1, sc.nextLine());
			System.out.print("Email: ");
			ps.setString(2, sc.nextLine());
			int a = ps.executeUpdate();
			if (a == 1) {
				System.out.println("User added successfully.");
			}
		}
	}

	public void listUsers() throws SQLException {
		try (Connection conn = DBUtils.getConnection()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM users");
			while (rs.next()) {
				System.out.println(rs.getInt("user_id") + " | " + rs.getString("name") + " | " + rs.getString("email"));
			}
		}
	}
}
