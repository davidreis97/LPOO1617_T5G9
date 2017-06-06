package com.drfl.twinstickshooter.model.entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

public class EnemyModel extends EntityModel {

    //Both in milliseconds
    private static final int MOVE_COOLDOWN_MIN = 300;
    private static final int MOVE_COOLDOWN_MAX = 750;
    private static final float TIME_BETWEEN_SHOTS = 1.2f;

    /**
     * RNG Seed
     */
    private static final Random rand = new Random();

    private float timeToNextDirection = 0;

    private Vector2 moveDirection = new Vector2(0, 0);
    private Vector2 shootDirection = new Vector2(0, 0);

    private Array<Vector2> previousDirection = new Array<Vector2>();

    /**
     * Creates a new ship model in a certain position and having a certain rotation.
     *
     * @param x the x-coordinate in meters
     * @param y the y-coordinate in meters
     * @param rotation the rotation in radians
     */
    public EnemyModel(float x, float y, int rotation) {
        super(x + TILESIZE * PIXEL_TO_METER / 2.0f, y + TILESIZE * PIXEL_TO_METER / 2.0f, rotation);
        this.hitpoints = 20;
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

        if(this.previousDirection.size < 2) {
            this.previousDirection.add(moveDirection.scl(-1));
        } else {
            this.previousDirection.get(0).set(this.previousDirection.get(1));
            this.previousDirection.get(1).set(moveDirection.scl(-1));
        }

        this.moveDirection = moveDirection;
    }

    public Vector2 getShootDirection() {
        return shootDirection;
    }

    public void setShootDirection(Vector2 shootDirection) {
        this.shootDirection = shootDirection;
    }

    public void resetTimeToNextShoot() {
        this.timeToNextShoot = TIME_BETWEEN_SHOTS;
    }

    public Array<Vector2> getPreviousDirection() {
        return previousDirection;
    }
}
