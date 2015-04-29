package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Class.Zone;
import fr.fliizweb.risk.Screens.Actors.ZoneActor;

/**
 * Created by rcdsm on 29/04/15.
 */
public class GameScreen implements Screen {

    private ZoneActor zoneShape;
    private Stage stage;
    private Group background;

    Map map;
    ArrayList<Player> players;

    private int 			height;
    private int 			width;

    public GameScreen() {
        map = new Map();
        zoneShape = new ZoneActor();
        players = new ArrayList<Player>();

        stage = new Stage();
        stage.stageToScreenCoordinates(new Vector2(0, 0));
        Gdx.input.setInputProcessor(stage);


        Gdx.app.log("GameScreen", String.valueOf(stage.getHeight()));

        background = new Group();
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(1, 1, 1, 1);

        stage.addActor(background);

        Gdx.input.setInputProcessor(stage);

        players.add(new Player("g0rp", PlayerColor.RED));
        players.add(new Player("Joadar", PlayerColor.GREEN));
        players.add(new Player("Thierry", PlayerColor.BLUE));
        players.add(new Player("Peric", PlayerColor.YELLOW));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(int i = 0; i < map.getZones().size(); i++) {
            Zone zone = map.getZone(i);
            zoneShape.setPosition(zone.getPosition().getX(), zone.getPosition().getY());
            background.addActor(zoneShape);
        }

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
}
