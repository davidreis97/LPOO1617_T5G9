package com.drfl.twinstickshooter.model.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * The enemy spawner model.
 */
public class EnemySpawnerModel extends EntityModel {

    //NOTEME javadoc
    /**
     * Whether the spawner has spawned an enemy.
     */
    private boolean spawned = false;

    //NOTEME javadoc
    /**
     * Constructs an enemy spawner model belonging to a game.
     *
     * @param coords The coordinates of the enemy spawner
     */
    public EnemySpawnerModel(Vector2 coords) {
        super(new Vector2(coords.x + CENTER_ADJUST, coords.y + CENTER_ADJUST), 0);
    }

    //NOTEME javadoc
    /**
     *  @return Whether the enemy spawner has spawned an enemy
     */
    public boolean isSpawned() {
        return spawned;
    }

    //NOTEME javadoc
    /**
     *  @param spawned Whether the enemy spawner has spawned an enemy
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    //NOTEME javadoc
    @Override
    public ModelType getType() {
        return ModelType.SPAWNER;
    }
}