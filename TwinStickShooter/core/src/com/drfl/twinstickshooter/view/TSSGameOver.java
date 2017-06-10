package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.game.TSSState;
import com.drfl.twinstickshooter.model.TSSModel;

/**
 * Game over screen, displays for a set amount of time and changes to highscore or main menu screen
 * depending on whether the player achieved an high score.
 */
public class TSSGameOver extends ScreenAdapter {

    //NOTEME javadoc
    /**
     * Time to spend on game over screen.
     */
    private static final float GAMEOVER_COOLDOWN = 2.5f;

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
     * Total time spent on game over screen.
     */
    private float totalTime = 0;

    //NOTEME javadoc
    /**
     * Constructs a game over screen belonging to a certain game.
     *
     * @param game The game this screen belongs to
     */
    public TSSGameOver(TSSGame game) {

        this.game = game;

        TSSMenuHelper.initInstance(game);

        loadAssets();
        TSSMenuHelper.getInstance().playMusic("GameOver.ogg", game.getMusicVolume(), false);

        camera = TSSMenuHelper.getInstance().createCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    //NOTEME javadoc
    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load( "GameOver.png" , Texture.class);
        TSSMenuHelper.getInstance().loadMusic("GameOver.ogg");
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

        totalTime += delta;

        if(totalTime >= GAMEOVER_COOLDOWN) {
            if(game.checkHighScore(TSSModel.getInstance().getScore())) {
                game.getStateM().processState(TSSState.GameEvent.HIGHSCORE);
            } else game.getStateM().processState(TSSState.GameEvent.MAIN);

            return;
        }

        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        drawBackground();
    }

    //NOTEME javadoc
    /**
     * Draws the game over screen background, calls batch begin and end.
     */
    private void drawBackground() {

        game.getBatch().begin();
        game.getBatch().draw((Texture) game.getAssetManager().get("GameOver.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        game.getAssetManager().unload("GameOver.png");
        game.getAssetManager().unload("GameOver.ogg");
    }
}