package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;

/**
 * A abstract view capable of holding a sprite with a certain
 * position and rotation.
 */
public abstract class EntityView {

    /**
     * The sprite representing this entity view.
     */
    Sprite sprite;

    /**
     * Creates a view belonging to a game.
     *
     * @param game The game this view belongs to
     */
    EntityView(TSSGame game) {
        sprite = createSprite(game);
    }

    /**
     * Draws the sprite from this view using a sprite batch.
     *
     * @param batch The sprite batch to be used for drawing
     */
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Abstract method that creates the view sprite. Concrete
     * implementation should extend this method to create their
     * own sprites.
     *
     * @param game The game this view belongs to
     * @return The sprite representing this view
     */
    public abstract Sprite createSprite(TSSGame game);

    /**
     * Updates this view based on a certain model.
     *
     * @param model The model used to update this view
     */
    public void update(EntityModel model) {
        sprite.setCenter(model.getPosition().x / PIXEL_TO_METER, model.getPosition().y / PIXEL_TO_METER);
        sprite.setRotation((float) Math.toDegrees(model.getRotation()));
    }
}