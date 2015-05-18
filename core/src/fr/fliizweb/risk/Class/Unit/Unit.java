package fr.fliizweb.risk.Class.Unit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by rcdsm on 28/04/15.
 */
public interface Unit {

    TextureRegion getTexture();
    void setTexture(String path);

    int getAttack();
    int getDef();
    int getValue();
}
