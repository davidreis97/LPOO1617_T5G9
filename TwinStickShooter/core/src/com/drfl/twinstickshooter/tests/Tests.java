package com.drfl.twinstickshooter.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.EnemySpawnerModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the Model/Controller packages
 */
public class Tests {

    //NOTEME javadoc
    /**
     * Physics step to use for tests.
     */
    private static final float UPDATE_STEP = 50;

    //NOTEME javadoc
    /**
     * Controller instance.
     */
    private TSSController controller;

    //NOTEME javadoc
    /**
     * Model instance.
     */
    private TSSModel model;

    //NOTEME javadoc
    /**
     * Game instance.
     */
    private static TSSGame game;

    //NOTEME javadoc
    /**
     * Runs before all tests once. Initializes headless LibGDX.
     */
    @BeforeClass
    public static void setUpBeforeAll() {

        final HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        config.renderInterval = 1.0f / 60;

        game = new TSSGame();
        game.setTesting(true);

        new HeadlessApplication(game, config);
    }

    //NOTEME javadoc
    /**
     * Runs before each test. Makes sure LibGDX has been initialized before running tests.
     */
    @Before
    public void setUpBefore() {

        while(true) {
            if(game.isReadyForTest()) break;
        }

        model = TSSModel.initInstance();
        model.setMainChar(new MainCharModel(new Vector2(0, 0),0));

        controller = TSSController.initInstance(game);
    }

    //NOTEME javadoc
    /**
     * Spawns enemy and tests that score goes up if enemy's health drops to 0 or less.
     */
    @Test
    public void testScore() {

        model.getEnemySpawners().add(new EnemySpawnerModel(new Vector2(5, 0)));

        controller.update(TSSController.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(TSSController.getSpawnMaxCool());

        int score = model.getScore();

        model.getEnemies().get(0).removeHitpoints(model.getEnemies().get(0).getHitpoints());

        assertNotEquals(score, model.getScore());
    }

    //NOTEME javadoc
    /**
     * Spawns enemy and tests that enemy changes position over time.
     */
    @Test(timeout = 2000)
    public void testEnemyMovement() {

        Vector2 finalPos, initialPos;
        model.getEnemySpawners().add(new EnemySpawnerModel(new Vector2(5, 0)));

        controller.update(TSSController.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(TSSController.getSpawnMaxCool());

        do {
            finalPos = model.getEnemies().get(0).getPosition();
            controller.update(UPDATE_STEP);
            initialPos = model.getEnemies().get(0).getPosition();
        } while(finalPos == initialPos);

        assertTrue(true);
    }

    //NOTEME javadoc
    /**
     * Spawns enemy and tests shooting by allowing the enemy
     * to hit the player and checking that player health drops.
     */
    @Test
    public void testEnemyAutoShootPlayer() {

        model.getEnemySpawners().add(new EnemySpawnerModel(new Vector2(5, 0)));

        int oldHitpoints = model.getMainChar().getHitpoints();

        controller.update(TSSController.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(TSSController.getSpawnMaxCool());

        controller.update(UPDATE_STEP); //Guarantee enemy shoot
        controller.update(UPDATE_STEP);

        assertTrue(oldHitpoints > model.getMainChar().getHitpoints());
    }

    //NOTEME javadoc
    /**
     * Spawns enemy and tests if player hitting it with a bullet results in enemy losing health.
     */
    @Test
    public void testBulletsEnemy() {

        model.getEnemySpawners().add(new EnemySpawnerModel(new Vector2(1.5f, 0)));

        controller.update(TSSController.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(TSSController.getSpawnMaxCool());

        int oldHitpoints = model.getEnemies().get(0).getHitpoints();

        controller.shoot(model.getMainChar(),new Vector2(1,0));
        controller.update(UPDATE_STEP);

        assertTrue(oldHitpoints > model.getEnemies().get(0).getHitpoints());
    }

    //NOTEME javadoc
    /**
     * Tests 8-directional movement of the main character by
     * checking whether the character moved as expected.
     */
    @Test
    public void testMovement() {

        Vector2[] possibleMovements = {
                new Vector2(0,0),
                new Vector2(0,1),
                new Vector2(0,-1),
                new Vector2(-1,0),
                new Vector2(-1,1),
                new Vector2(-1,-1),
                new Vector2(1,0),
                new Vector2(1,1),
                new Vector2(1,-1)};

        for (Vector2 movement : possibleMovements) {

            Vector2 initialPos = model.getMainChar().getPosition();

            model.getMainChar().setMoveDirection(movement);

            controller.update(1);

            if(movement.x > 0) {
                assertTrue(initialPos.x < model.getMainChar().getPosition().x);
            } else if(movement.x == 0) {
                assertTrue(initialPos.x == model.getMainChar().getPosition().x);
            } else {
                assertTrue(initialPos.x > model.getMainChar().getPosition().x);
            }

            if(movement.y > 0){
                assertTrue(initialPos.y < model.getMainChar().getPosition().y);
            } else if(movement.y == 0 ){
                assertTrue(initialPos.y == model.getMainChar().getPosition().y);
            } else {
                assertTrue(initialPos.y > model.getMainChar().getPosition().y);
            }

            Array<Body> bodies = new Array<>();
            controller.getWorld().getBodies(bodies);
            bodies.get(0).setLinearVelocity(0,0); //Stops the MainCharBody before starting the test again
        }
    }
}