package com.drfl.twinstickshooter.model.entities;


import com.badlogic.gdx.math.Vector2;
import com.drfl.twinstickshooter.model.TSSModel;

import java.util.ArrayList;
import java.util.Random;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;
import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

public class EnemyModel extends EntityModel {

    //Both in milliseconds
    private static final int MOVE_COOLDOWN_MIN = 300;
    private static final int MOVE_COOLDOWN_MAX = 750;

    private static final float TIME_BETWEEN_SHOTS = 1.2f; //Seconds

    /**
     * RNG Seed
     */
    private static final Random rand = new Random();

    private float timeToNextDirection = 0;

    private Vector2 oppositeDirection = new Vector2(0, 0);

    /**
     * Creates a new ship model in a certain position and having a certain rotation.
     *
     * @param x the x-coordinate in meters
     * @param y the y-coordinate in meters
     * @param rotation the rotation in radians
     */
    public EnemyModel(float x, float y, int rotation) {
        super(x + TILESIZE * PIXEL_TO_METER / 2.0f, y + TILESIZE * PIXEL_TO_METER / 2.0f, rotation);
        this.hitpoints = 20; //TODO magic value
        this.timeToNextShoot = TIME_BETWEEN_SHOTS;
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

    @Override
    public void setMoveDirection(Vector2 moveDirection) {

        oppositeDirection = moveDirection.scl(-1);

        this.moveDirection = moveDirection;
    }

    public Vector2 getOppositeDirection() {
        return oppositeDirection;
    }

    @Override
    public void removeHitpoints(int value) {
        this.hitpoints -= value;
        if(hitpoints <= 0) TSSModel.getInstance().setScore(TSSModel.getInstance().getScore() + 100); //TODO magic value
    }

    /**
     * Randomly selects a 4-way direction
     *
     * @return random direction vector
     */
    public Vector2 generateMovement() {

        ArrayList<Vector2> directions = new ArrayList<Vector2>();
        directions.add(new Vector2(0, 0));
        directions.add(new Vector2(0, 1));
        directions.add(new Vector2(0, -1));
        directions.add(new Vector2(1, 0));
        directions.add(new Vector2(-1, 0));

        return directions.get(rand.nextInt(directions.size()));
    }

    @Override
    public float getShootCooldown() {
        return TIME_BETWEEN_SHOTS;
    }
}