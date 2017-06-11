package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.gamepad.TSSGamePad;
import com.drfl.twinstickshooter.game.TSSState;
import com.drfl.twinstickshooter.model.TSSModel;

/**
 * Main menu screen, options for starting game, seeing highscores, controlling volume,
 * choosing input method and exiting game.
 */
public class TSSMainMenu extends ScreenAdapter {

    /**
     * Max cooldown for playing a sound effect when changing sound volume.
     */
    private static final float SOUND_CD = 0.3f;

    /**
     * Current value of the sound change cooldown.
     */
    private float changeSound = SOUND_CD;

    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    /**
     * The viewport for the Scene2D stage.
     */
    private final Viewport viewport;

    /**
     * Selected input method for the game.
     */
    private TSSGame.ControlType selectedController = TSSGame.ControlType.KBM;

    /**
     * Select box for choosing the input method.
     */
    private SelectBox inputChoose;

    /**
     * Label for warning about potential issues on input method chosen.
     */
    private Label controlWarning;

    /**
     * Label for showing local IP.
     */
    private Label IPBox;

    /**
     * Actual value of the IPBox label.
     */
    private String IPField;

    /**
     * Slider for controlling music volume.
     */
    private Slider musicVolume;

    /**
     * Slider for controlling sound volume.
     */
    private Slider soundVolume;

    /**
     * Input options available for setting in the select box.
     */
    private String[] inputOptions = new String[] {
            "Keyboard / Mouse",
            "X360 Controller",
            "Android Controller",
    };

    /**
     * Constructs a main menu screen belonging to a certain game.
     *
     * @param game The game this screen belongs to
     */
    public TSSMainMenu(TSSGame game) {

        this.game = game;

        TSSMenuHelper.initInstance(game);

        loadAssets();
        TSSMenuHelper.getInstance().playMusic("Menu.ogg", game.getMusicVolume(), true);

        camera = TSSMenuHelper.getInstance().createCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = TSSMenuHelper.getInstance().createFitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        TSSModel.getInstance().setScore(0); //Prevent score from carrying over
    }

    /**
     * Called when this screen becomes the current screen for a game. Creates all the actors for a Scene2D stage
     * representing the main menu screen.
     */
    @Override
    public void show() {

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        Vector2 currCoords = new Vector2(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight()); //Center X and topmost Y
        Vector2 currSize = new Vector2(0.25f * Gdx.graphics.getWidth(), 0.10f * Gdx.graphics.getHeight());
        float paddingY = 0.04f * Gdx.graphics.getHeight();

        currCoords.y -= currSize.y + paddingY;
        TSSMenuHelper.getInstance().createTextButton("Start Game", currSize, currCoords, new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.setInputMode(selectedController);
                game.getStateM().processState(TSSState.GameEvent.START);
                return false;
            }
        });

        currCoords.y -= currSize.y + paddingY;
        TSSMenuHelper.getInstance().createTextButton("Highscores", currSize, currCoords, new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.getStateM().processState(TSSState.GameEvent.HIGHSCORE);
                return false;
            }
        });

        currCoords.y -= currSize.y / 2.0f;
        currSize.y = 0.05f * Gdx.graphics.getHeight();
        currCoords.y -= currSize.y / 2.0f + paddingY;
        inputChoose = TSSMenuHelper.getInstance().createSelectBox(currSize, currCoords, inputOptions);

        currCoords.y -= currSize.y;
        controlWarning = TSSMenuHelper.getInstance().createLabel("", currSize, currCoords, Color.RED, Align.left);

        currCoords.y -= currSize.y;
        if(!game.findSiteLocalAddress().isEmpty()) {
            IPBox = TSSMenuHelper.getInstance().createLabel("Suggested IP: " + game.findSiteLocalAddress().get(0), currSize, currCoords, Color.WHITE, Align.left);
            IPField = "Suggested IP: " + game.findSiteLocalAddress().get(0);
        } else {
            IPBox = TSSMenuHelper.getInstance().createLabel("Suggested IP: not found", currSize, currCoords, Color.WHITE, Align.left);
            IPField = "Suggested IP: not found";
        }

        currCoords.y -= currSize.y;
        TSSMenuHelper.getInstance().createLabel("Music Volume", currSize, currCoords, Color.WHITE, Align.center);

        currCoords.y -= currSize.y;
        musicVolume = TSSMenuHelper.getInstance().createHorSlider(new Vector2(0, 1), game.getMusicVolume(), currSize, currCoords, new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.setMusicVolume(musicVolume.getValue());
                ((Music)game.getAssetManager().get("Menu.ogg")).setVolume(game.getMusicVolume());
            }
        });

        currCoords.y -= currSize.y;
        TSSMenuHelper.getInstance().createLabel("Sound Volume", currSize, currCoords, Color.WHITE, Align.center);

        currCoords.y -= currSize.y;
        soundVolume = TSSMenuHelper.getInstance().createHorSlider(new Vector2(0, 1), game.getSoundVolume(), currSize, currCoords, new ClickListener() {

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

        currCoords.y -= currSize.y / 2.0f;
        currSize.y = 0.10f * Gdx.graphics.getHeight();
        currCoords.y -= currSize.y / 2.0f + paddingY;
        TSSMenuHelper.getInstance().createTextButton("Exit", currSize, currCoords, new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.getStateM().processState(TSSState.GameEvent.EXIT);
                return false;
            }
        });
    }

    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
        TSSMenuHelper.getInstance().loadMusic("Menu.ogg");
        this.game.getAssetManager().finishLoading();
    }

    /**
     * Handles input method choosing by checking the select box value and checking whether the input method
     * is valid by testing whether needed device exists.
     *
     * @return The control method to use
     */
    private TSSGame.ControlType parseInputMode() {

        controlWarning.setText("");
        IPBox.setText("");

        if(inputChoose.getSelected().equals("Keyboard / Mouse")) {
            return TSSGame.ControlType.KBM;

        } else if(inputChoose.getSelected().equals("X360 Controller")) {
            if(TSSGamePad.getInstance().controllerExists()) {
                return TSSGame.ControlType.CONTROLLER;
            } else controlWarning.setText("Controller not found, plug it and restart game please.");

        } else if(inputChoose.getSelected().equals("Android Controller")) {
            IPBox.setText(IPField);
            if(game.getServer().getConnections().length != 0) {
                return TSSGame.ControlType.REMOTE;
            } else controlWarning.setText("Controller app not connected, insert IP into controller app to connect.");
        }

        return TSSGame.ControlType.KBM;
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {

        if(changeSound > 0) changeSound -= delta;

        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        game.getBatch().begin();
        game.getBatch().draw((Texture) game.getAssetManager().get("MainMenuBack.jpg"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().end();

        game.getStage().act(delta);
        game.getStage().draw();

        selectedController = parseInputMode();
    }

    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    @Override
    public void hide() {
        this.dispose();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        game.getStage().dispose();
        game.getAssetManager().unload("MainMenuBack.jpg");
        game.getAssetManager().unload("Menu.ogg");
    }
}