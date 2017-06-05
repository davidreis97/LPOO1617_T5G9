package com.drfl.twinstickshooter.model.entities;

/**
 * Abstract model for representing entities in a game model.
 */
public abstract class EntityModel {
    public enum ModelType {MAINCHAR, BULLET, SPAWNER, ENEMY};
    public enum AnimDirection {NONE, DOWN, LEFT, RIGHT, UP};

    private static final int HP_MAX = 20;

    /**
     * The x-coordinate of this model in meters.
     */
    private float x;

    /**
     * The y-coordinate of this model in meters.
     */
    private float y;

    /**
     * The current rotation of this model in radians.
     */
    private float rotation;

    /**
     * Has this model been flagged for removal?
     */
    private boolean flaggedForRemoval = false;

    /**
     * Animation direction of model
     */
    private AnimDirection direction = AnimDirection.NONE;

    /**
     * Hitpoints for character entities
     */
    protected int hitpoints = HP_MAX;

    /**
     * Time until character entity can shoot again
     */
    protected float timeToNextShoot = 0;

    protected boolean isHurt = false;

    /**
     * Constructs a model with a position and a rotation.
     *
     * @param x The x-coordinate of this entity in meters.
     * @param y The y-coordinate of this entity in meters.
     * @param rotation The current rotation of this entity in radians.
     */
    EntityModel(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    /**
     * @return The x-coordinate of this entity in meters.
     */
    public float getX() {
        return x;
    }

    /**
     * @return The y-coordinate of this entity in meters.
     */
    public float getY() {
        return y;
    }

    /**
     * @return The rotation of this entity in radians.
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * @param x The x-coordinate of this entity in meters.
     * @param y The y-coordinate of this entity in meters.
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
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

    /**
     * @param rotation The current rotation of this entity in radians.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Returns if this entity has been flagged for removal
     *
     * @return
     */
    public boolean isFlaggedToBeRemoved() {
        return flaggedForRemoval;
    }

    /**
     * Makes this model flagged for removal on next step
     */
    public void setFlaggedForRemoval(boolean flaggedForRemoval) {
        this.flaggedForRemoval = flaggedForRemoval;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void removeHitpoints(int value) {
        this.hitpoints -= value;
    }

    public abstract ModelType getType();

    public float getTimeToNextShoot() {
        return timeToNextShoot;
    }

    public void setTimeToNextShoot(float timeToNextShoot) {
        this.timeToNextShoot = timeToNextShoot;
    }

    public boolean isHurt() {
        return isHurt;
    }

    public void setHurt(boolean hurt) {
        isHurt = hurt;
    }

    public static int getHpMax() {
        return HP_MAX;
    }
}
