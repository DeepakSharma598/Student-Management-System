import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class StudentManagementSystem {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, rollField, phoneField, subjectField, gradeField, searchField;
    private final ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.frame.setVisible(true);
        });
    }

    public StudentManagementSystem() {
        initializeUI();
    }

   
    private void initializeUI() {
        frame = new JFrame("Student Management System");
        frame.setSize(1100, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        nameField = addLabeledField(inputPanel, "Name");
        rollField = addLabeledField(inputPanel, "Roll No");
        phoneField = addLabeledField(inputPanel, "Phone");
        subjectField = addLabeledField(inputPanel, "Subject");
        gradeField = addLabeledField(inputPanel, "Grade");

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addStudent());
        inputPanel.add(addButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Name", "Roll No", "Phone", "Subject", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        controlPanel.add(deleteButton);

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        controlPanel.add(clearButton);

        controlPanel.add(new JLabel("Search Roll No:"));
        searchField = new JTextField(15);
        controlPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudent());
        controlPanel.add(searchButton);

        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    
    private JTextField addLabeledField(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField field = new JTextField();
        panel.add(field);
        return field;
    }

   
    private void addStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String phone = phoneField.getText().trim();
        String subject = subjectField.getText().trim();
        String grade = gradeField.getText().trim();

        // Validation
        if (name.isEmpty() || roll.isEmpty() || phone.isEmpty() || subject.isEmpty() || grade.isEmpty()) {
            showMessage("All fields are required.", "Validation Error");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            showMessage("Phone number must be exactly 10 digits.", "Validation Error");
            return;
        }

        if (students.stream().anyMatch(s -> s.getRollNo().equalsIgnoreCase(roll))) {
            showMessage("Roll number already exists.", "Validation Error");
            return;
        }

        // Add student
        Student student = new Student(name, roll, phone, subject, grade);
        students.add(student);
        tableModel.addRow(student.toObjectArray());
        showMessage("Student added successfully!", "Success");
        clearFields();
    }

   
    private void deleteSelectedStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("No student selected.", "Selection Error");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(row);
            tableModel.removeRow(row);
            showMessage("Student deleted.", "Success");
        }
    }

 
    private void searchStudent() {
        String roll = searchField.getText().trim();
        if (roll.isEmpty()) {
            showMessage("Enter roll number to search.", "Search Error");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRollNo().equalsIgnoreCase(roll)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
                return;
            }
        }

        showMessage("Student not found.", "Search Result");
    }

    
    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        phoneField.setText("");
        subjectField.setText("");
        gradeField.setText("");
        searchField.setText("");
        nameField.requestFocus();
    }

   
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    
    static class Student {
        private final String name, rollNo, phone, subject, grade;

        public Student(String name, String rollNo, String phone, String subject, String grade) {
            this.name = name;
            this.rollNo = rollNo;
            this.phone = phone;
            this.subject = subject;
            this.grade = grade;
        }

        public String getRollNo() {
            return rollNo;
        }

        public Object[] toObjectArray() {
            return new Object[]{name, rollNo, phone, subject, grade};
        }
    }
}
