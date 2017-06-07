package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.TSSGamePad;
import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.EnemyModel;
import com.drfl.twinstickshooter.model.entities.EntityModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.drfl.twinstickshooter.view.entities.EnemyView;
import com.drfl.twinstickshooter.view.entities.EntityView;
import com.drfl.twinstickshooter.view.entities.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class TSSView extends ScreenAdapter {

    /**
     * Used to debug the position of the physics fixtures
     */
    private static final boolean DEBUG_PHYSICS = true;

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
     * The singleton instance of the game view
     */
    private static TSSView instance;

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

    String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "uniform mat4 u_projTrans;\n" //
            + "varying vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "\n" //
            + "void main()\n" //
            + "{\n" //
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "}\n";
    String fragmentShader = "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" //
            + "varying LOWP vec4 v_color;\n" //
            + "const vec4 c_color = vec4(1.0, 0.5, 0.5, 1.0);\n" //
            + "varying vec2 v_texCoords;\n" //
            + "uniform sampler2D u_texture;\n" //
            + "void main()\n"//
            + "{\n" //
            + "  gl_FragColor = c_color * texture2D(u_texture, v_texCoords).a;\n" //
            + "}";

    private ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);

    /**
     * Array of enemy views to keep track of independent animation cycles
     */
    private ArrayList<EnemyView> enemies = new ArrayList<EnemyView>();

    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private Label score;

    private final Viewport viewport;

    /**
     * Create game view using libGDX screen
     * @param game game this screen belongs to
     */
    public TSSView(TSSGame game) {

        this.game = game;

        loadAssets();

        startMusic();

        TiledMap map = game.getAssetManager().get("Badlands.tmx");

        TSSModel.initInstance();
        TSSModel.getInstance().createEntityModels(map.getLayers().get("Entities"));

        TSSController.initInstance(this.game);
        TSSController.getInstance().createTileEntities(map.getLayers().get("Collision"));

        camera = createCamera();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    public static void setInstance(TSSView instance) {
        TSSView.instance = instance;
    }

    public static TSSView getInstance() {
        return instance;
    }

    private void startMusic() {

        ((Music)game.getAssetManager().get("GameIntro.ogg")).setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                ((Music)game.getAssetManager().get("Game.ogg")).setLooping(true);
                ((Music)game.getAssetManager().get("Game.ogg")).setVolume(game.getMusicVolume());
                ((Music)game.getAssetManager().get("Game.ogg")).play();
            }
        });

        ((Music)game.getAssetManager().get("GameIntro.ogg")).setVolume(game.getMusicVolume());
        ((Music)game.getAssetManager().get("GameIntro.ogg")).play();
    }

    @Override
    public void show() {

        game.setStage(new Stage(viewport));

        Gdx.input.setInputProcessor(game.getStage());

        score = new Label("Score: 0", skin);

        game.getStage().addActor(score);
    }

    /**
     * Creates the camera used to show the viewport.
     *
     * @return the camera
     */
    private OrthographicCamera createCamera() {

        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_HEIGHT / PIXEL_TO_METER);
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
    private void loadAssets() {

        this.game.getAssetManager().load( "Pistolero.png" , Texture.class);
        this.game.getAssetManager().load( "Rogue.png" , Texture.class);
        this.game.getAssetManager().load("Heart.png", Texture.class);
        this.game.getAssetManager().load("Bullet.png", Texture.class); //NOTEME add more bullet types if adding more weapons

        this.game.getAssetManager().load("GameIntro.ogg", Music.class);
        this.game.getAssetManager().load("Game.ogg", Music.class);

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
        TSSController.getInstance().removeDead();

        handleInputs();

        TSSController.getInstance().update(delta);

        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().setShader(null);

        drawTileMap(game.getBatch());

        game.getBatch().begin();
        drawHUD();
        drawEntities();
        game.getBatch().end();

        game.getStage().draw();

        if (DEBUG_PHYSICS) {
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
            debugRenderer.render(TSSController.getInstance().getWorld(), debugCamera);
        }
    }

    private void drawHUD() {

        Texture health = game.getAssetManager().get("Heart.png");
        int width = Math.round(health.getWidth() * TSSModel.getInstance().getMainChar().getHitpoints() / EntityModel.getHpMax());

        TextureRegion healthRegion = new TextureRegion(health, 0, 0, width, health.getHeight());

        score.setPosition(10.0f, Gdx.graphics.getHeight() - healthRegion.getRegionHeight() - score.getHeight());
        score.setText("Score: " + TSSModel.getInstance().getScore());

        game.getBatch().draw(healthRegion, 10.0f, Gdx.graphics.getHeight() - healthRegion.getRegionHeight() * 1.1f);
    }

    /**
     * Handles any inputs and passes them to the controller.
     */
    private void handleInputs() {

        if(game.getInputMode() == TSSGame.ControlType.CONTROLLER) {

            TSSController.getInstance().setMoveInput(TSSGamePad.getInstance().getLeftStickVector());
            TSSController.getInstance().setShootInput(TSSGamePad.getInstance().getRightStickVector());
        } else if(game.getInputMode() == TSSGame.ControlType.REMOTE) {

            TSSController.getInstance().setMoveInput(game.getServer().getMovement());
            TSSController.getInstance().setShootInput(game.getServer().getShooting());
        } else if(game.getInputMode() == TSSGame.ControlType.KBM) {

            Vector2 moveDirection = new Vector2(0, 0);

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                moveDirection.y = 1.0f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                moveDirection.x = -1.0f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                moveDirection.y = -1.0f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                moveDirection.x = 1.0f;
            }

            TSSController.getInstance().setMoveInput(moveDirection);
            TSSController.getInstance().setShootInput(new Vector2(Gdx.input.getX() * PIXEL_TO_METER - TSSModel.getInstance().getMainChar().getX(),
                    (VIEWPORT_HEIGHT - Gdx.input.getY() * PIXEL_TO_METER) - TSSModel.getInstance().getMainChar().getY())); //Flip Y on mouse
        }
    }

    /**
     * Draws the entities to the screen.
     */
    private void drawEntities() {

        game.getBatch().setShader(null);

        List<BulletModel> bullets = TSSModel.getInstance().getBullets();
        for(BulletModel bullet : bullets) {
            EntityView view = ViewFactory.makeView(game, bullet);
            view.update(bullet);
            view.draw(game.getBatch());
        }

        ArrayList<EnemyModel> enemies = TSSModel.getInstance().getEnemies();

        for(int i = 0; i < enemies.size(); i++) {
            if(enemies.get(i).isHurt()) { game.getBatch().setShader(shader);} else game.getBatch().setShader(null);
            this.enemies.get(i).update(enemies.get(i));
            this.enemies.get(i).draw(game.getBatch());
        }

        MainCharModel mc = TSSModel.getInstance().getMainChar();

        if(mc.isHurt()) { game.getBatch().setShader(shader);} else game.getBatch().setShader(null);
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

    public void addEnemyView() {
        enemies.add(new EnemyView(game));
    }

    public void removeEnemyView(int index) {
        enemies.remove(index);
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {

        this.game.getStage().dispose();
        this.skin.dispose();
        this.shader.dispose();
        game.getAssetManager().unload("Game.ogg");
        game.getAssetManager().unload("GameIntro.ogg");
    }

    public TSSGame getGame() {
        return game;
    }
}