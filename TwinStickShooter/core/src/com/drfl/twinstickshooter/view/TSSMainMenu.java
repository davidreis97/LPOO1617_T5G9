package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSState;

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

//    /**
//     * The singleton instance of the game view
//     */
//    private static TSSMainMenu instance;

    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    private final Viewport viewport;

    private TextButton startGame;

    Skin skin = new Skin(Gdx.files.internal("uiskin.json")); //TODO can use assetManager?

    public TSSMainMenu(TSSGame game) {

        this.game = game;

        loadAssets();

        camera = createCamera();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());
    }

    @Override
    public void show() {

        //TODO move to function
        startGame = new TextButton("Start Game", skin);
        startGame.setSize(0.25f * Gdx.graphics.getWidth(),0.15f * Gdx.graphics.getHeight());
        startGame.setPosition(Gdx.graphics.getWidth() / 2.0f - startGame.getWidth() / 2.0f,Gdx.graphics.getHeight() - startGame.getHeight() - 0.10f * Gdx.graphics.getHeight());
//        startGame.getLabel().setFontScale(4);

        //TODO optional depending on input method?
        startGame.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.getStateM().processState(TSSState.GameEvent.START);
                return false;
            }
        });

        game.getStage().addActor(startGame);
    }

    private OrthographicCamera createCamera() {

        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_HEIGHT / PIXEL_TO_METER);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        return camera;
    }

    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
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

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        game.getBatch().begin();
        drawElements();
        game.getBatch().end();

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
        skin.dispose();
    }
}
