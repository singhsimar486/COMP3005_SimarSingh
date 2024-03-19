import java.sql.*;
import java.util.Scanner;

public class Main {
    // Initialized all the variables here for the connection purposes
    private static final String url = "jdbc:postgresql://localhost:5432/A3Q1";
    private static final String user = "postgres";
    private static final String password = "Simar@2002";
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            displayMenu(); // Called the display function to display the table before the user makes a choice

            Scanner scanner = new Scanner(System.in); // Used the scanner to take in user input
            int choice = scanner.nextInt();

            // Used switch cases to call the function depending on the user's choice
            switch (choice) {
                case 1:
                    getAllStudents();
                    break;
                case 2:
                    addStudent();
                    break;
                case 3:
                    updateStudentEmail();
                    break;
                case 4:
                    deleteStudent();
                    break;
                default:
                    System.out.println("Invalid choice. Exiting...");
            }

            if (connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Not connected");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Display menu options
    private static void displayMenu() {
        System.out.println("Choose an operation:");
        System.out.println("1. Retrieve all students");
        System.out.println("2. Add a new student");
        System.out.println("3. Update student email");
        System.out.println("4. Delete a student");
        System.out.print("Enter your choice: ");
    }
    // Prints all the data stored in the table
    private static void getAllStudents() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM students"); // Executed the query for to get the contents of the Students table
        while (resultSet.next()) { // Used a while loop ot print the contents
            System.out.println(resultSet.getInt("student_id") + ", "
                    + resultSet.getString("first_name") + ", "
                    + resultSet.getString("last_name") + ", "
                    + resultSet.getString("email") + ", "
                    + resultSet.getDate("enrollment_date"));
        }
        resultSet.close();
    }

    // Inserts a new student into the students table
    private static void addStudent() throws SQLException {
        getAllStudents(); // Called this function so the user can see the updated functon
        // Using the scanner to get the user input for adding the student
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter enrollment date (YYYY-MM-DD): ");
        String enrollmentDate = scanner.nextLine();

        // Using the insert query to insert the student contents
        String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, email);
        preparedStatement.setDate(4, Date.valueOf(enrollmentDate));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("New student added successfully.");
        System.out.println("Updated Table:");
        getAllStudents(); // printed the updated table
    }

    // Updates the email address for a student with the specified student_id
    private static void updateStudentEmail() throws SQLException {
        getAllStudents();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();

        // Using the update query to update the students email
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, newEmail);
        preparedStatement.setInt(2, studentId);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Email address updated successfully.");
            System.out.println("Updated Table:");
            getAllStudents();
        } else {
            System.out.println("No student found with the specified student ID.");
        }
        preparedStatement.close();
    }

    // Deletes the record of the student with the specified student_id
    private static void deleteStudent() throws SQLException {
        getAllStudents();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        // Using the delete query to delete the chosen student
        String sql = "DELETE FROM students WHERE student_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, studentId);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Student record deleted successfully.");
            System.out.println("Updated Table:");
            getAllStudents();
        } else {
            System.out.println("No student found with the specified student ID.");
        }
        preparedStatement.close();
    }
}
