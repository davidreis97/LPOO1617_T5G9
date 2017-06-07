package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSState;

public class TSSMapSelect extends ScreenAdapter {

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
    private OrthographicCamera camera;

    private Viewport viewport;

    private Label title;

    private ImageButton firstMap;
    private Skin firstMapSkin;
    private Label firstMapText;

    private ImageButton secondMap;
    private Skin secondMapSkin;
    private Label secondMapText;

    private ImageButton thirdMap;
    private Skin thirdMapSkin;
    private Label thirdMapText;

    private Array<Texture> mapTextures = new Array<Texture>();
    private Array<TiledMap> tiledMaps = new Array<TiledMap>();

    private boolean isTakingSnapshots = true;
    private boolean isStageCreated = false;

    private int currSnapshot = 0;

    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    public TSSMapSelect(TSSGame game) {

        this.game = game;

        loadAssets();

        startMusic();

        camera = createSnapshotCamera();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    private void startMusic() {

        ((Music)game.getAssetManager().get("Menu.ogg")).setLooping(true);
        ((Music)game.getAssetManager().get("Menu.ogg")).setVolume(game.getMusicVolume());
        ((Music)game.getAssetManager().get("Menu.ogg")).play();
    }

    @Override
    public void show() {

        tiledMaps.add((TiledMap) game.getAssetManager().get("Badlands.tmx"));
        tiledMaps.add((TiledMap) game.getAssetManager().get("Grasslands.tmx"));
        tiledMaps.add((TiledMap) game.getAssetManager().get("Custom.tmx"));
    }

    private OrthographicCamera createSnapshotCamera() {

        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_HEIGHT / PIXEL_TO_METER);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        return camera;
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

        this.game.getAssetManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.game.getAssetManager().load("Badlands.tmx", TiledMap.class);
        this.game.getAssetManager().load("Grasslands.tmx", TiledMap.class);
        this.game.getAssetManager().load("Custom.tmx", TiledMap.class);

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

        if(isTakingSnapshots) {

            drawTileMap(game.getBatch(), tiledMaps.get(currSnapshot));
            mapTextures.add(takeSnapshot());
            currSnapshot++;
            if(currSnapshot > 2) isTakingSnapshots = !isTakingSnapshots;

            game.getBatch().begin();
            drawElements();
            game.getBatch().end();
        } else if(!isStageCreated) {

            camera = createCamera();
            viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

            createStage();

            game.getBatch().begin();
            drawElements();
            game.getBatch().end();
        } else {

            game.getBatch().begin();
            drawElements();
            game.getBatch().end();

            game.getStage().act(delta);
            game.getStage().draw();
        }
    }

    private void createStage() {

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        float paddingRatio = 0.10f;
        float spacingRatio = (1.00f - 0.25f * 3 - 2 * paddingRatio) / 2.0f;
        float incrementRatio = 0.125f;

        title = new Label("Map Select", skin);
        title.setFontScale(1.2f);
        title.setColor(Color.DARK_GRAY);
        title.setPosition(calcWidthRatio(title.getWidth(), 0.5f), calcHeightRatio(title.getHeight(), 0.90f));

        firstMapSkin = new Skin();
        firstMapSkin.add("default", mapTextures.get(0));

        firstMap = new ImageButton(firstMapSkin.getDrawable("default"));
        firstMap.setSize(0.25f * Gdx.graphics.getWidth(),0.25f * Gdx.graphics.getHeight());
        firstMap.setPosition(calcWidthRatio(firstMap.getWidth(), paddingRatio + incrementRatio), calcHeightCenter(firstMap.getHeight()));
        firstMap.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.setCurrentMap(tiledMaps.get(0));
                game.getStateM().processState(TSSState.GameEvent.CHOOSE);
                return false;
            }
        });

        firstMapText = new Label("Badlands", skin);
        firstMapText.setPosition(calcWidthRatio(firstMap.getWidth(), paddingRatio + incrementRatio), calcHeightCenter(-firstMap.getHeight() - firstMapText.getHeight()));

        secondMapSkin = new Skin();
        secondMapSkin.add("default", mapTextures.get(1));

        secondMap = new ImageButton(secondMapSkin.getDrawable("default"));
        secondMap.setSize(0.25f * Gdx.graphics.getWidth(),0.25f * Gdx.graphics.getHeight());
        secondMap.setPosition(calcWidthRatio(secondMap.getWidth(), paddingRatio + incrementRatio * 3 + spacingRatio), calcHeightCenter(secondMap.getHeight()));
        secondMap.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.setCurrentMap(tiledMaps.get(1));
                game.getStateM().processState(TSSState.GameEvent.CHOOSE);
                return false;
            }
        });

        secondMapText = new Label("Grasslands", skin);
        secondMapText.setPosition(calcWidthRatio(secondMap.getWidth(), paddingRatio + incrementRatio * 3 + spacingRatio), calcHeightCenter(-secondMap.getHeight() - secondMapText.getHeight()));

        thirdMapSkin = new Skin();
        thirdMapSkin.add("default", mapTextures.get(2));

        thirdMap = new ImageButton(thirdMapSkin.getDrawable("default"));
        thirdMap.setSize(0.25f * Gdx.graphics.getWidth(),0.25f * Gdx.graphics.getHeight());
        thirdMap.setPosition(calcWidthRatio(thirdMap.getWidth(), paddingRatio + incrementRatio * 5 + spacingRatio * 2), calcHeightCenter(thirdMap.getHeight()));
        thirdMap.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {

                game.setCurrentMap(tiledMaps.get(2));
                game.getStateM().processState(TSSState.GameEvent.CHOOSE);
                return false;
            }
        });

        thirdMapText = new Label("Custom", skin);
        thirdMapText.setPosition(calcWidthRatio(thirdMap.getWidth(), paddingRatio + incrementRatio * 5 + spacingRatio * 2), calcHeightCenter(-thirdMap.getHeight() - thirdMapText.getHeight()));

        game.getStage().addActor(title);
        game.getStage().addActor(firstMap);
        game.getStage().addActor(firstMapText);
        game.getStage().addActor(secondMap);
        game.getStage().addActor(secondMapText);
        game.getStage().addActor(thirdMap);
        game.getStage().addActor(thirdMapText);

        this.isStageCreated = true;
    }

    private float calcHeightCenter(float y) {
        return Gdx.graphics.getHeight() / 2.0f - y / 2.0f;
    }

    private float calcWidthRatio(float x, float ratio) {
        return Gdx.graphics.getWidth() * ratio - x / 2.0f;
    }

    private float calcHeightRatio(float y, float ratio) {
        return Gdx.graphics.getHeight() * ratio - y / 2.0f;
    }

    private Texture takeSnapshot() {

        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        return new Texture(pixmap);
    }

    /**
     * Draws the Tile Map.
     */
    private void drawTileMap(SpriteBatch batch, TiledMap map) {

        OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, batch);
        renderer.setView(camera);
        renderer.render();
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
        firstMapSkin.dispose();
        secondMapSkin.dispose();
        thirdMapSkin.dispose();
        for(TiledMap tiled : tiledMaps) {
            tiled.dispose();
        }
    }
}
