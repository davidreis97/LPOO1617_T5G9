package com.drfl.twinstickshooter.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.drfl.twinstickshooter.controller.entities.TileEntity;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.EntityModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * TSS world model
 */

public class TSSModel {
    /**
     * The singleton instance of the game model
     */
    private static TSSModel instance;

    /**
     * Character controlled by the player
     */
    private MainCharModel mc;

    /**
     * The bullets currently on screen.
     */
    private List<BulletModel> bullets;

    /**
     * A pool of bullets
     */
    Pool<BulletModel> bulletPool = new Pool<BulletModel>() {
        @Override
        protected BulletModel newObject() {
            return new BulletModel(0, 0, 0);
        }
    };

    /**
     * Constructs game with main character //TODO expand game model
     */
    private TSSModel() {

        bullets = new ArrayList<BulletModel>();
        //createEntityModels is called right after getInstance creates Singleton instance
    }

    /**
     * Returns a singleton instance of the game model
     *
     * @return the singleton instance
     */
    public static TSSModel getInstance() {

        if (instance == null)
            instance = new TSSModel();
        return instance;
    }

    /**
     * Creates entity models from a Tiled file using an object layer named "Entities".
     */
    public void createEntityModels(MapLayer entitiesLayer) {
        for(MapObject object : entitiesLayer.getObjects()) {
            if(object.getProperties().get("type", String.class).equals("MainChar")) {
                mc = new MainCharModel(object.getProperties().get("x", float.class), object.getProperties().get("y", float.class), 0);
            }
        }
    }

    public BulletModel createBullet(MainCharModel mc, Vector2 shootInput) {

        BulletModel bullet = bulletPool.obtain();

        bullet.setFlaggedForRemoval(false);

        float angle = shootInput.angle() * (float) Math.PI / 180.0f - (float) Math.PI / 2.0f;

        bullet.setPosition(mc.getX() - (float) Math.sin(angle), mc.getY() + (float) Math.cos(angle));
        bullet.setRotation(angle);

//      bullet.setTimeToLive(.5f); //TODO needed if implementing weapons with decaying bullets

        bullets.add(bullet);

        return bullet;
    }

    /**
     * @return the main character model.
     */
    public MainCharModel getMainChar() {
        return mc;
    }

    /**
     * @return the bullet list
     */
    public List<BulletModel> getBullets() {
        return bullets;
    }

    /**
     * Removes a model from this game.
     *
     * @param model the model to be removed
     */
    public void remove(EntityModel model) {
        if (model instanceof BulletModel) {
            bullets.remove(model);
            bulletPool.free((BulletModel) model);
        }
    }
}