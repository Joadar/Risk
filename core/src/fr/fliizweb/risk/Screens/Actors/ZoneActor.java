package fr.fliizweb.risk.Screens.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

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
}
