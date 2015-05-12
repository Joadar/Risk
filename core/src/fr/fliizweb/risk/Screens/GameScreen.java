package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Unit.Artillery;
import fr.fliizweb.risk.Class.Unit.Cavalry;
import fr.fliizweb.risk.Class.Unit.Infantry;
import fr.fliizweb.risk.Class.Unit.Unit;
import fr.fliizweb.risk.Class.Zone;
import fr.fliizweb.risk.Screens.Actors.BackgroundActor;
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
    private boolean showForm = false, validForm = false;

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
                    if (!map.isZoneSelected() && !showForm) {
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
                        if (zone.getID() == map.getZoneSelected() && !showForm) { //Si la zone tapée est la même que celle selectionnée
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
                            Zone z = map.getZone(0);
                            if (!showForm) {
                                z = map.getZoneByID(map.getZoneSelected());
                            }

                            final Zone finalZ = z;

                            final Color colorNeutral = new Color(200, 200, 200, 0.6f);

                            //Création d'un Hashtable pour stocker temporairement les unités présentes sur la zone.
                            final Hashtable ht = new Hashtable();

                            // Parmi toutes les unités de la zone d'origine
                            // Génération automatique des unités dans le hashtable (ht)
                            for (int in = 0; in < z.getUnits().size(); in++) {
                                Unit unitDetail = z.getUnit(in);
                                if (ht.containsKey(unitDetail.getClass().getSimpleName()))
                                    ht.put(unitDetail.getClass().getSimpleName(), (Integer) (ht.get(unitDetail.getClass().getSimpleName())) + 1);
                                else
                                    ht.put(unitDetail.getClass().getSimpleName(), 1);
                            }

                            //On affiche le formulaire
                            showForm = true;

                            // On récupère le skin qu'on veut donner à notre formulaire (android.assets/ui/defaultskin.json)
                            Skin skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));

                            final Table table = new Table(); // On créé une table pour mettre nos éléments du formulaire
                            table.setVisible(true); // Visible à vrai (ici c'est juste un test, par défaut c'est vrai)
                            table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // On met la taille de la table à celle de l'écran

                            camera.zoom = 1.0f; // Faire un zoom lorsqu'on affiche le formulaire et le placer correctement par rapport à l'écran ? Désactiver le scroll et zoom lorsque le formulaire est affiché ?

                            // Pixmap ? C'est une bonne question ^
                            // On ne sais toujours pas à quoi ça sert mais c'est la pour une bonne raison :D
                            Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
                            pm1.setColor(new Color(0f, 0f, 0f, 0f));
                            pm1.fill();
                            table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

                            /*
                             * Hashtable de TextField
                             * Permet de stocker les textfields en fonction des unités disponibles sur la zone.
                             *
                             * ex : Si dans la zone il n'existe que des Artillery, nous n'auront que le champ artillery
                             * */
                            final Hashtable textFields = new Hashtable();

                            // Boucle pour afficher le tableau d'unités du formulaire.
                            for (final Object key : ht.keySet()) {
                                // Label
                                Label label = new Label(key.toString(), skin);
                                label.setFontScale(2);
                                table.add(label).width(150).padTop(10).padBottom(3);

                                //TextField
                                final TextField text = new TextField("0", skin);
                                textFields.put(key.toString(), text); //Stockage du TextField dans notre Hashtable de textfields.
                                table.add(text).width(50).height(50);

                                // Nombre d'unités disponibles - Valeur récupérée du HashTable ht.
                                Label total = new Label("/" + ht.get(key), skin);
                                total.setFontScale(2);
                                table.add(total).width(50).height(50);

                                TextButton BtnAdd = new TextButton("+", skin);
                                BtnAdd.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        int value = Integer.parseInt(text.getText().toString());
                                        if(value < (Integer) ht.get(key))
                                            value++;
                                        text.setText(String.valueOf(value));
                                        return super.touchDown(event, x, y, pointer, button);
                                    }
                                });
                                table.add(BtnAdd).width(50).height(50);

                                TextButton BtnDel = new TextButton("-", skin);
                                BtnDel.addListener(new InputListener() {

                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        super.touchUp(event, x, y, pointer, button);
                                        int value = Integer.parseInt(text.getText().toString());
                                        if(value > 0)
                                            value--;
                                        text.setText(String.valueOf(value));
                                        return super.touchDown(event, x, y, pointer, button);
                                    }
                                });
                                table.add(BtnDel).width(50).height(50);

                                table.row(); // On revient à la ligne dans le tableau
                            }

                            // Le bouton valider
                            BitmapFont font = new BitmapFont();
                            font.getData().scale(1.7f);
                            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
                            style.font = font;

                            /*
                             * ATTENTION
                             * Dans le cas du bleu c'est pas vraiment lisible...
                             * Mais le principe est cool
                             */
                            style.fontColor = new Color(z.getColor());

                            TextButton valid = new TextButton("Valider", skin);
                            valid.setStyle(style);
                            valid.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    int counter = 0;
                                    int unitsInZone = 0;
                                    TextField text;
                                    for (Object key : textFields.keySet()) {
                                        text = (TextField) textFields.get(key);
                                        counter += Integer.parseInt(text.getText().toString());
                                        unitsInZone += (Integer) ht.get(key) - Integer.parseInt(text.getText().toString());
                                    }

                                    // Si aucune unité n'est rentrée, on affiche un message au joueur
                                    if (counter == 0) {
                                        return;
                                    }

                                    // Si le nombre  d'unités rentrées dépasse le nombre d'unité disponible dans la zone, on affiche un message au joueur comme quoi sa zone va devenir neutre
                                    //if (Integer.parseInt(textInfantry.getText().toString()) > numInf || Integer.parseInt(textArtillery.getText().toString()) > numCav || Integer.parseInt(textArtillery.getText().toString()) > numArt) {
                                    /*if (unitsInZone == 0) {
                                        Gdx.app.log("GameScreen", "Unités restantes sur la zone : 0");
                                        return;
                                    }
                                    */

                                    table.remove();
                                    camera.zoom = 2.0f;
                                    showForm = false;
                                    validForm = true;

                                    // Si on rencontre une zone sous le control du joueur (pour déplacer ses troupes) ou neutre (pour acquerir)
                                    if((zone.getColor() == finalZ.getColor()) ||
                                            zone.getPlayer() == null)
                                            /*(zone.getColor().equals(colorNeutral) ||
                                                    (zone.getDefaultColor().r == colorNeutral.r && zone.getDefaultColor().g == colorNeutral.g && zone.getDefaultColor().b == colorNeutral.b)))*/
                                    {

                                        // Pour le test on fait une liste d'unités à déplacer
                                        ArrayList<Unit> unitsToMove = new ArrayList<Unit>();
                                        // On fait la liste des unités remplis par le formulaire
                                        ArrayList<Unit> formUnits = new ArrayList<Unit>();

                                        // On fait la liste des unités à déplacer
                                        for (Object key : textFields.keySet()) {
                                            text = (TextField) textFields.get(key);
                                            for(int i = 0; i < Integer.parseInt(text.getText().toString()); i++) {
                                                try {
                                                    Class tmp = Class.forName("fr.fliizweb.risk.Class.Unit." + key.toString());
                                                    Class[] types = {};
                                                    Constructor constructor = tmp.getConstructor(types);
                                                    Object[] params = {};
                                                    Object instanceOfUnit = constructor.newInstance(params);
                                                    formUnits.add((Unit)instanceOfUnit);
                                                } catch (NoSuchMethodException e1) {
                                                    e1.printStackTrace();
                                                } catch (InvocationTargetException e1) {
                                                    e1.printStackTrace();
                                                } catch (InstantiationException e1) {
                                                    e1.printStackTrace();
                                                } catch (IllegalAccessException e1) {
                                                    e1.printStackTrace();
                                                } catch (ClassNotFoundException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        }

                                        // On récupère les unités dans la zone d'origine
                                        ArrayList<Unit> totalUnitsZone = finalZ.getUnits();

                                        int totalUnitStay = finalZ.getUnits().size() - 1;
                                        Color originColor = finalZ.getColor();
                                        if (totalUnitStay == 0) {
                                            finalZ.setColor(colorNeutral);
                                        }

                                        Unit unitToRemove; // L'unité à supprimer

                                        int inf = 0, cav = 0, art = 0;

                                        // Parmi toutes les unités du formulaire de déplacement
                                        for (int in = 0; in < formUnits.size(); in++) {
                                            unitToRemove = formUnits.get(in);
                                            /*
                                            // Si la class d'une des unité de la zone d'origine est égale à la class de l'unité qui part (ici une infanterie en test)
                                            if (unitToRemove.getClass().getSimpleName().equals("Infantry")) {
                                                Gdx.app.log("GameScreen", "Value of textInfantry = " + textInfantry.getText().toString());
                                                if (inf == Integer.parseInt(textInfantry.getText().toString())) {
                                                    continue;
                                                }
                                                inf++;

                                            } else if (unitsToMove.getClass().getSimpleName().equals("Cavalry")) {
                                                if (cav == Integer.parseInt(textCavalry.getText().toString())) {
                                                    continue;
                                                }
                                                cav++;
                                            } else if (unitsToMove.getClass().getSimpleName().equals("Artillery")) {
                                                if (art == Integer.parseInt(textArtillery.getText().toString())) {
                                                    continue;
                                                }
                                                art++;
                                            }
                                            */
                                            unitsToMove.add(unitToRemove); // On ajoute nos unités à déplacer
                                        }

                                        // On met à jour la précédente zone avec le départ des unités
                                        for (int ttU = 0; ttU < totalUnitsZone.size(); ttU++) {
                                            for (int utM = 0; utM < unitsToMove.size(); utM++) {
                                                Unit unitToMove = unitsToMove.get(utM);
                                                Unit unitInTotal = totalUnitsZone.get(ttU);
                                                if (unitToMove.getClass().getSimpleName().equals(unitInTotal.getClass().getSimpleName())) {
                                                    Gdx.app.log("unitsToMove", "Hello");
                                                    totalUnitsZone.remove(ttU);
                                                }
                                            }
                                        }

                                        Gdx.app.log("unitsToMove", "totalUnitsZone fin = " + totalUnitsZone);

                                        //totalUnitsZone.remove(unitToRemove); // Nombre d'unité restante

                                        finalZ.setUnits(totalUnitsZone);
                                        zone.setUnits(unitsToMove);
                                        zone.setColor(originColor);
                                        zone.setDefaultColor(zone.getColor());
                                        finalZ.setActive(false);
                                        finalZ.setSelected(false);
                                        zone.setActive(false);
                                        zone.setSelected(false);
                                        map.desactiveZones();
                                        map.setZoneSelected(0);
                                    }
                                    else
                                    { // Sinon on rencontre un joueur adverse (d'une autre couleur donc) : on peut donc l'attaquer

                                    }

                                // On retire le keyboard dans tous les cas
                                Gdx.input.setOnscreenKeyboardVisible(false);
                            }
                        });

                        table.add(valid).width(400).height(80).padTop(10);

                        TextButton close = new TextButton("Annuler", skin);
                        close.setStyle(style);
                        close.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                table.remove();
                                camera.zoom = 2.0f;
                                showForm = false;
                                validForm = false;
                                Gdx.input.setOnscreenKeyboardVisible(false);
                            }
                        });
                        table.add(close).width(80).height(80).padTop(10);

                        table.setPosition(camera.position.x - (Gdx.graphics.getWidth() / 2), camera.position.y - (Gdx.graphics.getHeight() / 2));

                        // On ajoute le formulaire au stage
                        stage.addActor(table);
                        Gdx.app.log("GameScreen", "validForm = " + validForm);

                        // Si on a cliqué sur le bouton "valider"
                        if (validForm) {

                        } else { // On a cliqué sur le bouton "annuler", on peut continuer l'action mouvement

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
        if(!showForm){
            stage.getViewport().update(width, height, true);
            moveCamera(true, 0, 0);
            camera.update();
        }
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
        if(!showForm){
            moveCamera(true, -deltaX, deltaY);
            camera.update();
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(!showForm) {
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
        }
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

        camera.position.x = newX;
        camera.position.y = newY;
    }

}
