package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSGamePad;
import com.drfl.twinstickshooter.TSSState;

import java.util.ArrayList;

public class TSSMainMenu extends ScreenAdapter {

    /**
     * Tileset size. (must be square tiles)
     */
    public static final int TILESIZE = 32; //32x32

    /**
     * Every tile is a meter.
     */
    public static final float PIXEL_TO_METER = 1.0f / TILESIZE;

    /**
     * The width of the viewport in meters (equivalent to number of tiles). The height is
     * automatically calculated using the screen ratio.
     */
    private static final float VIEWPORT_WIDTH = 40;
    private static final float VIEWPORT_HEIGHT = 22.5f;

    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    private final Viewport viewport;

    private TSSGame.ControlType selectedController = TSSGame.ControlType.KBM;

    private TextButton startGame;
    private TextButton scoreboard;
    private TextButton exitGame;
    private SelectBox inputMethod;
    private Label controlWarning;
    private Label IPBox;
    private String IPField;
    private Label musicLabel;
    private Slider musicVolume;
    private Label soundLabel;
    private Slider soundVolume;

    ArrayList<String> inputOptions = new ArrayList<String>();

    private static final float SOUND_CD = 0.3f;

    private float changeSound = SOUND_CD;

    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    public TSSMainMenu(TSSGame game) {

        this.game = game;

        loadAssets();

        startMusic();

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

        inputOptions.add("Keyboard / Mouse");
        inputOptions.add("X360 Controller");
        inputOptions.add("Android Controller");

        startGame = new TextButton("Start Game", skin);
        startGame.setSize(0.25f * Gdx.graphics.getWidth(),0.15f * Gdx.graphics.getHeight());
        startGame.setPosition(Gdx.graphics.getWidth() / 2.0f - startGame.getWidth() / 2.0f,Gdx.graphics.getHeight() - startGame.getHeight() - 0.10f * Gdx.graphics.getHeight());

        startGame.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.setInputMode(selectedController);
                game.getStateM().processState(TSSState.GameEvent.START);
                return false;
            }
        });

        scoreboard = new TextButton("Highscores", skin);
        scoreboard.setSize(0.25f * Gdx.graphics.getWidth(),0.15f * Gdx.graphics.getHeight());
        scoreboard.setPosition(Gdx.graphics.getWidth() / 2.0f - scoreboard.getWidth() / 2.0f,Gdx.graphics.getHeight() - scoreboard.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight());

        scoreboard.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.getStateM().processState(TSSState.GameEvent.HIGHSCORE);
                return false;
            }
        });

        inputMethod = new SelectBox(skin);
        inputMethod.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        inputMethod.setPosition(Gdx.graphics.getWidth() / 2.0f - inputMethod.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - inputMethod.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - 1.10f * scoreboard.getHeight());
        inputMethod.setItems(inputOptions.toArray());

        controlWarning = new Label("", skin);
        controlWarning.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        controlWarning.setPosition(Gdx.graphics.getWidth() / 2.0f - controlWarning.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - controlWarning.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - 1.10f * inputMethod.getHeight());
        controlWarning.setColor(Color.RED);

        if(!game.findSiteLocalAddress().isEmpty()) {
            IPBox = new Label("Suggested IP: " + game.findSiteLocalAddress().get(0), skin);
            IPField = "Suggested IP: " + game.findSiteLocalAddress().get(0);
        } else {
            IPBox = new Label("Suggested IP: not found", skin);
            IPField = "Suggested IP: not found";
        }
        IPBox.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        IPBox.setPosition(Gdx.graphics.getWidth() / 2.0f - IPBox.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - IPBox.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - inputMethod.getHeight() - 1.10f * controlWarning.getHeight());

        musicLabel = new Label("Music Volume:", skin);
        musicLabel.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        musicLabel.setPosition(Gdx.graphics.getWidth() / 2.0f - musicLabel.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - musicLabel.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - inputMethod.getHeight() - controlWarning.getHeight() - 1.10f * IPBox.getHeight());

        musicVolume = new Slider(0.0f, 1.0f, 0.05f, false, skin);
        musicVolume.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        musicVolume.setPosition(Gdx.graphics.getWidth() / 2.0f - musicVolume.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - musicVolume.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - inputMethod.getHeight() - controlWarning.getHeight() - IPBox.getHeight() - 0.75f * musicLabel.getHeight());
        musicVolume.setValue(game.getMusicVolume());
        musicVolume.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.setMusicVolume(musicVolume.getValue());
                ((Music)game.getAssetManager().get("Menu.ogg")).setVolume(game.getMusicVolume());
            }
        });

        soundLabel = new Label("SFX Volume:", skin);
        soundLabel.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        soundLabel.setPosition(Gdx.graphics.getWidth() / 2.0f - soundLabel.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - soundLabel.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - inputMethod.getHeight() - controlWarning.getHeight() - IPBox.getHeight() - musicLabel.getHeight() - 1.10f * musicVolume.getHeight());

        soundVolume = new Slider(0.0f, 1.0f, 0.05f, false, skin);
        soundVolume.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        soundVolume.setPosition(Gdx.graphics.getWidth() / 2.0f - soundVolume.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - soundVolume.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - inputMethod.getHeight() - controlWarning.getHeight() - IPBox.getHeight() - musicLabel.getHeight() - musicVolume.getHeight() - 0.75f * soundLabel.getHeight());
        soundVolume.setValue(game.getSoundVolume());
        soundVolume.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {
                return true;
            }

            @Override

            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setSoundVolume(soundVolume.getValue());
                if(changeSound <= 0) {
                    ((Sound)game.getAssetManager().get("Shoot.mp3")).stop();
                    ((Sound)game.getAssetManager().get("Shoot.mp3")).play(game.getSoundVolume());
                    changeSound = SOUND_CD;
                }
            }
        });

        exitGame = new TextButton("Exit", skin);
        exitGame.setSize(0.25f * Gdx.graphics.getWidth(),0.15f * Gdx.graphics.getHeight());
        exitGame.setPosition(Gdx.graphics.getWidth() / 2.0f - exitGame.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - exitGame.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - scoreboard.getHeight() - inputMethod.getHeight() - controlWarning.getHeight() - IPBox.getHeight() - musicLabel.getHeight() - musicVolume.getHeight() - soundLabel.getHeight() - 1.10f * soundVolume.getHeight());
        exitGame.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.getStateM().processState(TSSState.GameEvent.EXIT);
                return false;
            }
        });

        game.getStage().addActor(startGame);
        game.getStage().addActor(scoreboard);
        game.getStage().addActor(inputMethod);
        game.getStage().addActor(controlWarning);
        game.getStage().addActor(IPBox);
        game.getStage().addActor(musicLabel);
        game.getStage().addActor(musicVolume);
        game.getStage().addActor(soundLabel);
        game.getStage().addActor(soundVolume);
        game.getStage().addActor(exitGame);
    }

    private OrthographicCamera createCamera() {

        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_HEIGHT / PIXEL_TO_METER);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        return camera;
    }

    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
        this.game.getAssetManager().load("Menu.ogg", Music.class);
        this.game.getAssetManager().finishLoading();
    }

    private TSSGame.ControlType parseInputMode() {

        controlWarning.setText("");
        IPBox.setText("");

        if(inputMethod.getSelected().equals("Keyboard / Mouse")) {
            return TSSGame.ControlType.KBM;

        } else if(inputMethod.getSelected().equals("X360 Controller")) {
            if(TSSGamePad.getInstance().controllerExists()) {
                return TSSGame.ControlType.CONTROLLER;
            } else controlWarning.setText("Controller not found, plug it and restart game please.");

        } else if(inputMethod.getSelected().equals("Android Controller")) {
            IPBox.setText(IPField);
            if(game.getServer().getConnections().length != 0) {
                return TSSGame.ControlType.REMOTE;
            } else controlWarning.setText("Controller app not connected, insert IP into controller app to connect.");
        }

        return TSSGame.ControlType.KBM;
    }

    /**
     * Renders this screen.
     *
     * @param delta time since last renders in seconds.
     */
    @Override
    public void render(float delta) {

//        handleInputs();

        if(changeSound > 0) changeSound -= delta;

        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        game.getBatch().begin();
        drawElements();
        game.getBatch().end();

        game.getStage().act(delta);
        game.getStage().draw();

        selectedController = parseInputMode();
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
