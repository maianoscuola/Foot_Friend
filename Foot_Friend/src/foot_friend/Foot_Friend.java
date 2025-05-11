package foot_friend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Foot_Friend {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, User> users;
    private String currentUser;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Foot_Friend::new);
    }

    public Foot_Friend() {
        users = loadUsers();
        frame = new JFrame("Foot Friend");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createCompleteProfilePanel(), "CompleteProfile");
        mainPanel.add(createHomePanel(), "Home");

        frame.add(mainPanel);
        frame.setVisible(true);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Email:")); 
        panel.add(emailField);
        panel.add(new JLabel("Password:")); 
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (users.containsKey(email) && users.get(email).getPassword().equals(password)) {
                currentUser = email;
                User user = users.get(email);
                if (user.isProfileComplete()) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    navigateToHome();
                } else {
                    cardLayout.show(mainPanel, "CompleteProfile");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password.");
            }
        });

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (!users.containsKey(email)) {
                users.put(email, new User(email, password));
                saveUsers();
                JOptionPane.showMessageDialog(frame, "Registration successful! Please log in.");
            } else {
                JOptionPane.showMessageDialog(frame, "Email already registered.");
            }
        });

        return panel;
    }

    private JPanel createCompleteProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JTextField nicknameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField roleField = new JTextField();
        JButton saveButton = new JButton("Save");

        panel.add(new JLabel("Nickname:")); 
        panel.add(nicknameField);
        panel.add(new JLabel("Age:")); 
        panel.add(ageField);
        panel.add(new JLabel("Role:")); 
        panel.add(roleField);
        panel.add(saveButton);

        saveButton.addActionListener(e -> {
            User user = users.get(currentUser);
            user.setNickname(nicknameField.getText());
            user.setAge(Integer.parseInt(ageField.getText()));
            user.setRole(roleField.getText());
            saveUsers();
            JOptionPane.showMessageDialog(frame, "Profile completed!");
            navigateToHome();
        });

        return panel;
    }

private JPanel createHomePanel() {
    JPanel panel = new JPanel(new GridLayout(5, 1));

    JLabel nicknameLabel = new JLabel("", SwingConstants.CENTER);
    JLabel ageLabel = new JLabel("", SwingConstants.CENTER);
    JLabel roleLabel = new JLabel("", SwingConstants.CENTER);
    JProgressBar xpBar = new JProgressBar(0, 15);

    // Aggiorna dinamicamente i dati del pannello
    SwingUtilities.invokeLater(() -> {
        User user = users.get(currentUser);
        if (user != null) {
            nicknameLabel.setText("Nickname: " + user.getNickname());
            ageLabel.setText("Age: " + user.getAge());
            roleLabel.setText("Role: " + user.getRole());
            int xp = user.getXp();
            int level = user.getLevel();
            xpBar.setValue(xp % 15);
            xpBar.setString("Level: " + level + " | XP: " + xp + "/15");
            xpBar.setStringPainted(true);
        }
    });

    panel.add(nicknameLabel);
    panel.add(ageLabel);
    panel.add(roleLabel);
    panel.add(xpBar);

    return panel;
}

    private void navigateToHome() {
    mainPanel.remove(mainPanel.getComponent(2)); // Rimuove il vecchio pannello Home
    mainPanel.add(createHomePanel(), "Home");   // Aggiunge il nuovo pannello Home
    cardLayout.show(mainPanel, "Home");
}

    private Map<String, User> loadUsers() {
        Map<String, User> loadedUsers = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            loadedUsers = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing user data found.");
        }
        return loadedUsers;
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}