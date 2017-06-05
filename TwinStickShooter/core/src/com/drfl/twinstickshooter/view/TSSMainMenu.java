package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private SelectBox inputMethod;
    private Label controlWarning;
    private Label IPBox;
    private String IPField;

    ArrayList<String> inputOptions = new ArrayList<String>();

    Skin skin = new Skin(Gdx.files.internal("uiskin.json")); //TODO can use assetManager?

    public TSSMainMenu(TSSGame game) {

        this.game = game;

        loadAssets();

        startMusic();

        camera = createCamera();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    private void startMusic() {

        ((Music)game.getAssetManager().get("MenuIntro.wav")).setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                ((Music)game.getAssetManager().get("Menu.wav")).setLooping(true);
                ((Music)game.getAssetManager().get("Menu.wav")).setVolume(0.5f);
                ((Music)game.getAssetManager().get("Menu.wav")).play();
            }
        });

        ((Music)game.getAssetManager().get("MenuIntro.wav")).setVolume(0.5f);
        ((Music)game.getAssetManager().get("MenuIntro.wav")).play();
    }

    @Override
    public void show() {

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        inputOptions.add("Keyboard / Mouse");
        inputOptions.add("X360 Controller");
        inputOptions.add("Android Controller");

        //TODO move to function
        startGame = new TextButton("Start Game", skin);
        startGame.setSize(0.25f * Gdx.graphics.getWidth(),0.15f * Gdx.graphics.getHeight());
        startGame.setPosition(Gdx.graphics.getWidth() / 2.0f - startGame.getWidth() / 2.0f,Gdx.graphics.getHeight() - startGame.getHeight() - 0.10f * Gdx.graphics.getHeight());

        //TODO optional depending on input method?
        startGame.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.setInputMode(selectedController);
                game.getStateM().processState(TSSState.GameEvent.START);
                return false;
            }
        });

        inputMethod = new SelectBox(skin);
        inputMethod.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        inputMethod.setPosition(Gdx.graphics.getWidth() / 2.0f - inputMethod.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - inputMethod.getHeight() - 0.10f * Gdx.graphics.getHeight() - 1.10f * startGame.getHeight());
        inputMethod.setItems(inputOptions.toArray());

        controlWarning = new Label("", skin);
        controlWarning.setSize(0.25f * Gdx.graphics.getWidth(),0.05f * Gdx.graphics.getHeight());
        controlWarning.setPosition(Gdx.graphics.getWidth() / 2.0f - controlWarning.getWidth() / 2.0f,
                Gdx.graphics.getHeight() - controlWarning.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - 1.10f * inputMethod.getHeight());
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
                Gdx.graphics.getHeight() - IPBox.getHeight() - 0.10f * Gdx.graphics.getHeight() - startGame.getHeight() - inputMethod.getHeight() - 1.10f * controlWarning.getHeight());

        game.getStage().addActor(startGame);
        game.getStage().addActor(inputMethod);
        game.getStage().addActor(controlWarning);
        game.getStage().addActor(IPBox);
    }

    private OrthographicCamera createCamera() {

        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_HEIGHT / PIXEL_TO_METER);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        return camera;
    }

    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
        this.game.getAssetManager().load("MenuIntro.wav", Music.class);
        this.game.getAssetManager().load("Menu.wav", Music.class);
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
        game.getAssetManager().unload("MenuIntro.wav");
        game.getAssetManager().unload("Menu.wav");
        skin.dispose();
    }
}
