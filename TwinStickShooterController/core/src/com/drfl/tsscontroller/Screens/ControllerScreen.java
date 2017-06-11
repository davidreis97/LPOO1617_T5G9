package com.drfl.tsscontroller.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.tsscontroller.Network.Packet.ControllerInfoPacket;
import com.drfl.tsscontroller.Network.TSSCClient;
import com.drfl.tsscontroller.TSSCGame;
import com.esotericsoftware.minlog.Log;

/**
 * ControllerScreen, handles movement and shooting input to send to game server.
 */
public class ControllerScreen implements Screen {

    /**
     * Debug flag.
     */
    private static final boolean DEBUG = false;

    /**
     * The camera used to show the viewport.
     */
    private Camera camera;

    /**
     * The viewport for the Scene2D stage.
     */
    private Viewport viewport;

    /**
     * The game this screen belongs to.
     */
    private TSSCGame game;

    /**
     * Kryonet client.
     */
    private TSSCClient client;

    /**
     * Left touchpad for movement input.
     */
    private Touchpad touchpadLeft;

    /**
     * Right touchpad for shooting input.
     */
    private Touchpad touchpadRight;

    /**
     * Skin used for touch pads.
     */
    private Skin touchpadSkin;

    /**
     * Scene2D stage used for UI.
     */
    private Stage stage;

    /**
     * Touchpad style.
     */
    private Touchpad.TouchpadStyle touchpadStyle;

    /**
     * Constructs a ControllerScreen belonging to a certain game.
     *
     * @param game The game this screen belongs to
     */
    ControllerScreen(TSSCGame game) {

        this.game = game;
        loadAssets();
    }

    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load("Background.jpg", Texture.class);
        this.game.getAssetManager().finishLoading();
    }

    /**
     * Called when this screen becomes the current screen for a game. Creates all the actors for a Scene2D stage
     * representing the ControllerScreen.
     */
    @Override
    public void show() {

        loadTouchpadStyle();

        loadTouchpadRight();

        loadTouchpadLeft();

        client = this.game.getClient();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(1920, 1080, camera);

        stage = new Stage(viewport);

        stage.addActor(touchpadLeft);
        stage.addActor(touchpadRight);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Initializes the left touch pad.
     */
    private void loadTouchpadLeft() {

        touchpadLeft = new Touchpad(10, touchpadStyle);

        touchpadLeft.setBounds(300, 100, 500, 500);

        touchpadLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ControllerInfoPacket packet = new ControllerInfoPacket();
                packet.movement = new Vector2(touchpadLeft.getKnobPercentX(),touchpadLeft.getKnobPercentY());
                packet.shooting = new Vector2(touchpadRight.getKnobPercentX(),touchpadRight.getKnobPercentY());
                client.client.sendTCP(packet);
                if(DEBUG) Log.info("Sent Packet from Left Touchpad");
            }
        });
    }

    /**
     * Initializes the right touch pad.
     */
    private void loadTouchpadRight() {

        touchpadRight = new Touchpad(10, touchpadStyle);

        touchpadRight.setBounds( 1000, 100, 500, 500);

        touchpadRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ControllerInfoPacket packet = new ControllerInfoPacket();
                packet.movement = new Vector2(touchpadLeft.getKnobPercentX(),touchpadLeft.getKnobPercentY());
                packet.shooting = new Vector2(touchpadRight.getKnobPercentX(),touchpadRight.getKnobPercentY());
                client.client.sendTCP(packet);
                if(DEBUG) Log.info("Sent Packet from Right Touchpad");
            }
        });
    }

    /**
     * Creates the touch pad style to use as a skin.
     */
    private void loadTouchpadStyle() {

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));

        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");

        touchKnob.setMinHeight((float) (touchBackground.getMinHeight() * 1.2));
        touchKnob.setMinWidth((float) (touchBackground.getMinWidth() * 1.2));

        touchpadStyle = new Touchpad.TouchpadStyle();

        Pixmap background = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
        background.setBlending(Pixmap.Blending.None);
        background.setColor(1, 1, 1, .6f);
        background.fillCircle(100, 100, 100);
        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(background)));

        touchpadStyle.knob = touchKnob;
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.getBatch().begin();
        Texture back = this.game.getAssetManager().get("Background.jpg");
        this.game.getBatch().draw(back, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game.getBatch().end();

        stage.act();
        stage.draw();
    }

    /**
     * Called when screen is resized.
     *
     * @param width The new width
     * @param height The new height
     */
    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        touchpadSkin.dispose();
        stage.dispose();
    }
}
