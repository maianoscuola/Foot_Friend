package foot_friend;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String nickname;
    private int age;
    private String role;
    private int xp; // XP accumulata

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.xp = 0; // Inizialmente 0
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isProfileComplete() {
        return nickname != null && !nickname.isEmpty() && age > 0 && role != null && !role.isEmpty();
    }

    public int getXp() {
        return xp;
    }

    public void addXp(int amount) {
        this.xp += amount;
    }

    public int getLevel() {
        return xp / 15;
    }
}
