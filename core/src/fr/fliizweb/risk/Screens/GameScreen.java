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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;

import fr.fliizweb.risk.Class.GameSave;
import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Unit.Unit;
import fr.fliizweb.risk.Class.Zone;
import fr.fliizweb.risk.Risk;
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

    private final static float ZOOM_MAX = 3.0f;
    private final static float ZOOM_MIN = 1.f;
    private final static float ZOOM_DEFAULT = 1.693f;

    private float origDistance, baseDistance, origZoom;
    private boolean showForm = false, validForm = false;

    Map map;
    ArrayList<Player> players;
    ListIterator<Player> playersIterator;
    Player player;

    Skin skin;

    private Risk game;

    public GameScreen(Risk game){
        this.game = game;
        init();
    }

    public GameScreen() {
        init();
    }

    public void init(){
        try {
            map = new Map();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        players = new ArrayList<Player>();

        players.add(new Player("g0rp", Color.RED));
        players.add(new Player("Joadar", Color.GREEN));
        players.add(new Player("Thierry", Color.BLUE));
        players.add(new Player("Peric", Color.YELLOW));

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(this));

        // On récupère le skin qu'on veut donner à notre formulaire (android.assets/ui/defaultskin.json)
        skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));
    }

    @Override
    public void show() {

        player = players.get(0); //Selection du joueur 1
        playersIterator = players.listIterator();

        //camera
        camera = new OrthographicCamera();
        camera.zoom = ZOOM_DEFAULT;
        origZoom = camera.zoom;
        camera.position.set(map.getSizex() / 2, map.getSizey() / 2, 0);
        camera.update();

        //viewport & stage
        vp = new FitViewport( 800, 450, camera );
        stage = new Stage( vp );
        stage.getViewport().setCamera(camera);

        batch = new SpriteBatch();

        for(int i = 0; i < map.getZones().size(); i++) {
            final Zone zone = map.getZone(i);
            final ZoneActor zoneShape = new ZoneActor(zone, map);

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
                    Array<Actor> stageActors = stage.getActors();

                    //Si aucune zone n'est selectionnée
                    if (!map.isZoneSelected() && !showForm) {
                        //On selectionne la zone.
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

                            final Hashtable ht = z.getUnitsHashtable();

                            // Si le formulaire n'est pas affiché, alors on peut l'afficher, pas besoin de l'afficher à chaque tap sur une zone
                            if (!showForm) {

                                //On affiche le formulaire
                                showForm = true;


                                final Table table = new Table(); // On créé une table pour mettre nos éléments du formulaire
                                table.setZIndex(100);
                                table.setVisible(true); // Visible à vrai (ici c'est juste un test, par défaut c'est vrai)
                                table.setSize(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() - 30); // On met la taille de la table à celle de l'écran
                                table.setPosition(30, 30);

                                camera.zoom = ZOOM_MIN; // Faire un zoom lorsqu'on affiche le formulaire et le placer correctement par rapport à l'écran ? Désactiver le scroll et zoom lorsque le formulaire est affiché ?

                                Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
                                pm1.setColor(new Color(0f, 0f, 0f, 0.1f));
                                pm1.fill();
                                table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

                                // Permet de stocker les textfields en fonction des unités disponibles sur la zone.
                                final Hashtable textFields = new Hashtable();

                                // Boucle pour afficher le tableau d'unités du formulaire.
                                for (final Object key : ht.keySet()) {
                                    addTableLine(table, key, ht, textFields, skin);
                                }

                                // Le bouton valider
                                /*
                                BitmapFont font = new BitmapFont();
                                font.getData().scale(1.7f);
                                TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
                                style.font = font;
                                style.fontColor = new Color(z.getDefaultColor());
                                */

                                TextButton valid = new TextButton("Valider", skin);
                                valid.align(Align.center);
                                //valid.setStyle(style);
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
                                        camera.zoom = origZoom;
                                        showForm = false;
                                        validForm = true;

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
                                                            /*if ((zone.getColor() == finalZ.getColor()) ||
                                                                    zone.getPlayer() == null) {*/
                                                                finalZ.removeUnits(formUnits);
                                                                //}
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

                                        // Si on rencontre une zone sous le control du joueur (pour déplacer ses troupes) ou neutre (pour acquerir)
                                        if ((zone.getColor() == finalZ.getColor()) ||
                                                zone.getPlayer() == null)
                                            /*(zone.getColor().equals(colorNeutral) ||
                                                    (zone.getDefaultColor().r == colorNeutral.r && zone.getDefaultColor().g == colorNeutral.g && zone.getDefaultColor().b == colorNeutral.b)))*/ {

                                            moveTo(finalZ, zone, formUnits);
                                        } else { // Sinon on rencontre un joueur adverse (d'une autre couleur donc) : on peut donc l'attaquer
                                            doAttack(formUnits, finalZ, zone);
                                        }

                                        player.setActive(false);

                                        // On retire le keyboard dans tous les cas
                                        Gdx.input.setOnscreenKeyboardVisible(false);
                                    }
                                });

                                table.add(valid).width(300).height(60).padTop(30).padRight(5);

                                TextButton close = new TextButton("Annuler", skin);
                                //close.setStyle(style);
                                close.addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        table.remove();
                                        camera.zoom = origZoom;
                                        showForm = false;
                                        validForm = false;
                                        Gdx.input.setOnscreenKeyboardVisible(false);
                                    }
                                });
                                close.align(Align.center);
                                table.align(Align.center);
                                table.add(close).width(120).height(60).padTop(30);

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
                }
            });
            stage.addActor(zoneShape);
        }

        Gdx.input.isTouched();

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void moveTo(Zone zoneFrom, Zone zoneTo, ArrayList<Unit> units) {

        //Couleur de la zone d'origine
        Color originColor = zoneFrom.getColor();

        // On assigne le joueur à la zone
        zoneTo.setPlayer(zoneFrom.getPlayer());

        //On donne à la zoneTo selectionnée les unités restantes
        zoneFrom.removeUnits(units);
        zoneFrom.setActive(false);
        zoneFrom.setSelected(false);

        //Si la zone de départ devient vide.
        if (zoneFrom.getUnits().size() <= 0) {
            zoneFrom.setPlayer(null);
            zoneFrom.setColor(Color.WHITE);
            zoneFrom.setDefaultColor(zoneFrom.getColor());
        }

        units.addAll(zoneTo.getUnits());

        //On donne à la nouvelle zone acquise les unités passées dans le formulaire
        zoneTo.setUnits(units);
        //On assigne la couleur à la zone
        zoneTo.setColor(originColor);
        zoneTo.setDefaultColor(zoneTo.getColor());
        //On désactive la zone & on déselectionne
        zoneTo.setActive(false);
        zoneTo.setSelected(false);

        // On met à jour le fichier de la partie :
        GameSave.saveZone(zoneFrom.getID() - 1, zoneFrom.getStrColor(), zoneFrom.getUnits());
        GameSave.saveZone(zoneTo.getID() - 1, zoneTo.getStrColor(), zoneTo.getUnits());

        //On désactive toutes les zones de la map
        map.desactiveZones();
        //On donne une zone inexistante comme zone selectionnée
        map.setZoneSelected(0);
    }

    private void doAttack(ArrayList<Unit> attackUnits, Zone zoneFrom, Zone zoneTo) {
        ArrayList<Unit> attacker = attackUnits;
        ArrayList<Unit> defender = zoneTo.getUnits();
        int powerAttack = 0;
        int powerDefense = 0;

        // On récupère la valeur d'attaque générale sans bonus (dès)
        for (int idxATK = 0; idxATK < attacker.size(); idxATK++)
            powerAttack += attackUnits.get(idxATK).getAttack();

        // On récupère la valeur de défense générale sans bonus (dès)
        for (int idxDEF = 0; idxDEF < defender.size(); idxDEF++)
            powerDefense += zoneTo.getUnits().get(idxDEF).getDef();

        // Si l'attaquant est plus fort que le défenseur
        if (powerAttack - powerDefense > 0) {
            // On retire les troupes adverses
            zoneTo.getUnits().clear();

            // On déplace ses troupes conquérentes
            zoneFrom.getUnits().removeAll(attacker);
            zoneTo.setUnits(attacker);

            zoneTo.setPlayer(zoneFrom.getPlayer());
            zoneTo.setColor(zoneFrom.getColor());
            zoneTo.setDefaultColor(zoneFrom.getColor());

            if (zoneFrom.getUnits().size() - attacker.size() < 0) {
                zoneFrom.setPlayer(null);
                zoneFrom.setColor(Color.WHITE);
            }
        } else { // Si le défenseur est plus fort que l'attaquant
            // On retire les troupes de l'attaquant
            zoneFrom.getUnits().clear();
            zoneFrom.setPlayer(null);
            zoneFrom.setColor(Color.WHITE);
        }

        // On met à jour le fichier de la partie :
        GameSave.saveZone(zoneFrom.getID() - 1, zoneFrom.getStrColor(), zoneFrom.getUnits());
        GameSave.saveZone(zoneTo.getID() - 1, zoneTo.getStrColor(), zoneTo.getUnits());

        //On désactive la zoneTo & on déselectionne
        zoneFrom.setActive(false);
        zoneFrom.setSelected(false);

        //On désactive toutes les zones de la map
        map.desactiveZones();
        //On donne une zone inexistante comme zone selectionnée
        map.setZoneSelected(0);

    }

    private void addTableLine(Table T, final Object key, final Hashtable ht, Hashtable textFields, Skin skin) {
        // Label
        Label label = new Label(key.toString(), skin);
        label.setFontScale(1.3f);
        label.setAlignment(Align.left);
        T.add(label).width(200).padTop(10).padBottom(5);

        //TextField
        final Label text = new Label("0", skin);
        text.setFontScale(1.3f);
        textFields.put(key.toString(), text); //Stockage du TextField dans notre Hashtable de textfields.
        T.add(text).width(50).height(50);

        // Nombre d'unités disponibles - Valeur récupérée du HashTable ht.
        Label total = new Label("/" + ht.get(key), skin);
        total.setFontScale(1.3f);
        T.add(total).width(50).height(50);

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
        T.add(BtnDel).width(50).height(50).padRight(5).padBottom(5);

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
        T.add(BtnAdd).width(50).height(50).padRight(5).padBottom(5);

        T.row(); // On revient à la ligne dans le tableau
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
        if(selectedZone.getPlayer() == null)
            return;

        if(selectedZone.getPlayer() != player)
            return;

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
        Gdx.gl.glClearColor(30.f/255.f, 30.f/255.f, 30.f/255.f, 0.1f);

        int countAlive = 0;
        for(Player p : players) {
            if(p.isAlive())
                countAlive++;
        }

        if(countAlive == 1) //Fin du jeu
            showEnd();

        if(map.getPlayerZones( player ) == 0) {
            player.setDead(true);
        }

        if(!player.isActive() || player.isDead()) {
            if(playersIterator.hasNext()) {
                player = playersIterator.next();
                player.setActive(true);
            } else {
                playersIterator = players.listIterator();
            }
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void showEnd() {

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

            if (newZoom >= ZOOM_MAX) {
                camera.zoom = ZOOM_MAX;
                origZoom = ZOOM_MAX;
                baseDistance = distance;
            } else if (newZoom <= ZOOM_MIN) {
                camera.zoom = (float) ZOOM_MIN;
                origZoom = (float) ZOOM_MIN;
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

    public void moveCamera (boolean add, float x, float y) {
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

        if (newX - camera.viewportWidth/2*camera.zoom < (Math.abs(map.MARGIN) * -1))
            newX = (Math.abs(map.MARGIN) * -1) + camera.viewportWidth/2*camera.zoom;
        if (newX + camera.viewportWidth/2*camera.zoom > map.getSizex() + map.MARGIN)
            newX = map.getSizex() - camera.viewportWidth/2*camera.zoom + map.MARGIN;
        if (newY + camera.viewportHeight/2*camera.zoom > map.getSizey() + map.MARGIN)
            newY = map.getSizey() - camera.viewportHeight/2*camera.zoom + map.MARGIN;
        if (newY - camera.viewportHeight/2*camera.zoom < (Math.abs(map.MARGIN) * -1))
            newY = (Math.abs(map.MARGIN) * -1) + camera.viewportHeight/2*camera.zoom;

        camera.position.x = newX;
        camera.position.y = newY;
    }

}
