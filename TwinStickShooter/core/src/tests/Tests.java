package tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.*;

import com.drfl.twinstickshooter.controller.TSSController;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.esotericsoftware.minlog.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

@RunWith(GdxTestRunner.class)
public class Tests{

    TSSGame game;
    TSSController controller;
    TSSModel model;

    @Before
    public void setUp(){
        game = new TSSGame();
        game.setAssetManager(new AssetManager());

        model = TSSModel.getInstance();
        model.setMainChar(new MainCharModel(0,0,0));

        controller = TSSController.getInstance();
    }

    @Test
    public void testMainCharMovement(){
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

        for (Vector2 movement : possibleMovements){
            Vector2 initialPos = new Vector2(model.getMainChar().getX(),model.getMainChar().getY());

            controller.setMoveInput(movement);
            controller.update(1);

            if(movement.x > 0){
                assertTrue(initialPos.x < model.getMainChar().getX());
            }else if(movement.x == 0){
                assertTrue(initialPos.x == model.getMainChar().getX());
            }else{
                assertTrue(initialPos.x > model.getMainChar().getX());
            }

            if(movement.y > 0){
                assertTrue(initialPos.y < model.getMainChar().getY());
            }else if(movement.y == 0){
                assertTrue(initialPos.y == model.getMainChar().getY());
            }else {
                assertTrue(initialPos.y > model.getMainChar().getY());
            }

            Array<Body> bodies = new Array<Body>();
            controller.getWorld().getBodies(bodies);
            bodies.get(0).setLinearVelocity(0,0); //Stops the MainCharBody before starting the test again
        }
    }
}
