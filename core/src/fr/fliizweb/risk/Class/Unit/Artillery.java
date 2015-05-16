package fr.fliizweb.risk.Class.Unit;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Artillery extends GroundUnit {
    protected int value = 10;
    protected int attack = 10;
    protected int def = 2;

    public Artillery() {
        super();
    }


    public int getAttack() { return attack; }

    public int getDef() { return def; }

    public int getValue() { return value; }
}
