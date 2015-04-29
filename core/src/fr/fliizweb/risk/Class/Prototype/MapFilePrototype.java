package fr.fliizweb.risk.Class.Prototype;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.fliizweb.risk.Class.Zone;

/**
 * Created by rcdsm on 29/04/15.
 */
public class MapFilePrototype implements Serializable {

    public String name;
    public MapPrototype map;
    public ArrayList zones;
}
