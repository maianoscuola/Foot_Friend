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
    private java.util.List<Match> matches;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Foot_Friend::new);
    }

    public Foot_Friend() {
        try {
        users = loadUsers();
        matches = loadMatches(); 
    } catch (Exception e) {
        users = new HashMap<>();
        matches = new java.util.ArrayList<>(); 
        System.out.println("Errore durante il caricamento: " + e.getMessage());
    }

    frame = new JFrame("Foot Friend");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    mainPanel.add(createLoginPanel(), "Login");
    mainPanel.add(createCompleteProfilePanel(), "CompleteProfile");
    mainPanel.add(createMainScreen(), "MainScreen");
    mainPanel.add(createPartitePanel(), "Partite");
    mainPanel.add(createCreateMatchPanel(), "CreateMatch");

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
        profiloButton.addActionListener(e -> { 
            updateProfiloPanel();
        ((CardLayout) screens.getLayout()).show(screens, "Profilo");
        });

        navBar.add(homeButton);
        navBar.add(partiteButton);
        navBar.add(profiloButton);

        mainScreen.add(screens, BorderLayout.CENTER);
        mainScreen.add(navBar, BorderLayout.SOUTH);

        return mainScreen;
    }
    private void updateProfiloPanel() {
    JPanel mainScreen = (JPanel) mainPanel.getComponent(2);
    JPanel screens = (JPanel) mainScreen.getComponent(0);
    CardLayout screensLayout = (CardLayout) screens.getLayout();

    screens.remove(2); 
    screens.add(createProfiloPanel(), "Profilo");

    screensLayout.show(screens, "Profilo");
}


    private void updateHomePanel() {
    
    JPanel mainScreen = (JPanel) mainPanel.getComponent(2); 
    
   
    JPanel screens = (JPanel) mainScreen.getComponent(0); 
    CardLayout screensLayout = (CardLayout) screens.getLayout();
    
    
    screens.remove(0);

   
    screens.add(createHomePanel(), "Home");

    
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
    JPanel panel = new JPanel(new BorderLayout());
    JPanel matchesPanel = new JPanel(new GridLayout(0, 1));
    JScrollPane scrollPane = new JScrollPane(matchesPanel);
    JButton createMatchButton = new JButton("Crea Partita");

    for (Match match : matches) {
        JPanel matchPanel = new JPanel(new GridLayout(6, 1));
        matchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel ownerLabel = new JLabel("Proprietario: " + match.getOwner());
        JLabel locationLabel = new JLabel("Luogo: " + match.getLocation());
        JLabel dateLabel = new JLabel("Data: " + match.getDate());
        JLabel modeLabel = new JLabel("Modalità: " + match.getMode());
        JLabel spotsLabel = new JLabel("Posti disponibili: " + match.getAvailableSpots());

        JButton joinButton = new JButton("Unisciti");
        joinButton.setEnabled(match.getAvailableSpots() > 0); 
        joinButton.addActionListener(e -> {
            match.joinMatch();
            users.get(currentUser).addXp(5); 
            saveUsers();
            updatePartitePanel();
        });

        matchPanel.add(ownerLabel);
        matchPanel.add(locationLabel);
        matchPanel.add(dateLabel);
        matchPanel.add(modeLabel);
        matchPanel.add(spotsLabel);
        matchPanel.add(joinButton);

        matchesPanel.add(matchPanel);
    }

    createMatchButton.addActionListener(e -> cardLayout.show(mainPanel, "CreateMatch"));
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(createMatchButton, BorderLayout.SOUTH);

    return panel;
}

private void updatePartitePanel() {
    JPanel mainScreen = (JPanel) mainPanel.getComponent(2);
    JPanel screens = (JPanel) mainScreen.getComponent(0);
    CardLayout screensLayout = (CardLayout) screens.getLayout();

    screens.remove(1); 
    screens.add(createPartitePanel(), "Partite");

    screensLayout.show(screens, "Partite");
}

private JPanel createProfiloPanel() {
    JPanel panel = new JPanel(new GridLayout(4, 1));

    if (currentUser == null || !users.containsKey(currentUser)) {
        panel.add(new JLabel("Errore: utente non trovato!", SwingConstants.CENTER));
        return panel;
    }

    User user = users.get(currentUser);
    JLabel emailLabel = new JLabel("Email: " + user.getEmail(), SwingConstants.CENTER);
    JLabel nicknameLabel = new JLabel("Nickname: " + user.getNickname(), SwingConstants.CENTER);
    JLabel ageLabel = new JLabel("Età: " + user.getAge(), SwingConstants.CENTER);

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(e -> {
        currentUser = null; 
        cardLayout.show(mainPanel, "Login"); 
    });

    panel.add(emailLabel);
    panel.add(nicknameLabel);
    panel.add(ageLabel);
    panel.add(logoutButton);

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
    private JPanel createCreateMatchPanel() {
    JPanel panel = new JPanel(new GridLayout(5, 2));

    JTextField locationField = new JTextField();
    JTextField dateField = new JTextField();
    JComboBox<String> modeBox = new JComboBox<>(new String[]{"5 vs 5", "7 vs 7", "11 vs 11"});
    JTextField maxPlayersField = new JTextField();
    JButton createButton = new JButton("Crea");

    panel.add(new JLabel("Luogo:"));
    panel.add(locationField);
    panel.add(new JLabel("Data:"));
    panel.add(dateField);
    panel.add(new JLabel("Modalità:"));
    panel.add(modeBox);
    panel.add(new JLabel("Numero massimo di giocatori:"));
    panel.add(maxPlayersField);
    panel.add(new JLabel());
    panel.add(createButton);

    createButton.addActionListener(e -> {
        try {
            String location = locationField.getText();
            String date = dateField.getText();
            String mode = (String) modeBox.getSelectedItem();
            int maxPlayers = Integer.parseInt(maxPlayersField.getText());

            Match newMatch = new Match(currentUser, location, date, mode, maxPlayers);
            matches.add(newMatch);
            saveMatches(); 
            updatePartitePanel(); 
            cardLayout.show(mainPanel, "Partite");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Inserisci un numero valido per i giocatori.");
        }
    });
    
    return panel;
}
    private void saveMatches() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("matches.dat"))) {
        oos.writeObject(matches);
    } catch (IOException e) {
        System.out.println("Errore durante il salvataggio delle partite: " + e.getMessage());
    }
}

    private java.util.List<Match> loadMatches() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("matches.dat"))) {
        return (java.util.List<Match>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Nessun dato sulle partite trovato.");
        return new java.util.ArrayList<>();
    }
}
    
}