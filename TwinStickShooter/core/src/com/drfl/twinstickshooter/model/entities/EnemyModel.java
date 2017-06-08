package com.drfl.twinstickshooter.model.entities;


import com.badlogic.gdx.math.Vector2;
import com.drfl.twinstickshooter.model.TSSModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * The enemy model.
 */
public class EnemyModel extends EntityModel {

    //NOTEME javadoc
    /**
     * Minimum milliseconds until next movement.
     */
    private static final int MOVE_COOLDOWN_MIN = 300;

    //NOTEME javadoc
    /**
     * Max HP.
     */
    private static final int HP_MAX = 15;

    //NOTEME javadoc
    /**
     * Amount of score to increment for each enemy death
     */
    private static final int SCORE_INC = 100;

    //NOTEME javadoc
    /**
     * Maximum milliseconds until next movement.
     */
    private static final int MOVE_COOLDOWN_MAX = 750;

    //NOTEME javadoc
    /**
     * Shoot cooldown.
     */
    private static final float TIME_BETWEEN_SHOTS = 1.2f; //Seconds

    //NOTEME javadoc
    /**
     * RNG Seed
     */
    private static final Random rand = new Random();

    //NOTEME javadoc
    /**
     * Seconds until next direction.
     */
    private float timeToNextDirection = 0;

    //NOTEME javadoc
    /**
     * Opposite of the current enemy's direction.
     */
    private Vector2 oppositeDirection = new Vector2(0, 0);

    //NOTEME javadoc
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

    //NOTEME javadoc
    /**
     * Randomly chooses seconds until the next movement direction between
     * a minimum and a maximum value.
     */
    public void resetTimeToNextDirection() {
        this.timeToNextDirection = (rand.nextInt(MOVE_COOLDOWN_MAX + 1) + MOVE_COOLDOWN_MIN) / 1000.0f;
    }

    //NOTEME javadoc
    @Override
    public EntityModel.ModelType getType() {
        return ModelType.ENEMY;
    }

    //NOTEME javadoc
    /**
     *  @return Seconds until next movement direction
     */
    public float getTimeToNextDirection() {
        return timeToNextDirection;
    }

    //NOTEME javadoc
    /**
     *  @param timeToNextDirection Seconds until next movement direction
     */
    public void setTimeToNextDirection(float timeToNextDirection) {
        this.timeToNextDirection = timeToNextDirection;
    }

    //NOTEME javadoc
    @Override
    public void setMoveDirection(Vector2 moveDirection) {

        oppositeDirection = moveDirection.scl(-1);
        super.setMoveDirection(moveDirection);
    }

    //NOTEME javadoc
    /**
     *  @return A vector copy of the opposite movement direction.
     */
    public Vector2 getOppositeDirection() {
        return oppositeDirection.cpy();
    }

    //NOTEME javadoc
    /**
     * Removes hitpoints from an enemy. Also adds score if hp <= 0.
     *
     * @param value The amount of hitpoints to remove
     */
    @Override
    public void removeHitpoints(int value) {
        this.hitpoints -= value;
        if(hitpoints <= 0) TSSModel.getInstance().setScore(TSSModel.getInstance().getScore() + SCORE_INC);
    }

    //NOTEME javadoc
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

    //NOTEME javadoc
    @Override
    public float getShootCooldown() {
        return TIME_BETWEEN_SHOTS;
    }
}