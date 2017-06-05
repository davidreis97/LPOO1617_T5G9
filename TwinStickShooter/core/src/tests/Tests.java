package tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.EnemySpawnerModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class Tests {

    static TSSController controller;
    static TSSModel model;

    @Before
    public void setUpBefore() {

        model = TSSModel.initInstance();
        model.setMainChar(new MainCharModel(0,0,0));

        controller = TSSController.initInstance();
    }

    @Test
    public void testEnemyPlacement() {

        model.getEnemySpawners().add(new EnemySpawnerModel(5,0));

        controller.setTimeToNextSpawn(0f);
        controller.update(0.01f);

        assertTrue(model.getEnemies().size() > 0);

        assertEquals(model.getEnemies().get(0).getX(),model.getEnemySpawners().get(0).getX(),0.1f);
        assertEquals(model.getEnemies().get(0).getX(),model.getEnemySpawners().get(0).getX(),0.1f);
    }

    @Test
    public void testEnemyAutoShootPlayer() {

        model.getEnemySpawners().add(new EnemySpawnerModel(5,0));
        controller.spawnTestEnemy(0);

        int oldHitpoints = model.getMainChar().getHitpoints();

        controller.update(50);

        assertTrue(oldHitpoints > model.getMainChar().getHitpoints());
    }

    @Test
    public void testBulletsEnemy() {

        model.getEnemySpawners().add(new EnemySpawnerModel(1.5f,0));
        controller.spawnTestEnemy(0);

        int oldHitpoints = model.getEnemies().get(0).getHitpoints();

        controller.shoot(model.getMainChar(),new Vector2(1,0));
        controller.update(50);

        assertTrue(oldHitpoints > model.getEnemies().get(0).getHitpoints());
    }

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

            Vector2 initialPos = new Vector2(model.getMainChar().getX(),model.getMainChar().getY());

            controller.setMoveInput(movement);
            controller.update(1);

            if(movement.x > 0) {
                assertTrue(initialPos.x < model.getMainChar().getX());
            } else if(movement.x == 0) {
                assertTrue(initialPos.x == model.getMainChar().getX());
            } else {
                assertTrue(initialPos.x > model.getMainChar().getX());
            }

            if(movement.y > 0) {
                assertTrue(initialPos.y < model.getMainChar().getY());
            } else if(movement.y == 0) {
                assertTrue(initialPos.y == model.getMainChar().getY());
            } else {
                assertTrue(initialPos.y > model.getMainChar().getY());
            }

            Array<Body> bodies = new Array<Body>();
            controller.getWorld().getBodies(bodies);
            bodies.get(0).setLinearVelocity(0,0); //Stops the MainCharBody before starting the test again
        }
    }
}
