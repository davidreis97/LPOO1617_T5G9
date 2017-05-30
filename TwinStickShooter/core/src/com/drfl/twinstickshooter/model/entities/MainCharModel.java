package com.drfl.twinstickshooter.model.entities;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * A model representing the player character.
 */
public class MainCharModel extends EntityModel {

    /**
     * Is this ship accelerating in this update delta
     */
    private AnimDirection direction = AnimDirection.NONE;

    /**
     * Creates a new ship model in a certain position and having a certain rotation.
     *
     * @param x the x-coordinate in meters
     * @param y the y-coordinate in meters
     * @param rotation the rotation in radians
     */
    public MainCharModel(float x, float y, int rotation) {
        super(x / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f, y / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f, rotation);
    }

    /**
     * @param direction the movement direction to set
     */
    public void setDirection(AnimDirection direction) {
        this.direction = direction;
    }

    /**
     * @return the current movement direction
     */
    public AnimDirection getDirection() {
        return this.direction;
    }

    @Override
    public ModelType getType() {
        return ModelType.MAINCHAR;
    }
}
