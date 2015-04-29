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

    public Map(){

        //Zones = new Zone[this.sizeX][this.sizeY];

        loadJSON();

    }

    public void generateMap(){
        /*for(int y = 0; y < this.sizeY; y++ ){
            for(int x = 0; x < this.sizeX; x++){
                Zone zone = new Zone(x, y);
                Zones[x][y] = zone;
            }
        }*/
        Json obj = new Json();
        obj.fromJson(Map.class, loadJSON());

    }

    public String loadJSON() {
        String content = null;
        /*FileHandle handle = Gdx.files.internal("Maps/default.json");
        content = handle.readString();
        Gdx.app.log("Map", "Json = " + content);
        Json json = new Json();
        String test = json.toJson(content);
        //json.readField(json, "zones", test);
        json.fromJson(Zone.class, content);
        Gdx.app.log("Map", "Json = " + json.toJson(content));*/

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
            Gdx.app.log("Map", "color = " + p.color + "x = " + p.x + "y =" + p.y);
        }


        return content;

    }

}
