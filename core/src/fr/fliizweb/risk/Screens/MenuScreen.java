package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fr.fliizweb.risk.Class.GameSave;
import fr.fliizweb.risk.Risk;

/**
 * Created by rcdsm on 17/05/15.
 */
public class MenuScreen extends com.badlogic.gdx.Game implements Screen, GestureDetector.GestureListener {

    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport vp;
    private SpriteBatch batch;

    public Risk game;

    InputMultiplexer inputMultiplexer;

    public MenuScreen() {
        init();
    }

    public MenuScreen(Risk game){
        this.game = game;
        init();
    }

    public void init() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(this));
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        stage = new Stage();
        this.show();
    }

    @Override
    public void show() {
        //camera

        //viewport & stage
        vp = new FitViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera );
        stage = new Stage( vp );
        stage.getViewport().setCamera(camera);

        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();

        batch = new SpriteBatch();
        // On récupère le skin qu'on veut donner à notre formulaire (android.assets/ui/defaultskin.json)
        Skin skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));

        final Table table = new Table(); // On créé une table pour mettre nos éléments du formulaire
        table.setVisible(true); // Visible à vrai (ici c'est juste un test, par défaut c'est vrai)
        table.setSize(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() - 30); // On met la taille de la table à celle de l'écran
        table.setPosition(30, 30);

        camera.zoom = 0.4f; // Faire un zoom lorsqu'on affiche le formulaire et le placer correctement par rapport à l'écran ? Désactiver le scroll et zoom lorsque le formulaire est affiché ?

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(new Color(0f, 0f, 0f, 0.1f));
        pm1.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

        TextButton start = new TextButton("Reprendre", skin);
        table.add(start).width(200).height(60).pad(5);
        if(GameSave.fileExist()){
            start.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(game.getGameScreen());
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        } else {
            start.setTouchable(Touchable.disabled);
        }

        table.row();

        TextButton newGame = new TextButton("Nouvelle partie", skin);
        table.add(newGame).width(200).height(60).pad(5);
        newGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //GameSave.newGame(Gdx.files.internal("Maps/default.json")); // On lance une nouvelle partie avec la carte default.json
                super.touchDown(event, x, y, pointer, button);
                game.setScreen(game.getGameParametersScreen());
                return true;
            }
        });

        table.row();

        TextButton stats = new TextButton("Statistiques", skin);
        table.add(stats).width(200).height(60).pad(5);
        stats.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.getStatsScreen());

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        stage.addActor(table);

        Gdx.input.isTouched();

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
