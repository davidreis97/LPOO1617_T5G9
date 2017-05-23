package com.drfl.twinstickshooter.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.controller.entities.MainCharBody;
import com.drfl.twinstickshooter.model.TSSModel;
import com.drfl.twinstickshooter.model.entities.EntityModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;

public class TSSController implements ContactListener {

    /**
     * The singleton instance of this controller
     */
    private static TSSController instance;

    /**
     * User movement input in x,y.
     */
    private static Vector2 move_input = new Vector2(0, 0);

    /**
     * The arena width in meters.
     */
    public static final int MAP_WIDTH = 40;

    /**
     * The arena height in meters.
     */
    public static final int MAP_HEIGHT = 21;

    /**
     * The acceleration impulse in newtons.
     */
    private static final float ACCEL_IMPULSE = 1f;

    /**
     * The physics world controlled by this controller.
     */
    private final World world;

    /**
     * The main character body.
     */
    private final MainCharBody mainCharBody;
    private final MainCharBody mainCharBody2; //TODO temp

    /**
     * Accumulator used to calculate the simulation step.
     */
    private float accumulator;

    /**
     * Creates a new GameController that controls the physics of a TSSModel.
     *
     */
    private TSSController() {
        world = new World(new Vector2(0, 0), true);

        mainCharBody = new MainCharBody(world, TSSModel.getInstance().getMainChar());
        TSSModel.getInstance().getMainChar().setPosition(5.0f - 0.5f, 5.0f + 0.5f);
        mainCharBody2 = new MainCharBody(world, TSSModel.getInstance().getMainChar());
        TSSModel.getInstance().getMainChar().setPosition(2.0f - 0.5f, 2.0f + 0.5f);

        world.setContactListener(this);
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

//        timeToNextShoot -= delta;
//        Vector2 currVel = mainCharBody.getLinearVelocity();
//        mainCharBody.setLinearVelocity(currVel.x + move_input.x, currVel.y + move_input.y);
//        mainCharBody.setLinearVelocity(2 * move_input.x, 2 * move_input.y); //TODO take current velocity into account?

        //Vector2 currVel = mainCharBody.getLinearVelocity();
        //Vector2 velChange = new Vector2(currVel.x - move_input.x, currVel.y - move_input.y);
        Vector2 velChange = new Vector2(move_input.x, move_input.y);
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
            verifyBounds(body);
            ((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
            //((EntityModel) body.getUserData()).setRotation(body.getAngle()); //TODO model sprite rotation unused
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
     * @param x The x-component of the input
     * @param y The y-component of the input
     */
    public void setMovement(float x, float y) {
        move_input = new Vector2(x, y);
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
    public void accelerate(float delta) {

        mainCharBody.setLinearVelocity(move_input.x, move_input.y);
        mainCharBody.applyLinearImpulse(-(float) Math.sin(mainCharBody.getAngle()) * ACCEL_IMPULSE, (float) Math.cos(mainCharBody.getAngle()) * ACCEL_IMPULSE, true);
        //mainCharBody.applyForceToCenter(-(float) Math.sin(mainCharBody.getAngle()) * ACCEL_IMPULSE * delta, (float) Math.cos(mainCharBody.getAngle()) * ACCEL_IMPULSE * delta, true);
       // ((MainCharModel)mainCharBody.getUserData()).setAccelerating(true); //TODO use for animating
    }

//    /**
//     * Shoots a bullet from the spaceship at 10m/s
//     */
//    public void shoot() {
//        if (timeToNextShoot < 0) {
//            BulletModel bullet = GameModel.getInstance().createBullet(GameModel.getInstance().getShip());
//            BulletBody body = new BulletBody(world, bullet);
//            body.setLinearVelocity(BULLET_SPEED);
//            timeToNextShoot = TIME_BETWEEN_SHOTS;
//        }
//    }

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

//        if (bodyA.getUserData() instanceof BulletModel)
//            bulletCollision(bodyA);
//        if (bodyB.getUserData() instanceof BulletModel)
//            bulletCollision(bodyB);
//
//        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof AsteroidModel)
//            bulletAsteroidCollision(bodyA, bodyB);
//        if (bodyA.getUserData() instanceof AsteroidModel && bodyB.getUserData() instanceof BulletModel)
//            bulletAsteroidCollision(bodyB, bodyA);
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