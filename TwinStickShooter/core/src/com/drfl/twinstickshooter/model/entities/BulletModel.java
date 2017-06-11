package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * The bullet model.
 */
public class BulletModel extends EntityModel {

    /**
     * The model type of the owner of the bullet.
     */
    private ModelType owner;

    /**
     * Vector representing bullet direction.
     */
    private Vector2 bulletDirection;

    /**
     * Constructs a bullet model belonging to a game.
     *
     * @param coords The coordinates of the bullet
     * @param rotation The rotation of the bullet in radians
     */
    public BulletModel(Vector2 coords, float rotation) {
        super(coords, rotation);
    }

    @Override
    public ModelType getType() {
        return ModelType.BULLET;
    }

    /**
     *  @return The model type that owns the bullet
     */
    public ModelType getOwner() {
        return owner;
    }

    /**
     *  @param owner The model type to set as owner of the bullet
     */
    public void setOwner(ModelType owner) {
        this.owner = owner;
    }

    /**
     *  @param direction The direction vector of the bullet
     */
    public void setBulletDirection(Vector2 direction) {
        this.bulletDirection = direction;
    }

    /**
     *  @return A vector copy of the bullet direction
     */
    public Vector2 getBulletDirection() {
        return bulletDirection.cpy();
    }
}