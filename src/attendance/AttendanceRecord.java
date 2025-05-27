package attendance;

import java.time.LocalDate;

public class AttendanceRecord {

    private int studentId;
    private LocalDate attendanceDate;
    private String status;

    public AttendanceRecord(int studentId, LocalDate attendanceDate, String status){
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public String getStatus() {
        return status;
    }
}
