import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordStrengthChecker {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Password Strength Checker");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        frame.setLocationRelativeTo(null);

        JLabel questionLabel = new JLabel("Do you want to check password strength?");
        questionLabel.setBounds(50, 20, 300, 25);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(questionLabel);

        JRadioButton yesButton = new JRadioButton("Yes");
        JRadioButton noButton = new JRadioButton("No");
        yesButton.setBounds(100, 50, 60, 20);
        noButton.setBounds(200, 50, 60, 20);
        yesButton.setBackground(Color.LIGHT_GRAY);
        noButton.setBackground(Color.LIGHT_GRAY);

        ButtonGroup group = new ButtonGroup();
        group.add(yesButton);
        group.add(noButton);
        frame.add(yesButton);
        frame.add(noButton);

        yesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                openPasswordFrame();
            }
        });

        noButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); 
            }
        });

        frame.setVisible(true);
    }

    public static void openPasswordFrame() {
        JFrame passwordFrame = new JFrame("Check Password Strength");
        passwordFrame.setSize(400, 300);
        passwordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passwordFrame.setLayout(null);
        passwordFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
        passwordFrame.setLocationRelativeTo(null);

        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordLabel.setBounds(50, 40, 150, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(180, 40, 150, 30);
        passwordFrame.add(passwordField);

        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(180, 80, 150, 20);
        showPassword.setBackground(Color.LIGHT_GRAY);
        passwordFrame.add(showPassword);

        JProgressBar strengthBar = new JProgressBar(0, 100);
        strengthBar.setBounds(50, 110, 280, 20);
        strengthBar.setStringPainted(true);
        passwordFrame.add(strengthBar);

        JButton submitButton = new JButton("Check Strength");
        submitButton.setBounds(130, 150, 150, 30);
        passwordFrame.add(submitButton);

        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                int strength = calculateStrength(password);
                strengthBar.setValue(strength);

                if (strength < 55) {
                    strengthBar.setForeground(Color.RED);
                    strengthBar.setString("Weak");
                } else if (strength < 85) {
                    strengthBar.setForeground(Color.ORANGE);
                    strengthBar.setString("Medium");
                } else {
                    strengthBar.setForeground(Color.GREEN);
                    strengthBar.setString("Strong");
                }

                if (strength < 95) {
                    String suggestion = getSuggestions(password);
                    JOptionPane.showMessageDialog(passwordFrame, "For ensuring your password strength, you can use this:\n" + suggestion, "Suggestion", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        passwordFrame.setVisible(true);
    }

    public static int calculateStrength(String password) {
        int length = password.length();
        int upperCount = 0, lowerCount = 0, digitCount = 0, specialCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) upperCount++;
            if (Character.isLowerCase(c)) lowerCount++;
            if (Character.isDigit(c)) digitCount++;
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }

        int strength = 0;
        if (upperCount >= 2) {
            strength += 15;
        } else if (upperCount == 1) {
            strength += 7;
        }

        if (lowerCount >= 2) {
            strength += 15;
        } else if (lowerCount == 1) {
            strength += 7;
        }

        if (digitCount > 1) {
            strength += 20;
        } else if (digitCount == 1) {
            strength += 10;
        }

        if (specialCount > 1) {
            strength += 20;
        } else if (specialCount == 1) {
            strength += 10;
        }

        // Password length evaluation
        if (length <= 4) strength += 10;
        else if (length <= 8) strength += 20;
        else strength += 30;

        return Math.min(strength, 100);
    }

    public static String getSuggestions(String password) {
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        int upperCount = 0, lowerCount = 0, digitCount = 0, specialCount = 0;
        int length = password.length();

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
                upperCount++;
            }
            if (Character.isLowerCase(c)) {
                hasLower = true;
                lowerCount++;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
                digitCount++;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
                specialCount++;
            }
        }

        StringBuilder suggestion = new StringBuilder();
        if (!hasUpper) suggestion.append("Uppercase Letter, ");
        if (!hasLower) suggestion.append("Lowercase Letter, ");
        if (!hasDigit) suggestion.append("Number, ");
        if (!hasSpecial) suggestion.append("Special Character, ");

        // Suggest at least 2 of each type if only 1 is used
        if (upperCount == 1) suggestion.append("Use at least 2 Uppercase Letters, ");
        if (lowerCount == 1) suggestion.append("Use at least 2 Lowercase Letters, ");
        if (digitCount == 1) suggestion.append("Use at least 2 Digits, ");
        if (specialCount == 1) suggestion.append("Use at least 2 Special Characters, ");

        // Suggest length improvements
        if (length < 9) suggestion.append("Use a password with at least 9 characters, ");

        // Removing the last comma and space
        if (suggestion.length() > 0) {
            suggestion.setLength(suggestion.length() - 2); 
        }

        return suggestion.toString();
    }
}
