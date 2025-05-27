package attendanceGUI;

import javax.imageio.stream.ImageInputStream;
import javax.lang.model.element.Name;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;

public class ViewAttendanceGUI extends JFrame implements ActionListener, ListSelectionListener {
    private JTextField standardField;
    private JTextField monthField;
    private JTextField yearField;
    private JButton viewButton;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JLabel imageLabel;
    private JPanel holidayPanel;
    private List<Student> currentStudents;

    public ViewAttendanceGUI() {
        setTitle("View Attendance");
        setSize(1300, 350);
        setLayout(new BorderLayout());

        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new FlowLayout());

        JLabel standardLabel = new JLabel("Standard:");
        standardField = new JTextField(5);
        JLabel monthLabel = new JLabel("Month (MM):");
        monthField = new JTextField(3);
        JLabel yearLabel = new JLabel("Year (YYYY):");
        yearField = new JTextField(5);
        viewButton = new JButton("View");
        viewButton.addActionListener(this);

        inputPanel.add(standardLabel);
        inputPanel.add(standardField);
        inputPanel.add(monthLabel);
        inputPanel.add(monthField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearField);
        inputPanel.add(viewButton);

        tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel);


        attendanceTable.getSelectionModel().addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 150));
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);
        imagePanel.setVisible(true);

        holidayPanel = new JPanel();
        holidayPanel.setLayout(new BoxLayout(holidayPanel, BoxLayout.Y_AXIS));
        JScrollPane holidayScrollPane = new JScrollPane(holidayPanel);
        holidayScrollPane.setPreferredSize(new Dimension(400, 100));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(holidayScrollPane, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.EAST);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewButton) {
            String standard = standardField.getText();
            try {
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());

                currentStudents = AttendanceAppGUI.getStudentsByStandard(standard);
//                System.out.println("Viewing attendance for standard:"+standard+", month: "+month+", year: "+year);
//
//                List<Student> students = AttendanceAppGUI.getStudentsByStandard(standard);
//                System.out.println("Number of students found: "+students.size());
                if (currentStudents.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No students found for this standard.");
                    tableModel.setRowCount(0);
                    imageLabel.setIcon(null);
                    holidayPanel.removeAll();
                    holidayPanel.revalidate();
                    holidayPanel.repaint();
                    return;
                }
                tableModel.setColumnCount(0);
                tableModel.setRowCount(0);
                tableModel.addColumn("Name");
                tableModel.addColumn("ID");

                LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
                int daysInMonth = firstDayOfMonth.lengthOfMonth();
                String[] dayAbbreviations = new String[daysInMonth];
                String[] dayNumbers = new String[daysInMonth];
                for (int i = 0; i < daysInMonth; i++) {
                    LocalDate date = firstDayOfMonth.plusDays(i);
                    dayAbbreviations[i] = date.getDayOfWeek().toString().substring(0, 1);
                    dayNumbers[i] = String.valueOf(date.getDayOfMonth());
                    tableModel.addColumn(dayAbbreviations[i]);
                }

                Object[] dayNumberRow = new Object[2 + daysInMonth];
                dayNumberRow[0] = "";
                dayNumberRow[1] = "";
                for (int i = 0; i < daysInMonth; i++) {
                    dayNumberRow[i + 2] = dayNumbers[i];
                }
                tableModel.addRow(dayNumberRow);


//                 daysInMonth = java.time.YearMonth.of(year,month).lengthOfMonth();
//                for (int i = 1; i<= daysInMonth; i++){
//                    tableModel.addColumn(i);
//            }
//                String[] dayNumbers = new String[daysInMonth];
//                Object[] dayNumberRow = new Object[2 + daysInMonth];
//                dayNumberRow[0] ="";
//                dayNumberRow[1] = "";
//                for (int i = 0; i<daysInMonth; i++){
//                    dayNumberRow[i + 2] = dayNumbers[i];
//                }
//                tableModel.addRow(dayNumberRow);

                for (Student student : currentStudents) {
                    Object[] rowData = new Object[2 + daysInMonth];
                    rowData[0] = student.getName();
                    rowData[1] = student.getId();
                    attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(300);
                    TableColumn nameColumn = attendanceTable.getColumnModel().getColumn(0);
                    TableCellRenderer defaultRenderer = attendanceTable.getTableHeader().getDefaultRenderer();
                    TableCellRenderer boldHeaderRenderer = (table, value, isSelected, hasFocus, row, column) -> {
                        JLabel label = (JLabel) defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                        return label;
                    };
                    currentStudents = AttendanceAppGUI.getStudentsByStandard(standard);
                    System.out.println("currentStudents size: " + currentStudents.size());
                    nameColumn.setHeaderRenderer(boldHeaderRenderer);
                    setVisible(true);

                    Map<Integer, String> attendanceMap = AttendanceAppGUI.getMonthlyAttendance(student.getId(), year, month);
                    for (int i = 0; i < daysInMonth; i++) {
                        rowData[i + 2] = attendanceMap.getOrDefault(i + 1, "");
                    }
                    tableModel.addRow(rowData);

                    attendanceTable.repaint();
                }
                tableModel.addRow(new Object[tableModel.getColumnCount()]);
                imageLabel.setIcon(null);
                List<Map<LocalDate, String>> holidays = AttendanceAppGUI.getHolidaysWithDescription(year, month);
                holidayPanel.removeAll();
                if (!holidays.isEmpty()) {
                    holidayPanel.add(new JLabel(""));
                    holidayPanel.add(new JLabel("Holidays:"));
                    holidayPanel.add(new JLabel("---------"));

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    for (Map<LocalDate, String> holiday : holidays) {
                        for (Map.Entry<LocalDate, String> entry : holiday.entrySet()) {
                            holidayPanel.add(new JLabel(entry.getKey().format(dateFormatter) + " : " + entry.getValue()));
                        }
                    }
                }
                holidayPanel.revalidate();
                holidayPanel.repaint();

                JTableHeader header = attendanceTable.getTableHeader();
                DefaultTableCellRenderer headerRender = (DefaultTableCellRenderer) header.getDefaultRenderer();
                headerRender.setHorizontalAlignment(SwingConstants.CENTER);
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 1; i < attendanceTable.getColumnCount(); i++) {
                    attendanceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Month or Year!");
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        attendanceTable.getSelectionModel().addListSelectionListener(this);
        if (!e.getValueIsAdjusting()) {
            try {
                int selectedRow = attendanceTable.getSelectedRow();
                if (selectedRow > 0 && selectedRow < currentStudents.size()+1) {
                    Student selectedStudent = currentStudents.get(selectedRow-1);
                    String imagePath = selectedStudent.getImagePath();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        ImageIcon imageIcon = new ImageIcon(imagePath);
                        if (imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
                            System.err.println("Error loading image at path: " + imagePath);
                        }
                        Image scaledImage = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));

                    } else {
                        imageLabel.setIcon(null);
                    }
                } else {
                    imageLabel.setIcon(null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}

