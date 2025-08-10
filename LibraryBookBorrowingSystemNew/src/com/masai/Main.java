package com.masai;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws SQLException {
		BookService bookService = new BookService();
		UserService userService = new UserService();
		BorrowService borrowService = new BorrowService();
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println(
					"\n1. Add User\n2. Add Book\n3. Borrow Book\n4. Return Book\n5. List Books\n6. List Users\n7. User Borrowed Books\n8. Unreturned Books\n9. Borrow History\n0. Exit");
			int choice = Integer.parseInt(sc.nextLine());
			switch (choice) {
			case 1:
				userService.addUser(sc);
				break;

			case 2:
				bookService.addBook(sc);
				break;
			case 3:
				borrowService.borrowBook(sc);
				break;
			case 4:
				borrowService.returnBook(sc);
				break;
			case 5:
				bookService.listBooks();
				break;
			case 6:
				userService.listUsers();
				break;
			case 7:
				borrowService.listBorrowedBooksByUser(sc);
				break;
			case 8:
				borrowService.listUnreturnedBooks();
				break;
			case 9:
				borrowService.borrowHistory();
				break;
			case 0:
				System.exit(0);
			}
		}
	}
}
