package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.drfl.twinstickshooter.game.TSSGame;

/**
 * A view representing the main character.
 */
public class MainCharView extends AnimatedEntityView {

    //NOTEME javadoc
    /**
     * Constructs a main character view.
     *
     * @param game The game this view belongs to
     */
    public MainCharView(TSSGame game) {
        super(game, (Texture) game.getAssetManager().get("Pistolero.png"), 0.15f);
    }
}