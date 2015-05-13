package fr.fliizweb.risk.Class.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by rcdsm on 28/04/15.
 */
public class GroundUnit implements Unit {

    /**
     * Variable : value
     * Type : int
     * Description : valeur de l'unité en terme de "poids" ou points
     */
    protected int value = 1;

    /**
     * Variable : attack
     * Type : int
     * Description : valeur de l'unité en attaque
     */
    protected int attack = 1;

    /**
     * Variable : def
     * Type : int
     * Description : valeur de l'unité en défense
     */
    protected int def = 1;

    protected TextureRegion textureRegion;
    protected Texture texture;


    /**
     * Constructeur Unit
     */
    public GroundUnit() {
        setTexture("Icons/" + this.getClass().getSimpleName() + ".png");
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }

    public void setTexture(String path) {
        texture = new Texture(Gdx.files.internal(path));
        textureRegion = new TextureRegion(texture);
    }

}
