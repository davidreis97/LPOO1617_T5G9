package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.drfl.twinstickshooter.TSSGame;

/**
 * A view representing the main character.
 */
public class EnemyView extends AnimatedEntityView {

    /**
     * Constructs a enemy character view.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public EnemyView(TSSGame game) {
        super(game, (Texture) game.getAssetManager().get("Rogue.png"), 0.15f);

    }
}