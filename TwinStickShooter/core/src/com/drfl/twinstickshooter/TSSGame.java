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
        TSSView gameScreen = new TSSView(this, new TSSServer());
        gameScreen.initInstance(gameScreen);
        setScreen(gameScreen);
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
}