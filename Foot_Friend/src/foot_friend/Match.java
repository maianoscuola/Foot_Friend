package foot_friend;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable {
    private String owner;
    private String location;
    private String date;
    private String mode;
    private int maxPlayers;
    private int currentPlayers;
    private List<String> joinedUsers;

    public Match(String owner, String location, String date, String mode, int maxPlayers) {
        this.owner = owner;
        this.location = location;
        this.date = date;
        this.mode = mode;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0; 
        this.joinedUsers = new ArrayList<>();
    }

    public String getOwner() {
        return owner;
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
        return currentPlayers;
    }

    public int getAvailableSpots() {
        return maxPlayers - currentPlayers;
    }

    public void joinMatch() {
        if (currentPlayers < maxPlayers) {
            currentPlayers++;
        }
    }
}

