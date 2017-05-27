package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSGamePad;
import com.drfl.twinstickshooter.TSSServer;
import com.drfl.twinstickshooter.XBox360Pad;
import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.drfl.twinstickshooter.view.entities.EntityView;
import com.drfl.twinstickshooter.view.entities.ViewFactory;
import com.esotericsoftware.minlog.Log;

import java.util.Iterator;
import java.util.List;

public class TSSView extends ScreenAdapter {

    /**
     * Used to debug the position of the physics fixtures
     */
    private static final boolean DEBUG_PHYSICS = true;

    /**
     * Tileset size. (must be square tiles)
     */
    public static final int TILESIZE = 32; //32x32 //TODO import from TSSModel.java?

    /**
     * Every tile is a meter.
     */
    public static final float PIXEL_TO_METER = 1.0f / TILESIZE; //TODO check this value

    /**
     * The width of the viewport in meters (equivalent to number of tiles). The height is
     * automatically calculated using the screen ratio.
     */
    private static final float VIEWPORT_WIDTH = 40; //TODO check this value

    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    /**
     * The server this screen uses.
     */
    private final TSSServer server;

    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    /**
     * A renderer used to debug the physical fixtures.
     */
    private Box2DDebugRenderer debugRenderer;

    /**
     * The transformation matrix used to transform meters into
     * pixels in order to show fixtures in their correct places.
     */
    private Matrix4 debugCamera;

    /**
     * The type of input currently in use to control the game.
     */
    private String inputMode;

    /**
     * Create game view using libGDX screen
     * @param game game this screen belongs to
     */
    public TSSView(TSSGame game, TSSServer server) {

        this.game = game;
        this.server = server;

        loadAssets();

        TiledMap map = game.getAssetManager().get("Badlands.tmx");
        TSSModel.getInstance().createEntityModels(map.getLayers().get("Entities"));
        TSSController.getInstance().createTileEntities(map.getLayers().get("Collision"));

        inputMode = chooseInput();

        camera = createCamera();
    }

    /**
     * Decides which input mode will be used.
     * If a controller is connected, the controller is used to control the game. Otherwise, the server is used.
     * @return The type of input currently in use to control the game.
     */
    private String chooseInput() {
        if(TSSGamePad.getInstance().controllerExists()){
            Log.info("Controller found, using it as input");
            return "controller";
        }else{
            Log.info("Controller NOT found, using SERVER as input");
            return "server";
        }
    }


    /**
     * Creates the camera used to show the viewport.
     *
     * @return the camera
     */
    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_WIDTH / PIXEL_TO_METER * ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        if (DEBUG_PHYSICS) {
            debugRenderer = new Box2DDebugRenderer();
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
        }

        return camera;
    }

    /**
     * Loads the assets needed by this screen.
     */
    private void loadAssets() { //TODO can show progress bar

        this.game.getAssetManager().load( "Engineer.png" , Texture.class); //TODO replace with main char spritemap
        this.game.getAssetManager().load("Bullet.png", Texture.class); //TODO add more bullet types if adding more weapons

        //Tile map loading
        this.game.getAssetManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.game.getAssetManager().load("Badlands.tmx", TiledMap.class);

        this.game.getAssetManager().finishLoading();
    }

    /**
     * Renders this screen.
     *
     * @param delta time since last renders in seconds.
     */
    @Override
    public void render(float delta) {

        TSSController.getInstance().removeFlagged();
//        GameController.getInstance().createNewAsteroids();

        handleInputs(delta);

        TSSController.getInstance().update(delta);

//        camera.position.set(GameModel.getInstance().getShip().getX() / PIXEL_TO_METER, GameModel.getInstance().getShip().getY() / PIXEL_TO_METER, 0);
//        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        drawTileMap(game.getBatch());

        game.getBatch().begin();
        drawEntities();
        game.getBatch().end();

        if (DEBUG_PHYSICS) {
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
            debugRenderer.render(TSSController.getInstance().getWorld(), debugCamera);
        }
    }

    /**
     * Handles any inputs and passes them to the controller.
     *
     * @param delta time since last time inputs where handled in seconds
     */
    private void handleInputs(float delta) {

        if(inputMode.equals("controller")) {
            TSSController.getInstance().setMoveInput(TSSGamePad.getInstance().getLeftStickVector());
            TSSController.getInstance().setShootInput(TSSGamePad.getInstance().getRightStickVector());
//            if(TSSGamePad.getInstance().getButton(XBox360Pad.BUTTON_RB)) TSSController.getInstance().shoot();
        } else if(inputMode.equals("server")) {
            TSSController.getInstance().setMoveInput(server.getMovement());
            TSSController.getInstance().setShootInput(server.getShooting());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            TSSController.getInstance().shoot();
        }

        //TODO add keyboard controls
        //TODO option menu for choosing input method
    }

    /**
     * Draws the entities to the screen.
     */
    private void drawEntities() {

        List<BulletModel> bullets = TSSModel.getInstance().getBullets();
        for (BulletModel bullet : bullets) {
            EntityView view = ViewFactory.makeView(game, bullet);
            view.update(bullet);
            view.draw(game.getBatch());
        }

        MainCharModel mc = TSSModel.getInstance().getMainChar();
        EntityView view = ViewFactory.makeView(game, mc);
        view.update(mc);
        view.draw(game.getBatch());
    }

    /**
     * Draws the Tile Map.
     */
    private void drawTileMap(SpriteBatch batch) {

        TiledMap map = game.getAssetManager().get("Badlands.tmx");

        OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, batch);
        renderer.setView(camera);
        renderer.render();
    }
}