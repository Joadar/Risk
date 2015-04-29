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
    private ArrayList texture;
    private int x;
    private int y;
    private int sizex;
    private int sizey;

    public void setZones(ArrayList<Zone> zones) { Zones = zones; }

    public ArrayList getTexture() { return texture; }


    public Map() {
        Zones = new ArrayList<Zone>();
        loadJSON();
    }
    
    public ArrayList<Zone> getZones() {
        return this.Zones;
    }

    public Zone getZone(int idx) {
        Zone zone;
        if((zone = this.Zones.get(idx)) != null)
            return zone;
        return null;
    }

    public void loadJSON() {
        String content = null;

        FileHandle handle = Gdx.files.internal("Maps/default.json");
        String fileContent = handle.readString(); // Lecture du fichier

        Json  json = new Json();
        json.setElementType(MapFilePrototype.class, "zones", ZonePrototype.class);

        MapFilePrototype data;
        data = json.fromJson(MapFilePrototype.class, fileContent);

        Gdx.app.log("Map", "Data name = " + data.name);
        Gdx.app.log("Map", "Map details sizex = " + data.map.sizex);

        // Implementation des données de map
        this.setTexture(data.map.texture);
        this.setSizex(data.map.sizex);
        this.setSizey(data.map.sizey);
        this.setX(data.map.x);
        this.setY(data.map.y);

        // Création des zones
        for(Object e :data.zones){
            ZonePrototype p = (ZonePrototype)e;
            Zone zone = new Zone(p.x, p.y, p.sizex, p.sizey);
            Zones.add(zone);
            Gdx.app.log("Map", "color = " + p.color + "x = " + p.x + "y =" + p.y);
            Gdx.app.log("Map", "sizex = " + p.sizex + "sizey =" + p.sizey);

        }
    }


    /**
     * GETTERS
     */

    public void setTexture(ArrayList texture) { this.texture = texture; }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public int getSizex() { return sizex; }

    public void setSizex(int sizex) { this.sizex = sizex; }

    public int getSizey() { return sizey; }

    public void setSizey(int sizey) { this.sizey = sizey; }

}
