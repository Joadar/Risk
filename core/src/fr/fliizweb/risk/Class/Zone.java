package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Unit.Unit;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Zone {

    private int             id;
    private ArrayList       Units;
    private int[]           next;
    private Position        position;
    private Size            size;
    private Color           color;
    private Color           defaultColor;
    private Player          player;

    private Boolean         selected;
    private Boolean         active;

    public Zone(int x, int y, int sizex, int sizey) {
        position = new Position(x, y);
        size = new Size(sizex, sizey);
        selected = false;
        active = false;
        player = null;
        Units = new ArrayList<Unit>();
    }

    public String toString() {
        String output = "Zone\n";
        output += "Position:\n";
        output += "x: " + position.getX() + "/ y:" + position.getY() + "\n";
        output += "Size:\n";
        output += "x: " + size.getX() + "/ y:" + size.getY() + "\n";
        return output;
    }

    public ArrayList<Unit> removeUnits(ArrayList<Unit> unitsToRemove) {
        Units.removeAll(unitsToRemove);
        //Gros problème ici... La liste ne se supprime pas comme il faut.
        // BREF faut juste bosser ce point et c'est parfait :)
        Gdx.app.log("GameScreen", "Units " + Units);
        return Units;
    }



    /**
     * GETTERS
     */

    public void setUnits(ArrayList<Unit> units) { this.Units = units; }
    public ArrayList<Unit> getUnits() { return this.Units; }
    public ArrayList<Unit> getSortedUnits() {
        //Sorting
        Collections.sort(Units, new Comparator<Unit>() {
            @Override
            public int compare(Unit unit1, Unit unit2) {
                return unit1.getClass().getSimpleName().compareTo(unit2.getClass().getSimpleName());
            }
        });
        return Units;
    }
    public Unit getUnit(int index) { return (Unit)this.Units.get(index); }

    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return this.player; }

    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return id;
    }

    public int[] getNextZones() {
        return this.next;
    }
    public void setNextZones(int[] zones) {
        this.next = zones;
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

    public Color getDefaultColor() {
        return this.defaultColor;
    }
    public void setDefaultColor(Color color) {
        this.defaultColor = color;
    }

    public Color getColor() {
        return this.color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setColor(String color) {
        setColor(color, 1.0f);
    }
    public void setColor(String color, float alpha) {
        if(color.equals("RED"))
            this.color = new Color(255, 0, 0, alpha);
        else if(color.equals("GREEN"))
            this.color = new Color(0, 255, 0, alpha);
        else if (color.equals("BLUE"))
            this.color = new Color(0, 0, 255, alpha);
        else if (color.equals("YELLOW"))
            this.color = new Color(255, 255, 0, alpha);
        else if (color.equals("MAJENTA"))
            this.color = new Color(255, 0, 255, alpha);
        else if (color.equals("PURPLE"))
            this.color = new Color(128, 0, 128, alpha);
        else if (color.equals("NEUTRAL"))
            this.color = new Color(200, 200, 200, alpha);
        else if (color.equals("WHITE"))
            this.color = new Color(255, 255, 255, alpha);
    }

    public Boolean isSelected() {
        return this.selected;
    }
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean isActive() { return this.active; }
    public void setActive(Boolean active) { this.active = active; }


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
