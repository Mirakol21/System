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

    private static final String DB_URL = "jdbc:mysql://localhost/Mystore?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void initialize() {
        setTitle("CHOPPEE");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        orderListModel = new DefaultListModel<>();

        String[] products = {"Uniqlo White Shirt", "Uniqlo Black Shirt", "Uniqlo Gray Shirt", "Uniqlo Brown Shirt", "H&M Sweatshirt", "H&M Baggy Pants", "H&M Skinny Jeans", "H&M Polo Shirt", "VANS Old Skool", "VANS Checkered White", "NIKE Lebron Tagalog 64", "NIKE Kobe Paras 8", "ADIDADAS Ultra Booster", "UNDERARMS Curry 66", "Jordan 1", "Jordan 2", "Jordan 3", "Jordan 4", "Jordan 5", "Jordan 6", "Jordan 7", "Jordan 8", "Jordan 9", "Jordan 10"};

        JList<String> productList = new JList<>(products);
        productList.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13));
        productList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JList<String> orderList = new JList<>(orderListModel);
        orderList.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 13));
        orderList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        orderList.setBackground(new java.awt.Color(255, 255, 204));
        JScrollPane orderScrollPane = new JScrollPane(orderList);

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
        JButton deleteAllButton = new JButton("Clear All");
        deleteAllButton.setBackground(new java.awt.Color(255, 204, 204));
        deleteAllButton.setFont(new java.awt.Font("Impact", 0, 16));
        deleteAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            orderListModel.clear();
            }
        });
        JButton placeOrderButton = new JButton("PLACE ORDER");
        placeOrderButton.setFont(new java.awt.Font("Lucida Sans", 1, 13));
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrderToDatabase();
            }
        });

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                LoginForm loginForm = new LoginForm(); 
                loginForm.initialize(); 
            }
        });

        JPanel productPanel = new JPanel(new BorderLayout());
        JLabel availableProductsLabel = new JLabel("Available Products");
        availableProductsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); 
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

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

        add(logoutPanel, BorderLayout.SOUTH);

        mainPanel.add(placeOrderButton, BorderLayout.SOUTH);

        buttonPanel.add(deleteAllButton);

        setVisible(true);
    }

    private void placeOrderToDatabase() {
        if (orderListModel.isEmpty()) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Please add items to the order before placing it.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String insertQuery = "INSERT INTO orders (product_name) VALUES (?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    for (int i = 0; i < orderListModel.getSize(); i++) {
                        String orderedItem = orderListModel.getElementAt(i);
                        preparedStatement.setString(1, orderedItem);
                        preparedStatement.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(MainFrame.this,
                        "Order Placed Successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

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