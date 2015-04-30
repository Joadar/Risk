package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Class.Unit.Unit;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Zone {

    private int             id;
    private ArrayList<Unit> Units;
    private int[]           next;
    private Position        position;
    private Size            size;
    private PlayerColor     color;

    public Zone(int x, int y, int sizex, int sizey) {
        position = new Position(x, y);
        size = new Size(sizex, sizey);
    }

    public String toString() {
        String output = "Zone\n";
        output += "Position:\n";
        output += "x: " + position.getX() + "/ y:" + position.getY() + "\n";
        output += "Size:\n";
        output += "x: " + size.getX() + "/ y:" + size.getY() + "\n";
        return output;
    }

    

    /**
     * GETTERS
     */

    public int getID() {
        return id;
    }

    public int[] getNextZones() {
        return next;
    }

    public int getNextZone(int id) {
        for(int i = 0; i < next.length; i++) {
            if(id == next[i])
                return i;
        }
        return -1;
    }

    public Position getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public Color getRGBColor() {
        Color c = null;
        switch (this.color) {
            case RED:
                c = new Color(255, 0, 0, 1);
            break;
            case GREEN:
                c = new Color(0, 255, 0, 1);
            break;
            case BLUE:
                c = new Color(0, 0, 255, 1);
            break;
            case YELLOW:
                c = new Color(255, 255, 0, 1);
            break;
            case MAJENTA:
                c = new Color(255, 0, 255, 1);
            break;
            case PURPLE:
                c = new Color(128, 0, 128, 1);
            break;
            case NEUTRAL:
                c = Color.LIGHT_GRAY;
                break;
            case WHITE:
                c = Color.WHITE;
                break;
        }

        return c;
    }

    public void setColor(String color) {
        this.color = PlayerColor.valueOf(color.toUpperCase());
    }


    /**
     * CLASSES
     */

    public class Position {
        private int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public class Size {
        private int x, y;

        public Size(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

}
