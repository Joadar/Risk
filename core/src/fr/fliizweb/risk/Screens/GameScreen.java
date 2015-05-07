package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
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


    Map map;
    ArrayList<Player> players;

    public GameScreen() {
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
            zoneShape.setPlayers(players);
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
                    if(!map.isZoneSelected()) {
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
                        if(zone.getID() == map.getZoneSelected()) { //Si la zone tapée est la même que celle selectionnée
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
                        } else if(zone.isActive()) { //Si la zone selectionnée est une zone active
                            //On met la zone tapée de la couleur de la zone selectionnée au préalable.
                            //Puis on désactive toutes les zones.
                            Zone z = map.getZoneByID(map.getZoneSelected());
                            zone.setColor(z.getColor());
                            zone.setDefaultColor(zone.getColor());
                            z.setActive(false);
                            z.setSelected(false);
                            zone.setActive(false);
                            zone.setSelected(false);
                            map.desactiveZones();
                            map.setZoneSelected(0);
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

        moveCamera(true, camera.zoom * -deltaX, camera.zoom * deltaY);
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
