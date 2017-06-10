package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.game.TSSScore;
import com.drfl.twinstickshooter.game.TSSState;
import com.drfl.twinstickshooter.model.TSSModel;

/**
 * Scoreboard screen, shows top 10 high scores and allows inputting of high scores after a game.
 */
public class TSSScoreboard extends ScreenAdapter {

    //NOTEME javadoc
    /**
     * Amount of columns in high scores table.
     */
    private static final int COLUMN_NUMBER = 3;

    //NOTEME javadoc
    /**
     * Max number of letters allowed for high score name.
     */
    private static final int MAX_NAME_SIZE = 12;

    //NOTEME javadoc
    /**
     * Height constant for labels.
     */
    private static final float LABEL_HEIGHT = 0.05f * Gdx.graphics.getHeight();

    //NOTEME javadoc
    /**
     * Width constant for all widgets.
     */
    private static final float WIDGET_WIDTH = 0.25f * Gdx.graphics.getWidth();

    //NOTEME javadoc
    /**
     * Increment constant between each column. Jumps from the center of a
     * column to the next one with a spacing of (0.80f * Gdx.graphics.getWidth() - WIDGET_WIDTH * 3) / 2.0f.
     */
    private static final float INCREMENT = WIDGET_WIDTH + (0.80f * Gdx.graphics.getWidth() - WIDGET_WIDTH * 3) / 2.0f;

    //NOTEME javadoc
    /**
     * Center width of the first column. Pads 10% of the screen to the left.
     */
    private static final float START_WIDTH = WIDGET_WIDTH / 2.0f + 0.10f * Gdx.graphics.getWidth();

    //NOTEME javadoc
    /**
     * Adapter pattern interface for getting the different values from a TSSScore object without having to
     * explicitly write the getter methods.
     */
    public interface ScoreFunction {
        String getter(TSSScore score);
    }

    //NOTEME javadoc
    /**
     * Adapter pattern class to get a TSSScore object's name by overriding the
     * interface method with the getter from TSSScore.
     */
    private class ScoreName implements ScoreFunction {

        public String getter(TSSScore score) {
            return score.getName();
        }
    }

    //NOTEME javadoc
    /**
     * Adapter pattern class to get a TSSScore object's score value by overriding the
     * interface method with the getter from TSSScore.
     */
    private class ScoreValue implements ScoreFunction {

        public String getter(TSSScore score) {
            return score.getScore() + "";
        }
    }

    //NOTEME javadoc
    /**
     * Adapter pattern class to get a TSSScore object's date by overriding the
     * interface method with the getter from TSSScore.
     */
    private class ScoreDate implements ScoreFunction {

        public String getter(TSSScore score) {
            return score.getDate();
        }
    }

    //NOTEME javadoc
    /**
     * Array of concrete classes that implement ScoreFunction to use for adapter pattern.
     */
    private ScoreFunction[] scoreFunctions = new ScoreFunction[] {
            new ScoreName(),
            new ScoreValue(),
            new ScoreDate(),
    };

    //NOTEME javadoc
    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    //NOTEME javadoc
    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    //NOTEME javadoc
    /**
     * The viewport for the Scene2D stage.
     */
    private final Viewport viewport;

    //NOTEME javadoc
    /**
     * Text field to input name for high scores.
     */
    private TextField nameField;

    //NOTEME javadoc
    /**
     * Label for warning the user of mistakes in name input.
     */
    private Label warning;

    //NOTEME javadoc
    /**
     * High score table's column header names.
     */
    private String[] columnHeaders = new String[] {
            "Name",
            "Score",
            "Date (YYYY/MM/DD)",
    };

    //NOTEME javadoc
    /**
     * Constructs a scoreboard screen belonging to a certain game.
     *
     * @param game The game this screen belongs to
     */
    public TSSScoreboard(TSSGame game) {

        this.game = game;

        TSSMenuHelper.initInstance(game);

        loadAssets();
        TSSMenuHelper.getInstance().playMusic("Menu.ogg", game.getMusicVolume(), true);

        camera = TSSMenuHelper.getInstance().createCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = TSSMenuHelper.getInstance().createFitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    //NOTEME javadoc
    /**
     * Called when this screen becomes the current screen for a game. Creates all the actors for a Scene2D stage
     * representing the scoreboard screen.
     */
    @Override
    public void show() {

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        Array<TSSScore> scores = game.getScores();

        Vector2 currCoords = new Vector2();
        Vector2 currSize = new Vector2(WIDGET_WIDTH, LABEL_HEIGHT);

        for(int i = 0; i < COLUMN_NUMBER; i++) { //Table headers

            currCoords.set(START_WIDTH + INCREMENT * i, Gdx.graphics.getHeight() - LABEL_HEIGHT);
            TSSMenuHelper.getInstance().createLabel(columnHeaders[i], currSize, currCoords, Color.DARK_GRAY, Align.center);
        }

        for(int i = 0; i < scores.size; i++) { //Table row content
            for(int j = 0; j < COLUMN_NUMBER; j++) {

                currCoords.set(START_WIDTH + INCREMENT * j, Gdx.graphics.getHeight() - LABEL_HEIGHT * (i + 3));
                TSSMenuHelper.getInstance().createLabel(scoreFunctions[j].getter(scores.get(i)), currSize, currCoords, Color.WHITE, Align.center);
            }
        }

        currCoords.set(Gdx.graphics.getWidth() / 2.0f, currSize.y);
        nameField = TSSMenuHelper.getInstance().createTextField(currSize, currCoords);
        TSSMenuHelper.getInstance().createLabel("Input name:", currSize, new Vector2(currCoords.x, currCoords.y + currSize.y), Color.WHITE, Align.left);

        currCoords.x += currSize.x / 2.0f;
        currSize.x = LABEL_HEIGHT;
        currCoords.x += currSize.x / 2.0f;
        TSSMenuHelper.getInstance().createTextButton("OK", currSize, currCoords, new ClickListener() {

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

        currCoords.x += currSize.x / 2.0f;
        currSize.x = WIDGET_WIDTH;
        currCoords.x += currSize.x / 2.0f;
        warning = TSSMenuHelper.getInstance().createLabel("", currSize, currCoords, Color.RED, Align.left);
    }

    //NOTEME javadoc
    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
        TSSMenuHelper.getInstance().loadMusic("Menu.ogg");
        this.game.getAssetManager().finishLoading();
    }

    //NOTEME javadoc
    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        game.getBatch().setProjectionMatrix(camera.combined);

        if(nameField.getText().length() > MAX_NAME_SIZE) {
            nameField.setText(nameField.getText().substring(0, MAX_NAME_SIZE));
            nameField.setCursorPosition(MAX_NAME_SIZE);
        }

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        drawBackground();

        game.getStage().act(delta);
        game.getStage().draw();
    }

    //NOTEME javadoc
    /**
     * Draws the scoreboard screen background, calls batch begin and end.
     */
    private void drawBackground() {

        game.getBatch().begin();
        game.getBatch().draw((Texture) game.getAssetManager().get("MainMenuBack.jpg"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().end();
    }

    //NOTEME javadoc
    /**
     * Called when this screen is no longer the current screen for a Game.
     */
    @Override
    public void hide() {
        this.dispose();
    }

    //NOTEME javadoc
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
