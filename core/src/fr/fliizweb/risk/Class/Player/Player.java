package fr.fliizweb.risk.Class.Player;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by rcdsm on 29/04/15.
 */

public class Player {

    private String      username;

    private Color color;

    public Player(String username, Color color) {
        this.username = username;
        this.color = color;
    }

    public String toString() {
        String output = "Player\n";
        output += "Username:" + username + "\n";
        return output;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Color getColor() {
        return color;
    }

}
