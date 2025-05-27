package attendanceGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;

public class MainGUI extends JFrame implements ActionListener {

    private JButton addStudentButton;
    private JButton addAttendanceButton;
    private JButton viewAttendanceButton;
    private Image backgroundImage;

    private static final String BACKGROUND_IMAGE_PATH= "school-classroom.jpg";

    public MainGUI(){

        setTitle("School Attendance System");
        setSize(800,350);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            URL imageUrl= getClass().getResource(BACKGROUND_IMAGE_PATH);
            if (imageUrl != null){
                backgroundImage = new ImageIcon(imageUrl).getImage();
            }else {
                System.err.println("Background image not found: "+BACKGROUND_IMAGE_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading background image: "+BACKGROUND_IMAGE_PATH);
        }
        JPanel backgroundPanel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if (backgroundImage != null){
                    g.drawImage(backgroundImage,0,0, getWidth(),getHeight(), this);
                }else {
                    g.setColor(new Color(140,140,190));
                    g.fillRect(0,0,getWidth(),getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);
        JLabel welcomeLabel = new JLabel("Welcome to students attendance management system");
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setFont(new Font("Serif",Font.BOLD,32));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        backgroundPanel.add(welcomeLabel, BorderLayout.NORTH);

        //JLabel label = new JLabel();

        //label.setOpaque(true);

        //getContentPane().setBackground(new Color(140,140,190));
        setLocationRelativeTo(null);

        addStudentButton = createStyledButton("Add Student");
        addAttendanceButton = createStyledButton("Add Attendance");
        viewAttendanceButton = createStyledButton("View Attendance");

        addStudentButton.addActionListener(this);
        addAttendanceButton.addActionListener(this);
        viewAttendanceButton.addActionListener(this);

        JPanel topRowPanel = new JPanel();
        topRowPanel.setOpaque(false);
        topRowPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topRowPanel.add(addStudentButton);
        topRowPanel.add(addAttendanceButton);

        JPanel bottomRowPanel = new JPanel();
        bottomRowPanel.setOpaque(false);
        bottomRowPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomRowPanel.add(viewAttendanceButton);

        JPanel triangleButtonPanel = new JPanel();
        triangleButtonPanel.setOpaque(false);
        triangleButtonPanel.setLayout(new BoxLayout(triangleButtonPanel, BoxLayout.Y_AXIS));
        triangleButtonPanel.add(topRowPanel);
        triangleButtonPanel.add(bottomRowPanel);
        JPanel centerContainerPanel = new JPanel();
        centerContainerPanel.setOpaque(false);
        centerContainerPanel.setLayout(new BoxLayout(centerContainerPanel, BoxLayout.Y_AXIS));
        centerContainerPanel.add(Box.createVerticalGlue());
        centerContainerPanel.add(triangleButtonPanel);
        centerContainerPanel.add(Box.createVerticalGlue());
        add(centerContainerPanel, BorderLayout.CENTER);

        setVisible(true);

    }
    private JButton createStyledButton(String text){
        JButton button = new JButton(text);
        button.setBackground(new Color(220,220,220));
        button.setForeground(Color.DARK_GRAY);
        button.setFont(new Font("Arial", Font.BOLD,16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10,25,10,25));
        return button;
    }
    public void  actionPerformed(ActionEvent e){
        if (e.getSource() == addStudentButton){
            new AddStudentGUI();
        } else if (e.getSource() == addAttendanceButton) {
            new AddAttendanceGUI();
        } else if (e.getSource() == viewAttendanceButton) {
            new ViewAttendanceGUI();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI :: new);
    }
}
