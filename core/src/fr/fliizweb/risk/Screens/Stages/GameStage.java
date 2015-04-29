package fr.fliizweb.risk.Screens.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Class.Zone;
import fr.fliizweb.risk.Screens.Actors.ZoneActor;

/**
 * Created by rcdsm on 29/04/15.
 */
public class GameStage extends Stage {

    //private ZoneActor zoneShape;
    private ZoneActor zoneShape;
    private Group background;
    private OrthographicCamera camera;

    Map map;
    ArrayList<Player> players;

    public GameStage() {
        map = new Map();
        //zoneShape = new ZoneActor();
        players = new ArrayList<Player>();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        players.add(new Player("g0rp", PlayerColor.RED));
        players.add(new Player("Joadar", PlayerColor.GREEN));
        players.add(new Player("Thierry", PlayerColor.BLUE));
        players.add(new Player("Peric", PlayerColor.YELLOW));

        for(int i = 0; i < map.getZones().size(); i++) {
            zoneShape = new ZoneActor(map.getZone(i));
            zoneShape.setPlayers(players);
            addActor(zoneShape);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();

        camera.update();

    }

}
