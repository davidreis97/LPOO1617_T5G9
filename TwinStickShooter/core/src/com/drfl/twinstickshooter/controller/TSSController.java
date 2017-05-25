package com.drfl.twinstickshooter.controller;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.controller.entities.BulletBody;
import com.drfl.twinstickshooter.controller.entities.MainCharBody;
import com.drfl.twinstickshooter.controller.entities.TileEntity;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.BulletModel;
import com.drfl.twinstickshooter.model.entities.EntityModel;

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
     * The speed of bullets
     */
    private static final float BULLET_SPEED = 20f; //TODO move to weaponModel if using different bullet speeds

    /**
     * Time left until gun cools down
     */
    private float timeToNextShoot = 0;

    /**
     * The physics world controlled by this controller.
     */
    private final World world;

    /**
     * The main character body.
     */
    private final MainCharBody mainCharBody;

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
        if (instance == null)
            instance = new TSSController();
        return instance;
    }

    /**
     * Calculates the next physics step of duration delta (in seconds).
     *
     * @param delta The size of this physics step in seconds.
     */
    public void update(float delta) {

//        TSSModel.getInstance().update(delta); //TODO can use for bullets

        timeToNextShoot -= delta;

        Vector2 velChange = new Vector2(moveInput.x, moveInput.y);
        Vector2 impulse = new Vector2(mainCharBody.getMass() * velChange.x, mainCharBody.getMass() * velChange.y);
        mainCharBody.applyLinearImpulse(impulse.x, impulse.y, true);

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
                ((EntityModel) body.getUserData()).setRotation(body.getAngle()); //TODO model sprite rotation unused
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
     * Sets main character rotation.
     */
    public void setRotation(float angle) {

        mainCharBody.setTransform(mainCharBody.getX(), mainCharBody.getY(), angle);
    }

    /**
     * Accelerates the main character.
     *
     * @param delta Duration of the rotation in seconds.
     */
//    public void accelerate(float delta) {
//
//        mainCharBody.setLinearVelocity(moveInput.x, moveInput.y);
//        mainCharBody.applyLinearImpulse(-(float) Math.sin(mainCharBody.getAngle()) * ACCEL_IMPULSE, (float) Math.cos(mainCharBody.getAngle()) * ACCEL_IMPULSE, true);
//        //mainCharBody.applyForceToCenter(-(float) Math.sin(mainCharBody.getAngle()) * ACCEL_IMPULSE * delta, (float) Math.cos(mainCharBody.getAngle()) * ACCEL_IMPULSE * delta, true);
//       // ((MainCharModel)mainCharBody.getUserData()).setAccelerating(true); //TODO use for animating
//    }

    /**
     * Shoots a bullet
     */
    public void shoot() { //TODO add owner of bullet, pass bullet type if different weapons

        if(this.shootInput.x == 0 && this.shootInput.y == 0) return;

        if (timeToNextShoot < 0) {

            BulletModel bullet = TSSModel.getInstance().createBullet(TSSModel.getInstance().getMainChar(), this.shootInput);

            BulletBody body = new BulletBody(world, bullet);
            body.setLinearDamping(0);
            body.setLinearVelocity(BULLET_SPEED);

            timeToNextShoot = TIME_BETWEEN_SHOTS;
        }
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

        if (bodyA.getUserData() instanceof BulletModel)
            bulletCollision(bodyA);
        if (bodyB.getUserData() instanceof BulletModel)
            bulletCollision(bodyB);
    }

    /**
     * A bullet collided with something, remove it.
     *
     * @param bulletBody the bullet that collided
     */
    private void bulletCollision(Body bulletBody) {
        ((BulletModel)bulletBody.getUserData()).setFlaggedForRemoval(true);
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