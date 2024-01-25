import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private DefaultListModel<String> orderListModel;

    // MySQL database configuration
    private static final String DB_URL = "jdbc:mysql://localhost/Mystore?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void initialize() {
        setTitle("CHOPPEE");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        orderListModel = new DefaultListModel<>();

        // Product list
        String[] products = {"Uniqlo White Shirt", "Uniqlo Black Shirt", "Uniqlo Gray Shirt", "Uniqlo Brown Shirt", "H&M Sweatshirt", "H&M Baggy Pants", "H&M Skinny Jeans", "H&M Polo Shirt", "VANS Old Skool", "VANS Checkered White", "NIKE Lebron Tagalog 64", "NIKE Kobe Paras 8", "ADIDADAS Ultra Booster", "UNDERARMS Curry 66", "Jordan 1", "Jordan 2", "Jordan 3", "Jordan 4", "Jordan 5"};

        // Product list panel
        JList<String> productList = new JList<>(products);
        productList.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13));
        productList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Order list panel
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13));
        orderList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        orderList.setBackground(new java.awt.Color(255, 255, 204));
        JScrollPane orderScrollPane = new JScrollPane(orderList);

        // Add button
        JButton orderButton = new JButton("Add Item");
        orderButton.setBackground(new java.awt.Color(204, 255, 204));
        orderButton.setFont(new java.awt.Font("Impact", 0, 16));
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected products
                int[] selectedIndices = productList.getSelectedIndices();
                for (int index : selectedIndices) {
                    String selectedProduct = products[index];
                    orderListModel.addElement(selectedProduct);
                }
            }
        });

        // Clear order button
        JButton clearButton = new JButton("Clear Item");
        clearButton.setBackground(new java.awt.Color(255, 153, 153));
        clearButton.setFont(new java.awt.Font("Impact", 0, 16));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = orderList.getSelectedIndex();
                if (selectedIndex != -1) {
                    orderListModel.remove(selectedIndex);
                }
            }
        });

        // Place Order button
        JButton placeOrderButton = new JButton("PLACE ORDER");
        placeOrderButton.setFont(new java.awt.Font("Lucida Sans", 1, 13));
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrderToDatabase();
            }
        });

        /* Log Out Button */
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform any log out logic here
                // For example, closing the current frame and opening the first page
                dispose();  // Close the current frame
                LoginForm loginForm = new LoginForm();  // Assuming LoginForm is the class for your first page
                loginForm.initialize();  // Use a method like show() to display the first page
            }
        });

        // Create panels
        JPanel productPanel = new JPanel(new BorderLayout());
        JLabel availableProductsLabel = new JLabel("Available Products");
        availableProductsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add spacing on the right
        productPanel.add(availableProductsLabel, BorderLayout.NORTH);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);

        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.add(new JLabel("Your Order"), BorderLayout.NORTH);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(productPanel, BorderLayout.WEST);
        mainPanel.add(orderPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        // Create a panel for the Log Out button at the bottom
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);

        // Add the main panel to the content pane
        add(mainPanel, BorderLayout.CENTER);

        // Add the logout panel to the bottom of the content pane
        add(logoutPanel, BorderLayout.SOUTH);

        // Add the Place Order button to the main panel
        mainPanel.add(placeOrderButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void placeOrderToDatabase() {
        if (orderListModel.isEmpty()) {
            // Show a message if the order list is empty
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Please add items to the order before placing it.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Prepare the SQL statement for inserting ordered items into the database
                String insertQuery = "INSERT INTO orders (product_name) VALUES (?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    // Insert each ordered item into the database
                    for (int i = 0; i < orderListModel.getSize(); i++) {
                        String orderedItem = orderListModel.getElementAt(i);
                        preparedStatement.setString(1, orderedItem);
                        preparedStatement.executeUpdate();
                    }
                }
    
                // Show a success message
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Order Placed Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
    
                // Clear the order list
                orderListModel.clear();
                dispose();
                LoginForm loginForm = new LoginForm();
                loginForm.initialize();
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Error placing order. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().initialize();
            }
        });
    }
}
