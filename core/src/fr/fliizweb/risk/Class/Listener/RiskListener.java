package fr.fliizweb.risk.Class.Listener;

import com.apple.eawt.event.GestureListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by rcdsm on 29/04/15.
 */
public class RiskListener implements GestureDetector.GestureListener {

    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log("Read", "touchdown");
        return false;
    }

    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("Read", "tap");
        return false;
    }

    public boolean longPress(float x, float y) {
        Gdx.app.log("Read", "longpress");
        return false;
    }

    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log("Read", "fling");
        return false;
    }

    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Gdx.app.log("Read", "pan");
        return false;
    }

    public boolean panStop(float x, float y, int pointer, int button) {
        Gdx.app.log("Read", "panStop");
        return false;
    }

    public boolean zoom (float originalDistance, float currentDistance){
        Gdx.app.log("Read", "zoom");
        return false;
    }

    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
        Gdx.app.log("Read", "pinch");
        return false;
    }
}