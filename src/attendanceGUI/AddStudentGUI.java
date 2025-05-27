package attendanceGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AddStudentGUI extends JFrame implements ActionListener {

    private JTextField nameField;
    private JTextField standardField;
    private JTextField imagePathField;
    private  JButton browseButton;
    private JButton addButton;
    private JFileChooser fileChooser;

    public AddStudentGUI(){
        setTitle("Add Student");
        setSize(600,250);
        setLayout(new GridLayout(4,2,5,5));

        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel standardLabel = new JLabel("Standard:");
       standardField = new JTextField();
       JLabel imagePathLabel = new JLabel("Image Path:");
       imagePathField = new JTextField();
       imagePathField.setEditable(false);
       browseButton = new JButton("Browse");
       browseButton.addActionListener(this);
       addButton = new JButton("Add");
       addButton.addActionListener(this);
       fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image File","jpg","jpeg","png","gif");
        fileChooser.setFileFilter(filter);
       //addButton = new JButton("Add");
       //addButton.addActionListener(this);

               add(nameLabel);
               add(nameField);
               add(standardLabel);
               add(standardField);
               add(imagePathLabel);
               add(imagePathField);
               add(new JLabel());
               add(browseButton);
               add(new JLabel());
               add(addButton);

               setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addButton){
            String name = nameField.getText();
            String standard = standardField.getText();
            String imagePath = imagePathField.getText();

            AttendanceAppGUI.addStudent(name, standard, imagePath);
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            dispose();
        } else if (e.getSource()== browseButton) {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        }
    }
}
