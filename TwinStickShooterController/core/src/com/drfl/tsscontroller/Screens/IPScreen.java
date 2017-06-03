package com.drfl.tsscontroller.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.drfl.tsscontroller.Network.TSSCClient;
import com.drfl.tsscontroller.TSSCGame;

public class IPScreen implements Screen {

    private TSSCGame game;
    private Stage stage;

    private Viewport viewport;
    private Camera camera;

    private TextButton btnAccept;
    private TextField ipAddressField;

    private Label askIP;

    private Table statusTable;
    private Label status;

    private int attempts = 0;

    public IPScreen(TSSCGame game){
        this.game = game;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);

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

        ipAddressField = new TextField("", textField); //TODO- Change font size
        ipAddressField.setPosition(100,700);
        ipAddressField.setSize(1000,200);

        status = new Label("",skin);
        status.setFontScale(3);
        status.setWidth(1000);
        status.setWrap(true);

        askIP = new Label("IP Address: ",skin);
        askIP.setPosition(100,1000);
        askIP.setFontScale(5);

        statusTable = new Table(skin);
        statusTable.setPosition(600,600);
        statusTable.add(status).width(1000f);

        setStatus("Ready");

        stage.addActor(ipAddressField);
        stage.addActor(askIP);
        stage.addActor(statusTable);
        stage.addActor(btnAccept);
    }

    public void setStatus(String text){
        status.setText("STATUS: " + text);
    }

    @Override
    public void render(float delta) {

        if(ipAddressField.getText().isEmpty()) {
            btnAccept.setDisabled(true);
        } else {
            btnAccept.setDisabled(false);
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        stage.dispose();
    }
}
