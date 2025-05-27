package attendanceGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAttendanceGUI  extends JFrame implements ActionListener {
    private JButton addSingleDayButton;
    private JButton addMonthlyButton;

    public AddAttendanceGUI(){
        setTitle("Add Attendance");
        setSize(300, 100);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        addSingleDayButton = new JButton("Single Day");
        addMonthlyButton = new JButton("Whole Month");

        addSingleDayButton.addActionListener(this);
        addMonthlyButton.addActionListener(this);

        add(addSingleDayButton);
        add(addMonthlyButton);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addSingleDayButton){
            new AddSingleDayAttendanceGUI();
        } else if (e.getSource() == addMonthlyButton) {
            new AddMonthlyAttendanceGUI();
        }
        dispose();
    }
}
class AddSingleDayAttendanceGUI extends JFrame implements ActionListener{
    private JTextField studentIdField;
    private  JTextField dateField;
    private JTextField statusField;
    private JButton addButton;

    public AddSingleDayAttendanceGUI(){
        setTitle("Add Single Day Attendance");
        setSize(300,180);
        setLayout(new GridLayout(4,2,5,5));
        setLocationRelativeTo(null);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdField = new JTextField();
        JLabel dateLabel = new JLabel("Date (dd/MM/yyyy):");
        dateField = new JTextField();
        JLabel statusLabel = new JLabel("Status (P/A):");
        statusField = new JTextField();
        addButton = new JButton("Add");
        addButton.addActionListener(this);

        add(studentIdLabel);
        add(studentIdField);
        add(dateLabel);
        add(dateField);
        add(statusLabel);
        add(statusField);
        add(new JLabel());
        add(addButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addButton){
            try{
                int studentId = Integer.parseInt(studentIdField.getText());
                String dateStr = dateField.getText();
                String status = statusField.getText().toUpperCase();

                AttendanceAppGUI.addAttendanceSingleDay(studentId, dateStr, status);
                JOptionPane.showMessageDialog(this,"Attendance added successfully!");
                dispose();
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Invalid attendanceGUI.Student ID!");
            }
        }
    }
}
class AddMonthlyAttendanceGUI extends JFrame implements ActionListener{
    private JTextField studentIdField;
    private JTextField yearField;
    private JTextField monthField;
    private JButton addButton;

    public AddMonthlyAttendanceGUI(){
        setTitle("Add Monthly Attendance");
        setSize(300,180);
        setLayout(new GridLayout(4,2,5,5));
        setLocationRelativeTo(null);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdField = new JTextField();
        JLabel yearLabel = new JLabel("Year (YYYY):");
        yearField = new JTextField();
        JLabel monthLabel = new JLabel("Month (MM):");
        monthField = new JTextField();
        addButton = new JButton("Add");
        addButton.addActionListener(this);

        add(studentIdLabel);
        add(studentIdField);
        add(yearLabel);
        add(yearField);
        add(monthLabel);
        add(monthField);
        add(new JLabel());
        add(addButton);

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addButton){
            try{
                int studentId =Integer.parseInt(studentIdField.getText());
                int year = Integer.parseInt(yearField.getText());
                int month = Integer.parseInt(monthField.getText());

                AttendanceAppGUI.addMonthlyAttendance(studentId, year, month);
                JOptionPane.showMessageDialog(this,"Monthly attendance initiated!");
                dispose();
            }catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Invalid input for ID, Year or Mo!nth");
            }
        }
    }
}