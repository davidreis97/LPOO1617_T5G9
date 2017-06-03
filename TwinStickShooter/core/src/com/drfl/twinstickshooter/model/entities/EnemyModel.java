package com.drfl.twinstickshooter.model.entities;


import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

public class EnemyModel extends EntityModel {

    //Both in milliseconds
    private static final int MOVE_COOLDOWN_MIN = 500;
    private static final int MOVE_COOLDOWN_MAX = 2500;

    /**
     * RNG Seed
     */
    private static final Random rand = new Random();

    private float timeToNextDirection = 0;

    private Vector2 moveDirection = new Vector2(0, 0);

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

    public float getTimeToNextDirection() {
        return timeToNextDirection;
    }

    public void setTimeToNextDirection(float timeToNextDirection) {
        this.timeToNextDirection = timeToNextDirection;
    }

    public void resetTimeToNextDirection() {
        this.timeToNextDirection = (rand.nextInt(MOVE_COOLDOWN_MAX + 1) + MOVE_COOLDOWN_MIN) / 1000.0f;
    }

    public Vector2 getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(Vector2 moveDirection) {
        this.moveDirection = moveDirection;
    }
}
