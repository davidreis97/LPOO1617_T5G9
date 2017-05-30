package com.drfl.tsscontroller.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

        btnAccept = new TextButton("Enter", skin);
        btnAccept.setPosition(Gdx.graphics.getWidth()/2 + 100,Gdx.graphics.getHeight()/2);
        btnAccept.setSize(50,50);
        btnAccept.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button){
                game.setClient(new TSSCClient(ipAddressField.getText()));
                game.setScreen(new ControllerScreen(game));
                dispose();
            }
        });

        ipAddressField = new TextField("",skin);
        ipAddressField.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        ipAddressField.setSize(200,60);

        stage.addActor(ipAddressField);
        stage.addActor(btnAccept);
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
