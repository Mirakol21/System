import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Open Sans", Font.BOLD, 18);

    public void initialize(User user) {
        /* Info Panel */
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        infoPanel.add(new JLabel("Name"));
        infoPanel.add(new JLabel(user.name));
        infoPanel.add(new JLabel("Email"));
        infoPanel.add(new JLabel(user.email));
        infoPanel.add(new JLabel("Phone"));
        infoPanel.add(new JLabel(user.phone));
        infoPanel.add(new JLabel("Address"));
        infoPanel.add(new JLabel(user.address));

        Component[] labels = infoPanel.getComponents();
        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(new Font("Open Sans", Font.BOLD, 18));
        }

        add(infoPanel, BorderLayout.NORTH);

        /* Update Button */
        JButton updateButton = new JButton("Edit");
        updateButton.setFont(mainFont);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a dialog for updating information
                UpdateDialog updateDialog = new UpdateDialog(MainFrame.this, user, labels);
                updateDialog.setVisible(true);
            }
        });

        /* Log Out Button */
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(mainFont);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform any log out logic here
                // For example, closing the current frame and opening the first page
                dispose();  // Close the current frame
                LoginForm loginForm = new LoginForm();  // Assuming FirstPage is the class for your first page
                loginForm.initialize();  // Use a method like show() to display the first page
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.add(updateButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Dashboard");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateLabels(User user, Component[] labels) {
        for (int i = 0; i < labels.length; i += 2) {
            JLabel label = (JLabel) labels[i + 1];
            switch (i / 2) {
                case 0:
                    label.setText(user.name);
                    break;
                case 1:
                    label.setText(user.email);
                    break;
                case 2:
                    label.setText(user.phone);
                    break;
                case 3:
                    label.setText(user.address);
                    break;
            }
        }
    }

    // Inner class for the update dialog
    private class UpdateDialog extends JDialog {
        private JTextField nameField, emailField, phoneField, addressField;
        public UpdateDialog(JFrame parent, User user, Component[] labels) {
            super(parent, "Update Information", true);
            // Create components for the dialog
            nameField = new JTextField(user.name);
            emailField = new JTextField(user.email);
            phoneField = new JTextField(user.phone);
            addressField = new JTextField(user.address);

            // Layout for the dialog
            JPanel dialogPanel = new JPanel(new GridLayout(5, 5, 5, 5));
            dialogPanel.add(new JLabel("Name"));
            dialogPanel.add(nameField);
            dialogPanel.add(new JLabel("Email"));
            dialogPanel.add(emailField);
            dialogPanel.add(new JLabel("Phone"));
            dialogPanel.add(phoneField);
            dialogPanel.add(new JLabel("Address"));
            dialogPanel.add(addressField);

            // OK button to confirm changes
            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Update user information
                    user.name = nameField.getText();
                    user.email = emailField.getText();
                    user.phone = phoneField.getText();
                    user.address = addressField.getText();

                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Update successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                    // Update labels on the main frame
                    updateLabels(user, labels);

                    // Update information in the database
                    updateDatabase(user);

                    // Close the dialog
                    dispose();
                }
            });

            // Cancel button to discard changes
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the dialog without making changes
                    dispose();
                }
            });

            // Add components to the dialog
            dialogPanel.add(okButton);
            dialogPanel.add(cancelButton);
            add(dialogPanel);

            // Set dialog properties
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(400, 200);
            setLocationRelativeTo(parent);
        }

        private void updateDatabase(User user) {
            // JDBC URL, username, and password of your database
            String url = "jdbc:mysql://localhost/Mystore?serverTimezone=UTC";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // Update SQL statement
                String sql = "UPDATE users SET name=?, email=?, phone=?, address=? WHERE id=?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, user.name);
                    preparedStatement.setString(2, user.email);
                    preparedStatement.setString(3, user.phone);
                    preparedStatement.setString(4, user.address);
                    preparedStatement.setInt(5, user.id); // Assuming there is an 'id' field in your database

                    // Execute the update
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle the exception as needed
            }
        }
    }
}
