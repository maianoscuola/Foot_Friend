package foot_friend;


import java.util.HashSet;
import java.util.Set;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Match implements Serializable {
    private String creator;
    private String location;
    private String date;
    private String mode;
    private int maxPlayers;
    private Set<String> players;

    public Match(String creator, String location, String date, String mode, int maxPlayers) {
        this.creator = creator;
        this.location = location;
        this.date = date;
        this.mode = mode;
        this.maxPlayers = maxPlayers;
        this.players = new HashSet<>();
        this.players.add(creator); // Aggiungi il creatore come primo giocatore
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getMode() {
        return mode;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getCurrentPlayers() {
        return players.size();
    }

    public int getAvailableSpots() {
        return maxPlayers - players.size();
    }

    public boolean joinMatch(String userEmail) {
        if (players.size() >= maxPlayers) {
            return false; // Partita piena
        }
        if (players.contains(userEmail)) {
            return false; // Utente già unito
        }
        players.add(userEmail);
        return true;
    }

    public boolean isPlayerInMatch(String userEmail) {
        return players.contains(userEmail);
    }
}