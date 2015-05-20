package fr.fliizweb.risk.Class.Player;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by rcdsm on 29/04/15.
 */

public class Player {

    private String      username;
    private Boolean     active;
    private Boolean     dead;

    private Color color;

    public Player(String username, Color color) {
        this.username = username;
        this.color = color;
        this.active = false;
        this.dead = false;
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

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isActive() {
        return active;
    }

    public void setDead(Boolean set) { this.dead = set; }
    public Boolean isDead() {
        return dead;
    }

    public Boolean isAlive() {
        if(dead)
            return false;
        return true;
    }

}
