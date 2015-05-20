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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

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
    Map map;

    private static final int BORDERSIZE = 10;
    private static final int UNIT_ICON_SIZE = 92;

    public ZoneActor(Zone zone, Map map) {
        super();

        region = new TextureRegion( new Texture("blank.jpg") );

        this.zone = zone;
        this.map = map;
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
            /*
            for(Zone z : map.getActiveZones()) {
                Gdx.app.log("ZoneActor", "DRAW LINE");
                batch.setColor(Color.BLACK);
                this.setZIndex(12);
                drawLine(batch, region, zone.getPosition().getX() + zone.getSize().getX() / 2, zone.getPosition().getY() + zone.getSize().getY() / 2, z.getPosition().getX() + z.getSize().getX() / 2, z.getPosition().getY() + z.getSize().getY() / 2, 10.f);
            }
            */
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
        Hashtable unitsHashtable = zone.getUnitsHashtable();
        int i = 0;
        int countTypeUnits = unitsHashtable.size();

        String tmp = null;
        c.a = 1.0f;
        batch.setColor(Color.WHITE);
        for(Unit unit : zone.getSortedUnits()) {
            if(tmp == null || !(tmp.equals(unit.getClass().getSimpleName()))) {
                tmp = unit.getClass().getSimpleName();
                i++;
                if(this.getHeight() > this.getWidth()) {
                    float total = UNIT_ICON_SIZE * countTypeUnits;
                    float posX = ((zone.getPosition().getX() + zone.getSize().getX() / 2) - (UNIT_ICON_SIZE / 2));
                    float posY = ((zone.getPosition().getY() + (zone.getSize().getY() / 2)) - (UNIT_ICON_SIZE / 2)) + (total / 2) - (UNIT_ICON_SIZE * i) + (15 / countTypeUnits);

                    batch.draw(unit.getTexture(), posX, posY, getOriginX(), getOriginY(), UNIT_ICON_SIZE, UNIT_ICON_SIZE, getScaleX(), getScaleY(), getRotation());

                    CharSequence str = String.valueOf(unitsHashtable.get(unit.getClass().getSimpleName()));
                    font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    font.getData().setScale(2, 2);
                    font.draw(batch, str, posX + UNIT_ICON_SIZE + 20, posY + UNIT_ICON_SIZE / 2 + 10);
                } else {
                    float total = UNIT_ICON_SIZE * countTypeUnits;
                    float posX = ((zone.getPosition().getX() + zone.getSize().getX() / 2) - (UNIT_ICON_SIZE / 2)) + (total / 2) - (UNIT_ICON_SIZE * i);
                    float posY = (zone.getPosition().getY() + zone.getSize().getY() / 2) - (UNIT_ICON_SIZE / 2);

                    batch.draw(unit.getTexture(), posX, posY, getOriginX(), getOriginY(), UNIT_ICON_SIZE, UNIT_ICON_SIZE, getScaleX(), getScaleY(), getRotation());

                    CharSequence str = String.valueOf(unitsHashtable.get(unit.getClass().getSimpleName()));
                    font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    font.getData().setScale(2, 2);
                    font.draw(batch, str, posX + UNIT_ICON_SIZE / 2, posY - 20);
                }
            }
        }

    }

    void drawLine(Batch batch, TextureRegion region, float x1, float y1, float x2, float y2, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        float rad = (float)Math.atan2(dy, dx);
        batch.draw(region, x1, y1, x2, y2, dist, thickness, 1.f, 1.f, rad);
    }
}
