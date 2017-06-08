package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * A model representing the player character.
 */
public class MainCharModel extends EntityModel {

    private static final float TIME_BETWEEN_SHOTS = 0.2f;

    private boolean isDead = false;

    /**
     * Creates a new ship model in a certain position and having a certain rotation.
     *
     * @param rotation the rotation in radians
     */
    public MainCharModel(Vector2 coords, int rotation) {
//        super(new Vector2(coords.x / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f, coords.y / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f), rotation);
        super(new Vector2(coords.x + TILESIZE * PIXEL_TO_METER / 2.0f, coords.y + TILESIZE * PIXEL_TO_METER / 2.0f), rotation);
    }

    @Override
    public ModelType getType() {
        return ModelType.MAINCHAR;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getShootCooldown() {
        return TIME_BETWEEN_SHOTS;
    }
}
