package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import fr.fliizweb.risk.Class.Prototype.MapFilePrototype;
import fr.fliizweb.risk.Class.Prototype.UnitPrototype;
import fr.fliizweb.risk.Class.Prototype.ZonePrototype;
import fr.fliizweb.risk.Class.Unit.Infantry;
import fr.fliizweb.risk.Class.Unit.Unit;

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

    private int zoneSelected;

    public void setZones(ArrayList<Zone> zones) { Zones = zones; }

    public ArrayList getTexture() { return texture; }

    public Map() throws ClassNotFoundException {
        Zones = new ArrayList<Zone>();
        zoneSelected = 0;
        this.loadJSON();
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

    public Zone getZoneByID(int id) {
        for(Zone z: Zones) {
            if((z.getID() > 0) && z.getID() == id)
                return z;
        }
        return null;
    }

    public Boolean isZoneSelected() {
        if(this.zoneSelected > 0) return true;
        return false;
    }

    public int getZoneSelected() {
        return this.zoneSelected;
    }

    public void setZoneSelected(int ID) {
        this.zoneSelected = ID;
    }

    public void desactiveZones() {
        for(Zone z: Zones) {
            z.setActive(false);
        }
    }

    public void loadJSON() throws ClassNotFoundException {
        String content = null;
        String fileContent = null;


        Boolean gameExist = GameSave.fileExist();
        FileHandle gameToLoad = GameSave.getFile();

        // Si une partie existe déjà on joue sur cette map
        if(gameExist){
            fileContent = gameToLoad.readString(); // Lecture du fichier

        } else { // sinon on copie la map d'origine et on créé une nouvelle map
            FileHandle handle = Gdx.files.internal("Maps/default.json");

            GameSave.newGame(handle); // nouvelle partie avec la map choisie
            gameToLoad = GameSave.getFile();
            fileContent = gameToLoad.readString(); // On joue sur la nouvelle partie créée
        }

        ArrayList<Unit> listUnitss = new ArrayList<Unit>();
        Infantry newInfantry = new Infantry();
        listUnitss.add(newInfantry);
        listUnitss.add(newInfantry);
        listUnitss.add(newInfantry);

        Json json = new Json();
        json.setElementType(MapFilePrototype.class, "zones", ZonePrototype.class);
        json.setElementType(ZonePrototype.class, "units", UnitPrototype.class);

        MapFilePrototype data;
        data = json.fromJson(MapFilePrototype.class, fileContent);

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
            zone.setColor(p.color);
            zone.setDefaultColor(zone.getColor());
            zone.setID(p.id);
            zone.setNextZones(p.next);
            zone.setTexture(p.texture);

            ArrayList<Unit> listUnits = new ArrayList<Unit>();

            for(Object u :p.units){
                UnitPrototype anUnit = (UnitPrototype)u;

                for(int z = 0; z < anUnit.number; z++){
                    /*
                    *
                    * GENERATION AUTOMATIQUE DES CLASSES D'UNITES
                    * Methode appelée "Reflection"
                    *
                    */
                    Class test = Class.forName("fr.fliizweb.risk.Class.Unit." + anUnit.type);
                    Class[] types = {};
                    try {
                        Constructor constructor = test.getConstructor(types);
                        Object[] params = {};
                        Object instanceOfUnit = constructor.newInstance(params);
                        listUnits.add((Unit)instanceOfUnit);
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            zone.setUnits(listUnits);
            Zones.add(zone);
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
