package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.fliizweb.risk.Class.Prototype.MapFilePrototype;
import fr.fliizweb.risk.Class.Prototype.UnitPrototype;
import fr.fliizweb.risk.Class.Prototype.ZonePrototype;
import fr.fliizweb.risk.Class.Unit.Unit;

/**
 * Created by rcdsm on 17/05/15.
 */
public class ManagePartie {

    private String titleFile;
    private FileHandle fileCreated;

    private final String FILE_PATH = "Risk/Partie";
    private final String FULL_FILE_PATH = FILE_PATH + "/";

    public ManagePartie(){
        Date date = new Date();
        DateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        titleFile = currentDate.format(date);
        fileCreated = Gdx.files.external(FULL_FILE_PATH + titleFile + ".json"); // On créé un fichier ayant pour nom la date du début de la partie
        Gdx.app.log("absolute", "absolute path = " + fileCreated.file().getAbsolutePath());
    }

    public void copy(FileHandle src){
        if(!Gdx.files.external(FILE_PATH).exists()){
            Gdx.files.external(FILE_PATH).file().mkdirs(); // On créé le dossier "Partie"
        }

        if(!this.fileExist()){ // Si aucune partie n'est en cours
            try {
                fileCreated.file().createNewFile(); // On créé la partie
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        src.copyTo(fileCreated); // On copie le fichier source dans le fichier de la partie
    }

    public boolean fileExist(){
        if(Gdx.files.external(FULL_FILE_PATH).list(".json").length > 0){ // Si il existe déjà un fichier .json

            for (FileHandle entry: Gdx.files.external(FULL_FILE_PATH).list()) {
                fileCreated = Gdx.files.external("" + entry + ""); // On récupère la partie
            }

            return true;
        } else {
            return false;
        }
    }

    public FileHandle getFile(){
        return fileCreated;
    }

    public void editZone(int idZone, String colorZone, ArrayList<Unit> unitsZone){
        // On édite la zone du fichier selon l'id correspondant.
        // On remplace sa couleur et les unités qui y sont présentes avec les bonnes valeurs
        // Appeler cette méthode à la fin d'un tap sur une zone

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setElementType(MapFilePrototype.class, "zones", ZonePrototype.class);
        json.setElementType(ZonePrototype.class, "units", UnitPrototype.class);

        MapFilePrototype data;
        data = json.fromJson(MapFilePrototype.class, fileCreated);

        ZonePrototype zoneProto = (ZonePrototype) data.zones.get(idZone);
        zoneProto.color = colorZone;
        Zone zone = new Zone();
        // Convertir la liste des unités

        int nbInf = 0, nbCav = 0, nbArt = 0;

        for(int i = 0; i < unitsZone.size(); i++){
            Unit unit = unitsZone.get(i);
            if(unit.getClass().getSimpleName().equals("Infantry")){
                nbInf++;
            } else if (unit.getClass().getSimpleName().equals("Cavalry")){
                nbCav++;
            } else if (unit.getClass().getSimpleName().equals("Artillery")){
                nbArt++;
            }
        }

        ArrayList listUnits = new ArrayList();

        /* Infantry */
        UnitPrototype unitProto = new UnitPrototype();
        unitProto.number = nbInf;
        unitProto.type = "Infantry";

        Json newJson = new Json();
        newJson.setOutputType(JsonWriter.OutputType.json);
        String text = newJson.toJson(unitProto, UnitPrototype.class);

        listUnits.add(new Json().fromJson(UnitPrototype.class, text));


        /* Cavalry */
        UnitPrototype unitProtoCav = new UnitPrototype();
        unitProtoCav.number = nbCav;
        unitProtoCav.type = "Cavalry";

        Json newJsonCav = new Json();
        newJson.setOutputType(JsonWriter.OutputType.json);
        String textCav = newJsonCav.toJson(unitProtoCav, UnitPrototype.class);

        listUnits.add(new Json().fromJson(UnitPrototype.class, textCav));


        /* Artillery */
        UnitPrototype unitProtoArt = new UnitPrototype();
        unitProtoArt.number = nbArt;
        unitProtoArt.type = "Artillery";

        Json newJsonArt = new Json();
        newJson.setOutputType(JsonWriter.OutputType.json);
        String textArt = newJsonArt.toJson(unitProtoArt, UnitPrototype.class);

        listUnits.add(new Json().fromJson(UnitPrototype.class, textArt));

        zoneProto.units = listUnits;
        data.zones.set(idZone, zoneProto); // On modifie la zone

        // On écrase la précédente version avec les changements
        fileCreated.writeString(json.prettyPrint(data), false);
    }

}
