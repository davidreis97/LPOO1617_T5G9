package com.drfl.tsscontroller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.drfl.tsscontroller.Network.TSSCClient;
import com.drfl.tsscontroller.Screens.IPScreen;

public class TSSCGame extends Game {

	private TSSCClient client;
	private SpriteBatch batch;
	private AssetManager assetManager;

	@Override
	public void create () {

        this.batch = new SpriteBatch();
        this.assetManager = new AssetManager();

		setScreen(new IPScreen(this));
	}

	public void dispose() {
	    this.batch.dispose();
	    this.assetManager.dispose();
    }

    public TSSCClient getClient() {
        return client;
    }

    public void setClient(TSSCClient client) {
        this.client = client;
    }

	public SpriteBatch getBatch() {
		return batch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
