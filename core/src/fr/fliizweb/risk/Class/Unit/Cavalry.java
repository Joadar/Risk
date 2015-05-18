package fr.fliizweb.risk.Class.Unit;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Cavalry extends GroundUnit {
    protected int value = 5;
    protected int attack = 4;
    protected int def = 3;

    public Cavalry() {
        super();
    }

    public int getAttack() { return this.attack; }

    public int getDef() { return this.def; }

    public int getValue() { return this.value; }
}
