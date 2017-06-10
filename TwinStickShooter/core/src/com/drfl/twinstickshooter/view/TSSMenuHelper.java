package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.drfl.twinstickshooter.game.TSSGame;

/**
 * Singleton utility class for common menu functionalities.
 */
public class TSSMenuHelper {

    //NOTEME javadoc
    /**
     * The singleton instance of the menu helper.
     */
    private static TSSMenuHelper instance;

    //NOTEME javadoc
    /**
     * The game currently active to access asset manager.
     */
    private static TSSGame game;

    //NOTEME javadoc
    /**
     * Skin used for Scene2D stage actors.
     */
    private static Skin skin;

    //NOTEME javadoc
    /**
     * Constructs a new menu helper with a certain game.
     *
     * @param game The game associated with this menu helper
     */
    private TSSMenuHelper(TSSGame game) {
        TSSMenuHelper.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    //NOTEME javadoc
    /**
     * Returns a singleton instance of menu helper, instance must
     * be initiated by a call to initInstance beforehand. Null is returned if not.
     *
     * @return The singleton instance
     */
    public static TSSMenuHelper getInstance() {
        return instance;
    }

    //NOTEME javadoc
    /**
     * Initializes a menu helper instance, associating it with a game.
     *
     * @param game The game associated with this menu helper
     */
    static void initInstance(TSSGame game) {
        if(skin != null) skin.dispose();
        instance = new TSSMenuHelper(game);
    }

    //NOTEME javadoc
    /**
     * Load music specified.
     *
     * @param music The music filename to load
     */
    void loadMusic(String music) {
        game.getAssetManager().load(music, Music.class);
    }

    //NOTEME javadoc
    /**
     * Plays music specified at a certain volume, looping or not.
     * Music must've been loaded by asset manager.
     *
     * @param music The music filename to play
     * @param volume Sets the volume of this music stream in the range [0, 1]
     * @param loop Sets whether the music stream is looping
     */
    void playMusic(String music, float volume, boolean loop) {

        ((Music) game.getAssetManager().get(music)).setLooping(loop);
        ((Music) game.getAssetManager().get(music)).setVolume(volume);
        ((Music) game.getAssetManager().get(music)).play();
    }

    //NOTEME javadoc
    /**
     * Creates an orthographic camera for displaying the screen.
     *
     * @param viewportWidth The width of the viewport
     * @param viewportHeight The height of the viewport
     * @return The orthographic camera
     */
    OrthographicCamera createCamera(float viewportWidth, float viewportHeight) {

        OrthographicCamera camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        return camera;
    }

    //NOTEME javadoc
    /**
     * Creates a FitViewport from the specified width, height and already created camera.
     *
     * @param viewportWidth The width of the viewport
     * @param viewportHeight The height of the viewport
     * @param camera The orthographic camera
     * @return The viewport created
     */
    FitViewport createFitViewport(float viewportWidth, float viewportHeight, Camera camera) {
        return new FitViewport(viewportWidth, viewportHeight, camera);
    }

    //NOTEME javadoc
    /**
     * Creates and adds a Scene2D Text Button using the specified parameters and default skin.
     *
     * @param text The text on the Text Button
     * @param size The size of the Text Button
     * @param pixelCoords The center coordinates of the Text Button
     * @param listener The event listener to add
     * @return The created Text Button
     */
    TextButton createTextButton(String text, Vector2 size, Vector2 pixelCoords, EventListener listener) {

        TextButton textButton = new TextButton(text, skin);
        textButton.setSize(size.x, size.y);
        textButton.setPosition(pixelCoords.x - textButton.getWidth() / 2.0f, pixelCoords.y - textButton.getHeight() / 2.0f);
        textButton.addListener(listener);

        game.getStage().addActor(textButton);

        return textButton;
    }

    //NOTEME javadoc
    /**
     * Creates and adds a Scene2D Select Box using the specified parameters and default skin.
     *
     * @param size The size of the Select Box
     * @param pixelCoords The center coordinates of the Select Box
     * @param items The backing Array that makes up the choices available in the SelectBox
     * @return The created Select Box
     */
    SelectBox createSelectBox(Vector2 size, Vector2 pixelCoords, String[] items) {

        SelectBox selectBox = new SelectBox(skin);
        selectBox.setSize(size.x, size.y);
        selectBox.setPosition(pixelCoords.x - selectBox.getWidth() / 2.0f, pixelCoords.y - selectBox.getHeight() / 2.0f);
        selectBox.setItems(items);

        game.getStage().addActor(selectBox);

        return selectBox;
    }

    //NOTEME javadoc
    /**
     * Creates and adds a Scene2D Label using the specified parameters and default skin.
     *
     * @param text The text of the Label
     * @param size The size of the Label
     * @param pixelCoords The center coordinates of the Label
     * @param color The color of the Label text
     * @param alignment The text alignment of the Label
     * @return The created Label
     */
    Label createLabel(String text, Vector2 size, Vector2 pixelCoords, Color color, int alignment) {

        Label label = new Label(text, skin);
        label.setSize(size.x, size.y);
        label.setPosition(pixelCoords.x - label.getWidth() / 2.0f, pixelCoords.y - label.getHeight() / 2.0f);
        label.setColor(color);
        label.setAlignment(alignment);

        game.getStage().addActor(label);

        return label;
    }

    //NOTEME javadoc
    /**
     * Creates and adds a Scene2D Slider using the specified parameters and default skin, always horizontal.
     *
     * @param range The minimum and maximum values of the Slider
     * @param initValue The initial value of the Slider
     * @param size The size of the Slider
     * @param pixelCoords The center coordinates of the Slider
     * @param listener The event listener to add
     * @return The created Slider
     */
    Slider createHorSlider(Vector2 range, float initValue, Vector2 size, Vector2 pixelCoords, EventListener listener) {

        Slider slider = new Slider(range.x, range.y, 0.05f, false, skin);
        slider.setSize(size.x, size.y);
        slider.setPosition(pixelCoords.x - slider.getWidth() / 2.0f, pixelCoords.y - slider.getHeight() / 2.0f);
        slider.setValue(initValue);
        slider.addListener(listener);

        game.getStage().addActor(slider);

        return slider;
    }

    //NOTEME javadoc
    /**
     * Creates and adds a Scene2D Image Button using the specified parameters and custom texture.
     *
     * @param imageUp The texture to show on the Image Button
     * @param size The size of the Image Button
     * @param pixelCoords The center coordinates of the Image Button
     * @param listener The event listener to add
     * @return The created Image Button
     */
    ImageButton createImageButton(Drawable imageUp, Vector2 size, Vector2 pixelCoords, EventListener listener) {

        ImageButton imageButton = new ImageButton(imageUp);
        imageButton.setSize(size.x, size.y);
        imageButton.setPosition(pixelCoords.x - imageButton.getWidth() / 2.0f, pixelCoords.y - imageButton.getHeight() / 2.0f);
        imageButton.addListener(listener);

        game.getStage().addActor(imageButton);

        return imageButton;
    }
}