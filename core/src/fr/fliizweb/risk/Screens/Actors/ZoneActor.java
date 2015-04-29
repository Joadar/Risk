package fr.fliizweb.risk.Screens.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by rcdsm on 29/04/15.
 */
public class ZoneActor extends Actor {

    ShapeRenderer shape;

    public ZoneActor () {
        shape = new ShapeRenderer();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        shape.setProjectionMatrix(batch.getProjectionMatrix());
        shape.setTransformMatrix(batch.getTransformMatrix());
        shape.translate(getX(), getY(), 0);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLUE);
        shape.rect(0, 0, getWidth(), getHeight());
        shape.end();

        batch.begin();
    }
}
