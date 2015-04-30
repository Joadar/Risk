package fr.fliizweb.risk.Screens.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    TextureRegion region;

    Zone zone;
    ArrayList<Player> players;

    public ZoneActor(Zone zone) {
        super();

        region = new TextureRegion( new Texture("blank.jpg") );

        this.zone = zone;
        setWidth(zone.getSize().getX());
        setHeight(zone.getSize().getY());
        this.setBounds(zone.getPosition().getX(), zone.getPosition().getY(), getWidth(), getHeight());
        this.addListener(inputListener);
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
        //super.draw(batch, parentAlpha);
        Player p = getZonePlayer(zone);
        CharSequence str = String.valueOf(zone.getID());
        BitmapFont font = new BitmapFont();
        batch.setColor(zone.getRGBColor());
        batch.draw(region, zone.getPosition().getX(), zone.getPosition().getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.draw(batch, str, zone.getPosition().getX() + zone.getSize().getX() / 2, zone.getPosition().getY() + zone.getSize().getY() / 2);
    }


    private ActorGestureListener inputListener = new ActorGestureListener() {

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            super.tap(event, x, y, count, button);
        }
    };



}
