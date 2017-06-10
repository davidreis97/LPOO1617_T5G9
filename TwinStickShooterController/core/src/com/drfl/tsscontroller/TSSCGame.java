package com.drfl.tsscontroller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.drfl.tsscontroller.Network.TSSCClient;
import com.drfl.tsscontroller.Screens.IPScreen;

/**
 * Twin stick shooter controller main class.
 */
public class TSSCGame extends Game {

    //NOTEME javadoc
    /**
     * Kryonet client.
     */
	private TSSCClient client;

    //NOTEME javadoc
    /**
     * The batch associated with all the controller's sprites.
     */
	private SpriteBatch batch;

    //NOTEME javadoc
    /**
     * Asset manager for all the controller's assets.
     */
	private AssetManager assetManager;

    //NOTEME javadoc
    /**
     * Called when the Application is first created. Initializes sprite batch and asset manager.
     * Also sets the initial screen.
     */
	@Override
	public void create () {

        this.batch = new SpriteBatch();
        this.assetManager = new AssetManager();

		setScreen(new IPScreen(this));
	}

    //NOTEME javadoc
    /**
     * Disposes of all assets.
     */
	public void dispose() {
	    this.batch.dispose();
	    this.assetManager.dispose();
    }

    //NOTEME javadoc
    /**
     *  @return The controller's client
     */
    public TSSCClient getClient() {
        return client;
    }

    //NOTEME javadoc
    /**
     *  @param client A client to set to this controller
     */
    public void setClient(TSSCClient client) {
        this.client = client;
    }

    //NOTEME javadoc
    /**
     * @return The sprite batch of the controller
     */
	public SpriteBatch getBatch() {
		return batch;
	}

    //NOTEME javadoc
    /**
     * @return The asset manager used
     */
	public AssetManager getAssetManager() {
		return assetManager;
	}
}