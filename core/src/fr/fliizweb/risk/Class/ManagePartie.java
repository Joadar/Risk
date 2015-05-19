package fr.fliizweb.risk.Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;
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

    public ManagePartie(){
        Date date = new Date();
        titleFile = "new_game";
        fileCreated = Gdx.files.external("Risk/Partie/" + titleFile + ".json"); // On créé un fichier ayant pour nom la date du début de la partie
    }

    public void newGame(FileHandle src){


        if(!Gdx.files.external("Risk/Partie").exists()){
            Gdx.files.external("Risk/Partie").file().mkdirs(); // On créé le dossier "Partie"
        }

        if(!this.fileExist()){ // Si aucune partie n'est en cours
            try {
                fileCreated.file().createNewFile(); // On créé la partie
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  else { // Si une partie existe déjà, on la supprime.
            this.delete(this.getFile());
        }
        src.copyTo(fileCreated); // On copie le fichier source dans le fichier de la partie
    }

    public void delete(FileHandle file){
        // Si le fichier qu'on veut supprimer existe bel et bien
        if(file.exists()){
            file.delete();
        }
    }

    public boolean fileExist(){
        if(Gdx.files.external("Risk/Partie/").list(".json").length > 0){ // Si il existe déjà un fichier .json

            for (FileHandle entry: Gdx.files.external("Risk/Partie/").list()) {
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
