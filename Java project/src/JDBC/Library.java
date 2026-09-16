package JDBC;


import java.sql.*;

public class Library {

    public void addBook(Book book) {
        String query = "INSERT INTO books (book_id, title, author, is_issued) VALUES (?, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE title=?, author=?";
        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, book.getBookId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setBoolean(4, book.isIssued());
            pstmt.setString(5, book.getTitle());
            pstmt.setString(6, book.getAuthor());
            
            pstmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    public void registerStudent(Student student) {
        String query = "INSERT INTO students (student_id, name) VALUES (?, ?) "
                     + "ON DUPLICATE KEY UPDATE name=?";
        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getName());
            
            pstmt.executeUpdate();
            System.out.println("Student registered successfully!");
        } catch (SQLException e) {
            System.out.println("Error registering student: " + e.getMessage());
        }
    }

    public void displayBooks() {
        String query = "SELECT * FROM books";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\n--- Library Books ---");
            boolean trackingFlag = false;
            while (rs.next()) {
                trackingFlag = true;
                String bId = rs.getString("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean isIssued = rs.getBoolean("is_issued");
                System.out.println("[" + bId + "] " + title + " by " + author + " (" + (isIssued ? "Issued" : "Available") + ")");
            }
            if (!trackingFlag) {
                System.out.println("No books available in the library.");
            }
        } catch (SQLException e) {
            System.out.println("Error loading catalog inventory: " + e.getMessage());
        }
    }

    public void issueBook(String studentId, String bookId) {
        String checkStudent = "SELECT name FROM students WHERE student_id = ?";
        String checkBook = "SELECT title, is_issued FROM books WHERE book_id = ?";
        String insertIssue = "INSERT INTO borrowed_books (student_id, book_id) VALUES (?, ?)";
        String updateBookStatus = "UPDATE books SET is_issued = TRUE WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Enable manual transaction block execution

            try (java.sql.PreparedStatement psStu = conn.prepareStatement(checkStudent);
            	 java.sql.PreparedStatement psBook = conn.prepareStatement(checkBook);
                 java.sql.PreparedStatement psIssue = conn.prepareStatement(insertIssue);
                 java.sql.PreparedStatement psUpBook = conn.prepareStatement(updateBookStatus)) {

                // 1. Verify Student
                psStu.setString(1, studentId);
                ResultSet rsStu = psStu.executeQuery();
                if (!rsStu.next()) {
                    System.out.println(" Student not found.");
                    conn.rollback();
                    return;
                }
                String studentName = rsStu.getString("name");

                // 2. Verify Book
                psBook.setString(1, bookId);
                ResultSet rsBook = psBook.executeQuery();
                if (!rsBook.next()) {
                    System.out.println("Book not found.");
                    conn.rollback();
                    return;
                }
                String bookTitle = rsBook.getString("title");
                boolean matchesStatus = rsBook.getBoolean("is_issued");

                if (matchesStatus) {
                    System.out.println("Book is already issued to someone else.");
                    conn.rollback();
                    return;
                }

                // 3. Issue Book
                psIssue.setString(1, studentId);
                psIssue.setString(2, bookId);
                psIssue.executeUpdate();

                // 4. Set book flag
                psUpBook.setString(1, bookId);
                psUpBook.executeUpdate();

                conn.commit();
                System.out.println("Success! '" + bookTitle + "' issued to " + studentName + ".");

            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Transaction rolled back: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database connectivity error: " + e.getMessage());
        }
    }

    public void returnBook(String studentId, String bookId) {
        String checkMatch = "SELECT * FROM borrowed_books WHERE student_id = ? AND book_id = ?";
        String deleteIssue = "DELETE FROM borrowed_books WHERE student_id = ? AND book_id = ?";
        String updateBookStatus = "UPDATE books SET is_issued = FALSE WHERE book_id = ?";
        String getTitle = "SELECT title FROM books WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (java.sql.PreparedStatement psMatch = conn.prepareStatement(checkMatch);
                 java.sql.PreparedStatement psDel = conn.prepareStatement(deleteIssue);
                 java.sql.PreparedStatement psUp = conn.prepareStatement(updateBookStatus);
                 java.sql.PreparedStatement psTitle = conn.prepareStatement(getTitle)) {

                psMatch.setString(1, studentId);
                psMatch.setString(2, bookId);
                ResultSet rs = psMatch.executeQuery();

                if (!rs.next()) {
                    System.out.println("Invalid Student ID or Book ID, or this student did not borrow this book.");
                    conn.rollback();
                    return;
                }

                // Get book title for confirmation
                psTitle.setString(1, bookId);
                ResultSet rsTitle = psTitle.executeQuery();
                String title = rsTitle.next() ? rsTitle.getString("title") : "Unknown Book";

                // Drop link
                psDel.setString(1, studentId);
                psDel.setString(2, bookId);
                psDel.executeUpdate();

                // Unsent flag
                psUp.setString(1, bookId);
                psUp.executeUpdate();

                conn.commit();
                System.out.println("Success! '" + title + "' returned safely.");

            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Transaction error: " + ex.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}