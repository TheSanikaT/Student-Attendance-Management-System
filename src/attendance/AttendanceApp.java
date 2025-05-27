package attendance;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AttendanceApp {

    private static final DateTimeFormatter DATE_FORMATTER =  DateTimeFormatter.ofPattern("d/M/yyyy");

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("\nSchool Attendance Management System");

            System.out.println("1. Add Student");

            System.out.println("2. Add Attendance (Single Day)");

            System.out.println("3. Add Attendance (Whole Month for a student)");

            System.out.println("4. View Attendance");

            System.out.println("5. Exit");

            System.out.println("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    addAttendance(scanner);
                    break;
                case 3:
                    addMonthlyAttendance(scanner);
                    break;
                case 4:
                    viewAttendance(scanner);
                    break;
                case  5:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");

            }
        }
    }

    private static void addStudent(Scanner scanner){
        System.out.println("\nAdd attendance.attendanceGUI.Student");
        System.out.println("Enter student name: ");
        String name = scanner.nextLine();
        System.out.println("Enter student standard: ");
        String standard = scanner.nextLine();

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (name, standard) VALUES(?, ?)")) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,standard);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Student Added Successfully.");
            }else {
                System.out.println("Failed to add student.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void addAttendance(Scanner scanner){
        System.out.println("\nAdd Attendance");
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter attendance date (dd/MM/yyyy): ");
        String dateStr = scanner.nextLine();
        LocalDate attendanceDate;

        try {
            attendanceDate = LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            return;
        }
        System.out.println("Enter attendance status (P/A): ");
        String status = scanner.nextLine().toUpperCase();

        if (!status.equals("P") && !status.equals("A")){
            System.out.println("Invalid status. Please enter 'P' or 'A'.");
            return;
        }

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attendance(student_id, attendance_date, status) VALUES (?, ?, ?)")){
            preparedStatement.setInt(1, studentId);
            preparedStatement.setDate(2,
                    Date.valueOf(attendanceDate));

            preparedStatement.setString(3, status);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Attendance added successfully.");
            }else {
                System.out.println("Failed to add attendance. Make sure the student ID exits.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void addMonthlyAttendance(Scanner scanner){
        System.out.println("\nAdd Attendance (Whole month for a attendance.attendanceGUI.Student)");
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter year (YYYY): ");
        int year = scanner.nextInt();
        System.out.print("Enter month (MM): ");
        int month = scanner.nextInt();
        scanner.nextLine();

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        System.out.println("\nEnter attendance status for each day of "+firstDayOfMonth.getMonth()+" "+year+":");

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attendance (student_id, attendance_date, status) VALUES(? ,? ,?)")){
            connection.setAutoCommit(false);

            for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)){
                System.out.print(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" (P/A - Present/Absent): ");
                String status = scanner.nextLine().toUpperCase();

                if (status.equals("P")|| status.equals("A")){
                    preparedStatement.setInt(1,studentId);
                    preparedStatement.setDate(2,java.sql.Date.valueOf(date));
                    preparedStatement.setString(3, status);
                    preparedStatement.addBatch();
                }else {
                    System.out.println("Invalid status. Skipping "+date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
            int[] rowsAffected = preparedStatement.executeBatch();
            connection.commit();

            System.out.println(rowsAffected.length + " attendance records added successfully for student " + studentId+" in "+firstDayOfMonth.getMonth()+" "+year+".");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAttendance(Scanner scanner){
        System.out.println("\n View Attendance");
        System.out.println("Enter standard to view attendance: ");
        String standard = scanner.nextLine();
        System.out.println("Enter month (MM) : ");
        int month = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Year (YYYY) : ");
        int year = scanner.nextInt();
        scanner.nextLine();

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement studentStatement = connection.prepareStatement("SELECT id, name FROM students WHERE standard = ?");
            PreparedStatement attendanceStatement = connection.prepareStatement("SELECT attendance_date, status FROM attendance WHERE student_id = ? AND MONTH(attendance_date) = ? AND YEAR(attendance_date) = ?")){
            studentStatement.setString(1,standard);
            ResultSet studentResultSet = studentStatement.executeQuery();

            List<Student> students = new ArrayList<>();
            while (studentResultSet.next()){

                students.add(new Student(studentResultSet.getInt("id"), studentResultSet.getString("name"), standard));
            }
            if (students.isEmpty()){
                System.out.println("No student found for the given standard.");
                return;
            }
            System.out.println("\nAttendance of "+ standard+ " standard in "+java.time.Month.of(month).name()+" month");

            System.out.print("Name\tID");
            for (int day = 1; day <= LocalDate.of(year , month, 1).lengthOfMonth(); day++){
                System.out.print("\t|"+ day);
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

            for (Student student : students){
                System.out.print(student.getName()+ "\t"+ student.getId());
                attendanceStatement.setInt(1, student.getId());
                attendanceStatement.setInt(2,month);
                attendanceStatement.setInt(3,year);

                ResultSet attendanceResultSet = attendanceStatement.executeQuery();

                java.util.Map<Integer, String> attendanceMap = new java.util.HashMap<>();

                while (attendanceResultSet.next()){
                    LocalDate date = attendanceResultSet.getDate("attendance_date").toLocalDate();
                    attendanceMap.put(date.getDayOfMonth(), attendanceResultSet.getString("status"));
                }
                for (int day = 1; day <= LocalDate.of(year, month, 1).lengthOfMonth(); day++){
                    System.out.print("\t|"+attendanceMap.getOrDefault(day," "));
                }
                System.out.println();
                attendanceResultSet.close();
            }
            studentResultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
