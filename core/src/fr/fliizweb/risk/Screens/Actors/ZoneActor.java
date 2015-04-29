package fr.fliizweb.risk.Screens.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Zone;

/**
 * Created by rcdsm on 29/04/15.
 */
public class ZoneActor extends Actor {
    private ShapeRenderer shape;

    Map map;

    public ZoneActor() {
        super();
        shape = new ShapeRenderer();
    }

    public  void setMap(Map map) {
        this.map = map;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        for(int i = 0; i < map.getZones().size(); i++) {
            Zone zone = map.getZone(i);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.rect(zone.getPosition().getX(), zone.getPosition().getY(), zone.getSize().getX(), zone.getSize().getY());
            shape.setColor(1, 1f, 1f, 1f);
            shape.end();
        }
    }
}
