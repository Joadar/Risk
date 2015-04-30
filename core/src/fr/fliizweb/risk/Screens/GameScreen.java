package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Screens.Actors.BackgroundActor;
import fr.fliizweb.risk.Screens.Actors.ZoneActor;


/**
 * Created by rcdsm on 29/04/15.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {

    private Stage stage;
    private ScreenViewport vp;
    private SpriteBatch batch;

    private OrthographicCamera camera;

    Map map;
    ArrayList<Player> players;

    public GameScreen() {
        map = new Map();
        players = new ArrayList<Player>();

        players.add(new Player("g0rp", PlayerColor.RED));
        players.add(new Player("Joadar", PlayerColor.GREEN));
        players.add(new Player("Thierry", PlayerColor.BLUE));
        players.add(new Player("Peric", PlayerColor.YELLOW));
    }

    @Override
    public void show() {
        //camera
        camera = new OrthographicCamera();
        camera.position.set(map.getSizex() / 2, map.getSizey() / 2, 0);
        camera.update();


        //viewport & stage
        vp = new ScreenViewport( camera );
        vp.setScreenWidth(map.getSizex());
        vp.setScreenHeight(map.getSizey());
        stage = new Stage( vp );
        stage.getViewport().setCamera(camera);

        batch = new SpriteBatch();

        BackgroundActor background = new BackgroundActor(map);
        stage.addActor(background);

        for(int i = 0; i < map.getZones().size(); i++) {
            ZoneActor zoneShape = new ZoneActor(map.getZone(i));
            zoneShape.setPlayers(players);
            stage.addActor(zoneShape);
        }

        Gdx.input.isTouched();
        Gdx.input.setInputProcessor(new GestureDetector(this));
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
        Gdx.app.log("Stage", "x = " + String.valueOf(x) + "y = " + String.valueOf(y) + "deltax = " + String.valueOf(deltaX) + "deltay = " + String.valueOf(deltaY));

        camera.position.set(camera.position.x - deltaX, camera.position.y + deltaY, 0);
        camera.update();
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        Gdx.app.log("Stage", "Zoom = " + camera.zoom + " ||  initialDistance = " + initialDistance + " || distance = " + distance);

        if(camera.zoom <= 0.5) {
            camera.zoom = 0.501f;
            return false;
        } else if (camera.zoom >= 1.5){
            camera.zoom = 1.499f;
            return false;
        }

        if(distance < initialDistance){
            camera.zoom += 0.1;
        } else {
            camera.zoom -= 0.1;
        }
        camera.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

}
