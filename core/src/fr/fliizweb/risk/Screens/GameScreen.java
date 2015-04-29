package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Class.Zone;

/**
 * Created by rcdsm on 29/04/15.
 */
public class GameScreen implements Screen {

    private ShapeRenderer shape;
    private Stage stage;
    private Group background;

    Map map;
    ArrayList<Player> players;

    private int 			height;
    private int 			width;

    public GameScreen() {
        map = new Map();
        shape = new ShapeRenderer();
        players = new ArrayList<Player>();

        stage = new Stage(new ScreenViewport());
        background = new Group();

        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(1, 1, 1, 1);

        // Notice the order
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        for(int i = 0; i < map.getZones().size(); i++) {
            Zone zone = map.getZone(i);

            shape.setColor(1, 0, 0, 1);
            shape.rect(zone.getPosition().getX(), zone.getPosition().getY(), zone.getSize().getX(), zone.getSize().getY());
        }

        shape.end();

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
