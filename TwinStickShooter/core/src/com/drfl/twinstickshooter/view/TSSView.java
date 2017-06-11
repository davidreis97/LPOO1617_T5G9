package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.gamepad.TSSGamePad;
import com.drfl.twinstickshooter.game.TSSState;
import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.EnemyModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.drfl.twinstickshooter.view.entities.EnemyView;
import com.drfl.twinstickshooter.view.entities.EntityView;
import com.drfl.twinstickshooter.view.entities.ViewFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MVC View, handles displaying the graphics associated with the Model entities.
 */
public class TSSView extends ScreenAdapter {

    //NOTEME javadoc
    /**
     * Used to debug the position of the physics fixtures.
     */
    private static final boolean DEBUG_PHYSICS = true;

    //NOTEME javadoc
    /**
     * Tileset size. (must be square tiles)
     */
    public static final int TILESIZE = 32; //32x32

    //NOTEME javadoc
    /**
     * Every tile is a meter.
     */
    public static final float PIXEL_TO_METER = 1.0f / TILESIZE;

    //NOTEME javadoc
    /**
     * The width of the viewport in meters which equals the number of tiles.
     */
    static final float VIEWPORT_WIDTH = 40;

    //NOTEME javadoc
    /**
     * The height of the viewport in meters which equals the number of tiles.
     */
    static final float VIEWPORT_HEIGHT = 22.5f;

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
     * A renderer used to debug the physical fixtures.
     */
    private Box2DDebugRenderer debugRenderer;

    //NOTEME javadoc
    /**
     * The transformation matrix used to transform meters into
     * pixels in order to show fixtures in their correct places.
     */
    private Matrix4 debugCamera;

    //NOTEME javadoc
    /**
     * Vertex shader used for red tint effect.
     */
    private String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
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

    //NOTEME javadoc
    /**
     * Fragment shader used for red tint effect.
     */
    private String fragmentShader = "#ifdef GL_ES\n" //
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

    //NOTEME javadoc
    /**
     * Constructs a new ShaderProgram and immediately compiles it.
     */
    private ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);

    //NOTEME javadoc
    /**
     * Array of enemy views to keep track of independent animation cycles.
     */
    private ArrayList<EnemyView> enemies = new ArrayList<>();

    //NOTEME javadoc
    /**
     * Skin used for Scene2D stage actors.
     */
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    //NOTEME javadoc
    /**
     * Texture used to represent main character health.
     */
    private Texture health;

    //NOTEME javadoc
    /**
     * Label for representing current score.
     */
    private Label score;

    //NOTEME javadoc
    /**
     * Creates a new View that displays the current state of the Model.
     *
     * @param game The game associated with this view
     */
    public TSSView(TSSGame game) {

        this.game = game;

        ViewFactory.initViewFactory();

        loadAssets();

        setupMusic();

        TSSModel.initInstance();
        TSSModel.getInstance().createEntityModels(game.getCurrentMap().getLayers().get("Entities"));

        TSSController.initInstance(this.game);
        TSSController.getInstance().createTileEntities(game.getCurrentMap().getLayers().get("Collision"));

        camera = createCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
    }

    //NOTEME javadoc
    /**
     * Setups the music to be played during the game by playing the intro and registering a
     * listener so it plays the main theme after playing the intro.
     */
    private void setupMusic() {

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

    //NOTEME javadoc
    /**
     * Called when this screen becomes the current screen for a game. Creates all the actors for a Scene2D stage
     * representing the HUD.
     */
    @Override
    public void show() {

        game.setStage(new Stage(viewport));
        Gdx.input.setInputProcessor(game.getStage());

        score = new Label("Score: 0", skin);
        game.getStage().addActor(score);
    }

    //NOTEME javadoc
    /**
     * Creates an orthographic camera for displaying the screen.
     *
     * @return The orthographic camera
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

    //NOTEME javadoc
    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load( "Pistolero.png" , Texture.class);
        this.game.getAssetManager().load( "Rogue.png" , Texture.class);
        this.game.getAssetManager().load("Heart.png", Texture.class);
        this.game.getAssetManager().load("Bullet.png", Texture.class);

        this.game.getAssetManager().load("GameIntro.ogg", Music.class);
        this.game.getAssetManager().load("Game.ogg", Music.class);

        this.game.getAssetManager().finishLoading();
    }

    //NOTEME javadoc
    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {

        if(TSSModel.getInstance().getMainChar().isDead()) {
            game.getStateM().processState(TSSState.GameEvent.MC_DIED);
            return;
        }

        TSSController.getInstance().removeFlaggedBullets();
        this.cleanEnemyView(TSSController.getInstance().removeDead());

        handleInputs();

        TSSController.getInstance().update(delta);

        if(TSSController.getInstance().isSpawned()) addEnemyView();

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

    //NOTEME javadoc
    /**
     * Draws the game HUD, displays score and main character HP.
     */
    private void drawHUD() {

        health = game.getAssetManager().get("Heart.png");
        int width = Math.round(health.getWidth() * TSSModel.getInstance().getMainChar().getHitpoints() / TSSModel.getInstance().getMainChar().getHPMax());

        TextureRegion healthRegion = new TextureRegion(health, 0, 0, width, health.getHeight());

        score.setPosition(10.0f, Gdx.graphics.getHeight() - healthRegion.getRegionHeight() - score.getHeight());
        score.setText("Score: " + TSSModel.getInstance().getScore());

        game.getBatch().draw(healthRegion, 10.0f, Gdx.graphics.getHeight() - healthRegion.getRegionHeight() * 1.1f);
    }

    //NOTEME javadoc
    /**
     * Handles inputs and changes main character model so Controller can use the changes.
     */
    private void handleInputs() {

        if(game.getInputMode() == TSSGame.ControlType.CONTROLLER) {

            TSSModel.getInstance().getMainChar().setMoveDirection(TSSGamePad.getInstance().getLeftStickVector());
            TSSModel.getInstance().getMainChar().setShootDirection(TSSGamePad.getInstance().getRightStickVector());
        } else if(game.getInputMode() == TSSGame.ControlType.REMOTE) {

            TSSModel.getInstance().getMainChar().setMoveDirection(game.getServer().getMovement());
            TSSModel.getInstance().getMainChar().setShootDirection(game.getServer().getShooting());
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

            TSSModel.getInstance().getMainChar().setMoveDirection(moveDirection);
            TSSModel.getInstance().getMainChar().setShootDirection(new Vector2(Gdx.input.getX() * PIXEL_TO_METER - TSSModel.getInstance().getMainChar().getPosition().x,
                    (VIEWPORT_HEIGHT - Gdx.input.getY() * PIXEL_TO_METER) - TSSModel.getInstance().getMainChar().getPosition().y)); //Flip Y on mouse
        }
    }

    //NOTEME javadoc
    /**
     * Draws the entities to the screen, bullets, enemies and main character.
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

    //NOTEME javadoc
    /**
     * Draws current Tiled map to a certain sprite batch.
     *
     * @param batch The sprite batch to use
     */
    private void drawTileMap(SpriteBatch batch) {

        OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(game.getCurrentMap(), batch);
        renderer.setView(camera);
        renderer.render();
    }

    //NOTEME javadoc
    /**
     * Adds a new default enemy view.
     */
    private void addEnemyView() {
        enemies.add(new EnemyView(game));
    }

    //NOTEME javadoc
    /**
     * Removes all enemies that were flagged as dead by the Controller.
     *
     * @param deadIndex The indexes of the flagged enemies
     */
    private void cleanEnemyView(Array<Integer> deadIndex) {

        for(Integer index : deadIndex) {
            enemies.remove(index);
        }
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

        this.game.getStage().dispose();
        this.skin.dispose();
        this.shader.dispose();
        health.dispose();
        game.getAssetManager().unload("Game.ogg");
        game.getAssetManager().unload("GameIntro.ogg");
        game.getAssetManager().unload("Pistolero.png");
        game.getAssetManager().unload("Rogue.png");
        game.getAssetManager().unload("Heart.png");
        game.getAssetManager().unload("Bullet.png");
    }
}