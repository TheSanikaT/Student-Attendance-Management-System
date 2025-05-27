package attendanceGUI;

import attendance.DatabaseConnection;

import javax.swing.*;
import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class  AttendanceAppGUI {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
    public static void addStudent(String name, String standard, String imagePath){
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students(name,standard, image_path) VALUES(?,?, ?)")){
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,standard);
            preparedStatement.setString(3,imagePath);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
           e.printStackTrace();
           JOptionPane.showMessageDialog(null,"Error adding student to the database.");
        }
    }
    public static void addAttendanceSingleDay(int studentId, String dateStr, String status){
        LocalDate attendanceDate;
        try {
            attendanceDate = LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Invalid date format. Please use dd/MM/yyyy.");
            return;
        }
        if (!status.equals("P") && !status.equals("A") && !status.equals("H")){
            JOptionPane.showMessageDialog(null, "Invalid status. Please enter 'P', 'A' or 'H'.");
        }
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attendance (student_id, attendance_date, status) VALUES (?,?,?)")){
            preparedStatement.setInt(1,studentId);
            preparedStatement.setDate(2, Date.valueOf(attendanceDate));
            preparedStatement.setString(3,status);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0){
                JOptionPane.showMessageDialog(null,"Failed to add attendance. Make sure the student ID exits.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error adding attendance to the database.");
        }
    }
    public static void addMonthlyAttendance(int studentId, int year, int month){
        LocalDate firstDayOfMonth = LocalDate.of(year,month,1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        Set<LocalDate> holidays = getHolidaysForMonth(year, month);
        int daysInMonth = lastDayOfMonth.getDayOfMonth();
        Scanner scanner = new Scanner(System.in);

        JOptionPane.showMessageDialog(null,"Initiating monthly attendance addition for student "+studentId+" in "+firstDayOfMonth.getMonth()+" "+year+".", "Adding Attendance", JOptionPane.INFORMATION_MESSAGE);
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attendance (student_id, attendance_date, status) VALUES(?,?,?)")){
            connection.setAutoCommit(false);
//            for(int day =1; day<= daysInMonth; day++){
//                LocalDate currentDate = LocalDate.of(year, month, day);
//
//                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
//
//                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
//                    continue;
//                }
//                String attendanceInput = JOptionPane.showInputDialog(null,"Enter attendance status for "+currentDate.toString()+" (P for Present, A for Absent): ");
//                if (attendanceInput != null && !attendanceInput.trim().isEmpty()){
//                    String dailyStatus = attendanceInput.trim().equalsIgnoreCase("P")?"P":"A";
            for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)){
                String dailyStatus ;
                if(holidays.contains(date)){
                    dailyStatus = "H";
                }else if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    dailyStatus = "P";
                }else {
                    dailyStatus = "W";
                }

                    preparedStatement.setInt(1,studentId);
                    preparedStatement.setDate(2, java.sql.Date.valueOf(date));
                    preparedStatement.setString(3,dailyStatus);
                    preparedStatement.addBatch();

            }
            preparedStatement.executeBatch();
            connection.commit();
            JOptionPane.showMessageDialog(null,"Monthly attendance added successfully.(Including Holidays)");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding monthly attendance to the database: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }finally {
            if (scanner != null){
                scanner.close();
            }
        }
    }
    public static List<Student>getStudentsByStandard(String standard){
        List<Student> students = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id,name, image_path FROM students WHERE standard =?")){
            preparedStatement.setString(1,standard);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                students.add(new Student(resultSet.getInt("id"),resultSet.getString("name"), standard, resultSet.getString("image_path")));
                //Student student = new Student(resultSet.getInt("id"), resultSet.getString("name"),standard,resultSet.getString("image_path"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving students.");
        }
        return students;
    }
    public static Map<Integer, String>getMonthlyAttendance(int studentId, int year, int month){
        Map<Integer, String> attendanecMap = new HashMap<>();
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT attendance_date, status FROM attendance WHERE student_id =? AND MONTH(attendance_date)=? AND YEAR(attendance_date)=?")){
            preparedStatement.setInt(1,studentId);
            preparedStatement.setInt(2,month);
            preparedStatement.setInt(3,year);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                LocalDate date = resultSet.getDate("attendance_date").toLocalDate();
                attendanecMap.put(date.getDayOfMonth(), resultSet.getString("status"));
            }

        } catch (SQLException e) {
           e.printStackTrace();
           JOptionPane.showMessageDialog(null,"Error retrieving attendance data.");
        }
        return attendanecMap;
    }
    private static Set<LocalDate> getHolidaysForMonth(int year, int month){
        Set<LocalDate> holidays = new HashSet<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT holiday_date FROM holidays WHERE YEAR(holiday_date)=? AND MONTH(holiday_date)=?")){
            preparedStatement.setInt(1,year);
            preparedStatement.setInt(2,month);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                holidays.add(resultSet.getDate("holiday_date").toLocalDate());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error retrieving holidays.");
        }
        return holidays;
    }
    public static List<Map<LocalDate,String>> getHolidaysWithDescription(int year, int month){
        List<Map<LocalDate, String>> holidaysWithDescription = new ArrayList<>();
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement("SELECT holiday_date , holiday_description FROM holidays WHERE YEAR(holiday_date)=? AND MONTH(holiday_date)=?")){
            preparedStatement.setInt(1,year);
            preparedStatement.setInt(2,month);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Map<LocalDate, String> holidayMap = new HashMap<>();
                holidayMap.put(resultSet.getDate("holiday_date").toLocalDate(), resultSet.getString("holiday_description"));
                holidaysWithDescription.add(holidayMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving holidays with description.");
        }
        return holidaysWithDescription;
    }
}
//C:\Users\Admin\Desktop\Image\student-image.jpg