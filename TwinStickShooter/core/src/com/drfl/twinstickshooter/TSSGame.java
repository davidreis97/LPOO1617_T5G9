package com.drfl.twinstickshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.drfl.twinstickshooter.view.TSSView;

/**
 * TSS main game class
 */
public class TSSGame extends Game {
	private SpriteBatch batch;
	private AssetManager assetManager;

	//private TSSServer server;
    //private static Vector2 acceleration;
	//private static Vector2 bullets;

    /**
     * Initializes sprite batch and asset manager, starts the game
     */
    @Override
    public void create () {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        startGame();
    }

    /**
     * Start game
     */
    private void startGame() {
        setScreen(new TSSView(this, new TSSServer()));
    }

    /**
     * Disposes of all assets
     */
	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
	}

    /**
     * @return asset manager used for loading texture and sounds
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * @return sprite batch
     */
    public SpriteBatch getBatch() {
        return batch;
    }

//	public void render() {
//        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//
//        batch.begin();
//        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
//        sprite.draw(batch);
//        batch.end();
//	}
//    public static Vector2 getAcceleration() {
//        return acceleration;
//    }
//
//    public static void setAcceleration(Vector2 acceleration) {
//        TSSGame.acceleration = acceleration;
//    }
//
//    public static Vector2 getBullets() {
//        return bullets;
//    }
//
//    public static void setBullets(Vector2 bullets) {
//        TSSGame.bullets = bullets;
//    }
}