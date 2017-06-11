package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.drfl.twinstickshooter.game.TSSGame;

/**
 * A view representing bullets.
 */
public class BulletView extends EntityView {

    //NOTEME javadoc
    /**
     * Constructs a bullet view.
     *
     * @param game The game this view belongs to
     */
    public BulletView(TSSGame game) {
        super(game);
    }

    //NOTEME javadoc
    /**
     * Creates a sprite representing this bullet.
     *
     * @param game The game this view belongs to
     * @return The sprite representing this bullet
     */
    public Sprite createSprite(TSSGame game) {

        Texture texture = game.getAssetManager().get("Bullet.png");
        return new Sprite(texture, texture.getWidth(), texture.getHeight());
    }
}