package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
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
                    int[] nextZonesID = zone.getNextZones();
                    //ArrayList<Zone> nextZones = new ArrayList<Zone>();
                    Array<Actor> stageActors = stage.getActors();

                    //Si aucune zone n'est selectionnée
                    if (!map.isZoneSelected() && !showForm) {
                        selectZone(zone, nextZonesID, stageActors);
                    } else { //Dans le cas où une zone est selectionnée
                        if (zone.getID() == map.getZoneSelected() && !showForm) { //Si la zone tapée est la même que celle selectionnée
                            //On désélectionne toutes les zones actives
                            unselectZone(zone, nextZonesID, stageActors);
                        } else if (zone.isActive()) { //Si la zone selectionnée est une zone active
                            Zone z = map.getZone(0);
                            if (!showForm) {
                                z = map.getZoneByID(map.getZoneSelected());
                            }

                            final Zone finalZ = z;
                            final int countUnitCurrentZone = finalZ.getUnits().size();

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
                                final Label text = new Label("0", skin);
                                text.setFontScale(2);
                                textFields.put(key.toString(), text); //Stockage du TextField dans notre Hashtable de textfields.
                                table.add(text).width(50).height(50);

                                // Nombre d'unités disponibles - Valeur récupérée du HashTable ht.
                                Label total = new Label("/" + ht.get(key), skin);
                                total.setFontScale(2);
                                table.add(total).width(50).height(50);

                                TextButton BtnDel = new TextButton("-", skin);
                                BtnDel.addListener(new InputListener() {

                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        super.touchUp(event, x, y, pointer, button);
                                        int value = Integer.parseInt(text.getText().toString());
                                        if (value > 0)
                                            value--;
                                        text.setText(String.valueOf(value));
                                        return super.touchDown(event, x, y, pointer, button);
                                    }
                                });
                                table.add(BtnDel).width(50).height(50);

                                TextButton BtnAdd = new TextButton("+", skin);
                                BtnAdd.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        int value = Integer.parseInt(text.getText().toString());
                                        if (value < (Integer) ht.get(key))
                                            value++;
                                        text.setText(String.valueOf(value));
                                        return super.touchDown(event, x, y, pointer, button);
                                    }
                                });
                                table.add(BtnAdd).width(50).height(50);

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
                            style.fontColor = new Color(z.getDefaultColor());

                            TextButton valid = new TextButton("Valider", skin);
                            valid.setStyle(style);
                            valid.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    int counter = 0;
                                    int unitsInZone = 0;
                                    Label text;
                                    for (Object key : textFields.keySet()) {
                                        text = (Label) textFields.get(key);
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
                                    if ((zone.getColor() == finalZ.getColor()) ||
                                            zone.getPlayer() == null)
                                            /*(zone.getColor().equals(colorNeutral) ||
                                                    (zone.getDefaultColor().r == colorNeutral.r && zone.getDefaultColor().g == colorNeutral.g && zone.getDefaultColor().b == colorNeutral.b)))*/ {

                                        // On fait la liste des unités remplis par le formulaire
                                        ArrayList<Unit> formUnits = new ArrayList<Unit>();

                                        // On fait la liste des unités à déplacer
                                        for (Object key : textFields.keySet()) {
                                            text = (Label) textFields.get(key);
                                            for (int i = 0; i < Integer.parseInt(text.getText().toString()); i++) {
                                                try {
                                                    Class tmp = Class.forName("fr.fliizweb.risk.Class.Unit." + key.toString());
                                                    Class[] types = {};
                                                    Constructor constructor = tmp.getConstructor(types);
                                                    Object[] params = {};
                                                    Object instanceOfUnit = constructor.newInstance(params);
                                                    //formUnits.add((Unit)instanceOfUnit);

                                                    int uu = 0;
                                                    for (int ii = 0; ii < finalZ.getUnits().size(); ii++) {
                                                        Unit anUnit = finalZ.getUnits().get(ii);
                                                        if (anUnit.getClass().getSimpleName().equals(key.toString())) {
                                                            if (uu == 0) {
                                                                formUnits.add(anUnit);
                                                                finalZ.removeUnits(formUnits);
                                                                uu++;
                                                            }
                                                        }
                                                    }
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

                                        //Couleur de la zone d'origine
                                        Color originColor = finalZ.getColor();

                                        Gdx.app.log("final", "getUnits.size = " + finalZ.getUnits().size() + " || forumUnits.size = " + formUnits.size());
                                        if (countUnitCurrentZone - formUnits.size() <= 0) {
                                            finalZ.setPlayer(null);
                                            finalZ.setColor(Color.WHITE);
                                        }

                                        //On donne à la zone selectionnée les unités restantes
                                        finalZ.removeUnits(formUnits);
                                        finalZ.setActive(false);
                                        finalZ.setSelected(false);

                                        formUnits.addAll(zone.getUnits());

                                        //On donne à la nouvelle zone acquise les unités passées dans le formulaire
                                        zone.setUnits(formUnits);
                                        //On assigne la couleur à la zone
                                        zone.setColor(originColor);
                                        zone.setDefaultColor(zone.getColor());
                                        //On désactive la zone & on déselectionne
                                        zone.setActive(false);
                                        zone.setSelected(false);

                                        //On désactive toutes les zones de la map
                                        map.desactiveZones();
                                        //On donne une zone inexistante comme zone selectionnée
                                        map.setZoneSelected(0);
                                    } else { // Sinon on rencontre un joueur adverse (d'une autre couleur donc) : on peut donc l'attaquer

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

    private void unselectZone(Zone selectedZone, int[] nextZonesID, Array<Actor> stageActors) {
        map.setZoneSelected(0);
        selectedZone.setSelected(false);
        selectedZone.setColor(selectedZone.getDefaultColor());
        for (int i = 0; i < stageActors.size; i++) {
            Actor zoneActor = stageActors.get(i);
            for (int j = 0; j < nextZonesID.length; j++) {
                if (zoneActor.getName().equals(String.valueOf(nextZonesID[j]))) {
                    Zone z = map.getZoneByID(nextZonesID[j]);
                    z.setActive(false);
                }
            }
        }
    }

    private void selectZone(Zone selectedZone, int[] nextZonesID, Array<Actor> stageActors) {
        selectedZone.setSelected(true);
        map.setZoneSelected(selectedZone.getID());
        for (int i = 0; i < stageActors.size; i++) {
            Actor zoneActor = stageActors.get(i);
            for (int j = 0; j < nextZonesID.length; j++) {
                if (zoneActor.getName().equals(String.valueOf(nextZonesID[j]))) {
                    Zone z = map.getZoneByID(nextZonesID[j]);
                    z.setActive(true);
                }
            }
        }
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
