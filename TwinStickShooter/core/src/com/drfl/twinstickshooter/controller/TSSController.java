package com.drfl.twinstickshooter.controller;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.controller.entities.*;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.EnemyModel;
import com.drfl.twinstickshooter.model.entities.EntityModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;

import java.util.ArrayList;

/**
 * MVC Controller, handles Box2D physics for the whole game.
 */
public class TSSController implements ContactListener {

    //NOTEME javadoc
    /**
     * The singleton instance of the controller.
     */
    private static TSSController instance;

    //NOTEME javadoc
    /**
     * The tile map width in meters.
     */
    private static final int MAP_WIDTH = 40;

    //NOTEME javadoc
    /**
     * The tile map height in meters.
     */
    private static final int MAP_HEIGHT = 21;

    //NOTEME javadoc
    /**
     * The speed of bullets for all entities.
     */
    private static final float BULLET_SPEED = 20f;

    //NOTEME javadoc
    /**
     * Regular pitch for playing sounds
     */
    private static final float NORMAL_PITCH = 1.0f;

    //NOTEME javadoc
    /**
     * Enemy bullet SFX pitch
     */
    private static final float ENEMY_BULLET_PITCH = 1.6f;

    //NOTEME javadoc
    /**
     * Bullet damage
     */
    private static int BULLET_DAMAGE = 5;

    //NOTEME javadoc
    /**
     * Max cooldown for enemy spawning.
     */
    private static final float SPAWN_MAX_COOL = 4.0f;

    //NOTEME javadoc
    /**
     * Current enemy spawning cooldown.
     */
    private float timeToNextSpawn = SPAWN_MAX_COOL;

    //NOTEME javadoc
    /**
     * The physics world controlled by this controller.
     */
    private final World world;

    //NOTEME javadoc
    /**
     * The main character body.
     */
    private final MainCharBody mainCharBody;

    //NOTEME javadoc
    /**
     * Enemy bodies.
     */
    private ArrayList<EnemyBody> enemies = new ArrayList<>();

    //NOTEME javadoc
    /**
     * The game associated with this controller.
     */
    private final TSSGame game;

    //NOTEME javadoc
    /**
     * Accumulator used to calculate the simulation step.
     */
    private float accumulator;

    //NOTEME javadoc
    /**
     * Flags whether an enemy has been spawned recently, reset by accessing it through getter method.
     */
    private boolean spawned = false;

    //NOTEME javadoc
    /**
     * Creates a new GameController that controls the physics of a TSSModel.
     *
     * @param game The game associated with this controller
     */
    private TSSController(TSSGame game) {

        world = new World(new Vector2(0, 0), true);

        mainCharBody = new MainCharBody(world, TSSModel.getInstance().getMainChar());

        world.setContactListener(this);

        this.game = game;
    }

    //NOTEME javadoc
    /**
     * Returns a singleton instance of controller, instance must
     * be initiated by a call to initInstance beforehand. Null is returned if not.
     *
     * @return The singleton instance
     */
    public static TSSController getInstance() {
        return instance;
    }

    //NOTEME javadoc
    /**
     * Initializes a controller instance, associating it with a game.
     *
     * @param game The game associated with this controller
     * @return The singleton instance
     */
    public static TSSController initInstance(TSSGame game) {
        TSSController.instance = new TSSController(game);
        return instance;
    }

    //NOTEME javadoc
    /**
     * Creates tile entities from a collision map layer loaded from a Tiled map.
     *
     * @param collisionLayer The collision layer of the Tiled map
     */
    public void createTileEntities(MapLayer collisionLayer) {
        for(MapObject object : collisionLayer.getObjects()) {
            new TileEntity(world, new Vector2(object.getProperties().get("x", float.class),
                                object.getProperties().get("y", float.class)),
                                object.getProperties().get("width", float.class),
                                object.getProperties().get("height", float.class));
        }
    }

    //NOTEME javadoc
    /**
     * Moves an entity by applying a linear impulse. Also calls animation direction handling.
     *
     * @param entity The entity body affected
     * @param direction The 2D movement vector
     */
    private void doMove(EntityBody entity, Vector2 direction) {

        if(direction.x == 0 && direction.y == 0) return;
        entityAnimateDirection((EntityModel) entity.getUserData(), direction.angle());
        entity.applyLinearImpulse(new Vector2(entity.getMass() * direction.x, entity.getMass() * direction.y), true);
    }

    //NOTEME javadoc
    /**
     * Tries to shoot by testing the shot cooldown, returns whether it was able to shoot.
     *
     * @param model The entity model data
     * @param delta How many seconds to decrement cooldown
     * @return Whether a shot happened
     */
    private boolean tryShoot(EntityModel model, float delta) {

        float shootCooldown = model.getTimeToNextShoot();

        if(shootCooldown > 0) {
            model.setTimeToNextShoot(shootCooldown - delta);
            return false;
        }

        if(model.getShootDirection().x == 0 && model.getShootDirection().y == 0) return false;

        model.setTimeToNextShoot(model.getShootCooldown());
        this.shoot(model, model.getShootDirection());
        return true;
    }

    //NOTEME javadoc
    /**
     * Tries to change enemy direction by testing move cooldown.
     *
     * @param model The enemy model data
     * @param delta How many seconds to decrement cooldown
     */
    private void enemyTryChangeDirection(EnemyModel model, float delta) {

        float cooldown = model.getTimeToNextDirection();

        if(cooldown > 0) {
            model.setTimeToNextDirection(cooldown - delta);
            return;
        }

        model.resetTimeToNextDirection();

        Vector2 moveDir;
        do {
            moveDir = model.generateMovement();
        } while(model.getOppositeDirection() == moveDir);

        model.setMoveDirection(moveDir);
    }

    //NOTEME javadoc
    /** Plays the sound. If the sound is already playing, it will be played again, concurrently.
     *
     * @param sound The sound to play
     * @param volume The volume in the range [0,1]
     * @param pitch The pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     * @param pan Panning in the range -1 (full left) to 1 (full right). 0 is center position
     * */
    private void playSFX(Sound sound, float volume, float pitch, float pan) {
        sound.play(volume, pitch, pan);
    }

    //NOTEME javadoc
    /**
     * Handles all player logic. Moves the player and then tries to shoot a bullet.
     * Plays sound effect if bullet shot.
     *
     * @param delta How many seconds to decrement player cooldowns
     */
    private void playerDoLogic(float delta) {

        doMove(mainCharBody, ((MainCharModel) mainCharBody.getUserData()).getMoveDirection());

        if(tryShoot((MainCharModel) mainCharBody.getUserData(), delta)) {
            if(game.getSoundVolume() > 0 ) playSFX(((Sound) game.getAssetManager().get("Shoot.mp3")), game.getSoundVolume(), NORMAL_PITCH, 0);
        }
    }

    //NOTEME javadoc
    /**
     * Handles all enemy logic. For each enemy tries to change current direction,
     * moves the enemy and then tries to shoot in the player's direction. Plays sound effect
     * if bullet shot.
     *
     * @param delta How many seconds to decrement enemy's cooldowns
     */
    private void enemiesDoLogic(float delta) {

        for(EnemyBody enemy : enemies) {

            enemyTryChangeDirection((EnemyModel) enemy.getUserData(), delta);

            doMove(enemy, ((EnemyModel)enemy.getUserData()).getMoveDirection());

            ((EnemyModel)enemy.getUserData()).setShootDirection(shootToPlayer((MainCharModel)mainCharBody.getUserData(), (EnemyModel)enemy.getUserData()));

            if(tryShoot((EnemyModel) enemy.getUserData(), delta)) {
                if(game.getSoundVolume() > 0) playSFX((Sound) game.getAssetManager().get("Shoot.mp3"), game.getSoundVolume(), ENEMY_BULLET_PITCH, 0);
            }
        }
    }

    //NOTEME javadoc
    /**
     * Try to spawn an enemy if cooldown is <= 0. Can only spawn if
     * a spawner has free space.
     *
     * @param delta How many seconds to decrement cooldown
     */
    private void trySpawnEnemy(float delta) {

        if(this.timeToNextSpawn > 0) {
            timeToNextSpawn -= delta;
            return;
        }

        int spawnIndex = TSSModel.getInstance().searchSpawnIndex();

        if(spawnIndex != -1) {

            EnemyModel enemy = TSSModel.getInstance().createEnemy(spawnIndex);
            this.spawned = true;
            enemies.add(new EnemyBody(world, enemy));
        }

        timeToNextSpawn = SPAWN_MAX_COOL;
    }

    //NOTEME javadoc
    /**
     * Updates entity models to correspond to current body position and angle.
     * Checks if bodies are inside game area before updating.
     *
     * @param bodies The array of body entities
     */
    private void updateBodies(Array<Body> bodies) {

        for (Body body : bodies) {

            if(body.getType().equals(BodyDef.BodyType.StaticBody)) continue;

            this.verifyBounds(body);
            ((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
            ((EntityModel) body.getUserData()).setRotation(body.getAngle());
        }
    }

    //NOTEME javadoc
    /**
     * Calculates the next physics step of duration delta (in seconds).
     *
     * @param delta The size of this physics step in seconds
     */
    public void update(float delta) {

        this.playerDoLogic(delta);
        this.enemiesDoLogic(delta);

        this.trySpawnEnemy(delta);

        accumulator += Math.min(delta, 0.25f);

        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;
        }

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        this.updateBodies(bodies);
    }

    //NOTEME javadoc
    /**
     * Verifies if the body is inside the Tiled map bounds.
     *
     * @param body The body to be verified
     */
    private void verifyBounds(Body body) {

        if (body.getPosition().x < 0)
            body.setTransform(MAP_WIDTH, body.getPosition().y, body.getAngle());

        if (body.getPosition().y < 0)
            body.setTransform(body.getPosition().x, MAP_HEIGHT, body.getAngle());

        if (body.getPosition().x > MAP_WIDTH)
            body.setTransform(0, body.getPosition().y, body.getAngle());

        if (body.getPosition().y > MAP_HEIGHT)
            body.setTransform(body.getPosition().x, 0, body.getAngle());
    }

    //NOTEME javadoc
    /**
     * Sets animation direction for an entity model based on its heading.
     *
     * @param model The entity model
     * @param angle The angle of entity's heading
     */
    private void entityAnimateDirection(EntityModel model, float angle) {

        if((angle >= 0 && angle <= 45) || (angle > 315 && angle <= 360)) {
            model.setDirection(EntityModel.AnimDirection.RIGHT);

        } else if(angle > 45 && angle <= 135) {
            model.setDirection(EntityModel.AnimDirection.UP);

        } else if(angle > 135 && angle <= 225) {
            model.setDirection(EntityModel.AnimDirection.LEFT);

        } else if(angle > 225 && angle <= 315) {
            model.setDirection(EntityModel.AnimDirection.DOWN);
        }
    }

    //NOTEME javadoc
    /**
     * Returns a vector starting at enemy shooter's position
     * and ending in player's current position.
     *
     * @param target The player
     * @param shooter The enemy shooter
     * @return The vector between the two entities
     */
    private Vector2 shootToPlayer(MainCharModel target, EnemyModel shooter) {
        return new Vector2(target.getPosition().x - shooter.getPosition().x, target.getPosition().y - shooter.getPosition().y);
    }

    //NOTEME javadoc
    /**
     * Shoots a bullet by creating its model and body with specified direction and owner.
     *
     * @param owner The entity who owns the bullet
     * @param direction The direction vector of the bullet
     */
    public void shoot(EntityModel owner, Vector2 direction) {

        BulletModel bullet = TSSModel.getInstance().createBullet(owner, direction);

        BulletBody body = new BulletBody(world, bullet);
        body.setLinearDamping(0);
        body.setLinearVelocity(BULLET_SPEED);
    }

    //NOTEME javadoc
    /**
     * Called when two fixtures begin to touch. Handles specific
     * collision resolution.
     *
     * @param contact Contact between two shapes
     */
    @Override
    public void beginContact(Contact contact) {

        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof TileEntity && bodyB.getUserData() instanceof EnemyModel) enemyWall((EnemyModel) contact.getFixtureB().getBody().getUserData());
        if (bodyA.getUserData() instanceof EnemyModel && bodyB.getUserData() instanceof TileEntity) enemyWall((EnemyModel) contact.getFixtureA().getBody().getUserData());

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof EnemyModel) enemyCollide((BulletModel) bodyA.getUserData(), (EnemyModel) bodyB.getUserData());
        if (bodyA.getUserData() instanceof EnemyModel && bodyB.getUserData() instanceof BulletModel) enemyCollide((BulletModel) bodyB.getUserData(), (EnemyModel) bodyA.getUserData());

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof MainCharModel) mainCharCollide((BulletModel) bodyA.getUserData(), bodyB);
        if (bodyA.getUserData() instanceof MainCharModel && bodyB.getUserData() instanceof BulletModel) mainCharCollide((BulletModel) bodyB.getUserData(), bodyA);

        if (bodyA.getUserData() instanceof BulletModel) ((BulletModel) bodyA.getUserData()).setFlaggedForRemoval(true);
        if (bodyB.getUserData() instanceof BulletModel) ((BulletModel) bodyB.getUserData()).setFlaggedForRemoval(true);
    }

    //NOTEME javadoc
    /**
     * Set enemy's time to next direction to 0 since it hit a wall.
     *
     * @param enemy The enemy model
     */
    private void enemyWall(EnemyModel enemy) {
        enemy.setTimeToNextDirection(0);
    }

    //NOTEME javadoc
    /**
     * Bullet collided with enemy, flag bullet for removal and hurt enemy.
     *
     * @param bullet The bullet model that hit the enemy
     * @param enemy The enemy model hit by bullet
     */
    private void enemyCollide(BulletModel bullet, EnemyModel enemy) {

        bullet.setFlaggedForRemoval(true);

        if(bullet.getOwner().equals(EntityModel.ModelType.MAINCHAR)) {
            enemy.setHurt(true);
            enemy.removeHitpoints(BULLET_DAMAGE);
        }
    }

    //NOTEME javadoc
    /**
     * Bullet collided with main char, flag bullet for removal, hurt main char and apply knockback.
     * If main char is flagged as hurt nothing happens as it has invincibility frames.
     *
     * @param bullet The bullet model that hit main char
     * @param mainChar The main char hit by bullet
     */
    private void mainCharCollide(BulletModel bullet, Body mainChar) {

        bullet.setFlaggedForRemoval(true);

        if(((MainCharModel)mainChar.getUserData()).isHurt()) return;

        doMove(mainCharBody, bullet.getBulletDirection()); //Knockback

        if(bullet.getOwner().equals(EntityModel.ModelType.ENEMY)) {

            ((MainCharModel)mainChar.getUserData()).setHurt(true);
            ((MainCharModel)mainChar.getUserData()).removeHitpoints(BULLET_DAMAGE);
            if(game.getSoundVolume() > 0) playSFX((Sound) game.getAssetManager().get("Hurt.mp3"), game.getSoundVolume(), 1.0f, 0);
        }
    }

    //NOTEME javadoc
    /**
     * Removes bullet entities flagged for removal by destroying their Box2D body and removing them from the model.
     */
    public void removeFlaggedBullets() {

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body body : bodies) {

            if(!(body.getUserData() instanceof BulletModel)) continue;

            if (((BulletModel)body.getUserData()).isFlaggedToBeRemoved()) {
                TSSModel.getInstance().removeBullet((BulletModel) body.getUserData());
                world.destroyBody(body);
            }
        }
    }

    //NOTEME javadoc
    /**
     * Stops all sound effects.
     */
    private void stopSFX() {

        ((Sound) game.getAssetManager().get("Shoot.mp3")).stop();
        ((Sound) game.getAssetManager().get("Hurt.mp3")).stop();
    }

    //NOTEME javadoc
    /**
     * Removes entities whose HP is <= 0. For main char sets dead flag,
     * for enemies removes them from model and destroys Box2D body.
     */
    public Array<Integer> removeDead() {

        Array<Integer> deadIndex = new Array<>();

        if(((MainCharModel)mainCharBody.getUserData()).getHitpoints() <= 0) {
            stopSFX();
            ((MainCharModel)mainCharBody.getUserData()).setDead(true);
            return deadIndex;
        }

        for(int i = enemies.size() - 1; i >= 0; i--) {

            if(((EnemyModel)enemies.get(i).getUserData()).getHitpoints() <= 0) {

                TSSModel.getInstance().removeEnemy(i);
                deadIndex.add(i);
                world.destroyBody(enemies.get(i).getBody());
                enemies.remove(i);
            }
        }

        return deadIndex;
    }

    //NOTEME javadoc
    /**
     * @return The world controlled by this controller
     */
    public World getWorld() {
        return this.world;
    }

    //NOTEME javadoc
    /**
     * @return Enemy spawn flag and resets it
     */
    public boolean isSpawned() {

        if(this.spawned) {
            this.spawned = false;
            return true;
        } else return false;
    }

    //NOTEME javadoc
    /**
     * @return Enemy spawn max cooldown in seconds
     */
    public static float getSpawnMaxCool() {
        return SPAWN_MAX_COOL;
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
}