package fr.fliizweb.risk.Class.Unit;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Infantry extends GroundUnit {
    protected int value = 1;
    protected int attack = 1;
    protected int def = 1;

    public Infantry() {super();}

    public int getAttack() { return this.attack; }

    public int getDef() { return this.def; }

    public int getValue() { return this.value; }
}
