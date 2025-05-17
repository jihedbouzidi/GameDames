package model;

public class Player {
    private final String  username;
    private String password;
    private final String firstName;
    private final String lastName;
    private String avatar;

    public Player(String username, String password, String firstName, String lastName, String avatar) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    // Getters et setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAvatar() { return avatar; }

    public void setPassword(String password) { this.password = password; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}