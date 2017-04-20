package com.drfl.tsscontroller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.esotericsoftware.minlog.Log;

public class TSSCGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Touchpad touchpadLeft;
	Touchpad touchpadRight;
	Skin touchpadSkin;
	Stage stage;
	Drawable touchBackground;
	Drawable touchKnob;
	Touchpad.TouchpadStyle touchpadStyle;
	TSSCClient client;

	@Override
	public void create () {
		client = new TSSCClient();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground",new Texture("touchBackground.png"));
		touchpadSkin.add("touchKnob",new Texture("touchKnob.png"));

		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");

		touchKnob.setMinHeight((float) (touchBackground.getMinHeight() * 1.2));
		touchKnob.setMinWidth((float) (touchBackground.getMinWidth() * 1.2));

		touchpadStyle = new Touchpad.TouchpadStyle();

		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;

		touchpadLeft = new Touchpad(10,touchpadStyle);

		touchpadLeft.setBounds(300, 100, 500, 500);

		touchpadRight = new Touchpad(10,touchpadStyle);

		touchpadRight.setBounds( 1000, 100, 500, 500);

		touchpadRight.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    Packet.ControllerInfoPacket packet = new Packet.ControllerInfoPacket();
			    packet.acceleration = new Vector2(touchpadLeft.getKnobPercentX(),touchpadLeft.getKnobPercentY());
                packet.bullet = new Vector2(touchpadRight.getKnobPercentX(),touchpadRight.getKnobPercentY());
                client.client.sendTCP(packet);
                Log.info("Sent Packet from Right Touchpad");
			}
		});

        touchpadLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Packet.ControllerInfoPacket packet = new Packet.ControllerInfoPacket();
				packet.acceleration = new Vector2(touchpadLeft.getKnobPercentX(),touchpadLeft.getKnobPercentY());
				packet.bullet = new Vector2(touchpadRight.getKnobPercentX(),touchpadRight.getKnobPercentY());
                client.client.sendTCP(packet);
                Log.info("Sent Packet from Left Touchpad");
            }
        });

		stage = new Stage();
		stage.addActor(touchpadLeft);
		stage.addActor(touchpadRight);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
