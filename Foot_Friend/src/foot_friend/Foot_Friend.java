package foot_friend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Foot_Friend extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, User> users;
    private User currentUser;
    private List<Match> matches;

    private static final String USERS_FILE = "users.dat";
    private static final String MATCHES_FILE = "matches.dat";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Foot_Friend().setVisible(true));
    }
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blu
    private static final Color SECONDARY_COLOR = new Color(255, 193, 7); // Giallo
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Grigio chiaro
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Testo nero

    public Foot_Friend() {
        try {
            users = loadUsers();
            matches = loadMatches();
        } catch (Exception e) {
            users = new HashMap<>();
            matches = new ArrayList<>();
            System.out.println("Errore durante il caricamento: " + e.getMessage());
        }

        setTitle("Foot Friend");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 640);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createCompleteProfilePanel(), "CompleteProfile");
        mainPanel.add(createMainScreen(), "MainScreen");
        mainPanel.add(createMatchListPanel(), "MatchList");
        mainPanel.add(createCreateMatchPanel(), "CreateMatch");
        mainPanel.add(createProfilePanel(), "Profile");
        mainPanel.add(createMatchDetailPanel(), "MatchDetail"); // Nuovo pannello per i dettagli della partita

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    private Map<String, User> loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<String, User>) ois.readObject();
            }
        } else {
            return new HashMap<>();
        }
    }

    private void saveUsers(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio degli utenti: " + e.getMessage());
        }
    }

    private List<Match> loadMatches() throws IOException, ClassNotFoundException {
        File file = new File(MATCHES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Match>) ois.readObject();
            }
        } else {
            return new ArrayList<>();
        }
    }

    private void saveMatches(List<Match> matches) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MATCHES_FILE))) {
            oos.writeObject(matches);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio delle partite: " + e.getMessage());
        }
    }
    private JButton createRoundedButton(String text, Color bgColor, Color textColor) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Angoli arrotondati
            g2.dispose();
            super.paintComponent(g);
        }

       
    };
    button.setForeground(textColor);
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);
    button.setOpaque(false);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    return button;
}

  private JPanel createLoginPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(BACKGROUND_COLOR); // Sfondo personalizzato

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel titleLabel = new JLabel("Benvenuto su Foot Friend!", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(TEXT_COLOR); // Colore del testo

    JTextField emailField = new JTextField();
    emailField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR)); // Bordo blu

    JPasswordField passwordField = new JPasswordField();
    passwordField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR)); // Bordo blu

    JButton loginButton = createRoundedButton("Login", PRIMARY_COLOR, Color.WHITE);
    JButton registerButton = createRoundedButton("Registrati", SECONDARY_COLOR, TEXT_COLOR);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    panel.add(titleLabel, gbc);

    gbc.gridy++;
    gbc.gridwidth = 1;
    panel.add(new JLabel("Email:"), gbc);

    gbc.gridx = 1;
    panel.add(emailField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Password:"), gbc);

    gbc.gridx = 1;
    panel.add(passwordField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    panel.add(loginButton, gbc);

    gbc.gridy++;
    panel.add(registerButton, gbc);

    loginButton.addActionListener(e -> {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            if (currentUser.isProfileComplete()) {
                updateHomeScreen();
                showPanel("MainScreen");
            } else {
                showPanel("CompleteProfile");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenziali non valide.", "Errore di Login", JOptionPane.ERROR_MESSAGE);
        }
    });

    registerButton.addActionListener(e -> showPanel("Register"));

    return panel;
}

    private JPanel createRegisterPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(BACKGROUND_COLOR); // Sfondo personalizzato

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel titleLabel = new JLabel("Registrazione", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(TEXT_COLOR); // Colore del testo

    JTextField emailField = new JTextField();
    emailField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR)); // Bordo blu

    JPasswordField passwordField = new JPasswordField();
    passwordField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR)); // Bordo blu

    JButton registerButton = createRoundedButton("Registrati", PRIMARY_COLOR, Color.WHITE);
    JButton backButton = createRoundedButton("Indietro", SECONDARY_COLOR, TEXT_COLOR);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    panel.add(titleLabel, gbc);

    gbc.gridy++;
    gbc.gridwidth = 1;
    panel.add(new JLabel("Email:"), gbc);

    gbc.gridx = 1;
    panel.add(emailField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Password:"), gbc);

    gbc.gridx = 1;
    panel.add(passwordField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    panel.add(registerButton, gbc);

    gbc.gridy++;
    panel.add(backButton, gbc);

    registerButton.addActionListener(e -> {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        if (users.containsKey(email)) {
            JOptionPane.showMessageDialog(this, "Email già registrata.", "Errore di Registrazione", JOptionPane.ERROR_MESSAGE);
        } else {
            User newUser = new User(email, password);
            users.put(email, newUser);
            saveUsers(users);
            currentUser = newUser;
            showPanel("CompleteProfile");
        }
    });

    backButton.addActionListener(e -> showPanel("Login"));

    return panel;
}
    private JPanel createCompleteProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JLabel titleLabel = new JLabel("Completa il tuo profilo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField nicknameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField roleField = new JTextField();
        JButton saveButton = new JButton("Salva Profilo");

        panel.add(titleLabel);
        panel.add(new JLabel()); // Placeholder
        panel.add(new JLabel("Nickname:"));
        panel.add(nicknameField);
        panel.add(new JLabel("Età:"));
        panel.add(ageField);
        panel.add(new JLabel("Ruolo preferito:"));
        panel.add(roleField);
        panel.add(new JLabel()); // Placeholder
        panel.add(saveButton);

        saveButton.addActionListener(e -> {
            if (currentUser != null) {
                String nickname = nicknameField.getText();
                try {
                    int age = Integer.parseInt(ageField.getText());
                    String role = roleField.getText();
                    currentUser.setNickname(nickname);
                    currentUser.setAge(age);
                    currentUser.setRole(role);
                    saveUsers(users);
                    updateHomeScreen();
                    showPanel("MainScreen");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Inserisci un'età valida.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Errore: utente non loggato.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createMainScreen() {
        JPanel mainScreen = new JPanel(new BorderLayout());
        JPanel screens = new JPanel(new CardLayout());
        screens.add(createHomeScreen(), "Home");
        screens.add(createMatchListPanel(), "MatchList");
        screens.add(createProfilePanel(), "Profile");

        JPanel navBar = new JPanel(new GridLayout(1, 3));
        JButton homeButton = new JButton("Home");
        JButton matchListButton = new JButton("Partite");
        JButton profileButton = new JButton("Profilo");

        homeButton.addActionListener(e -> {
            updateHomeScreen();
            ((CardLayout) screens.getLayout()).show(screens, "Home");
        });
        matchListButton.addActionListener(e -> {
            updateMatchListPanel();
            ((CardLayout) screens.getLayout()).show(screens, "MatchList");
        });
        profileButton.addActionListener(e -> {
            updateProfilePanel();
            ((CardLayout) screens.getLayout()).show(screens, "Profile");
        });

        navBar.add(homeButton);
        navBar.add(matchListButton);
        navBar.add(profileButton);

        mainScreen.add(screens, BorderLayout.CENTER);
        mainScreen.add(navBar, BorderLayout.SOUTH);

        return mainScreen;
    }

    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        JLabel nicknameLabel = new JLabel("Nickname:", SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("Età:", SwingConstants.CENTER);
        JLabel roleLabel = new JLabel("Ruolo:", SwingConstants.CENTER);
        JLabel levelLabel = new JLabel("Livello:", SwingConstants.CENTER);
        JProgressBar xpBar = new JProgressBar(0, 15);
        xpBar.setStringPainted(true);

        panel.add(nicknameLabel);
        panel.add(ageLabel);
        panel.add(roleLabel);
        panel.add(levelLabel);
        panel.add(xpBar);

        return panel;
    }

    private void updateHomeScreen() {
        JPanel mainScreen = (JPanel) mainPanel.getComponent(3);
        JPanel homeScreen = (JPanel) ((JPanel) mainScreen.getComponent(0)).getComponent(0); // Naviga alla schermata Home
        if (currentUser != null) {
            ((JLabel) homeScreen.getComponent(0)).setText("Nickname: " + currentUser.getNickname());
            ((JLabel) homeScreen.getComponent(1)).setText("Età: " + currentUser.getAge());
            ((JLabel) homeScreen.getComponent(2)).setText("Ruolo: " + currentUser.getRole());
            ((JLabel) homeScreen.getComponent(3)).setText("Livello: " + currentUser.getLevel());
            ((JProgressBar) homeScreen.getComponent(4)).setValue(currentUser.getXp() % 15);
            ((JProgressBar) homeScreen.getComponent(4)).setString("XP: " + currentUser.getXp() + "/15");
        } else {
            ((JLabel) homeScreen.getComponent(0)).setText("Nickname:");
            ((JLabel) homeScreen.getComponent(1)).setText("Età:");
            ((JLabel) homeScreen.getComponent(2)).setText("Ruolo:");
            ((JLabel) homeScreen.getComponent(3)).setText("Livello:");
            ((JProgressBar) homeScreen.getComponent(4)).setValue(0);
            ((JProgressBar) homeScreen.getComponent(4)).setString("XP: 0/15");
        }
    }

    private JPanel createMatchListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(matchesPanel);
        JButton createMatchButton = new JButton("Crea Partita");

        createMatchButton.addActionListener(e -> showPanel("CreateMatch"));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createMatchButton, BorderLayout.SOUTH);

        return panel;
    }

    private void updateMatchListPanel() {
        JPanel mainScreen = (JPanel) mainPanel.getComponent(3);
        JPanel matchListScreen = (JPanel) ((JPanel) mainScreen.getComponent(0)).getComponent(1); // Naviga alla lista partite
        JPanel matchesPanel = (JPanel) ((JScrollPane) matchListScreen.getComponent(0)).getViewport().getComponent(0);
        matchesPanel.removeAll();

        for (Match match : matches) {
            JPanel matchInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            matchInfoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            matchInfoPanel.add(new JLabel("Luogo: " + match.getLocation()));
            matchInfoPanel.add(new JLabel("Data: " + match.getDate()));
            matchInfoPanel.add(new JLabel("Modalità: " + match.getMode()));
            matchInfoPanel.add(new JLabel(match.getCurrentPlayers() + "/" + match.getMaxPlayers() + " giocatori"));
            JButton detailsButton = new JButton("Dettagli");
            detailsButton.addActionListener(e -> showMatchDetails(match));
            matchInfoPanel.add(detailsButton);
            matchesPanel.add(matchInfoPanel);
        }

        matchesPanel.revalidate();
        matchesPanel.repaint();
    }

    private JPanel createMatchDetailPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        JLabel locationLabel = new JLabel("Luogo:");
        JLabel dateLabel = new JLabel("Data:");
        JLabel modeLabel = new JLabel("Modalità:");
        JLabel playersLabel = new JLabel("Giocatori:");
        JButton joinButton = new JButton("Unisciti");
        JButton backButton = new JButton("Indietro");

        panel.add(locationLabel);
        panel.add(dateLabel);
        panel.add(modeLabel);
        panel.add(playersLabel);
        panel.add(joinButton);
        panel.add(backButton);

        joinButton.addActionListener(e -> {
            if (currentMatch != null) {
                boolean joined = currentMatch.joinMatch(currentUser.getEmail());
                if (joined) {
                    saveMatches(matches);
                    updateMatchDetails(currentMatch);
                    updateMatchListPanel(); // Aggiorna la lista dopo l'adesione
                    JOptionPane.showMessageDialog(this, "Unito con successo alla partita!");
                } else {
                    JOptionPane.showMessageDialog(this, "Non puoi unirti alla partita. Potrebbe essere piena o sei già unito.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(e -> {
            // Aggiorna il pannello della lista delle partite prima di tornare indietro
            updateMatchListPanel();
            // Torna al pannello "MatchList"
            showPanel("MatchList");
        });
        return panel;
    }
    private Match currentMatch;

    private void showMatchDetails(Match match) {
        currentMatch = match;
        updateMatchDetails(match);
        showPanel("MatchDetail");
    }

    private void updateMatchDetails(Match match) {
        JPanel detailPanel = (JPanel) mainPanel.getComponent(7); // Ottieni il pannello dei dettagli
        ((JLabel) detailPanel.getComponent(0)).setText("Luogo: " + match.getLocation());
        ((JLabel) detailPanel.getComponent(1)).setText("Data: " + match.getDate());
        ((JLabel) detailPanel.getComponent(2)).setText("Modalità: " + match.getMode());
        ((JLabel) detailPanel.getComponent(3)).setText("Giocatori: " + match.getCurrentPlayers() + "/" + match.getMaxPlayers());
        JButton joinButton = (JButton) detailPanel.getComponent(4);
        joinButton.setEnabled(match.getAvailableSpots() > 0);
    }

    private JPanel createCreateMatchPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));  // Aumentato di una riga per la ComboBox
        JLabel titleLabel = new JLabel("Crea una nuova partita", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField locationField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<String> modeComboBox = new JComboBox<>(new String[]{"5 vs 5", "7 vs 7", "11 vs 11"});
        SpinnerModel maxPlayersModel = new SpinnerNumberModel(10, 2, 22, 1);  // Il massimo è 22 per 11 vs 11
        JSpinner maxPlayersSpinner = new JSpinner(maxPlayersModel);
        JButton createButton = new JButton("Crea Partita");
        JButton backButton = new JButton("Indietro");

        panel.add(titleLabel);
        panel.add(new JLabel()); // Placeholder
        panel.add(new JLabel("Luogo:"));
        panel.add(locationField);
        panel.add(new JLabel("Data (GG-MM-AAAA):"));
        panel.add(dateField);
        panel.add(new JLabel("Modalità:"));
        panel.add(modeComboBox);
        panel.add(new JLabel("Max Giocatori:"));
        panel.add(maxPlayersSpinner);
        panel.add(createButton);
        panel.add(backButton);

        // Aggiungi un listener per cambiare il numero massimo di giocatori in base alla modalità
        modeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedMode = (String) modeComboBox.getSelectedItem();
                int maxPlayers = 0;

                // Imposta il numero massimo di giocatori in base alla modalità scelta
                switch (selectedMode) {
                    case "5 vs 5":
                        maxPlayers = 10;  // 5 vs 5 = 10 giocatori
                        break;
                    case "7 vs 7":
                        maxPlayers = 14;  // 7 vs 7 = 14 giocatori
                        break;
                    case "11 vs 11":
                        maxPlayers = 22;  // 11 vs 11 = 22 giocatori
                        break;
                }

                // Imposta il valore del numero massimo di giocatori
                maxPlayersSpinner.setValue(maxPlayers);
                maxPlayersSpinner.setEnabled(false);
            }
        });

        createButton.addActionListener(e -> {
            if (currentUser != null) {
                String location = locationField.getText();
                String date = dateField.getText();
                String mode = (String) modeComboBox.getSelectedItem();
                int maxPlayers = (int) maxPlayersSpinner.getValue();

                if (location.isEmpty() || date.isEmpty() || mode.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Match newMatch = new Match(currentUser.getEmail(), location, date, mode, maxPlayers);
                matches.add(newMatch);
                saveMatches(matches);
                updateMatchListPanel();
                showPanel("MainScreen");
            } else {
                JOptionPane.showMessageDialog(this, "Errore: utente non loggato.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showPanel("MainScreen"));

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        JLabel titleLabel = new JLabel("Il tuo profilo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel nicknameLabel = new JLabel("Nickname:");
        JLabel nicknameValue = new JLabel("");
        JLabel ageLabel = new JLabel("Età:");
        JLabel ageValue = new JLabel("");
        JLabel roleLabel = new JLabel("Ruolo:");
        JLabel roleValue = new JLabel("");
        JLabel levelLabel = new JLabel("Livello:");
        JLabel levelValue = new JLabel("");
        JButton logoutButton = new JButton("Logout");

        panel.add(titleLabel);
        panel.add(new JLabel()); // Placeholder
        panel.add(nicknameLabel);
        panel.add(nicknameValue);
        panel.add(ageLabel);
        panel.add(ageValue);
        panel.add(roleLabel);
        panel.add(roleValue);
        panel.add(levelLabel);
        panel.add(levelValue);
        panel.add(new JLabel()); // Placeholder
        panel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            currentUser = null;
            showPanel("Login");
        });

        return panel;
    }

    private void updateProfilePanel() {
        JPanel mainScreen = (JPanel) mainPanel.getComponent(3);
        JPanel profileScreen = (JPanel) ((JPanel) mainScreen.getComponent(0)).getComponent(2); // Naviga al profilo
        if (currentUser != null) {
            ((JLabel) profileScreen.getComponent(1)).setText(currentUser.getNickname());
            ((JLabel) profileScreen.getComponent(3)).setText(String.valueOf(currentUser.getAge()));
            ((JLabel) profileScreen.getComponent(5)).setText(currentUser.getRole());
            ((JLabel) profileScreen.getComponent(7)).setText(String.valueOf(currentUser.getLevel()));
        } else {
            ((JLabel) profileScreen.getComponent(1)).setText("");
            ((JLabel) profileScreen.getComponent(3)).setText("");
            ((JLabel) profileScreen.getComponent(5)).setText("");
            ((JLabel) profileScreen.getComponent(7)).setText("");
        }
    }
}
