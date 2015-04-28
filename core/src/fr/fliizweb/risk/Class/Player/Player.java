package fr.fliizweb.risk.Class.Player;

/**
 * Created by rcdsm on 29/04/15.
 */

public class Player {

    private String      username;

    private PlayerColor color;

    public Player(String username, PlayerColor color) {
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
