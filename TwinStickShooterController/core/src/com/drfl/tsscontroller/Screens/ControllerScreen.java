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


public class ControllerScreen implements Screen{

    private Touchpad touchpadLeft;
    private Touchpad touchpadRight;
    private Skin touchpadSkin;
    private Stage stage;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Touchpad.TouchpadStyle touchpadStyle;
    private TSSCClient client;
    private Viewport viewport;
    private Camera camera;
    private TSSCGame game;

    public ControllerScreen(TSSCGame game){
        this.game = game;
        loadAssets();
    }

    private void loadAssets() {

        this.game.getAssetManager().load("Background.jpg", Texture.class);
        this.game.getAssetManager().finishLoading();
    }

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

    private void loadTouchpadLeft() {

        touchpadLeft = new Touchpad(10, touchpadStyle);

        touchpadLeft.setBounds(300, 100, 500, 500);

        touchpadLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ControllerInfoPacket packet = new ControllerInfoPacket();
                packet.acceleration = new Vector2(touchpadLeft.getKnobPercentX(),touchpadLeft.getKnobPercentY());
                packet.bullet = new Vector2(touchpadRight.getKnobPercentX(),touchpadRight.getKnobPercentY());
                client.client.sendTCP(packet);
                Log.info("Sent Packet from Left Touchpad");
            }
        });
    }

    private void loadTouchpadRight() {

        touchpadRight = new Touchpad(10, touchpadStyle);

        touchpadRight.setBounds( 1000, 100, 500, 500);

        touchpadRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ControllerInfoPacket packet = new ControllerInfoPacket();
                packet.acceleration = new Vector2(touchpadLeft.getKnobPercentX(),touchpadLeft.getKnobPercentY());
                packet.bullet = new Vector2(touchpadRight.getKnobPercentX(),touchpadRight.getKnobPercentY());
                client.client.sendTCP(packet);
                Log.info("Sent Packet from Right Touchpad");
            }
        });
    }

    private void loadTouchpadStyle() {

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));

        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");

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

    @Override
    public void dispose() {
        touchpadSkin.dispose();
        stage.dispose();
    }
}
