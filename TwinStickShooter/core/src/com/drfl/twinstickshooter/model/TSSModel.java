package com.drfl.twinstickshooter.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.drfl.twinstickshooter.model.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;

/**
 * MVC Model, handles the underlying data that supports both the Controller and View packages.
 */
public class TSSModel {

    //NOTEME javadoc
    /**
     * The singleton instance of the game model.
     */
    private static TSSModel instance;

    //NOTEME javadoc
    /**
     * RNG Seed
     */
    private static final Random rand = new Random();

    //NOTEME javadoc
    /**
     * Main character model.
     */
    private MainCharModel mc;

    //NOTEME javadoc
    /**
     * Enemy spawners.
     */
    private ArrayList<EnemySpawnerModel> enemySpawners = new ArrayList<>();

    //NOTEME javadoc
    /**
     * Enemy models.
     */
    private ArrayList<EnemyModel> enemies = new ArrayList<>();

    //NOTEME javadoc
    /**
     * Bullet models.
     */
    private List<BulletModel> bullets;

    //NOTEME javadoc
    /**
     * A pool of bullet models.
     */
    private Pool<BulletModel> bulletPool = new Pool<BulletModel>() {
        @Override
        protected BulletModel newObject() {
            return new BulletModel(new Vector2(0, 0), 0);
        }
    };

    //NOTEME javadoc
    /**
     * Game score
     */
    private int score = 0;

    //NOTEME javadoc
    /**
     * Constructs game with initial bullets array, other entities
     * are created by calling createEntityModels with the entities layer
     * from a Tiled map.
     */
    private TSSModel() {
        bullets = new ArrayList<BulletModel>();
    }

    //NOTEME javadoc
    /**
     * Returns a singleton instance of the model.
     *
     * @return The singleton instance
     */
    public static TSSModel getInstance() {

        if (instance == null) instance = new TSSModel();
        return instance;
    }

    //NOTEME javadoc
    /**
     * Initializes a model instance.
     *
     * @return The singleton instance
     */
    public static TSSModel initInstance() {
        TSSModel.instance = new TSSModel();
        return instance;
    }

    //NOTEME javadoc
    /**
     * Creates entity models from a Tiled map object layer. Object type is used to
     * instantiate entity models (MainChar / Spawner)
     *
     * @param entitiesLayer Tiled map object layer containing entities
     */
    public void createEntityModels(MapLayer entitiesLayer) {

        for(MapObject object : entitiesLayer.getObjects()) {

            if(object.getProperties().get("type", String.class).equals("MainChar")) {
                mc = new MainCharModel(new Vector2(object.getProperties().get("x", float.class) * PIXEL_TO_METER, object.getProperties().get("y", float.class) * PIXEL_TO_METER), 0);
            }

            if(object.getProperties().get("type", String.class).equals("Spawner")) {
                enemySpawners.add(new EnemySpawnerModel(new Vector2(object.getProperties().get("x", float.class) * PIXEL_TO_METER, object.getProperties().get("y", float.class) * PIXEL_TO_METER)));
            }
        }
    }

    //NOTEME javadoc
    /**
     * Creates a bullet model positioned near its owner and a certain heading.
     *
     * @param owner The entity who owns the bullet
     * @param direction The heading of the bullet
     * @return The bullet model created
     */
    public BulletModel createBullet(EntityModel owner, Vector2 direction) {

        BulletModel bullet = bulletPool.obtain();

        bullet.setFlaggedForRemoval(false);

        float angle = direction.angle() * (float) Math.PI / 180.0f - (float) Math.PI / 2.0f;

        bullet.setPosition(new Vector2(owner.getPosition().x - (float) Math.sin(angle), owner.getPosition().y + (float) Math.cos(angle)));
        bullet.setRotation(angle);

        if(owner instanceof MainCharModel) {
            bullet.setOwner(EntityModel.ModelType.MAINCHAR);
        } else bullet.setOwner(EntityModel.ModelType.ENEMY);

        bullet.setBulletDirection(direction);
        bullets.add(bullet);

        return bullet;
    }

    //NOTEME javadoc
    /**
     * Creates an enemy model from a spawner. Sets spawner flag so it can't spawn until
     * an enemy dies and refreshes it.
     *
     * @param index The spawner index
     * @return The enemy model created
     */
    public EnemyModel createEnemy(int index) {

        enemySpawners.get(index).setSpawned(true);
        enemies.add(new EnemyModel(new Vector2(enemySpawners.get(index).getPosition().x, enemySpawners.get(index).getPosition().y), 0));

        return enemies.get(enemies.size() - 1);
    }

    //NOTEME javadoc
    /**
     * Tries to find an enemy spawner flagged as free.
     *
     * @return Index of random free spawner, -1 if no free spawners
     */
    public int searchSpawnIndex() {

        if(enemySpawners.isEmpty()) return -1;

        ArrayList<Integer> freeIndex = new ArrayList<>();

        for(int i = 0; i < this.enemySpawners.size(); i++) {
            if(!this.enemySpawners.get(i).isSpawned()) {
                freeIndex.add(i);
            }
        }

        if(freeIndex.isEmpty()) {
            return -1;
        } else {
            return freeIndex.get(rand.nextInt(freeIndex.size()));
        }
    }

    //NOTEME javadoc
    /**
     * Remove bullet model and free it in pool.
     *
     * @param bullet The bullet model to remove
     */
    public void removeBullet(BulletModel bullet) {
        bullets.remove(bullet);
        bulletPool.free(bullet);
    }

    //NOTEME javadoc
    /**
     * Resets first non free spawner so it can spawn again.
     */
    private void resetSpawner() {

        for(EnemySpawnerModel spawner : enemySpawners) {
            if(spawner.isSpawned()) {
                spawner.setSpawned(false);
                break;
            }
        }
    }

    //NOTEME javadoc
    /**
     * Removes enemy model at a certain index.
     *
     * @param index The index of enemy model to remove
     */
    public void removeEnemy(int index) {

        resetSpawner();
        enemies.remove(index);
    }

    //NOTEME javadoc
    /**
     * @return The main character model
     */
    public MainCharModel getMainChar() {
        return mc;
    }

    //NOTEME javadoc
    /**
     * @param mc The main character model to set
     */
    public void setMainChar(MainCharModel mc) {
        this.mc = mc;
    }

    //NOTEME javadoc
    /**
     * @return The bullet model list
     */
    public List<BulletModel> getBullets() {
        return bullets;
    }

    //NOTEME javadoc
    /**
     * @return The enemy spawner model list
     */
    public ArrayList<EnemySpawnerModel> getEnemySpawners() {
        return enemySpawners;
    }

    //NOTEME javadoc
    /**
     * @return The enemy model list
     */
    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }

    //NOTEME javadoc
    /**
     * @return The game score
     */
    public int getScore() {
        return score;
    }

    //NOTEME javadoc
    /**
     * @param score The game score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
}