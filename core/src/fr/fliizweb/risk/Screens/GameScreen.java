package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Unit.Infantry;
import fr.fliizweb.risk.Class.Unit.Unit;
import fr.fliizweb.risk.Class.Zone;
import fr.fliizweb.risk.Screens.Actors.ZoneActor;


/**
 * Created by rcdsm on 29/04/15.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {

    private Stage stage;
    private FitViewport vp;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    InputMultiplexer inputMultiplexer;

    private float origDistance, baseDistance, origZoom;

    Map map;
    ArrayList<Player> players;

    public GameScreen() throws ClassNotFoundException {
        map = new Map();
        players = new ArrayList<Player>();

        players.add(new Player("g0rp", Color.RED));
        players.add(new Player("Joadar", Color.GREEN));
        players.add(new Player("Thierry", Color.BLUE));
        players.add(new Player("Peric", Color.YELLOW));

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(this));
    }

    @Override
    public void show() {
        //camera
        camera = new OrthographicCamera();
        camera.zoom = 1.693f;
        camera.position.set(map.getSizex() / 2, map.getSizey() / 2, 0);
        camera.update();


        //viewport & stage
        vp = new FitViewport( 1024, 576, camera );
        stage = new Stage( vp );
        stage.getViewport().setCamera(camera);

        Gdx.app.log("GameScreen", "Camera = " + camera.toString() + " || Width Screen = " + Gdx.graphics.getWidth() + " Height Screen = " + Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        for(int i = 0; i < map.getZones().size(); i++) {
            final Zone zone = map.getZone(i);
            final ZoneActor zoneShape = new ZoneActor(zone);

            //On associe un joueur à une zone en fonction de la couleur
            for(Player player: players) {
                if(zone.getColor() == player.getColor())
                    zone.setPlayer(player);
            }

            zoneShape.setName(String.valueOf(zone.getID()));
            zoneShape.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    Boolean loop = false;
                    int[] zones = zone.getNextZones();
                    ArrayList<Zone> nextZones = new ArrayList<Zone>();
                    Array<Actor> stageActors = stage.getActors();


                    //Si aucune zone n'est selectionnée
                    if (!map.isZoneSelected()) {
                        zone.setSelected(true);
                        map.setZoneSelected(zone.getID());
                        for (int i = 0; i < stageActors.size; i++) {
                            Actor zoneActor = stageActors.get(i);
                            for (int j = 0; j < zones.length; j++) {
                                if (zoneActor.getName().equals(String.valueOf(zones[j]))) {
                                    Zone z = map.getZoneByID(zones[j]);
                                    z.setActive(true);
                                }
                            }
                        }
                    } else { //Dans le cas où une zone est selectionnée
                        if (zone.getID() == map.getZoneSelected()) { //Si la zone tapée est la même que celle selectionnée
                            //On désélectionne toutes les zones actives
                            map.setZoneSelected(0);
                            zone.setSelected(false);
                            zone.setColor(zone.getDefaultColor());
                            for (int i = 0; i < stageActors.size; i++) {
                                Actor zoneActor = stageActors.get(i);
                                for (int j = 0; j < zones.length; j++) {
                                    if (zoneActor.getName().equals(String.valueOf(zones[j]))) {
                                        Zone z = map.getZoneByID(zones[j]);
                                        z.setActive(false);
                                    }
                                }
                            }
                        } else if (zone.isActive()) { //Si la zone selectionnée est une zone active
                            //On met la zone tapée de la couleur de la zone selectionnée au préalable.
                            //Puis on désactive toutes les zones.
                            Zone z = map.getZoneByID(map.getZoneSelected());

                            Color colorNeutral = new Color(200, 200, 200, 0.6f);

                            int numberInfantry = 0, numberCavalry = 0;

                            // Parmi toutes les unités de la zone d'origine
                            for (int in = 0; in < z.getUnits().size(); in++) {
                                Unit unitDetail = z.getUnit(in);
                                // Si la class d'une des unité de la zone d'origine est égale à la class de l'unité qui part (ici une infanterie en test)
                                if(unitDetail.getClass().getSimpleName().equals("Infantry")){
                                    numberInfantry++;
                                } else if (unitDetail.getClass().getSimpleName().equals("Cavalry")){
                                    numberCavalry++;
                                }
                            }

                            // On récupère le skin qu'on veut donner à notre formulaire (android.assets/ui/defaultskin.json)
                            Skin skin = new Skin( Gdx.files.internal( "ui/defaultskin.json" ));

                            final Table table = new Table(); // On créé une table pour mettre nos éléments du formulaire
                            table.setVisible(true); // Visible à vrai (ici c'est juste un test, par défaut c'est vrai)
                            table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // On met la taille de la table à celle de l'écran
                            Gdx.app.log("GameScreen", "W = " + Gdx.graphics.getWidth() + " || H = " + Gdx.graphics.getHeight());
                            camera.zoom = 1.0f; // Faire un zoom lorsqu'on affiche le formulaire et le placer correctement par rapport à l'écran ? Désactiver le scroll et zoom lorsque le formulaire est affiché ?

                            // Pixmap ? C'est une bonne question ^
                            Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
                            pm1.setColor(Color.GREEN);
                            pm1.fill();
                            table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

                            // Le label Infanterie
                            Label labelInfantry=new Label("Infanterie",skin);
                            table.add(labelInfantry).width(150).padTop(10).padBottom(3);

                            // Le champ text pour le nombre d'infanterie à déplacer (trouver le moyen pour afficher un keyboard numeric)
                            TextField textInfantry=new TextField("",skin);
                            table.add(textInfantry).width(200).height(50);

                            // Nombre total d'infanterie sur la zone
                            Label totalInfantry=new Label("/" + numberInfantry, skin);
                            table.add(totalInfantry).width(50).height(50);

                            table.row(); // On revient à la ligne dans le tableau

                            // Le label Cavalerie
                            Label labelCavalry=new Label("Cavalerie",skin);
                            table.add(labelCavalry).width(150).padTop(10).padBottom(3);

                            // Le champ text pour le nombre de cavalerie à déplacer (trouver le moyen pour afficher un keyboard numeric)
                            TextField textCavalry=new TextField("",skin);
                            table.add(textCavalry).width(200).height(50);

                            // Nombre total d'infanterie sur la zone
                            Label totalCavalry=new Label("/" + numberCavalry, skin);
                            table.add(totalCavalry).width(50).height(50);

                            table.row(); // On revient à la ligne dans le tableau

                            // Le bouton valider
                            TextButton valid=new TextButton("Valider",skin);
                            valid.addListener(new ClickListener(){
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    table.addAction(Actions.fadeOut(0.7f));
                                    table.remove();
                                    camera.zoom = 2.0f;
                                }
                            });

                            table.add(valid).width(400).height(80).padTop(10);

                            // On ajoute le formulaire au stage
                            stage.addActor(table);




                            // Si on rencontre une zone sous le control du joueur (pour déplacer ses troupes) ou neutre (pour acquerir)
                            if ((zone.getColor() == z.getColor()) || (zone.getColor().equals(colorNeutral))) {

                                // Pour le test on fait une liste d'unités à déplacer
                                ArrayList<Unit> unitsToMove = new ArrayList<Unit>();
                                Infantry infantry = new Infantry();

                                unitsToMove.addAll(zone.getUnits()); // On récupère toutes les unités de la zone selectionné
                                unitsToMove.add(infantry); // On ajoute nos unités à déplacer

                                ArrayList<Unit> totalUnitsZone = new ArrayList<Unit>();

                                // On récupère les unités dans la zone d'origine
                                totalUnitsZone = z.getUnits();
                                Unit unitToRemove = new Infantry(); // L'unité à supprimer

                                int totalUnitStay = z.getUnits().size() - 1;
                                Color originColor = z.getColor();
                                if(totalUnitStay == 0){
                                    z.setColor(colorNeutral);
                                }

                                // Parmi toutes les unités de la zone d'origine
                                for (int in = 0; in < z.getUnits().size(); in++) {
                                    unitToRemove = z.getUnit(in);
                                    // Si la class d'une des unité de la zone d'origine est égale à la class de l'unité qui part (ici une infanterie en test)
                                    if(unitToRemove.getClass().equals(infantry.getClass())){
                                        break;
                                    }
                                }

                                totalUnitsZone.remove(unitToRemove); // Nombre d'unité restante

                                z.setUnits(totalUnitsZone);
                                zone.setUnits(unitsToMove);
                                zone.setColor(originColor);
                                zone.setDefaultColor(zone.getColor());
                                z.setActive(false);
                                z.setSelected(false);
                                zone.setActive(false);
                                zone.setSelected(false);
                                map.desactiveZones();
                                map.setZoneSelected(0);

                            } else { // Sinon on rencontre un joueur adverse (d'une autre couleur donc) : on peut donc l'attaquer

                            }
                        }
                    }
                }
            });
            stage.addActor(zoneShape);
        }

        Gdx.input.isTouched();

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        moveCamera(true, 0, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }


    /* MOUVEMENTS */

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        moveCamera(true, -deltaX, deltaY);
        camera.update();

        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(origDistance != initialDistance){
            origDistance = initialDistance;
            baseDistance = initialDistance;
            origZoom = camera.zoom;
        }

        float ratio = baseDistance/distance;
        float newZoom = origZoom * ratio;

        if (newZoom >= 2.0f) {
            camera.zoom = 2.0f;
            origZoom = 2.0f;
            baseDistance = distance;
        } else if (newZoom <= 1.0) {
            camera.zoom = (float) 1.0;
            origZoom = (float) 1.0;
            baseDistance = distance;
        } else {
            camera.zoom = newZoom;
        }

        moveCamera(true, 0, 0);
        camera.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void moveCamera (boolean add, float x, float y)
    {
        float newX, newY;

        if (add)
        {
            newX = camera.position.x + x;
            newY = camera.position.y + y;
        } else
        {
            newX = x;
            newY = y;
        }

        if (newX - camera.viewportWidth/2*camera.zoom < 0)
            newX = 0 + camera.viewportWidth/2*camera.zoom;
        if (newX + camera.viewportWidth/2*camera.zoom > map.getSizex())
            newX = map.getSizex() - camera.viewportWidth/2*camera.zoom;
        if (newY + camera.viewportHeight/2*camera.zoom > map.getSizey())
            newY = map.getSizey() - camera.viewportHeight/2*camera.zoom;
        if (newY - camera.viewportHeight/2*camera.zoom < 0)
            newY = 0 + camera.viewportHeight/2*camera.zoom;

        Gdx.app.log("GameScreen", "Camera Width = " + camera.viewportWidth + " || camera height = " + camera.viewportHeight + " || Screen Width = " + Gdx.graphics.getWidth() + " || Screen Height = " + Gdx.graphics.getHeight());

        camera.position.x = newX;
        camera.position.y = newY;
    }

}
