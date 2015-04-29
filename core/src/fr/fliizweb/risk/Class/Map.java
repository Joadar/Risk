package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.naming.Context;

/**
 * Created by rcdsm on 28/04/15.
 */
public class Map {

    private Zone[][] Zones;

    public Map(){

        //Zones = new Zone[this.sizeX][this.sizeY];

        loadJSONFromAsset();

    }

    public void generateMap(){
        /*for(int y = 0; y < this.sizeY; y++ ){
            for(int x = 0; x < this.sizeX; x++){
                Zone zone = new Zone(x, y);
                Zones[x][y] = zone;
            }
        }*/

        //JSONObject obj = new JSONObject(json_return_by_the_function);


    }

    public String loadJSONFromAsset() {
        String json = null;
        FileHandle handle = Gdx.files.internal("Maps/default.json");
        json = handle.readString();
        Gdx.app.log("Map", "Json = " + json);

        return json;

    }

}
