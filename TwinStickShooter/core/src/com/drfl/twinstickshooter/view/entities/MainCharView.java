package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.drfl.twinstickshooter.TSSGame;

/**
 * A view representing the main character.
 */
public class MainCharView extends AnimatedEntityView {

    /**
     * Constructs a main character view.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public MainCharView(TSSGame game) {
        super(game, (Texture) game.getAssetManager().get("Pistolero.png"), 0.15f);
    }
}