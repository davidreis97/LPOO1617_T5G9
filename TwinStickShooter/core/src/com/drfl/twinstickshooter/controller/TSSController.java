package com.drfl.twinstickshooter.controller;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.TSSState;
import com.drfl.twinstickshooter.controller.entities.*;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.EnemyModel;
import com.drfl.twinstickshooter.model.entities.EntityModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;
import com.drfl.twinstickshooter.view.TSSView;

import java.util.ArrayList;
import java.util.Random;

public class TSSController implements ContactListener {

    /**
     * The singleton instance of this controller
     */
    private static TSSController instance;

    /**
     * User movement input in x,y.
     */
    private static Vector2 moveInput = new Vector2(0, 0);

    /**
     * User shoot input in x,y.
     */
    private static Vector2 shootInput = new Vector2(0, 0);

    /**
     * The arena width in meters.
     */
    public static final int MAP_WIDTH = 40;

    /**
     * The arena height in meters.
     */
    public static final int MAP_HEIGHT = 21;

    /**
     * Minimum time between consecutive shots in seconds
     */
    private static final float TIME_BETWEEN_SHOTS = .2f; //TODO move to weaponModel if considering weapons with different cooldowns

    /**
     * Cooldown for enemy spawning
     */
    private static final float ENEMY_SPAWN_CD = 4.0f;

    /**
     * The speed of bullets
     */
    private static final float BULLET_SPEED = 20f; //TODO move to weaponModel if using different bullet speeds

    /**
     * RNG Seed
     */
    private Random rand = new Random();

    /**
     * Current enemy spawn cooldown
     */
    private float timeToNextSpawn = ENEMY_SPAWN_CD;

    /**
     * The physics world controlled by this controller.
     */
//    private final World world;
    private World world;
    /**
     * The main character body.
     */
//    private final MainCharBody mainCharBody;
    private MainCharBody mainCharBody;

    /**
     * Enemy bodies
     */
    private ArrayList<EnemyBody> enemies = new ArrayList<EnemyBody>();

    /**
     * Accumulator used to calculate the simulation step.
     */
    private float accumulator;

    /**
     * Creates a new GameController that controls the physics of a TSSModel.
     */
    private TSSController() {

        world = new World(new Vector2(0, 0), true);

        mainCharBody = new MainCharBody(world, TSSModel.getInstance().getMainChar());

        world.setContactListener(this);
    }

    public void createTileEntities(MapLayer collisionLayer) {
        for(MapObject object : collisionLayer.getObjects()) {
            new TileEntity(world, object.getProperties().get("x", float.class),
                                object.getProperties().get("y", float.class),
                                object.getProperties().get("width", float.class),
                                object.getProperties().get("height", float.class));
        }
    }

    /**
     * Returns a singleton instance of a game controller
     *
     * @return the singleton instance
     */
    public static TSSController getInstance() {

        if (instance == null) {
            instance = new TSSController();
        }
        return instance;
    }

    public static TSSController initInstance() {
        TSSController.instance = new TSSController();
        return instance;
    }

    public void setTimeToNextSpawn(float timeToNextSpawn) {
        this.timeToNextSpawn = timeToNextSpawn;
    }

    public void spawnTestEnemy(int spawnIndex){
        if(spawnIndex != -1) {
            EnemyModel enemy = TSSModel.getInstance().createTestEnemy(spawnIndex);
            enemies.add(new EnemyBody(world, enemy));
        }
    }

    /**
     * Calculates the next physics step of duration delta (in seconds).
     *
     * @param delta The size of this physics step in seconds.
     */
    public void update(float delta) {

//        TSSModel.getInstance().update(delta); //TODO can use for bullet decay time, others

        //TODO refactor enemy/player handling

        this.spawnEnemy();
        if(timeToNextSpawn > 0) timeToNextSpawn -= delta;

        //Player movement/animation
        if(!(moveInput.x == 0 && moveInput.y == 0)) {

            entityAnimate(mainCharBody, moveInput);
            Vector2 impulse = new Vector2(mainCharBody.getMass() * moveInput.x, mainCharBody.getMass() * moveInput.y);
            mainCharBody.applyLinearImpulse(impulse.x, impulse.y, true);
        }

        //Main character shoot
        float mainCharCD = ((MainCharModel)mainCharBody.getUserData()).getTimeToNextShoot();

        if(mainCharCD <= 0) {
            ((MainCharModel)mainCharBody.getUserData()).setTimeToNextShoot(TIME_BETWEEN_SHOTS);
            this.shoot(((MainCharModel)mainCharBody.getUserData()), shootInput);
        } else {
            ((MainCharModel)mainCharBody.getUserData()).setTimeToNextShoot(mainCharCD - delta);
        }

        //Enemy movement/animation/shooting
        for(EnemyBody enemy : enemies) {

            float currCooldown = ((EnemyModel)enemy.getUserData()).getTimeToNextDirection();
            float shootCooldown = ((EnemyModel)enemy.getUserData()).getTimeToNextShoot();

            if(currCooldown > 0) {
                ((EnemyModel)enemy.getUserData()).setTimeToNextDirection(currCooldown - delta);
            } else {
                ((EnemyModel)enemy.getUserData()).resetTimeToNextDirection();
                ((EnemyModel)enemy.getUserData()).setMoveDirection(generateMovement()); //TODO make movement in direction of player
            }

            if(shootCooldown > 0) {
                ((EnemyModel)enemy.getUserData()).setTimeToNextShoot(shootCooldown - delta);
            } else {
                ((EnemyModel)enemy.getUserData()).resetTimeToNextShoot();
                ((EnemyModel)enemy.getUserData()).setShootDirection(generateShootDirection((MainCharModel)mainCharBody.getUserData(), (EnemyModel)enemy.getUserData()));
                shoot((EnemyModel)enemy.getUserData(), ((EnemyModel) enemy.getUserData()).getShootDirection());
            }

            Vector2 direction = ((EnemyModel)enemy.getUserData()).getMoveDirection();

            if(direction.x == 0 && direction.y == 0) continue;

            entityAnimate(enemy, direction);

            Vector2 impulse = new Vector2(enemy.getMass() * direction.x, enemy.getMass() * direction.y);
            enemy.applyLinearImpulse(impulse.x, impulse.y, true);
        }

        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;

        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;
        }

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if(!body.getType().equals(BodyDef.BodyType.StaticBody)) {
                verifyBounds(body);
                ((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
                ((EntityModel) body.getUserData()).setRotation(body.getAngle());
            }
        }
    }

    /**
     * Verifies if the body is inside the tile map bounds.
     *
     * @param body The body to be verified.
     */
    private void verifyBounds(Body body) { //TODO close tile maps so OOB isn't possible
        if (body.getPosition().x < 0)
            body.setTransform(MAP_WIDTH, body.getPosition().y, body.getAngle());

        if (body.getPosition().y < 0)
            body.setTransform(body.getPosition().x, MAP_HEIGHT, body.getAngle());

        if (body.getPosition().x > MAP_WIDTH)
            body.setTransform(0, body.getPosition().y, body.getAngle());

        if (body.getPosition().y > MAP_HEIGHT)
            body.setTransform(body.getPosition().x, 0, body.getAngle());
    }

    private void entityAnimate(EntityBody entity, Vector2 direction) {

        float angle = direction.angle();

        if((angle >= 0 && angle <= 45) || (angle > 315 && angle <= 360)) {
            ((EntityModel)entity.getUserData()).setDirection(EntityModel.AnimDirection.RIGHT);

        } else if(angle > 45 && angle <= 135) {
            ((EntityModel)entity.getUserData()).setDirection(EntityModel.AnimDirection.UP);

        } else if(angle > 135 && angle <= 225) {
            ((EntityModel)entity.getUserData()).setDirection(EntityModel.AnimDirection.LEFT);

        } else if(angle > 225 && angle <= 315) {
            ((EntityModel)entity.getUserData()).setDirection(EntityModel.AnimDirection.DOWN);
        }
    }

    private Vector2 generateShootDirection(MainCharModel target, EnemyModel shooter) {

        return new Vector2(target.getX() - shooter.getX(), target.getY() - shooter.getY());
    }

    /**
     * Randomly selects a 4-way direction
     *
     * @return random direction vector
     */
    private Vector2 generateMovement() {

        ArrayList<Vector2> directions = new ArrayList<Vector2>();
        directions.add(new Vector2(0, 0));
        directions.add(new Vector2(0, 1));
        directions.add(new Vector2(0, -1));
        directions.add(new Vector2(1, 0));
        directions.add(new Vector2(-1, 0));

        return directions.get(rand.nextInt(directions.size()));
    }

    /**
     * Try to spawn an enemy if cooldown seconds have passed. Can only spawn
     * if a spawner has space.
     */
    private void spawnEnemy() {

        if(timeToNextSpawn > 0) return;

        int spawnIndex = TSSModel.getInstance().createSpawnIndex();

        if(spawnIndex != -1) { //TODO remove magic value
            EnemyModel enemy = TSSModel.getInstance().createEnemy(spawnIndex);
            enemies.add(new EnemyBody(world, enemy));
        }

        timeToNextSpawn = ENEMY_SPAWN_CD;
    }

    /**
     * @param moveInput Set movement vector of main char
     */
    public void setMoveInput(Vector2 moveInput) {
        this.moveInput = moveInput;
    }

    /**
     * @param shootInput Set shoot vector of main char
     */
    public void setShootInput(Vector2 shootInput) {
        this.shootInput = shootInput.nor();
    }

    /**
     * Shoots a bullet
     */
    public void shoot(EntityModel owner, Vector2 direction) { //TODO add owner of bullet, pass bullet type if different weapons

        if(direction.x == 0 && direction.y == 0) return;

        BulletModel bullet = TSSModel.getInstance().createBullet(owner, direction);

        BulletBody body = new BulletBody(world, bullet);
        body.setLinearDamping(0);
        body.setLinearVelocity(BULLET_SPEED);
    }

    /**
     * @return The world controlled by this controller.
     */
    public World getWorld() {
        return world;
    }

    @Override
    public void beginContact(Contact contact) {

        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof EnemyModel) enemyCollide(bodyA, bodyB);
        if (bodyA.getUserData() instanceof EnemyModel && bodyB.getUserData() instanceof BulletModel) enemyCollide(bodyB, bodyA);

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof MainCharModel) mainCharCollide(bodyA, bodyB);
        if (bodyA.getUserData() instanceof MainCharModel && bodyB.getUserData() instanceof BulletModel) mainCharCollide(bodyB, bodyA);

        if (bodyA.getUserData() instanceof BulletModel) bulletCollision(bodyA);
        if (bodyB.getUserData() instanceof BulletModel) bulletCollision(bodyB);
    }

    /**
     * Bullet collided with enemy, remove bullet and hurt enemy
     */
    private void enemyCollide(Body bullet, Body enemy) {
        ((BulletModel)bullet.getUserData()).setFlaggedForRemoval(true);
        if(((BulletModel)bullet.getUserData()).getOwner().equals(EntityModel.ModelType.MAINCHAR)) {
            ((EnemyModel)enemy.getUserData()).setHurt(true);
            ((EnemyModel)enemy.getUserData()).removeHitpoints(5); //TODO remove magic value, use per bullet damage if different weapons
        }
    }

    /**
     * A bullet collided with something, remove it.
     *
     * @param bulletBody the bullet that collided
     */
    private void bulletCollision(Body bulletBody) {
        ((BulletModel)bulletBody.getUserData()).setFlaggedForRemoval(true);
    }

    private void mainCharCollide(Body bullet, Body mainChar) {

        ((BulletModel)bullet.getUserData()).setFlaggedForRemoval(true);

        if(((MainCharModel)mainChar.getUserData()).isHurt()) return;

        if(((BulletModel)bullet.getUserData()).getOwner().equals(EntityModel.ModelType.ENEMY)) {
            ((MainCharModel)mainChar.getUserData()).setHurt(true);
            ((MainCharModel)mainChar.getUserData()).removeHitpoints(5); //TODO remove magic value, use per bullet damage if different weapons
        }
    }

    /**
     * Removes objects that have been flagged for removal on the
     * previous step.
     */
    public void removeFlagged() {

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if(!body.getType().equals(BodyDef.BodyType.StaticBody)) {
                if (((EntityModel)body.getUserData()).isFlaggedToBeRemoved()) {
                    TSSModel.getInstance().remove((EntityModel) body.getUserData());
                    world.destroyBody(body);
                }
            }
        }
    }

    /**
     * Handle game entities' hitpoints
     */
    public void removeDead() {

        if(((MainCharModel)mainCharBody.getUserData()).getHitpoints() <= 0) {
            TSSView.getInstance().getGame().getStateM().processState(TSSState.GameEvent.MC_DIED);
        }

        for(int i = enemies.size() - 1; i >= 0; i--) {
            if(((EnemyModel)enemies.get(i).getUserData()).getHitpoints() <= 0) {
                TSSModel.getInstance().removeEnemy(i);
                world.destroyBody(enemies.get(i).getBody());
                enemies.remove(i);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public static void setInstance(TSSController instance) {
        TSSController.instance = instance;
    }
}