package fr.fliizweb.risk;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import fr.fliizweb.risk.Class.Map;
import fr.fliizweb.risk.Class.Zone;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture texture;
	private TextureRegion region;
	private Sprite sprite;

	Vector3 touchPoint = new Vector3();

	Map map;

	@Override
	public void create () {
		map = new Map();
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("badlogic.jpg"));
		region = new TextureRegion(texture, 20, 20, 50, 50);
		sprite = new Sprite(texture, 20, 20, 50, 50);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setColor(1, 0, 0, 1);

		for(int i = 0; i < map.getZones().size(); i++) {
			Zone zone = map.getZone(i);
			batch.draw(texture, zone.getPosition().getX(), zone.getPosition().getY());
			sprite.setColor(1, 0, 0, 1);
			sprite.draw(batch);
		}
		batch.end();
	}
}
