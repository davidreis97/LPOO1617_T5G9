package com.drfl.twinstickshooter.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.drfl.twinstickshooter.model.entities.*;
import com.drfl.twinstickshooter.view.TSSView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * RNG Seed
     */
    private Random rand = new Random();

    /**
     * Map enemy spawners.
     */
    private ArrayList<EnemySpawnerModel> enemySpawners = new ArrayList<EnemySpawnerModel>();

    /**
     * Enemy entities
     */
    private ArrayList<EnemyModel> enemies = new ArrayList<EnemyModel>();

    /**
     * The bullets currently on screen.
     */
    private List<BulletModel> bullets;

    private int score = 0;

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
     * Constructs game with initial bullets array, other entities
     * are created by TSSView calling createEntityModels with the entities layer
     * from the tile map.
     */
    private TSSModel() {

        bullets = new ArrayList<BulletModel>();
    }

    /**
     * Returns a singleton instance of the game model
     *
     * @return the singleton instance
     */
    public static TSSModel getInstance() {

        if (instance == null) instance = new TSSModel();
        return instance;
    }

    public static TSSModel initInstance() {
        TSSModel.instance = new TSSModel();
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

            if(object.getProperties().get("type", String.class).equals("Spawner")) {
                enemySpawners.add(new EnemySpawnerModel(object.getProperties().get("x", float.class), object.getProperties().get("y", float.class)));
            }
        }
    }

    public EnemyModel createTestEnemy(int index) {

        enemySpawners.get(index).setSpawned(true);

        enemies.add(new EnemyModel(enemySpawners.get(index).getX(), enemySpawners.get(index).getY(), 0));

        return enemies.get(enemies.size() - 1);
    }

    public BulletModel createBullet(EntityModel owner, Vector2 direction) {

        BulletModel bullet = bulletPool.obtain();

        bullet.setFlaggedForRemoval(false);

        float angle = direction.angle() * (float) Math.PI / 180.0f - (float) Math.PI / 2.0f;

        bullet.setPosition(owner.getX() - (float) Math.sin(angle), owner.getY() + (float) Math.cos(angle));
        bullet.setRotation(angle);

        if(owner instanceof MainCharModel) {
            bullet.setOwner(EntityModel.ModelType.MAINCHAR);
        } else bullet.setOwner(EntityModel.ModelType.ENEMY);

//      bullet.setTimeToLive(.5f); //NOTEME needed if implementing weapons with decaying bullets

        bullet.setBulletDirection(direction);
        bullets.add(bullet);

        return bullet;
    }

    /**
     * Creates an enemy model with a specified spawner's coordinates
     *
     * @param index index of spawner to use coordinates of
     * @return enemy model created
     */
    public EnemyModel createEnemy(int index) {

        enemySpawners.get(index).setSpawned(true);

        enemies.add(new EnemyModel(enemySpawners.get(index).getX(), enemySpawners.get(index).getY(), 0));

        TSSView.getInstance().addEnemyView();

        return enemies.get(enemies.size() - 1);
    }

    /**
     * Find a free spawner to use for spawning an enemy
     *
     * @return index of random free spawner, -1 if no free spawners
     */
    public int createSpawnIndex() {

        if(enemySpawners.isEmpty()) return -1;

        ArrayList<Integer> freeIndex = new ArrayList<Integer>();

        for(int i = 0; i < this.enemySpawners.size(); i++) {
            if(this.enemySpawners.get(i).isSpawned() == false) {
                freeIndex.add(i);
            }
        }

        if(freeIndex.isEmpty()) {
            return -1;
        } else {
            return freeIndex.get(rand.nextInt(freeIndex.size()));
        }
    }

    /**
     * @return the main character model.
     */
    public MainCharModel getMainChar() {
        return mc;
    }

    /**
     * @return the main character model.
     */
    public void setMainChar(MainCharModel mc) {
        this.mc = mc;
    }

    /**
     * @return the bullet list
     */
    public List<BulletModel> getBullets() {
        return bullets;
    }

    /**
     * @return the list of enemy spawners
     */
    public ArrayList<EnemySpawnerModel> getEnemySpawners() {
        return enemySpawners;
    }

    /**
     * @return the list of enemies
     */
    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
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

    /**
     * Resets one spawner so it can spawn another enemy
     */
    private void resetSpawner() {
        for(EnemySpawnerModel spawner : enemySpawners) {
            if(spawner.isSpawned()) {
                spawner.setSpawned(false);
                break;
            }
        }
    }

    /**
     * Removes enemy model and enemy view at specified index
     * @param index index on entities array
     */
    public void removeEnemy(int index) {

        resetSpawner();
        enemies.remove(index);
        TSSView.getInstance().removeEnemyView(index);
    }

    public static void setInstance(TSSModel instance) {
        TSSModel.instance = instance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}