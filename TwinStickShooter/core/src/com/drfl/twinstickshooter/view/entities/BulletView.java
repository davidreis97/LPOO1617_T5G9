package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.drfl.twinstickshooter.TSSGame;

/**
 * A view representing bullets.
 */
public class BulletView extends EntityView {

    /**
     * Constructs a bullet view.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public BulletView(TSSGame game) {
        super(game);
    }

    /**
     * Creates a sprite representing this bullet.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the sprite representing this bullet
     */
    public Sprite createSprite(TSSGame game) {

        Texture texture = game.getAssetManager().get("Bullet.png");
        return new Sprite(texture, texture.getWidth(), texture.getHeight());
    }
}
