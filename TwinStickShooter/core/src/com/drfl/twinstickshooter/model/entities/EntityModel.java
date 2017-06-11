package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract model for representing Model entities.
 */
public abstract class EntityModel {

    /**
     * Enumerator for model types.
     */
    public enum ModelType {MAINCHAR, BULLET, SPAWNER, ENEMY}

    /**
     * Enumerator for animation directions.
     */
    public enum AnimDirection {UP, LEFT, DOWN, RIGHT, NONE}

    /**
     * Meters needed to center entity.
     */
    static final float CENTER_ADJUST = 0.5f;

    /**
     * Vector representing entity shoot direction.
     */
    private Vector2 shootDirection = new Vector2(0, 0);

    /**
     * Vector representing entity move direction.
     */
    private Vector2 moveDirection = new Vector2(0, 0);

    /**
     * The coordinates of an entity model in meters.
     */
    private Vector2 coords;

    /**
     * The rotation of a model in radians.
     */
    private float rotation;

    /**
     * Has this model been flagged for removal?
     */
    private boolean flaggedForRemoval = false;

    /**
     * Animation direction of entity.
     */
    private AnimDirection direction = AnimDirection.NONE;

    /**
     * Entity hitpoints.
     */
    int hitpoints;

    /**
     * Seconds until entity can shoot again.
     */
    float timeToNextShoot = 0;

    /**
     * Flag representing whether an entity is currently damaged.
     */
    private boolean isHurt = false;

    /**
     * Constructs an entity model with a position and a rotation.
     *
     * @param coords The entity model's coordinates
     * @param rotation The entity model's rotation
     */
    EntityModel(Vector2 coords, float rotation) {
        this.coords = coords;
        this.rotation = rotation;
    }

    /**
     * Removes hitpoints from an entity.
     *
     *  @param value The amount of hitpoints to remove
     */
    public void removeHitpoints(int value) {
        this.hitpoints -= value;
    }

    /**
     * @return A copy of the entity's coordinates
     */
    public Vector2 getPosition() {
        return coords.cpy();
    }

    /**
     * @return The entity's rotation in radians
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * @param coords The entity's coordinates to set
     */
    public void setPosition(Vector2 coords) {
        this.coords = coords;
    }

    /**
     * @param direction The entity's animation direction to set
     */
    public void setAnimDirection(AnimDirection direction) {
        this.direction = direction;
    }

    /**
     * @return The entity's animation direction
     */
    public AnimDirection getAnimDirection() {
        return this.direction;
    }

    /**
     * @param rotation The entity's rotation to set (in radians)
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * @return Whether the entity has been flagged for removal
     */
    public boolean isFlaggedToBeRemoved() {
        return flaggedForRemoval;
    }

    /**
     * @param flaggedForRemoval Whether the entity should be removed on the next step
     */
    public void setFlaggedForRemoval(boolean flaggedForRemoval) {
        this.flaggedForRemoval = flaggedForRemoval;
    }

    /**
     * @return The entity's hitpoints
     */
    public int getHitpoints() {
        return hitpoints;
    }

    /**
     * @return The entity model type
     */
    public abstract ModelType getType();

    /**
     *  @return The time (in seconds) until entity can shoot again
     */
    public float getTimeToNextShoot() {
        return timeToNextShoot;
    }

    /**
     * @param timeToNextShoot How many seconds until entity can shoot again
     */
    public void setTimeToNextShoot(float timeToNextShoot) {
        this.timeToNextShoot = timeToNextShoot;
    }

    /**
     * Returns entity's max shoot cooldown.
     * Overrided by some subclasses, otherwise returns 0.
     *
     * @return The max shoot cooldown
     */
    public float getShootCooldown() {
        return 0;
    }

    /**
     *  @return Whether entity is currently damaged
     */
    public boolean isHurt() {
        return isHurt;
    }

    /**
     *  @param hurt Whether the entity is currently damaged
     */
    public void setHurt(boolean hurt) {
        isHurt = hurt;
    }

    /**
     *  @return A vector copy representing entity shoot direction
     */
    public Vector2 getShootDirection() {
        return shootDirection.cpy();
    }

    /**
     * @param shootDirection The vector representing entity shoot direction
     */
    public void setShootDirection(Vector2 shootDirection) {
        this.shootDirection = shootDirection;
    }

    /**
     * @return A vector copy representing entity move direction
     */
    public Vector2 getMoveDirection() {
        return moveDirection.cpy();
    }

    /**
     * @param moveDirection The vector representing entity move direction
     */
    public void setMoveDirection(Vector2 moveDirection) {
        this.moveDirection = moveDirection;
    }
}