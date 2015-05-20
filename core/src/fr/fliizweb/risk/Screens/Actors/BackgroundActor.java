package fr.fliizweb.risk.Screens.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fr.fliizweb.risk.Class.Map;

/**
 * Created by rcdsm on 29/04/15.
 */
public class BackgroundActor extends Actor {

    TextureRegion region;

    public BackgroundActor(Map map) {
        super();

        region = new TextureRegion( new Texture("blank.jpg") );

        setWidth(map.getSizex() * 2);
        setHeight(map.getSizey() * 2);
        this.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        batch.setColor( new Color(255, 255, 255, 1));
        batch.draw(region, 0, 0, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
