package fr.fliizweb.risk.Class;

import java.util.ArrayList;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Zone {

    private ArrayList<Unit> Units;
    private Position        position;

    public Zone(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public String toString() {
        String output = "Zone\n";
        output += "Position:\n";
        output += "x: " + position.getX() + "/ y:" + position.getY() + "\n";
        return output;
    }

    class Position {
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

}
