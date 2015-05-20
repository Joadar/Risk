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
import com.badlogic.gdx.math.MathUtils;
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
import fr.fliizweb.risk.Class.Unit.Unit;
import fr.fliizweb.risk.Class.Zone;

/**
 * Created by rcdsm on 29/04/15.
 */
public class ZoneActor extends Actor {

    TextureRegion region;
    CharSequence str;
    BitmapFont font;

    float cosX;

    Zone zone;

    private static final int BORDERSIZE = 10;

    public ZoneActor(Zone zone) {
        super();

        region = new TextureRegion( new Texture("blank.jpg") );

        this.zone = zone;
        setWidth(zone.getSize().getX());
        setHeight(zone.getSize().getY());
        this.setBounds(zone.getPosition().getX(), zone.getPosition().getY(), getWidth(), getHeight());
        str = String.valueOf(zone.getID());
        font = new BitmapFont();
        cosX = 0f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        cosX += 0.05f;
        Player p = zone.getPlayer();
        Color c = zone.getColor();
        this.setZIndex(10);

        c.a = 0.5f;
        batch.setColor(c);
        batch.draw(region, zone.getPosition().getX(), zone.getPosition().getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        c.a = 0.1f;

        if(zone.isActive())
            c.a = Math.abs(MathUtils.cos(cosX * 2)) / 2 + 0.5f;

        if(zone.isSelected()) {
            c.a = 1.0f;
            this.setZIndex(11);
        }

        batch.setColor(c);
        batch.draw(region, zone.getPosition().getX() + BORDERSIZE, zone.getPosition().getY() + BORDERSIZE, getOriginX(), getOriginY(),
                getWidth() - BORDERSIZE * 2, getHeight() - BORDERSIZE * 2, getScaleX(), getScaleY(), getRotation());

        /*
        ECRIRE LE NUMERO DE LA ZONE AU CENTRE DE L'ECRAN

        CharSequence str = String.valueOf(zone.getID());
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.draw(batch, str, zone.getPosition().getX() + zone.getSize().getX() / 2, zone.getPosition().getY() + zone.getSize().getY() / 2);
        */

        //On dessine les icones des unités.
        int i = 0, yPos = 0;
        c.a = 1.0f;
        batch.setColor(Color.WHITE);
        String tmp = null;
        for(Unit unit : zone.getSortedUnits()) {
            i++;
            if(tmp == null || !(tmp.equals(unit.getClass().getSimpleName()))) {
                tmp = unit.getClass().getSimpleName();
                yPos += 92;
                i = 1;
            }
            batch.draw(unit.getTexture(), zone.getPosition().getX() + 20 * i, zone.getPosition().getY() + (zone.getSize().getY() - yPos - 20), getOriginX(), getOriginY(), 92, 92, getScaleX(), getScaleY(), getRotation());
        }

    }
}
