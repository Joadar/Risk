package fr.fliizweb.risk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import fr.fliizweb.risk.Class.Player.Score;
import fr.fliizweb.risk.Risk;

/**
 * Created by rcdsm on 20/05/15.
 */
public class StatsScreen implements Screen {

    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport vp;
    private SpriteBatch batch;

    public Risk game;

    InputMultiplexer inputMultiplexer;

    private Skin skin;

    public StatsScreen() {
        init();
    }

    public StatsScreen(Risk game){
        this.game = game;
        init();
    }

    public void init() {
        inputMultiplexer = new InputMultiplexer();
        //inputMultiplexer.addProcessor(new GestureDetector(this));
    }

    @Override
    public void show() {//camera
        camera = new OrthographicCamera();
        camera.zoom = 1.693f;
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();

        //viewport & stage
        vp = new FitViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera );
        stage = new Stage( vp );
        stage.getViewport().setCamera(camera);

        batch = new SpriteBatch();

        // On récupère le skin qu'on veut donner à notre formulaire (android.assets/ui/defaultskin.json)
        skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        final Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://172.31.1.54/Risk/scores/32").build();
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String content = httpResponse.getResultAsString();
                try {
                    Json json = new Json();
                    JsonValue root = new JsonReader().parse(content);

                    ArrayList<Score> listScores = new ArrayList<Score>();

                    for(int i = 0; i < root.size; i++){
                        JsonValue score = root.get(i);
                        Score aScore = new Score();
                        aScore.setId(Integer.valueOf(score.getString("id")));
                        aScore.setScore(Integer.valueOf(score.getString("score")));
                        aScore.setUserId(Integer.valueOf(score.getString("user_id")));
                        aScore.setDate(score.getString("date"));

                        listScores.add(aScore);
                    }

                    showScores( listScores );

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });

        Gdx.input.isTouched();

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void showScores( ArrayList<Score> scores ) {

        final Table table = new Table(); // On créé une table pour mettre nos éléments du formulaire
        table.setVisible(true); // Visible à vrai (ici c'est juste un test, par défaut c'est vrai)
        table.setSize(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() - 30); // On met la taille de la table à celle de l'écran
        table.setPosition(30, 30);

        camera.zoom = 0.6f; // Faire un zoom lorsqu'on affiche le formulaire et le placer correctement par rapport à l'écran ? Désactiver le scroll et zoom lorsque le formulaire est affiché ?



        Label labelScore = new Label("Liste de scores", skin);
        labelScore.setWrap(true);
        table.add(labelScore).width(200).height(60).pad(5);
        table.row();

        for(Score score : scores){
            Label label = new Label(String.valueOf(score.getScore()), skin);
            label.setWrap(true);
            table.add(label).width(200).height(60).pad(5);
            table.row();
        }

        stage.addActor(table);
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
}
