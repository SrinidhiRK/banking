import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private int accountHolderAge;
    private String accountHolderGender;
    private String accountHolderAddress;
    private String accountHolderMobile;  // Changed to Mobile Number
    private double balance;
    private String pin;

    // Constructor to initialize a new account
    public BankAccount(String accountNumber, String accountHolderName, int accountHolderAge, 
                       String accountHolderGender, String accountHolderAddress, 
                       String accountHolderMobile, double initialBalance, String pin) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountHolderAge = accountHolderAge;
        this.accountHolderGender = accountHolderGender;
        this.accountHolderAddress = accountHolderAddress;
        this.accountHolderMobile = accountHolderMobile;
        this.balance = initialBalance;
        this.pin = pin;
    }

    // Method to validate the PIN
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    // Method to deposit money
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid deposit amount.");
        }
    }

    // Method to withdraw money
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid withdrawal amount or insufficient balance.");
        }
    }

    // Method to check the balance
    public double getBalance() {
        return balance;
    }

    // Method to display account details
    public String getAccountDetails() {
        return "Account Number: " + accountNumber + "\n" +
               "Account Holder: " + accountHolderName + "\n" +
               "Age: " + accountHolderAge + "\n" +
               "Gender: " + accountHolderGender + "\n" +
               "Mobile Number: " + accountHolderMobile + "\n" +  // Updated to Mobile Number
               "Address: " + accountHolderAddress + "\n" +
               "Balance: " + formatCurrency(balance);
    }

    // Method to format the currency in Indian Rupees
    public static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return formatter.format(amount);
    }
}

public class BankAccountManagementUI extends JFrame implements ActionListener {
    private BankAccount account;

    private JTextArea outputArea;
    private JButton createAccountButton, depositButton, withdrawButton, checkBalanceButton, displayDetailsButton;

    public BankAccountManagementUI() {
        // Frame properties
        setTitle("Welcome to Blackrock Bank");
        setSize(800, 600);  // Increased window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // GUI components
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Blackrock Bank", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(this);
        welcomePanel.add(createAccountButton, BorderLayout.SOUTH);

        // Adding components to the frame
        add(welcomePanel, BorderLayout.CENTER);

        // Set a better look and feel
        setLookAndFeel();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createAccountButton) {
            // Open a new interface for account creation
            SwingUtilities.invokeLater(() -> {
                NewAccountUI newAccountUI = new NewAccountUI(this);
                newAccountUI.setVisible(true);
            });
        } else if (account != null) {
            if (e.getSource() == depositButton) {
                validatePinAndExecute(() -> {
                    String depositAmountInput = JOptionPane.showInputDialog(this, "Enter deposit amount (₹):");
                    if (depositAmountInput != null && !depositAmountInput.trim().isEmpty()) {
                        double depositAmount = Double.parseDouble(depositAmountInput);
                        account.deposit(depositAmount);
                        outputArea.setText("Deposited: " + BankAccount.formatCurrency(depositAmount));
                    }
                });
            } else if (e.getSource() == withdrawButton) {
                validatePinAndExecute(() -> {
                    String withdrawAmountInput = JOptionPane.showInputDialog(this, "Enter withdrawal amount (₹):");
                    if (withdrawAmountInput != null && !withdrawAmountInput.trim().isEmpty()) {
                        double withdrawAmount = Double.parseDouble(withdrawAmountInput);
                        account.withdraw(withdrawAmount);
                        outputArea.setText("Withdrawn: " + BankAccount.formatCurrency(withdrawAmount));
                    }
                });
            } else if (e.getSource() == checkBalanceButton) {
                validatePinAndExecute(() -> {
                    outputArea.setText("Current Balance: " + BankAccount.formatCurrency(account.getBalance()));
                });
            } else if (e.getSource() == displayDetailsButton) {
                validatePinAndExecute(() -> {
                    outputArea.setText(account.getAccountDetails());
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please create an account first.");
        }
    }

    // Method to validate PIN and execute the provided action if PIN is valid
    private void validatePinAndExecute(Runnable action) {
        String inputPin = JOptionPane.showInputDialog(this, "Enter Security PIN:");
        if (inputPin != null && account.validatePin(inputPin)) {
            action.run();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid PIN. Access Denied.");
        }
    }

    // Method to set the account details
    public void setAccount(BankAccount account) {
        this.account = account;

        // After account creation, set up the main interface
        setupMainInterface();
    }

    // Set up the main interface after account creation
    private void setupMainInterface() {
        getContentPane().removeAll();
        setTitle("Blackrock Bank - Account Management");
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Buttons
        depositButton = new JButton("Deposit Money");
        withdrawButton = new JButton("Withdraw Money");
        checkBalanceButton = new JButton("Check Balance");
        displayDetailsButton = new JButton("Display Account Details");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(depositButton, gbc);
        gbc.gridy++;
        formPanel.add(withdrawButton, gbc);
        gbc.gridy++;
        formPanel.add(checkBalanceButton, gbc);
        gbc.gridy++;
        formPanel.add(displayDetailsButton, gbc);

        // Output area
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Adding components to the frame
        add(formPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Adding action listeners
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        checkBalanceButton.addActionListener(this);
        displayDetailsButton.addActionListener(this);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankAccountManagementUI frame = new BankAccountManagementUI();
            frame.setVisible(true);
        });
    }
}

// New Account Creation Interface
class NewAccountUI extends JFrame implements ActionListener {
    private JTextField accountHolderNameField, accountHolderAgeField, 
                       accountHolderGenderField, accountHolderMobileField;  // Updated field
    private JTextArea accountHolderAddressArea;
    private JPasswordField pinField;
    private JButton createButton;
    private BankAccountManagementUI mainUI;

    public NewAccountUI(BankAccountManagementUI mainUI) {
        this.mainUI = mainUI;

        // Frame properties
        setTitle("Create New Account");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // GUI components
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields
        JLabel nameLabel = new JLabel("Account Holder Name:");
        accountHolderNameField = new JTextField(20);
        addFormField(formPanel, nameLabel, accountHolderNameField, gbc, 0);

        JLabel ageLabel = new JLabel("Account Holder Age:");
        accountHolderAgeField = new JTextField(5);
        addFormField(formPanel, ageLabel, accountHolderAgeField, gbc, 1);

        JLabel genderLabel = new JLabel("Account Holder Gender:");
        accountHolderGenderField = new JTextField(10);
        addFormField(formPanel, genderLabel, accountHolderGenderField, gbc, 2);

        JLabel mobileLabel = new JLabel("Mobile Number:");  // Changed to Mobile Number
        accountHolderMobileField = new JTextField(10);  // Updated to 10 digits
        addFormField(formPanel, mobileLabel, accountHolderMobileField, gbc, 3);

        JLabel addressLabel = new JLabel("Address:");
        accountHolderAddressArea = new JTextArea(3, 20);
        JScrollPane addressScrollPane = new JScrollPane(accountHolderAddressArea);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(addressScrollPane, gbc);

        JLabel pinLabel = new JLabel("Security PIN:");
        pinField = new JPasswordField(6);
        addFormField(formPanel, pinLabel, pinField, gbc, 5);

        createButton = new JButton("Create Account");
        createButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(createButton, gbc);

        // Adding components to the frame
        add(formPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
        gbc.gridwidth = 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            // Generate a random account number
            String accountNumber = generateAccountNumber();
            String accountHolderName = accountHolderNameField.getText();
            int accountHolderAge = Integer.parseInt(accountHolderAgeField.getText());
            String accountHolderGender = accountHolderGenderField.getText();
            String accountHolderAddress = accountHolderAddressArea.getText();
            String accountHolderMobile = accountHolderMobileField.getText();
            double initialBalance = 0.0;
            String pin = new String(pinField.getPassword());

            // Validate the mobile number
            if (!isValidMobileNumber(accountHolderMobile)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit mobile number.");
                return;
            }

            // Create a new BankAccount object
            BankAccount newAccount = new BankAccount(accountNumber, accountHolderName, accountHolderAge, 
                                                     accountHolderGender, accountHolderAddress, 
                                                     accountHolderMobile, initialBalance, pin);

            // Pass the account object to the main UI
            mainUI.setAccount(newAccount);

            // Close the account creation window
            dispose();
        }
    }

    // Method to validate if the mobile number is 10 digits
    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    private String generateAccountNumber() {
        Random rand = new Random();
        int accountNumber = 100000 + rand.nextInt(900000);
        return String.valueOf(accountNumber);
    }
}
