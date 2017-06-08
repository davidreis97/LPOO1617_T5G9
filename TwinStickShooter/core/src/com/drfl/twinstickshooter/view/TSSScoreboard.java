package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSScore;
import com.drfl.twinstickshooter.TSSState;
import com.drfl.twinstickshooter.model.TSSModel;

public class TSSScoreboard extends ScreenAdapter {

    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    private final Viewport viewport;

    private TextField nameField;
    private Label nameTitle;
    private Label warning;
    private TextButton accept;
    private Array<Label> scoreLabel = new Array<Label>();

    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    public TSSScoreboard(TSSGame game) {

        this.game = game;

        loadAssets();

        startMusic(); //TODO music

        camera = createCamera();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    private void startMusic() {

        ((Music)game.getAssetManager().get("Menu.ogg")).setLooping(true);
        ((Music)game.getAssetManager().get("Menu.ogg")).setVolume(game.getMusicVolume());
        ((Music)game.getAssetManager().get("Menu.ogg")).play();
    }

    @Override
    public void show() {

        //TODO refactor

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        nameField = new TextField("", skin);
        nameField.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        nameField.setPosition(Gdx.graphics.getWidth() / 2.0f - nameField.getWidth() / 2.0f, nameField.getHeight() / 2.0f);

        accept = new TextButton("OK", skin);
        accept.setSize(accept.getWidth(),0.05f * Gdx.graphics.getHeight());
        accept.setPosition(Gdx.graphics.getWidth() / 2.0f + nameField.getWidth() / 2.0f, nameField.getHeight() / 2.0f);
        accept.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                if(TSSModel.getInstance().getScore() == 0) {
                    game.getStateM().processState(TSSState.GameEvent.MAIN);
                } else {
                    if(!nameField.getText().isEmpty()) {

                        game.addScore(nameField.getText(), TSSModel.getInstance().getScore());
                        
                        game.getStateM().processState(TSSState.GameEvent.MAIN);
                    } else warning.setText("Invalid name!");
                }
                return false;
            }
        });

        warning = new Label("", skin);
        warning.setSize(warning.getWidth(),0.05f * Gdx.graphics.getHeight());
        warning.setPosition(Gdx.graphics.getWidth() / 2.0f + nameField.getWidth() / 2.0f + accept.getWidth(), nameField.getHeight() / 2.0f);
        warning.setColor(Color.RED);

        nameTitle = new Label("Input name:", skin);
        nameTitle.setSize(0.25f * Gdx.graphics.getWidth(), nameTitle.getHeight());
        nameTitle.setPosition(Gdx.graphics.getWidth() / 2.0f - nameTitle.getWidth() / 2.0f,nameTitle.getHeight() + nameField.getHeight());

        Array<TSSScore> scores = game.getScores();

        float tableStartWidth = Gdx.graphics.getWidth() * 0.25f;
        float tableStartHeight = Gdx.graphics.getHeight() * 0.15f;

        Label nameHeader = new Label("Name", skin);
        nameHeader.setFontScale(1.2f);
        nameHeader.setColor(Color.DARK_GRAY);
        nameHeader.setPosition(tableStartWidth, Gdx.graphics.getHeight() - nameHeader.getHeight() - tableStartHeight);
        scoreLabel.add(nameHeader);

        Label scoreHeader = new Label("Score", skin);
        scoreHeader.setFontScale(1.2f);
        scoreHeader.setColor(Color.DARK_GRAY);
        scoreHeader.setPosition(tableStartWidth * 2, Gdx.graphics.getHeight() - scoreHeader.getHeight() - tableStartHeight);
        scoreLabel.add(scoreHeader);

        Label dateHeader = new Label("Date (YYYY-MM-DD)", skin);
        dateHeader.setFontScale(1.2f);
        dateHeader.setColor(Color.DARK_GRAY);
        dateHeader.setPosition(tableStartWidth * 3, Gdx.graphics.getHeight() - dateHeader.getHeight() - tableStartHeight);
        scoreLabel.add(dateHeader);

        int count = 3;
        for(TSSScore score : scores) {
            Label name = new Label(score.getName(), skin);
            name.setPosition(tableStartWidth, Gdx.graphics.getHeight() - name.getHeight() * count - tableStartHeight);
            scoreLabel.add(name);

            Label scoreText = new Label(score.getScore() + "", skin);
            scoreText.setPosition(tableStartWidth * 2, Gdx.graphics.getHeight() - name.getHeight() * count - tableStartHeight);
            scoreLabel.add(scoreText);

            Label date = new Label(score.getDate(), skin);
            date.setPosition(tableStartWidth * 3, Gdx.graphics.getHeight() - name.getHeight() * count - tableStartHeight);
            scoreLabel.add(date);

            count++;
        }

        game.getStage().addActor(nameField);
        game.getStage().addActor(accept);
        game.getStage().addActor(warning);
        game.getStage().addActor(nameTitle);

        for(Label label : scoreLabel) {
            game.getStage().addActor(label);
        }
    }

    private OrthographicCamera createCamera() {

        OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        return camera;
    }

    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
        this.game.getAssetManager().load("Menu.ogg", Music.class);
        this.game.getAssetManager().finishLoading();
    }

    /**
     * Renders this screen.
     *
     * @param delta time since last renders in seconds.
     */
    @Override
    public void render(float delta) {

//        handleInputs();

        game.getBatch().setProjectionMatrix(camera.combined);

        if(nameField.getText().length() > 12) { //TODO magic value
            nameField.setText(nameField.getText().substring(0, 12));
            nameField.setCursorPosition(12);
        }

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        game.getBatch().begin();
        drawElements();
        game.getBatch().end();

        game.getStage().act(delta);
        game.getStage().draw();
    }

    private void drawElements() {

        Texture background = game.getAssetManager().get("MainMenuBack.jpg");

        game.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        game.getStage().dispose();
        game.getAssetManager().unload("MainMenuBack.jpg");
        game.getAssetManager().unload("Menu.ogg");
        skin.dispose();
    }
}
