package foot_friend;

import javax.swing.*;
import java.awt.*;
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
        try {
            users = loadUsers();
        } catch (Exception e) {
            users = new HashMap<>();
            System.out.println("Errore durante il caricamento degli utenti: " + e.getMessage());
        }

        frame = new JFrame("Foot Friend");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        try {
            mainPanel.add(createLoginPanel(), "Login");
            mainPanel.add(createCompleteProfilePanel(), "CompleteProfile");
            mainPanel.add(createMainScreen(), "MainScreen");
        } catch (Exception e) {
            System.out.println("Errore durante l'inizializzazione delle schermate: " + e.getMessage());
        }

        frame.add(mainPanel);
        frame.setVisible(true);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createMainScreen() {
        JPanel mainScreen = new JPanel(new BorderLayout());
        JPanel screens = new JPanel(new CardLayout());
        screens.add(createHomePanel(), "Home");
        screens.add(createPartitePanel(), "Partite");
        screens.add(createProfiloPanel(), "Profilo");

        JPanel navBar = new JPanel(new GridLayout(1, 3));
        JButton homeButton = new JButton("Home");
        JButton partiteButton = new JButton("Partite");
        JButton profiloButton = new JButton("Profilo");

        homeButton.addActionListener(e -> ((CardLayout) screens.getLayout()).show(screens, "Home"));
        partiteButton.addActionListener(e -> ((CardLayout) screens.getLayout()).show(screens, "Partite"));
        profiloButton.addActionListener(e -> ((CardLayout) screens.getLayout()).show(screens, "Profilo"));

        navBar.add(homeButton);
        navBar.add(partiteButton);
        navBar.add(profiloButton);

        mainScreen.add(screens, BorderLayout.CENTER);
        mainScreen.add(navBar, BorderLayout.SOUTH);

        return mainScreen;
    }

    private void updateHomePanel() {
    // Trova il pannello principale della schermata principale
    JPanel mainScreen = (JPanel) mainPanel.getComponent(2); // La schermata "MainScreen"
    
    // Ottieni il pannello "screens" che contiene la home
    JPanel screens = (JPanel) mainScreen.getComponent(0); // Primo componente in "MainScreen"
    CardLayout screensLayout = (CardLayout) screens.getLayout();
    
    // Rimuovi il vecchio pannello Home
    screens.remove(0);

    // Aggiungi una nuova versione del pannello Home aggiornato
    screens.add(createHomePanel(), "Home");

    // Mostra il pannello Home aggiornato
    screensLayout.show(screens, "Home");
}

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        if (currentUser == null || !users.containsKey(currentUser)) {
            panel.add(new JLabel("Errore: utente non trovato!"));
            return panel;
        }

        User user = users.get(currentUser);
        JLabel nicknameLabel = new JLabel("Nickname: " + user.getNickname(), SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("Age: " + user.getAge(), SwingConstants.CENTER);
        JLabel roleLabel = new JLabel("Role: " + user.getRole(), SwingConstants.CENTER);

        int xp = user.getXp();
        int level = user.getLevel();
        JProgressBar xpBar = new JProgressBar(0, 15);
        xpBar.setValue(xp % 15);
        xpBar.setString("Level: " + level + " | XP: " + xp + "/15");
        xpBar.setStringPainted(true);

        panel.add(nicknameLabel);
        panel.add(ageLabel);
        panel.add(roleLabel);
        panel.add(xpBar);

        return panel;
    }

    private JPanel createPartitePanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Partite", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label);
        return panel;
    }

    private JPanel createProfiloPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Profilo", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));

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

            System.out.println("Attempting login for email: " + email);
            if (users.containsKey(email) && users.get(email).getPassword().equals(password)) {
                currentUser = email;
                System.out.println("Login successful for user: " + currentUser);
                User user = users.get(email);

                if (user.isProfileComplete()) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    updateHomePanel(); // Aggiorna la home
                    cardLayout.show(mainPanel, "MainScreen");
                } else {
                    cardLayout.show(mainPanel, "CompleteProfile");
                }
            } else {
                System.out.println("Login failed for email: " + email);
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
            cardLayout.show(mainPanel, "MainScreen");
        });

        return panel;
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