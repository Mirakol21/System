import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;

public class SignupForm extends JFrame {
    final private Font mainFont = new Font("Open Sans", Font.BOLD, 18);
    JTextField tfName, tfEmail, tfPhone, tfAddress;
    JPasswordField pfPassword;

    public void initialize() {
        /*Form Panel*/
        JLabel lbSignupForm = new JLabel("Signup Form", SwingConstants.CENTER);
        lbSignupForm.setFont(mainFont);

        JLabel lbName = new JLabel("Name");
        lbName.setFont(mainFont);

        tfName = new JTextField();
        tfName.setFont(mainFont);

        JLabel lbEmail = new JLabel("Email");
        lbEmail.setFont(mainFont);

        tfEmail = new JTextField();
        tfEmail.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JLabel lbPhone = new JLabel("Phone");
        lbPhone.setFont(mainFont);

        tfPhone = new JTextField();
        tfPhone.setFont(mainFont);

        JLabel lbAddress = new JLabel("Address");
        lbAddress.setFont(mainFont);

        tfAddress = new JTextField();
        tfAddress.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(lbSignupForm);
        formPanel.add(new JLabel()); // Empty label for layout purposes
        formPanel.add(lbName);
        formPanel.add(tfName);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);
        formPanel.add(lbPhone);
        formPanel.add(tfPhone);
        formPanel.add(lbAddress);
        formPanel.add(tfAddress);

        /*Bottom Panel*/
        JButton btnSignup = new JButton("Confirm");
        btnSignup.setFont(mainFont);
        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Add signup logic here
                // Example: Save data to the database
                saveUserData();

                // Optionally, show a success message or navigate to another screen
                JOptionPane.showMessageDialog(SignupForm.this,
                        "Signup successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear the form fields
                clearFields();
            }
        });

        JButton btnCancel = new JButton("Back");
        btnCancel.setFont(mainFont);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Add cancel logic here
                // Example: Close the form
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(btnSignup);
        buttonPanel.add(btnCancel);

        /*Initialize the Frame*/
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Signup Form");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setMinimumSize(new Dimension(450, 500));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveUserData() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String phone = tfPhone.getText();
        String address = tfAddress.getText();

        // TODO: Add code to save the user data to the database
        // Example: Insert into the users table

        final String DB_URL = "jdbc:mysql://localhost/Mystore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String sql = "INSERT INTO users (name, email, password, phone, address) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, phone);
                preparedStatement.setString(5, address);

                preparedStatement.executeUpdate();
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error during signup.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        pfPassword.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        SignupForm signupForm = new SignupForm();
        signupForm.initialize();
    }
}
