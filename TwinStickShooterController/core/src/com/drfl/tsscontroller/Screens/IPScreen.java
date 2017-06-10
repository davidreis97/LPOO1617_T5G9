package com.drfl.tsscontroller.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.tsscontroller.Network.TSSCClient;
import com.drfl.tsscontroller.TSSCGame;

/**
 * IPScreen, handles inputting an IP address to connect to a game instance.
 */
public class IPScreen implements Screen {

    //NOTEME javadoc
    /**
     * The game this screen belongs to.
     */
    private TSSCGame game;

    //NOTEME javadoc
    /**
     * Scene2D stage used for UI.
     */
    private Stage stage;

    //NOTEME javadoc
    /**
     * The camera used to show the viewport.
     */
    private Camera camera;

    //NOTEME javadoc
    /**
     * The viewport for the Scene2D stage.
     */
    private Viewport viewport;

    //NOTEME javadoc
    /**
     * Text button for accepting IP input.
     */
    private TextButton btnAccept;

    //NOTEME javadoc
    /**
     * Text field for inputting the IP adress to connect to.
     */
    private TextField ipAddressField;

    //NOTEME javadoc
    /**
     * Current connection status.
     */
    private Label status;

    //NOTEME javadoc
    /**
     * Constructs a IPScreen belonging to a certain game.
     *
     * @param game The game this screen belongs to
     */
    public IPScreen(TSSCGame game) {

        this.game = game;
        loadAssets();
    }

    //NOTEME javadoc
    /**
     * Loads assets needed for this screen.
     */
    private void loadAssets() {

        this.game.getAssetManager().load("Background.jpg", Texture.class);
        this.game.getAssetManager().finishLoading();
    }

    //NOTEME javadoc
    /**
     * Called when this screen becomes the current screen for a game. Creates all the actors for a Scene2D stage
     * representing the IPScreen.
     */
    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(1920, 1080, camera);

        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Skin textField = new Skin(Gdx.files.internal("uiskin.json"));
        textField.getFont("default-font").getData().setScale(5.00f,5.00f);

        btnAccept = new TextButton("Enter", skin);
        btnAccept.setPosition(1200,700);
        btnAccept.setSize(300,300);
        btnAccept.getLabel().setFontScale(4);
        btnAccept.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button){
                game.setClient(new TSSCClient(ipAddressField.getText()));
                if(game.getClient().isConnected()) {
                    game.setScreen(new ControllerScreen(game));
                    dispose();
                }else {
                    setStatus("Connection Failed: " + game.getClient().getErrorMsg());
                }
                Gdx.input.setOnscreenKeyboardVisible(false);
                return false;
            }
        });

        ipAddressField = new TextField("", textField);
        ipAddressField.setPosition(100,700);
        ipAddressField.setSize(1000,200);

        status = new Label("",skin);
        status.setFontScale(3);
        status.setWidth(1000);
        status.setWrap(true);

        Label askIP = new Label("IP Address: ",skin);
        askIP.setPosition(100,1000);
        askIP.setFontScale(5);

        Table statusTable = new Table(skin);
        statusTable.setPosition(600,600);
        statusTable.add(status).width(1000f);

        setStatus("Ready");

        stage.addActor(ipAddressField);
        stage.addActor(askIP);
        stage.addActor(statusTable);
        stage.addActor(btnAccept);
    }

    //NOTEME javadoc
    /**
     *  @param text The text to set in the status label.
     */
    private void setStatus(String text) {
        status.setText("STATUS: " + text);
    }

    //NOTEME javadoc
    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {

        if(ipAddressField.getText().isEmpty()) {
            btnAccept.setDisabled(true);
        } else {
            btnAccept.setDisabled(false);
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.getBatch().begin();
        this.game.getBatch().draw((Texture) game.getAssetManager().get("Background.jpg"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    //NOTEME javadoc
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

    //NOTEME javadoc
    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}