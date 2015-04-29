package fr.fliizweb.risk.Screens.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Player.Player;
import fr.fliizweb.risk.Class.Player.PlayerColor;
import fr.fliizweb.risk.Class.Zone;

/**
 * Created by rcdsm on 29/04/15.
 */
public class ZoneActor extends Actor {

    private ShapeRenderer shape;

    Zone zone;
    ArrayList<Player> players;

    public ZoneActor(Zone zone) {
        super();

        shape = new ShapeRenderer();
        this.zone = zone;
        setWidth(zone.getSize().getX());
        setHeight(zone.getSize().getY());
        this.setBounds(0, 0, getWidth(), getHeight());
        Gdx.app.log("Zone", String.valueOf(getWidth()));
        Gdx.app.log("Zone", String.valueOf(getHeight()));
        this.addListener(zoneInputListener);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Player getZonePlayer(Zone zone) {
        for(Player p: players) {
            if(p.getColor() == zone.getColor())
                return p;
        }
        return null;
    }

    public ShapeRenderer getShape() {
        return shape;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        Player p = getZonePlayer(zone);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(zone.getPosition().getX(), zone.getPosition().getY(), zone.getSize().getX(), zone.getSize().getY());
        shape.setColor(zone.getRGBColor());
        shape.end();
        batch.begin();
    }

    private static InputListener zoneInputListener = new InputListener() {
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            //Actor z = (Actor) event.getRelatedActor();
            Gdx.app.log("Zone", "TOUCH DOWN !");
            return true; //or false
        }
    };

}
