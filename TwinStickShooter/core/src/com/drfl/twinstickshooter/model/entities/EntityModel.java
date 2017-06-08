package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract model for representing entities in a game model.
 */
public abstract class EntityModel {
    public enum ModelType {MAINCHAR, BULLET, SPAWNER, ENEMY}
    public enum AnimDirection {NONE, DOWN, LEFT, RIGHT, UP}

    private static final int HP_MAX = 40;

    private Vector2 shootDirection = new Vector2(0, 0);
    private Vector2 moveDirection = new Vector2(0, 0);

    /**
     * The coordinates of an entity model in meters.
     */
    private Vector2 coords;

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
    int hitpoints = HP_MAX;

    /**
     * Time until character entity can shoot again
     */
    float timeToNextShoot = 0;

    private boolean isHurt = false;

    /**
     * Constructs a model with a position and a rotation.
     *
     * @param rotation The current rotation of this entity in radians.
     */
    EntityModel(Vector2 coords, float rotation) {
        this.coords = coords;
        this.rotation = rotation;
    }

    public Vector2 getPosition() {
        return coords.cpy();
    }

    /**
     * @return The rotation of this entity in radians.
     */
    public float getRotation() {
        return rotation;
    }

    public void setPosition(Vector2 coords) {
        this.coords = coords;
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

    public float getShootCooldown() {
        return 0;
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

    public Vector2 getShootDirection() {
        return shootDirection.cpy();
    }

    public void setShootDirection(Vector2 shootDirection) {
        this.shootDirection = shootDirection;
    }

    public Vector2 getMoveDirection() {
        return moveDirection.cpy();
    }

    public void setMoveDirection(Vector2 moveDirection) {
        this.moveDirection = moveDirection;
    }
}
