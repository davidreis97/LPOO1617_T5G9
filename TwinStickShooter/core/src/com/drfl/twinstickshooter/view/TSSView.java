package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSGamePad;
import com.drfl.twinstickshooter.TSSServer;
import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.drfl.twinstickshooter.view.entities.EntityView;
import com.drfl.twinstickshooter.view.entities.ViewFactory;
import com.esotericsoftware.minlog.Log;

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

        this.game.getAssetManager().load( "TilesetTest.png" , Texture.class);
        this.game.getAssetManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.game.getAssetManager().load("test.tmx", TiledMap.class);

        this.game.getAssetManager().finishLoading();
    }

    /**
     * Renders this screen.
     *
     * @param delta time since last renders in seconds.
     */
    @Override
    public void render(float delta) {
//        GameController.getInstance().removeFlagged();
//        GameController.getInstance().createNewAsteroids();
//
//        TSSController.getInstance().setMovement(0, 0);
        handleInputs(delta);

        TSSController.getInstance().update(delta);

//        camera.position.set(GameModel.getInstance().getShip().getX() / PIXEL_TO_METER, GameModel.getInstance().getShip().getY() / PIXEL_TO_METER, 0);
//        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().begin();
        drawTileMap();
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

        if(inputMode.equals("controller")){
            TSSController.getInstance().setMovement(TSSGamePad.getInstance().getLeftStickVector());
        }else if(inputMode.equals("server")){
            TSSController.getInstance().setMovement(server.getMovement());
        }



        boolean keyPress = false;

//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            TSSController.getInstance().setMovement(-1, 0);
//            keyPress = true;
////            TSSController.getInstance().setRotation((float) Math.PI / 2.0f);
////            TSSController.getInstance().accelerate(delta);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            TSSController.getInstance().setMovement(1, 0);
//            keyPress = true;
////            TSSController.getInstance().setRotation((float) -Math.PI / 2.0f);
////            TSSController.getInstance().accelerate(delta);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            TSSController.getInstance().setMovement(0, 1);
//            keyPress = true;
////            TSSController.getInstance().setRotation(0);
////            TSSController.getInstance().accelerate(delta);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            TSSController.getInstance().setMovement(0, -1);
//            keyPress = true;
////            TSSController.getInstance().setRotation((float) Math.PI);
////            TSSController.getInstance().accelerate(delta);
//        }
//
//        if(!keyPress) {
//            TSSController.getInstance().setMovement(0, 0);
//        }
    }

    /**
     * Draws the entities to the screen.
     */
    private void drawEntities() {

        MainCharModel mc = TSSModel.getInstance().getMainChar();
        EntityView view = ViewFactory.makeView(game, mc);
        view.update(mc);
        view.draw(game.getBatch());
    }

    /**
     * Draws the Tile Map.
     */
    private void drawTileMap() {
        TiledMap map = game.getAssetManager().get("test.tmx");
//        for(TiledMapTileSet tiles: map.getTileSets()) {
//            tiles.
//        }
        Texture tileMap = game.getAssetManager().get("TilesetTest.png", Texture.class);
        tileMap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        //game.getBatch().draw(background, 0, 0, 0, 0, (int)(ARENA_WIDTH / PIXEL_TO_METER), (int) (ARENA_HEIGHT / PIXEL_TO_METER));
        for(int i = 0; i < 21; i++) {
            for(int j = 0; j < VIEWPORT_WIDTH; j++) {
                game.getBatch().draw(tileMap, j * TILESIZE, i * TILESIZE, 0, 1 * TILESIZE, TILESIZE, TILESIZE);
            }
        }
    }
}