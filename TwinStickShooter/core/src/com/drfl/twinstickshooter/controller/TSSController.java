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
import com.drfl.twinstickshooter.view.TSSView;

import java.util.ArrayList;
import java.util.Random;

public class TSSController implements ContactListener {

    /**
     * The singleton instance of this controller
     */
    private static TSSController instance;

    /**
     * The arena width in meters.
     */
    public static final int MAP_WIDTH = 40;

    /**
     * The arena height in meters.
     */
    public static final int MAP_HEIGHT = 21;

    /**
     * Cooldown for enemy spawning
     */
    private static final float SPAWN_MAX_COOL = 4.0f;

    /**
     * The speed of bullets
     */
    private static final float BULLET_SPEED = 20f; //NOTEME move to weaponModel if using different bullet speeds

    /**
     * RNG Seed
     */
    private Random rand = new Random();

    /**
     * Current enemy spawn cooldown
     */
    private float timeToNextSpawn = SPAWN_MAX_COOL;

    /**
     * The physics world controlled by this controller.
     */
    private final World world;

    /**
     * The main character body.
     */
    private final MainCharBody mainCharBody;

    /**
     * Enemy bodies
     */
    private ArrayList<EnemyBody> enemies = new ArrayList<EnemyBody>();

    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    /**
     * Accumulator used to calculate the simulation step.
     */
    private float accumulator;

    /**
     * Creates a new GameController that controls the physics of a TSSModel.
     */
    private TSSController(TSSGame game) {

        world = new World(new Vector2(0, 0), true);

        mainCharBody = new MainCharBody(world, TSSModel.getInstance().getMainChar());

        world.setContactListener(this);

        this.game = game;
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
            return null;
        }
        return instance;
    }

    public static TSSController initInstance(TSSGame game) {
        TSSController.instance = new TSSController(game);
        return instance;
    }

    public void spawnTestEnemy(int spawnIndex) {

        if(spawnIndex != -1) {
            EnemyModel enemy = TSSModel.getInstance().createTestEnemy(spawnIndex);
            enemies.add(new EnemyBody(world, enemy));
        }
    }

    //NOTEME javadoc ready
    /**
     * Moves an entity by applying a linear impulse. Also calls animation direction handling.
     *
     * @param entity the entity body affected
     * @param direction 2D movement vector
     */
    private void doMove(EntityBody entity, Vector2 direction) {

        if(direction.x == 0 && direction.y == 0) return;
        entityAnimateDirection(entity, direction);
        entity.applyLinearImpulse(entity.getMass() * direction.x, entity.getMass() * direction.y, true);
    }

    //NOTEME javadoc ready
    /**
     * Tries to shoot by testing the shot cooldown, returns whether it was able to shoot.
     *
     * @param model entity model data
     * @param delta how many seconds to decrement cooldown
     * @return whether a shot happened
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

    //NOTEME javadoc ready
    /**
     * Tries to change enemy direction by testing move cooldown.
     *
     * @param model enemy model data
     * @param delta how many seconds to decrement cooldown
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
            moveDir = generateMovement();
        } while(model.getOppositeDirection() == moveDir);

        model.setMoveDirection(moveDir);
    }

    //NOTEME javadoc ready
    /** Plays the sound. If the sound is already playing, it will be played again, concurrently.
     *
     * @param sound the sound to play
     * @param volume the volume in the range [0,1]
     * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * */
    private void playSFX(Sound sound, float volume, float pitch, float pan) {
        sound.play(volume, pitch, pan);
    }

    //NOTEME javadoc ready
    /**
     * Handles all player logic. Moves the player and then tries to shoot a bullet.
     * Plays sound effect if bullet shot.
     *
     * @param delta how many seconds to decrement player cooldowns
     */
    private void playerDoLogic(float delta) {

        doMove(mainCharBody, ((MainCharModel) mainCharBody.getUserData()).getMoveDirection());

        if(tryShoot((MainCharModel) mainCharBody.getUserData(), delta)) {
            if(game.getSoundVolume() > 0 ) playSFX(((Sound) game.getAssetManager().get("Shoot.mp3")), game.getSoundVolume(), 1.0f, 0); //TODO magic value
        }
    }

    //NOTEME javadoc ready
    /**
     * Handles all enemy logic. For each enemy tries to change current direction,
     * moves the enemy and then tries to shoot in the player's direction. Plays sound effect
     * if bullet shot.
     *
     * @param delta how many seconds to decrement enemy's cooldowns
     */
    private void enemiesDoLogic(float delta) {

        for(EnemyBody enemy : enemies) {

            enemyTryChangeDirection((EnemyModel) enemy.getUserData(), delta);

            doMove(enemy, ((EnemyModel)enemy.getUserData()).getMoveDirection());

            ((EnemyModel)enemy.getUserData()).setShootDirection(generateShootDirection((MainCharModel)mainCharBody.getUserData(), (EnemyModel)enemy.getUserData()));

            if(tryShoot((EnemyModel) enemy.getUserData(), delta)) {
                if(game.getSoundVolume() > 0) playSFX((Sound) game.getAssetManager().get("Shoot.mp3"), game.getSoundVolume(), 1.6f, 0); //TODO magic value
            }
        }
    }

    //NOTEME javadoc ready
    /**
     * Try to spawn an enemy if cooldown is <= 0. Can only spawn if
     * a spawner has free space.
     *
     * @param delta how many seconds to decrement cooldown
     */
    private void trySpawnEnemy(float delta) {

        if(this.timeToNextSpawn > 0) {
            timeToNextSpawn -= delta;
            return;
        }

        int spawnIndex = TSSModel.getInstance().createSpawnIndex();

        if(spawnIndex != -1) { //TODO remove magic value
            EnemyModel enemy = TSSModel.getInstance().createEnemy(spawnIndex);
            enemies.add(new EnemyBody(world, enemy));
        }

        timeToNextSpawn = SPAWN_MAX_COOL;
    }

    //NOTEME javadoc ready
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

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            if(!body.getType().equals(BodyDef.BodyType.StaticBody)) {
                this.verifyBounds(body);
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

    private void entityAnimateDirection(EntityBody entity, Vector2 direction) {

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
     * Shoots a bullet
     */
    public void shoot(EntityModel owner, Vector2 direction) { //NOTEME pass bullet type if different weapons

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

        if (bodyA.getUserData() instanceof TileEntity && bodyB.getUserData() instanceof EnemyModel) enemyWall(contact.getFixtureB().getBody());
        if (bodyA.getUserData() instanceof EnemyModel && bodyB.getUserData() instanceof TileEntity) enemyWall(contact.getFixtureA().getBody());

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof EnemyModel) enemyCollide(bodyA, bodyB);
        if (bodyA.getUserData() instanceof EnemyModel && bodyB.getUserData() instanceof BulletModel) enemyCollide(bodyB, bodyA);

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof MainCharModel) mainCharCollide(bodyA, bodyB);
        if (bodyA.getUserData() instanceof MainCharModel && bodyB.getUserData() instanceof BulletModel) mainCharCollide(bodyB, bodyA);

        if (bodyA.getUserData() instanceof BulletModel) bulletCollision(bodyA);
        if (bodyB.getUserData() instanceof BulletModel) bulletCollision(bodyB);
    }

    private void enemyWall(Body enemyBody) {
        ((EnemyModel)enemyBody.getUserData()).setTimeToNextDirection(0);
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

        Vector2 knockback = ((BulletModel)bullet.getUserData()).getBulletDirection();

        Vector2 impulse = new Vector2(mainCharBody.getMass() * knockback.x, mainCharBody.getMass() * knockback.y);
        mainCharBody.applyLinearImpulse(impulse.x, impulse.y, true);

        if(((BulletModel)bullet.getUserData()).getOwner().equals(EntityModel.ModelType.ENEMY)) {
            ((MainCharModel)mainChar.getUserData()).setHurt(true);
            ((MainCharModel)mainChar.getUserData()).removeHitpoints(5); //TODO remove magic value, use per bullet damage if different weapons
            if(game.getSoundVolume() != 0)
            ((Sound)TSSView.getInstance().getGame().getAssetManager().get("Hurt.mp3")).play(game.getSoundVolume());
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
            ((Sound) TSSView.getInstance().getGame().getAssetManager().get("Shoot.mp3")).stop();
            ((Sound) TSSView.getInstance().getGame().getAssetManager().get("Hurt.mp3")).stop();
            ((MainCharModel)mainCharBody.getUserData()).setDead(true);
            return;
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
}