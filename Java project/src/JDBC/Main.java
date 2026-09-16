package JDBC;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        
        // Pre-populating test data (Will handle key uniqueness implicitly now)
        library.addBook(new Book("B101", "Introduction to Java", "James Gosling"));
        library.addBook(new Book("B102", "Clean Code", "Robert C. Martin"));
        library.registerStudent(new Student("S001", "Alex Smith"));

        while (true) {
            System.out.println("\n===== COLLEGE LIBRARY SYSTEM =====");
            System.out.println("1. Add a Book");
            System.out.println("2. Register a Student");
            System.out.println("3. Display All Books");
            System.out.println("4. Issue a Book");
            System.out.println("5. Return a Book");
            System.out.println("6. Exit System");
            System.out.print("Select an option (1-6): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear scanner buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    String bId = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    library.addBook(new Book(bId, title, author));
                    break;
                case 2:
                    System.out.print("Enter Student ID: ");
                    String sId = scanner.nextLine();
                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine();
                    library.registerStudent(new Student(sId, name));
                    break;
                case 3:
                    library.displayBooks();
                    break;
                case 4:
                    System.out.print("Enter Student ID: ");
                    String issueSId = scanner.nextLine();
                    System.out.print("Enter Book ID: ");
                    String issueBId = scanner.nextLine();
                    library.issueBook(issueSId, issueBId);
                    break;
                case 5:
                    System.out.print("Enter Student ID: ");
                    String retSId = scanner.nextLine();
                    System.out.print("Enter Book ID: ");
                    String retBId = scanner.nextLine();
                    library.returnBook(retSId, retBId);
                    break;
                case 6:
                    System.out.println("Shutting down library portal. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}