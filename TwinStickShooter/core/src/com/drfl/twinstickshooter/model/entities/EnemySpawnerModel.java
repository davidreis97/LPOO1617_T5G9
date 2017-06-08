package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

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
     */
    public EnemySpawnerModel(Vector2 coords) {
//        super(new Vector2(coords.x / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f, coords.y / TILESIZE + TILESIZE * PIXEL_TO_METER / 2.0f), 0);
        super(new Vector2(coords.x + TILESIZE * PIXEL_TO_METER / 2.0f, coords.y + TILESIZE * PIXEL_TO_METER / 2.0f), 0);
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