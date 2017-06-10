package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.game.TSSState;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.VIEWPORT_HEIGHT;
import static com.drfl.twinstickshooter.view.TSSView.VIEWPORT_WIDTH;

public class TSSMapSelect extends ScreenAdapter {

    //NOTEME javadoc
    /**
     * Height constant for image buttons.
     */
    private static final float BUTTON_HEIGHT = 0.25f * Gdx.graphics.getHeight();

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
     * Increment constant between each image button. Jumps from the center of an
     * image button to the next one with a spacing of (0.80f * Gdx.graphics.getWidth() - WIDGET_WIDTH * 3) / 2.0f.
     */
    private static final float INCREMENT = WIDGET_WIDTH + (0.80f * Gdx.graphics.getWidth() - WIDGET_WIDTH * 3) / 2.0f;

    //NOTEME javadoc
    /**
     * Center width of the first image button. Pads 10% of the screen to the left.
     */
    private static final float START_WIDTH = WIDGET_WIDTH / 2.0f + 0.10f * Gdx.graphics.getWidth();

    //NOTEME javadoc
    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    //NOTEME javadoc
    /**
     * The camera used to show the viewport.
     */
    private OrthographicCamera camera;

    //NOTEME javadoc
    /**
     * The viewport for the Scene2D stage.
     */
    private Viewport viewport;

    //NOTEME javadoc
    /**
     * Tiled maps to use for creating thumbnails.
     */
    private Array<TiledMap> tiledMaps = new Array<>();

    //NOTEME javadoc
    /**
     * Array to fill with textures created from the snapshots of Tiled maps.
     */
    private Array<Texture> mapTextures = new Array<>();

    //NOTEME javadoc
    /**
     * Thumbnails to use for the Scene2D image buttons for map selection.
     */
    private Array<Skin> thumbnails = new Array<>();

    //NOTEME javadoc
    /**
     * Names of the maps on the map select screen.
     */
    private String[] mapNames = new String[] {
            "Badlands",
            "Grasslands",
            "Custom",
    };

    //NOTEME javadoc
    /**
     * Are snapshots being created?
     */
    private boolean isTakingSnapshots = true;

    //NOTEME javadoc
    /**
     * Is Scene2D stage created?
     */
    private boolean isStageCreated = false;

    //NOTEME javadoc
    /**
     * Constructs a map select screen belonging to a certain game.
     *
     * @param game The game this screen belongs to
     */
    public TSSMapSelect(TSSGame game) {

        this.game = game;

        TSSMenuHelper.initInstance(game);

        loadAssets();
        TSSMenuHelper.getInstance().playMusic("Menu.ogg", game.getMusicVolume(), true);

        camera = TSSMenuHelper.getInstance().createCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_HEIGHT / PIXEL_TO_METER);
        viewport = TSSMenuHelper.getInstance().createFitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    //NOTEME javadoc
    /**
     * Called when this screen becomes the current screen for a game. Adds Tiled maps to class array.
     */
    @Override
    public void show() {

        tiledMaps.add((TiledMap) game.getAssetManager().get("Badlands.tmx"));
        tiledMaps.add((TiledMap) game.getAssetManager().get("Grasslands.tmx"));
        tiledMaps.add((TiledMap) game.getAssetManager().get("Custom.tmx"));
    }

    //NOTEME javadoc
    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load( "MainMenuBack.jpg" , Texture.class);
        TSSMenuHelper.getInstance().loadMusic("Menu.ogg");

        this.game.getAssetManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.game.getAssetManager().load("Badlands.tmx", TiledMap.class);
        this.game.getAssetManager().load("Grasslands.tmx", TiledMap.class);
        this.game.getAssetManager().load("Custom.tmx", TiledMap.class);

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

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        if(isTakingSnapshots) {

            createThumbnails();
            isTakingSnapshots = false;
            drawBackground();
        } else if(!isStageCreated) {

            camera = TSSMenuHelper.getInstance().createCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            viewport = TSSMenuHelper.getInstance().createFitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

            createStage();
            isStageCreated = true;
            drawBackground();
        } else {

            drawBackground();

            game.getStage().act(delta);
            game.getStage().draw();
        }
    }

    //NOTEME javadoc
    /**
     * Draws Tiled maps and saves a snapshot of the render to a texture which is then used
     * to create a skin for future usage.
     */
    private void createThumbnails() {

        for(TiledMap map : tiledMaps) {
            drawTileMap(game.getBatch(), map);
            mapTextures.add(takeSnapshot());
        }

        for(int i = 0; i < mapTextures.size; i++) {

            thumbnails.add(new Skin());
            thumbnails.get(i).add("default", mapTextures.get(i));
        }
    }

    //NOTEME javadoc
    /**
     * Creates all the actors for a Scene2D stage representing the map select screen.
     */
    private void createStage() {

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        Vector2 currCoords = new Vector2(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight());
        Vector2 currSize = new Vector2(WIDGET_WIDTH, LABEL_HEIGHT);

        currCoords.y -= currSize.y;
        TSSMenuHelper.getInstance().createLabel("Map Select", currSize, currCoords, Color.DARK_GRAY, Align.center);

        for(int i = 0; i < thumbnails.size; i++) {

            final int index = i;

            currSize.y = BUTTON_HEIGHT;
            currCoords.set(START_WIDTH + INCREMENT * i, Gdx.graphics.getHeight() / 2.0f);
            TSSMenuHelper.getInstance().createImageButton(thumbnails.get(i).getDrawable("default"), currSize, currCoords, new ClickListener() {
                @Override
                public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                    game.setCurrentMap(tiledMaps.get(index));
                    game.getStateM().processState(TSSState.GameEvent.CHOOSE);
                    return false;
                }
            });

            currCoords.y += currSize.y / 2.0f;
            currSize.y = LABEL_HEIGHT;
            currCoords.y += currSize.y / 2.0f;
            TSSMenuHelper.getInstance().createLabel(mapNames[i], currSize, currCoords, Color.WHITE, Align.center);
        }
    }

    //NOTEME javadoc
    /**
     * Saves current frame buffer to a texture (snapshot).
     *
     * @return The texture created from the frame buffer
     */
    private Texture takeSnapshot() {

        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        return new Texture(pixmap);
    }

    //NOTEME javadoc
    /**
     * Draws a Tiled map to a certain sprite batch.
     *
     * @param batch The sprite batch to use
     * @param map The Tiled map to use
     */
    private void drawTileMap(SpriteBatch batch, TiledMap map) {

        OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, batch);
        renderer.setView(camera);
        renderer.render();
    }

    //NOTEME javadoc
    /**
     * Draws the map select screen background, calls batch begin and end.
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
        for(TiledMap tiled : tiledMaps) {
            tiled.dispose();
        }
        for(Skin skin : thumbnails) {
            skin.dispose();
        }
    }
}
