package com.drfl.twinstickshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.drfl.twinstickshooter.view.TSSMainMenu;
import com.drfl.twinstickshooter.server.TSSServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public enum ControlType {CONTROLLER, REMOTE, KBM}

    /**
     * The type of input currently in use to control the game.
     */
    private ControlType inputMode = ControlType.KBM;
    private TSSServer server;

	private SpriteBatch batch;
	private AssetManager assetManager;
	private Stage stage;
	private TSSState stateM;
//	private float musicVolume = 0.60f;
//	private float soundVolume = 0.35f; //TODO set this back
    private float musicVolume = 0;
    private float soundVolume = 0;
	private Array<TSSScore> scores = new Array<>();
	private TiledMap currentMap;

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

        loadScore();

        stateM = new TSSState(TSSState.GameState.MAIN_MENU, this);
        TSSMainMenu menu = new TSSMainMenu(this);
        setScreen(menu);
    }

    private void loadScore() {

        FileHandle handle = Gdx.files.local("Score.json");
        if(handle.exists()) {
            Json json = new Json();
            try {
                this.scores = json.fromJson(Array.class, handle);
            } catch(SerializationException e) {
                this.scores = new Array<TSSScore>();
                e.printStackTrace();
            }
        }
    }

    private void saveScore() {

        Json json = new Json();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Gdx.files.getLocalStoragePath() + "Score.json"))) {

        bw.write(json.prettyPrint(scores));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScore(String name, int score) {

        this.scores.add(new TSSScore(name, score));
        this.scores.sort();

        if(scores.size > 10) {
            scores.removeRange(10, scores.size - 1);
        }

        saveScore();
    }

    /**
     * Finds the first site local host address
     *
     * @return first site local host address or getLocalHost() if no site local host found
     */
    public ArrayList<InetAddress> findSiteLocalAddress() {

        ArrayList<InetAddress> addresses = new ArrayList<>();

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
		if(currentMap != null) currentMap.dispose();
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

    public Array<TSSScore> getScores() {
        return scores;
    }

    public boolean checkHighScore(int score) {

        if(this.scores.size < 10 && score > 0) {
            return true;
        } else {
            for(TSSScore scores : this.scores) {
                if(score > scores.getScore()) {
                    return true;
                }
            }
        }

        return false;
    }

    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(TiledMap currentMap) {
        this.currentMap = currentMap;
    }
}