package com.drfl.twinstickshooter.model.entities;


import com.badlogic.gdx.math.Vector2;
import com.drfl.twinstickshooter.model.TSSModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * The enemy model.
 */
public class EnemyModel extends EntityModel {

    /**
     * Minimum milliseconds until next movement.
     */
    private static final int MOVE_COOLDOWN_MIN = 300;

    /**
     * Max HP.
     */
    private static final int HP_MAX = 15;

    /**
     * Amount of score to increment for each enemy death
     */
    private static final int SCORE_INC = 100;

    /**
     * Maximum milliseconds until next movement.
     */
    private static final int MOVE_COOLDOWN_MAX = 750;

    /**
     * Shoot cooldown.
     */
    private static final float TIME_BETWEEN_SHOTS = 1.2f; //Seconds

    /**
     * RNG Seed
     */
    private static final Random rand = new Random();

    /**
     * Seconds until next direction.
     */
    private float timeToNextDirection = 0;

    /**
     * Opposite of the current enemy's direction.
     */
    private Vector2 oppositeDirection = new Vector2(0, 0);

    /**
     * Constructs an enemy model belonging to a game.
     *
     * @param coords The coordinates of the enemy
     * @param rotation The rotation of the enemy in radians
     */
    public EnemyModel(Vector2 coords, int rotation) {

        super(new Vector2(coords.x + CENTER_ADJUST, coords.y + CENTER_ADJUST), rotation);
        this.hitpoints = HP_MAX;
        this.timeToNextShoot = TIME_BETWEEN_SHOTS;
    }

    /**
     * Randomly chooses seconds until the next movement direction between
     * a minimum and a maximum value.
     */
    public void resetTimeToNextDirection() {
        this.timeToNextDirection = (rand.nextInt(MOVE_COOLDOWN_MAX + 1) + MOVE_COOLDOWN_MIN) / 1000.0f;
    }

    @Override
    public EntityModel.ModelType getType() {
        return ModelType.ENEMY;
    }

    /**
     *  @return Seconds until next movement direction
     */
    public float getTimeToNextDirection() {
        return timeToNextDirection;
    }

    /**
     *  @param timeToNextDirection Seconds until next movement direction
     */
    public void setTimeToNextDirection(float timeToNextDirection) {
        this.timeToNextDirection = timeToNextDirection;
    }


    @Override
    public void setMoveDirection(Vector2 moveDirection) {

        oppositeDirection = moveDirection.scl(-1);
        super.setMoveDirection(moveDirection);
    }

    /**
     *  @return A vector copy of the opposite movement direction.
     */
    public Vector2 getOppositeDirection() {
        return oppositeDirection.cpy();
    }

    /**
     * Removes hitpoints from an enemy. Also adds score if hp &lt;= 0.
     *
     * @param value The amount of hitpoints to remove
     */
    @Override
    public void removeHitpoints(int value) {
        this.hitpoints -= value;
        if(hitpoints <= 0) TSSModel.getInstance().setScore(TSSModel.getInstance().getScore() + SCORE_INC);
    }

    /**
     * Randomly selects a 4-way direction, including stopping.
     *
     * @return Random direction vector
     */
    public Vector2 generateMovement() {

        ArrayList<Vector2> directions = new ArrayList<>();
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