package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

public class BulletModel extends EntityModel {

    private ModelType owner;
    private Vector2 bulletDirection;

    /**
     * Constructs a bullet model belonging to a game model.
     *
     * @param x The x-coordinate of this bullet.
     * @param y The y-coordinate of this bullet.
     * @param rotation The rotation of this bullet.
     */
    public BulletModel(float x, float y, float rotation) {
        super(x, y, rotation);
    }

    @Override
    public ModelType getType() {
        return ModelType.BULLET;
    }

    public ModelType getOwner() {
        return owner;
    }

    public void setOwner(ModelType owner) {
        this.owner = owner;
    }

    public void setBulletDirection(Vector2 direction) {
        this.bulletDirection = direction;
    }

    public Vector2 getBulletDirection() {
        return bulletDirection;
    }
}