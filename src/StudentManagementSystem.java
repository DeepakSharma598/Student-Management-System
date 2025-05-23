import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentManagementSystem {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, rollField, gradeField, phoneField, subjectField;

    private ArrayList<Student> students;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                StudentManagementSystem window = new StudentManagementSystem();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public StudentManagementSystem() {
        students = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Student Management System");
        frame.setBounds(100, 100, 1000, 500); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll No:"));
        rollField = new JTextField();
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        
        inputPanel.add(new JLabel("Subject:"));
        subjectField = new JTextField();
        inputPanel.add(subjectField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        inputPanel.add(addButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

        
        model = new DefaultTableModel(new Object[]{"Name", "Roll No", "Phone", "Subject", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteStudent());
        buttonPanel.add(deleteButton);

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String rollNo = rollField.getText().trim();
        String phone = phoneField.getText().trim();
        String subject = subjectField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || rollNo.isEmpty() || phone.isEmpty() || subject.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid 10-digit phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        for (Student student : students) {
            if (student.getRollNo().equals(rollNo)) {
                JOptionPane.showMessageDialog(frame, "Student with this roll number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        students.add(new Student(name, rollNo, phone, subject, grade));
        model.addRow(new Object[]{name, rollNo, phone, subject, grade});
        clearFields();
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            frame, 
            "Are you sure you want to delete this student?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(selectedRow);
            model.removeRow(selectedRow);
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        phoneField.setText("");
        subjectField.setText("");
        gradeField.setText("");
        nameField.requestFocus();
    }

    private static class Student {
        private final String name;
        private final String rollNo;
        private final String phone;
        private final String subject;
        private final String grade;

        public Student(String name, String rollNo, String phone, String subject, String grade) {
            this.name = name;
            this.rollNo = rollNo;
            this.phone = phone;
            this.subject = subject;
            this.grade = grade;
        }

        public String getName() {
            return name;
        }

        public String getRollNo() {
            return rollNo;
        }

        public String getPhone() {
            return phone;
        }

        public String getSubject() {
            return subject;
        }


        public String getGrade() {
            return grade;
        }
    }
}
