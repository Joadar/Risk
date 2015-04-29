package fr.fliizweb.risk.Class;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Class.Unit.Unit;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Zone {

    private ArrayList<Unit> Units;
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

    public Position getPosition() {
        return position;
    }

    public Size getSize() {
        return size;
    }

    public PlayerColor getColor() {
        return color;
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
