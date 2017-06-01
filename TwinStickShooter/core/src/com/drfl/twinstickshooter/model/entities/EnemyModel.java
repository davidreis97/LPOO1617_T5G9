package com.drfl.twinstickshooter.model.entities;


import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

public class EnemyModel extends EntityModel {

    /**
     * Creates a new ship model in a certain position and having a certain rotation.
     *
     * @param x the x-coordinate in meters
     * @param y the y-coordinate in meters
     * @param rotation the rotation in radians
     */
    public EnemyModel(float x, float y, int rotation) {
        super(x + TILESIZE * PIXEL_TO_METER / 2.0f, y + TILESIZE * PIXEL_TO_METER / 2.0f, rotation);
    }

    @Override
    public EntityModel.ModelType getType() {
        return ModelType.ENEMY;
    }
}
