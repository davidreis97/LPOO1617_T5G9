package com.drfl.twinstickshooter.game;

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
 * Twin stick shooter main class.
 */
public class TSSGame extends Game {

    /**
     * Maximum amount of high scores.
     */
    private static final int NUM_SCORES = 10;

    /**
     * Enumerator for controller types.
     */
    public enum ControlType {CONTROLLER, REMOTE, KBM}

    /**
     * The type of input currently in use to control the game.
     */
    private ControlType inputMode = ControlType.KBM;

    /**
     * The server instance of the game for android controller.
     */
    private TSSServer server;

    /**
     * The batch associated with all the game's sprites.
     */
	private SpriteBatch batch;

    /**
     * Asset manager for all the game's assets.
     */
	private AssetManager assetManager;

    /**
     * Scene2D stage used for UI and HUD.
     */
	private Stage stage;

    /**
     * The game's state machine for handling menus and screens.
     */
	private TSSState stateM;

    /**
     * Overall music volume.
     */
	private float musicVolume = 0.60f;

    /**
     * Overall sound effect volume.
     */
	private float soundVolume = 0.30f;

    /**
     * High scores.
     */
	private Array<TSSScore> scores = new Array<>();

    /**
     * Tiled map the game takes place in.
     */
	private TiledMap currentMap;

    /**
     * Flag used for testing, doesn't enable screens and sprite batches.
     */
	private boolean testing = false;

    /**
     * Flag for signalling whether this application listener has been initialized.
     */
	private volatile boolean readyForTest = false;

    /**
     * Called when the Application is first created. Initializes sprite batch, asset manager and server.
     * Also loads sound effects.
     */
    @Override
    public void create () {

        if(!testing) batch = new SpriteBatch();
        assetManager = new AssetManager();
        if(!testing) server = new TSSServer();

        assetManager.load("Shoot.mp3", Sound.class);
        assetManager.load("Hurt.mp3", Sound.class);
        assetManager.finishLoading();

        if(testing) readyForTest = true;
        if(!testing) startGame();
    }

    /**
     * Loads high scores from JSON file and changes state to main menu.
     */
    private void startGame() {

        loadScore();

        stateM = new TSSState(TSSState.GameState.MAIN_MENU, this);
        TSSMainMenu menu = new TSSMainMenu(this);
        setScreen(menu);
    }

    /**
     * Loads top 10 high scores from JSON file to array. Initializes array if no JSON file exists.
     */
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

    /**
     * Saves high scores to JSON file.
     */
    private void saveScore() {

        Json json = new Json();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Gdx.files.getLocalStoragePath() + "Score.json"))) {

        bw.write(json.prettyPrint(scores));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an high score and then sorts and trims the array to top 10 largest score values.
     *
     * @param name The name of the high scorer
     * @param score The value of the high score
     */
    public void addScore(String name, int score) {

        this.scores.add(new TSSScore(name, score));
        this.scores.sort();

        if(scores.size > NUM_SCORES) {
            scores.removeRange(NUM_SCORES, scores.size - 1);
        }

        saveScore();
    }

    /**
     * Finds the first site local host address.
     *
     * @return The first site local host address or getLocalHost() if no site local host found
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
     * Disposes of all assets.
     */
	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		server.dispose();
		if(currentMap != null) currentMap.dispose();
	}

    /**
     * @return The asset manager used for loading texture and sounds
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * @return The sprite batch of the game
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     *  @return The Scene2D stage of the game
     */
    public Stage getStage() {
        return stage;
    }

    /**
     *  @param stage A Scene2D stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     *  @return The state machine class of the game
     */
    public TSSState getStateM() {
        return stateM;
    }

    /**
     *  @return Enumerator with the current input method
     */
    public ControlType getInputMode() {
        return inputMode;
    }

    /**
     *  @param inputMode An enumerator defining an input method
     */
    public void setInputMode(ControlType inputMode) {
        this.inputMode = inputMode;
    }

    /**
     *  @return The server instance of the game
     */
    public TSSServer getServer() {
        return server;
    }

    /**
     *  @return The current music volume
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     *  @param musicVolume A music volume in the range [0, 1]
     */
    public void setMusicVolume(float musicVolume) {

        if(musicVolume > 1.0f || musicVolume < 0) return;
        this.musicVolume = musicVolume;
    }

    /**
     *  @return The current sound volume
     */
    public float getSoundVolume() {
        return soundVolume;
    }

    /**
     *  @param soundVolume A sound volume in the range [0, 1]
     */
    public void setSoundVolume(float soundVolume) {

        if(soundVolume > 1.0f || soundVolume < 0) return;
        this.soundVolume = soundVolume;
    }

    /**
     *  @return The array of high scores
     */
    public Array<TSSScore> getScores() {
        return scores;
    }

    /**
     * Checks if a score is an high score by comparing to current high scores.
     *
     * @param score The high score to test
     * @return Whether the score is a new high score
     */
    public boolean checkHighScore(int score) {

        if(this.scores.size < NUM_SCORES && score > 0) {
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

    /**
     *  @return The current Tiled map
     */
    public TiledMap getCurrentMap() {
        return currentMap;
    }

    /**
     *  @param currentMap A Tiled map
     */
    public void setCurrentMap(TiledMap currentMap) {
        this.currentMap = currentMap;
    }

    /**
     *  @param testing Whether testing is happening
     */
    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    /**
     *  @return Whether application listener has been initialized
     */
    public boolean isReadyForTest() {
        return readyForTest;
    }
}