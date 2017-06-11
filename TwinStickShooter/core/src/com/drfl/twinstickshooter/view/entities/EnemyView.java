package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.drfl.twinstickshooter.game.TSSGame;

/**
 * A view representing an enemy entity.
 */
public class EnemyView extends AnimatedEntityView {

    //NOTEME javadoc
    /**
     * Constructs an enemy entity view.
     *
     * @param game The game this view belongs to
     */
    public EnemyView(TSSGame game) {
        super(game, (Texture) game.getAssetManager().get("Rogue.png"), 0.15f);
    }
}