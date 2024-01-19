import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
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

        /* Log Out Button */
        JButton logoutButton = new JButton("Log Out");
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
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Dashboard");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
