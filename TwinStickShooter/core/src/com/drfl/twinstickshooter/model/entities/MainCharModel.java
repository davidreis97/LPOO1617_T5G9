package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * The main character model.
 */
public class MainCharModel extends EntityModel {

    /**
     * Shoot cooldown.
     */
    private static final float TIME_BETWEEN_SHOTS = 0.2f;

    /**
     * Max HP.
     */
    private static final int HP_MAX = 40;

    /**
     * Whether the main character is dead.
     */
    private boolean isDead = false;

    /**
     * Constructs a main character model belonging to a game.
     *
     * @param coords The coordinates of the main character
     * @param rotation The rotation of the main character in radians
     */
    public MainCharModel(Vector2 coords, int rotation) {

        super(new Vector2(coords.x + CENTER_ADJUST, coords.y + CENTER_ADJUST), rotation);
        this.hitpoints = HP_MAX;
    }

    @Override
    public ModelType getType() {
        return ModelType.MAINCHAR;
    }

    /**
     *  @return Whether main character is dead
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     *  @param dead Whether the main character is dead
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    @Override
    public float getShootCooldown() {
        return TIME_BETWEEN_SHOTS;
    }

    /**
     *  @return The maximum hitpoints of the main character
     */
    public int getHPMax() {
        return HP_MAX;
    }
}
