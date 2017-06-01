package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;

/**
 * Abstract class that represents an abstract Box2D body.
 */
public abstract class EntityBody {

    final static short MAINCHAR_BODY = 0x0001;
    final static short BULLET_BODY = 0x0002;
    final static short TILE_ENTITY = 0x0004;
    final static short ENEMY_BODY = 0x0008;
    final static short COLLIDE_ALL = (MAINCHAR_BODY | BULLET_BODY | TILE_ENTITY | ENEMY_BODY);

    /**
     * The Box2D body that supports this body.
     */
    final Body body;

    /**
     * Constructs a body representing a model in a certain world.
     *
     * @param world The world this body lives on.
     * @param model The model representing the body.
     */
    EntityBody(World world, EntityModel model) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(model.getX(), model.getY());
        bodyDef.fixedRotation = true; //TODO some objects might rotate?
        bodyDef.angle = model.getRotation();
        bodyDef.linearDamping = 5f;

        body = world.createBody(bodyDef);
        body.setUserData(model);
    }

    /**
     * Constructs a body representing a model in a certain world.
     *
     * @param world The world this body lives on.
     */
    EntityBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
    }

    /**
     * Helper method to create a polygon fixture represented by a set of vertexes.
     * @param body The body the fixture is to be attached to.
     * @param vertexes The vertexes defining the fixture in pixels so it is
     *                 easier to get them from a bitmap image.
     * @param width The width of the bitmap the vertexes where extracted from.
     * @param height The height of the bitmap the vertexes where extracted from.
     * @param density The density of the fixture. How heavy it is in relation to its area.
     * @param friction The friction of the fixture. How slippery it is.
     * @param restitution The restitution of the fixture. How much it bounces.
     * @param category
     * @param mask
     */
    final void createFixture(Body body, float[] vertexes, int width, int height, float density, float friction, float restitution, short category, short mask) {
        // Transform pixels into meters, center and invert the y-coordinate
        for (int i = 0; i < vertexes.length; i++) {
            if (i % 2 == 0) vertexes[i] -= width / 2;   // center the vertex x-coordinate
            if (i % 2 != 0) vertexes[i] -= height / 2;  // center the vertex y-coordinate

            if (i % 2 != 0) vertexes[i] *= -1;          // invert the y-coordinate

            vertexes[i] *= PIXEL_TO_METER;              // scale from pixel to meter
        }

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertexes);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;

        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;

        body.createFixture(fixtureDef);

        polygon.dispose();
    }

    /**
     * Wraps the getX method from the Box2D body class.
     *
     * @return the x-coordinate of this body.
     */
    public float getX() {
        return body.getPosition().x;
    }

    /**
     * Wraps the getY method from the Box2D body class.
     *
     * @return the y-coordinate of this body.
     */
    public float getY() {
        return body.getPosition().y;
    }

    /**
     * Wraps the getAngle method from the Box2D body class.
     *
     * @return the angle of rotation of this body.
     */
    public float getAngle() {
        return body.getAngle();
    }

    /**
     * Get the total mass of the body.
     *
     * @return The mass, usually in kilograms (kg).
     */
    public float getMass() {
        return body.getMass();
    }

    /**
     * Wraps the setTransform method from the Box2D body class.
     *
     * @param x the new x-coordinate for this body
     * @param y the new y-coordinate for this body
     * @param angle the new rotation angle for this body
     */
    public void setTransform(float x, float y, float angle) {
        body.setTransform(x, y, angle);
    }

    /**
     * Sets the angular velocity of this object in the direction it is rotated.
     *
     * @param velocity the new linear velocity angle for this body
     */
    public void setLinearVelocity(float velocity) {
        body.setLinearVelocity((float)(velocity * -Math.sin(getAngle())), (float) (velocity * Math.cos(getAngle())));
    }

    /**
     * Set the linear damping of the body.
     */
    public void setLinearDamping(float damping) {
        body.setLinearDamping(damping);
    }

//    /**
//     * Wraps the setAngularVelocity method from the Box2D body class.
//     *
//     * @param omega the new angular velocity angle for this body
//     */
//    public void setAngularVelocity(float omega) {
//        body.setAngularVelocity(omega);
//    }

    /**
     * Wraps the applyForceToCenter method from the Box2D body class.
     *
     * @param forceX the x-component of the force to be applied
     * @param forceY the y-component of the force to be applied
     * @param awake should the body be awaken
     */
    public void applyForceToCenter(float forceX, float forceY, boolean awake) {

        body.applyForceToCenter(forceX, forceY, awake);
    }

    public void applyLinearImpulse(float impulseX, float impulseY, boolean awake) {

        Vector2 center = body.getWorldCenter();
        Vector2 impulse = new Vector2(impulseX, impulseY);
        body.applyLinearImpulse(impulse, center, awake);
    }


    /**
     * @param velX the x-component of the velocity
     * @param velY the y-component of the velocity
     */
    public void setLinearVelocity(float velX, float velY) {

        body.setLinearVelocity(velX, velY);
    }

    /**
     * @return The linear velocity vector.
     */
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    /**
     * Wraps the getUserData method from the Box2D body class.
     *
     * @return the user data
     */
    public Object getUserData() {
        return body.getUserData();
    }
}