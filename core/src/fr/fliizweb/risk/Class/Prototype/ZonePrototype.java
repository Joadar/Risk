package fr.fliizweb.risk.Class.Prototype;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.fliizweb.risk.Class.Unit.Unit;

/**
 * Created by rcdsm on 29/04/15.
 */
public class ZonePrototype implements Serializable {
    public  int id;
    public String name;
    public String color;
    public ArrayList texture;
    public ArrayList units;
    public int[] next;
    public int x;
    public int y;
    public int sizex;
    public int sizey;
}
