package com.drfl.twinstickshooter.model.entities;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * A model representing enemy spawners.
 */
public class EnemySpawnerModel extends EntityModel {

    private boolean spawned = false;

    /**
     * Creates a new enemy spawner in a certain position.
     *
     * @param x the x-coordinate in meters
     * @param y the y-coordinate in meters
     */
    public EnemySpawnerModel(float x, float y) {
        super(x / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f, y / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f, 0);
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    @Override
    public ModelType getType() {
        return ModelType.SPAWNER;
    }
}