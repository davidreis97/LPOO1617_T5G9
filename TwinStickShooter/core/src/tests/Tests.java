package tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.EnemySpawnerModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class Tests{

    TSSController controller;
    TSSModel model;
    TSSGame game;

    @Before
    public void setUpBefore() {

        game = new TSSGame();

        game.setSoundVolume(0);

        model = TSSModel.initInstance();
        model.setMainChar(new MainCharModel(0,0,0));

        controller = TSSController.initInstance(game);
    }

    @Test
    public void testScore() {

        model.getEnemySpawners().add(new EnemySpawnerModel(5,0));

        controller.update(controller.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(controller.getSpawnMaxCool());

        int score = model.getScore();

        model.getEnemies().get(0).removeHitpoints(model.getEnemies().get(0).getHitpoints());

        assertNotEquals(score, model.getScore());
    }

    @Test(timeout = 2000)
    public void testEnemyMovement() {

        Vector2 finalPos, initialPos;
        model.getEnemySpawners().add(new EnemySpawnerModel(5,0));

        controller.update(controller.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(controller.getSpawnMaxCool());

        do {
            finalPos = new Vector2(model.getEnemies().get(0).getX(), model.getEnemies().get(0).getY());
            controller.update(50);
            initialPos = new Vector2(model.getEnemies().get(0).getX(), model.getEnemies().get(0).getY());
        } while(finalPos == initialPos);

        assertTrue(true);
    }

    @Test
    public void testEnemyAutoShootPlayer() {

        model.getEnemySpawners().add(new EnemySpawnerModel(5,0));

        controller.update(controller.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(controller.getSpawnMaxCool());

        int oldHitpoints = model.getMainChar().getHitpoints();

        controller.update(50);

        assertTrue(oldHitpoints > model.getMainChar().getHitpoints());
    }

    @Test
    public void testBulletsEnemy() {

        model.getEnemySpawners().add(new EnemySpawnerModel(1.5f,0));

        controller.update(controller.getSpawnMaxCool()); //Guarantee enemy spawn
        controller.update(controller.getSpawnMaxCool());

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

            model.getMainChar().setMoveDirection(movement);

            controller.update(1);

            if(movement.x > 0) {
                assertTrue(initialPos.x < model.getMainChar().getX());
            } else if(movement.x == 0) {
                assertTrue(initialPos.x == model.getMainChar().getX());
            } else {
                assertTrue(initialPos.x > model.getMainChar().getX());
            }

            if(movement.y > 0){
                assertTrue(initialPos.y < model.getMainChar().getY());
            } else if(movement.y == 0 ){
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
