package com.drfl.twinstickshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

//TODO extends gdx.Game?
public class TSSGame extends Game {
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private TSSServer server;

    private static Vector2 acceleration;
	private static Vector2 bullets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("jet.png"));
		sprite = new Sprite(texture);
	    server = new TSSServer();
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        batch.begin();
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        sprite.draw(batch);
        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
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
