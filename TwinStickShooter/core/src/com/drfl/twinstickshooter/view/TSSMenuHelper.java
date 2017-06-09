package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.game.TSSGame;

public class TSSMenuHelper {

    private static TSSMenuHelper instance;

    private static TSSGame game;

    private static Skin skin;

    private TSSMenuHelper(TSSGame game) {
        TSSMenuHelper.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    /**
     * Returns a singleton instance of controller, instance must
     * be initiated by a call to initInstance beforehand. Null is returned if not.
     *
     * @return The singleton instance
     */
    public static TSSMenuHelper getInstance() {
        return instance;
    }

    static void initInstance(TSSGame game) {
        if(skin != null) skin.dispose();
        instance = new TSSMenuHelper(game);
    }

    void loadMusic(String music) {
        game.getAssetManager().load(music, Music.class);
    }

    void playMusic(String music, float volume, boolean loop) {

        ((Music) game.getAssetManager().get(music)).setLooping(loop);
        ((Music) game.getAssetManager().get(music)).setVolume(volume);
        ((Music) game.getAssetManager().get(music)).play();
    }

    TextButton createTextButton(String text, Vector2 size, Vector2 pixelCoords, EventListener listener) {

        TextButton textButton = new TextButton(text, skin);
        textButton.setSize(size.x, size.y);
        textButton.setPosition(pixelCoords.x - textButton.getWidth() / 2.0f, pixelCoords.y - textButton.getHeight() / 2.0f);
        textButton.addListener(listener);

        game.getStage().addActor(textButton);

        return textButton;
    }

    SelectBox createSelectBox(Vector2 size, Vector2 pixelCoords, String[] items) {

        SelectBox selectBox = new SelectBox(skin);
        selectBox.setSize(size.x, size.y);
        selectBox.setPosition(pixelCoords.x - selectBox.getWidth() / 2.0f, pixelCoords.y - selectBox.getHeight() / 2.0f);
        selectBox.setItems(items);

        game.getStage().addActor(selectBox);

        return selectBox;
    }

    Label createLabel(String text, Vector2 size, Vector2 pixelCoords, Color color) {

        Label label = new Label(text, skin);
        label.setSize(size.x, size.y);
        label.setPosition(pixelCoords.x - label.getWidth() / 2.0f, pixelCoords.y - label.getHeight() / 2.0f);
        label.setColor(color);

        game.getStage().addActor(label);

        return label;
    }

    Slider createHorSlider(Vector2 range, float initValue, Vector2 size, Vector2 pixelCoords, EventListener listener) {

        Slider slider = new Slider(range.x, range.y, 0.05f, false, skin);
        slider.setSize(size.x, size.y);
        slider.setPosition(pixelCoords.x - slider.getWidth() / 2.0f, pixelCoords.y - slider.getHeight() / 2.0f);
        slider.setValue(initValue);
        slider.addListener(listener);

        game.getStage().addActor(slider);

        return slider;
    }
}