package com.drfl.twinstickshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.drfl.twinstickshooter.view.TSSMainMenu;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * TSS main game class
 */
public class TSSGame extends Game {

    public enum ControlType {CONTROLLER, REMOTE, KBM};

    /**
     * The type of input currently in use to control the game.
     */
    private ControlType inputMode = ControlType.KBM;
    private TSSServer server;

	private SpriteBatch batch;
	private AssetManager assetManager;
	private Stage stage;
	private TSSState stateM;
	private float musicVolume = 0.60f;
	private float soundVolume = 0.35f;

    /**
     * Initializes sprite batch and asset manager, starts the game
     */
    @Override
    public void create () {

        batch = new SpriteBatch();
        assetManager = new AssetManager();
        server = new TSSServer();

        assetManager.load("Shoot.mp3", Sound.class);
        assetManager.load("Hurt.mp3", Sound.class);
        assetManager.finishLoading();

        startGame();
    }

    /**
     * Start game
     */
    private void startGame() {

        stateM = new TSSState(TSSState.GameState.MAIN_MENU, this);
        TSSMainMenu menu = new TSSMainMenu(this);
        setScreen(menu);
    }

    /**
     * Finds the first site local host address
     *
     * @return first site local host address or getLocalHost() if no site local host found
     */
    public ArrayList<InetAddress> findSiteLocalAddress() {

        ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();

        try {
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {

                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {

                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();

                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            addresses.add(inetAddr);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }

        if(!addresses.isEmpty()) return addresses;

        try {
            return new ArrayList<InetAddress>(Arrays.asList(InetAddress.getLocalHost()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Disposes of all assets
     */
	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		server.dispose();
	}

    /**
     * @return asset manager used for loading texture and sounds
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * assetManager asset manager used for loading texture and sounds
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * @return sprite batch
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TSSState getStateM() {
        return stateM;
    }

    public ControlType getInputMode() {
        return inputMode;
    }

    public void setInputMode(ControlType inputMode) {
        this.inputMode = inputMode;
    }

    public TSSServer getServer() {
        return server;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }
}