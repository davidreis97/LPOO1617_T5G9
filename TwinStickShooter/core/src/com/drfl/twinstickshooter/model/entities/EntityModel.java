package com.drfl.twinstickshooter.model.entities;

/**
 * Abstract model for representing entities in a game model.
 */
public abstract class EntityModel {
    public enum ModelType {MAINCHAR};

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

//    /**
//     * Has this model been flagged for removal?
//     */
//    private boolean flaggedForRemoval = false;

    /**
     * Constructs a model with a position and a rotation.
     *
     * @param x The x-coordinate of this entity in meters.
     * @param y The y-coordinate of this entity in meters.
     * @param rotation The current rotation of this entity in radians.
     */
    EntityModel(float x, float y, float rotation) { //TODO rotation needed?
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
     * @param rotation The current rotation of this entity in radians.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

//    /**
//     * Returns if this entity has been flagged for removal
//     *
//     * @return
//     */
//    public boolean isFlaggedToBeRemoved() {
//        return flaggedForRemoval;
//    }

//    /**
//     * Makes this model flagged for removal on next step
//     */
//    public void setFlaggedForRemoval(boolean flaggedForRemoval) {
//        this.flaggedForRemoval = flaggedForRemoval;
//    }

    public abstract ModelType getType();
}
