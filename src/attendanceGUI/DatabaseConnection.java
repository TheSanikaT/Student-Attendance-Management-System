package attendanceGUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private  static final String url = "jdbc:mysql://127.0.0.1:3306/school_attendance";

    private static final String username = "root";

    private static final String password = "User@2024#";

    public static Connection getConnection() throws SQLException {
        return DriverManager. getConnection(url, username, password);
    }
}
