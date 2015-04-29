package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Prototype.MapFilePrototype;
import fr.fliizweb.risk.Class.Prototype.MapPrototype;
import fr.fliizweb.risk.Class.Prototype.ZonePrototype;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Map {

    private ArrayList<Zone> Zones;

    public Map() {
        loadJSON();
    }
    
    public ArrayList<Zone> getZones() {
        return this.Zones;
    }

    public Zone getZone(int idx) {
        Zone zone = null;
        if((zone = this.Zones.get(idx)) != null)
            return zone;
        return null;
    }

    public void loadJSON() {
        String content = null;

        FileHandle handle = Gdx.files.internal("Maps/default.json");
        String fileContent = handle.readString();
        Json  json = new Json();
        json.setElementType(MapFilePrototype.class, "zones", ZonePrototype.class);
        MapFilePrototype data;
        data = json.fromJson(MapFilePrototype.class, fileContent);
        Gdx.app.log("Map", "Data name = " + data.name);
        Gdx.app.log("Map", "Map details sizex = " + data.map.sizex);
        for(Object e :data.zones){
            ZonePrototype p = (ZonePrototype)e;
            Zone zone = new Zone(p.x, p.y, p.sizex, p.sizey);
            Zones.add(zone);
            Gdx.app.log("Map", "color = " + p.color + "x = " + p.x + "y =" + p.y);
        }
    }

}
