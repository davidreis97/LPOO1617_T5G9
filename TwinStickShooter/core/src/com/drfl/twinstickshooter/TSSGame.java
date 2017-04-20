package com.drfl.twinstickshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TSSGame extends ApplicationAdapter {
	SpriteBatch batch;
	TSSServer server;

    private static Vector2 acceleration;
	private static Vector2 bullets;

	@Override
	public void create () {
	    server = new TSSServer();

		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

    public static Vector2 getAcceleration() {
        return acceleration;
    }

    public static void setAcceleration(Vector2 acceleration) {
        TSSGame.acceleration = acceleration;
    }

    public static Vector2 getBullets() {
        return bullets;
    }

    public static void setBullets(Vector2 bullets) {
        TSSGame.bullets = bullets;
    }
}
