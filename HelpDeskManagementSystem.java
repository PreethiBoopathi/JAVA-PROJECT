import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class HelpDeskManagementSystem {
    private static final String[] companyAccounts = {"admin"};
    private static final String[] customerAccounts = {"Preethi", "Sneha", "Vinitha", "Hassani", "Mirna"};
    private static final String[] companyPasswords = {"admin123"};
    private static final String[] customerPasswords = {"preethi123", "sneha123", "vinitha123", "hassani123", "mirna123"};
    private static final ArrayList<String> tickets = new ArrayList<>();
    private static final ArrayList<String> ticketOwners = new ArrayList<>();

    private static boolean isLoggedIn = false;
    private static String loggedInRole = "";
    private static String loggedInUsername = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("HelpDesk Management System");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(173, 216, 230)); // Light blue background
            frame.setLayout(new BorderLayout());

            JLabel statusLabel = new JLabel("Welcome to the HelpDesk Management System", JLabel.CENTER);
            statusLabel.setOpaque(true);
            statusLabel.setBackground(new Color(255, 182, 193)); // Light pink
            statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
            frame.add(statusLabel, BorderLayout.SOUTH);

            JPanel loginPanel = new JPanel(new GridLayout(7, 2, 10, 10));
            loginPanel.setBackground(new Color(173, 216, 230)); // Light blue
            frame.add(loginPanel, BorderLayout.CENTER);

            setupLoginPanel(loginPanel, statusLabel, frame);

            frame.setVisible(true);
        });
    }

    private static void setupLoginPanel(JPanel loginPanel, JLabel statusLabel, JFrame frame) {
        loginPanel.removeAll();

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Checkboxes for Customer and Company login
        JCheckBox customerLoginCheckbox = new JCheckBox("Customer Login");
        JCheckBox companyLoginCheckbox = new JCheckBox("Company Login");

        // Disable the other checkbox when one is selected
        customerLoginCheckbox.addItemListener(e -> companyLoginCheckbox.setEnabled(!customerLoginCheckbox.isSelected()));
        companyLoginCheckbox.addItemListener(e -> customerLoginCheckbox.setEnabled(!companyLoginCheckbox.isSelected()));

        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (customerLoginCheckbox.isSelected()) {
                if (loginCustomer(username, password)) {
                    statusLabel.setText("Welcome, " + loggedInUsername + "!");
                    switchToCustomerDashboard(loginPanel, statusLabel, frame);
                } else {
                    statusLabel.setText("Invalid customer username or password. Try again!");
                }
            } else if (companyLoginCheckbox.isSelected()) {
                if (loginCompany(username, password)) {
                    statusLabel.setText("Welcome, " + loggedInUsername + "!");
                    switchToCompanyDashboard(loginPanel, statusLabel, frame);
                } else {
                    statusLabel.setText("Invalid company username or password. Try again!");
                }
            } else {
                statusLabel.setText("Please select a login option.");
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(customerLoginCheckbox);
        loginPanel.add(companyLoginCheckbox);
        loginPanel.add(loginButton);
        loginPanel.add(exitButton);

        loginPanel.revalidate();
        loginPanel.repaint();
    }

    private static boolean loginCustomer(String username, String password) {
        for (int i = 0; i < customerAccounts.length; i++) {
            if (customerAccounts[i].equals(username) && customerPasswords[i].equals(password)) {
                isLoggedIn = true;
                loggedInRole = "customer";
                loggedInUsername = username;
                return true;
            }
        }
        return false;
    }

    private static boolean loginCompany(String username, String password) {
        for (int i = 0; i < companyAccounts.length; i++) {
            if (companyAccounts[i].equals(username) && companyPasswords[i].equals(password)) {
                isLoggedIn = true;
                loggedInRole = "company";
                loggedInUsername = username;
                return true;
            }
        }
        return false;
    }

    private static void switchToCustomerDashboard(JPanel loginPanel, JLabel statusLabel, JFrame frame) {
        loginPanel.removeAll();

        JButton createTicketButton = new JButton("Create Ticket");
        JButton viewTicketsButton = new JButton("View My Tickets");
        JButton logoutButton = new JButton("Logout");

        createTicketButton.addActionListener(e -> createTicketDialog(statusLabel, frame));
        viewTicketsButton.addActionListener(e -> viewCustomerTickets(statusLabel, frame));
        logoutButton.addActionListener(e -> logout(loginPanel, statusLabel, frame));

        loginPanel.add(createTicketButton);
        loginPanel.add(viewTicketsButton);
        loginPanel.add(logoutButton);

        loginPanel.revalidate();
        loginPanel.repaint();
    }

    private static void switchToCompanyDashboard(JPanel loginPanel, JLabel statusLabel, JFrame frame) {
        loginPanel.removeAll();

        JButton viewAllTicketsButton = new JButton("View All Tickets");
        JButton resolveTicketButton = new JButton("Resolve Ticket");
        JButton logoutButton = new JButton("Logout");

        viewAllTicketsButton.addActionListener(e -> viewAllTickets(statusLabel, frame));
        resolveTicketButton.addActionListener(e -> resolveTicketDialog(statusLabel, frame));
        logoutButton.addActionListener(e -> logout(loginPanel, statusLabel, frame));

        loginPanel.add(viewAllTicketsButton);
        loginPanel.add(resolveTicketButton);
        loginPanel.add(logoutButton);

        loginPanel.revalidate();
        loginPanel.repaint();
    }

    private static void createTicketDialog(JLabel statusLabel, JFrame frame) {
        String issue = JOptionPane.showInputDialog(frame, "Enter your issue:", "Create Ticket", JOptionPane.PLAIN_MESSAGE);
        if (issue != null && !issue.trim().isEmpty()) {
            tickets.add("Open - " + issue);
            ticketOwners.add(loggedInUsername);
            statusLabel.setText("Ticket created successfully!");
        } else {
            statusLabel.setText("Ticket creation cancelled.");
        }
    }

    private static void viewCustomerTickets(JLabel statusLabel, JFrame frame) {
        StringBuilder ticketList = new StringBuilder();
        for (int i = 0; i < tickets.size(); i++) {
            if (ticketOwners.get(i).equals(loggedInUsername)) {
                ticketList.append("Ticket ").append(i + 1).append(": ").append(tickets.get(i)).append("\n");
            }
        }
        if (ticketList.length() == 0) {
            ticketList.append("No tickets found.");
        }
        JOptionPane.showMessageDialog(frame, ticketList.toString(), "My Tickets", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void viewAllTickets(JLabel statusLabel, JFrame frame) {
        StringBuilder ticketList = new StringBuilder();
        for (int i = 0; i < tickets.size(); i++) {
            ticketList.append("Ticket ").append(i + 1).append(": ").append(tickets.get(i)).append(" (Owner: ")
                    .append(ticketOwners.get(i)).append(")\n");
        }
        if (ticketList.length() == 0) {
            ticketList.append("No tickets found.");
        }
        JOptionPane.showMessageDialog(frame, ticketList.toString(), "All Tickets", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void resolveTicketDialog(JLabel statusLabel, JFrame frame) {
        String ticketIdStr = JOptionPane.showInputDialog(frame, "Enter Ticket ID to resolve:", "Resolve Ticket", JOptionPane.PLAIN_MESSAGE);
        try {
            int ticketId = Integer.parseInt(ticketIdStr) - 1;
            if (ticketId >= 0 && ticketId < tickets.size()) {
                tickets.set(ticketId, "Resolved - " + tickets.get(ticketId).split(" - ")[1]);
                statusLabel.setText("Ticket resolved!");
            } else {
                statusLabel.setText("Invalid ticket ID.");
            }
        } catch (NumberFormatException | NullPointerException e) {
            statusLabel.setText("Operation cancelled or invalid input.");
        }
    }

    private static void logout(JPanel loginPanel, JLabel statusLabel, JFrame frame) {
        isLoggedIn = false;
        loggedInRole = "";
        loggedInUsername = "";
        statusLabel.setText("Logged out successfully.");
        setupLoginPanel(loginPanel, statusLabel, frame);
    }
}
