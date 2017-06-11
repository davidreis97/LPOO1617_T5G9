package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;

/**
 * Abstract class for representing Controller entities which implement Box2D body functionalities.
 */
public abstract class EntityBody {

    /**
     * Bit flag representing main char body.
     */
    static final short MAINCHAR_BODY = 0x0001;

    /**
     * Bit flag representing bullet body.
     */
    static final short BULLET_BODY = 0x0002;

    /**
     * Bit flag representing tile body.
     */
    static final short TILE_ENTITY = 0x0004;

    /**
     * Bit flag representing enemy body.
     */
    static final short ENEMY_BODY = 0x0008;

    /**
     * Flag representing all possible collision bits.
     */
    static final short COLLIDE_ALL = (short) (MAINCHAR_BODY | BULLET_BODY | TILE_ENTITY | ENEMY_BODY);

    /**
     * Linear damping used for all dynamic bodies.
     */
    private static final float DAMPING = 5;

    /**
     * The Box2D body that supports this entity's body.
     */
    final Body body;

    /**
     * Constructs an entity body representing a model in a certain world.
     *
     * @param world The world this body lives on
     * @param model The model representing the body
     */
    EntityBody(World world, EntityModel model) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(model.getPosition().x, model.getPosition().y);
        bodyDef.fixedRotation = true;
        bodyDef.angle = model.getRotation();
        bodyDef.linearDamping = DAMPING;

        body = world.createBody(bodyDef);
        body.setUserData(model);
    }

    /**
     * Constructs a tile entity belonging to a certain world.
     *
     * @param world The world the tile entity belongs to
     */
    EntityBody(World world, Vector2 coords) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(coords.x, coords.y);

        body = world.createBody(bodyDef);
        body.setUserData(this);
    }

    /**
     * Helper method to create a polygon fixture represented by a set of vertexes.
     *
     * @param body The body the fixture is to be attached to
     * @param vertexes The vertexes defining the fixture in pixels so it is
     *                 easier to get them from a bitmap image
     * @param width The width of the bitmap the vertexes where extracted from
     * @param height The height of the bitmap the vertexes where extracted from
     * @param density The density of the fixture. How heavy it is in relation to its area
     * @param friction The friction of the fixture. How slippery it is
     * @param restitution The restitution of the fixture. How much it bounces
     * @param category The collision category bits
     * @param mask The collision mask bits. This states the categories that this shape would accept for collision
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
     * Wraps the getPosition method from the Box2D body class.
     *
     * @return The world position of the body's origin
     */
    public Vector2 getPosition() {
        return body.getPosition().cpy();
    }

    /**
     * Wraps the getMass method from the Box2D body class.
     *
     * @return The mass, usually in kilograms (kg)
     */
    public float getMass() {
        return body.getMass();
    }

    /**
     * Wraps the setLinearVelocity method from the Box2D body class.
     *
     * @param velocity The new linear velocity for this body
     */
    public void setLinearVelocity(float velocity) {
        body.setLinearVelocity((float) (velocity * -Math.sin(body.getAngle())), (float) (velocity * Math.cos(body.getAngle())));
    }

    /**
     * Wraps the setLinearDamping method from the Box2D body class.
     *
     * @param damping The linear damping
     */
    public void setLinearDamping(float damping) {
        body.setLinearDamping(damping);
    }

    /**
     * Wraps the applyLinearImpulse method from the Box2D body class.
     * Point of application is always the center of the body.
     *
     * @param impulse The world impulse vector, usually in N-seconds or kg-m/s
     * @param wake Wake the body
     */
    public void applyLinearImpulse(Vector2 impulse, boolean wake) {
        body.applyLinearImpulse(impulse, body.getWorldCenter(), wake);
    }

    /**
     * @return The Box2D body used by an entity body.
     */
    public Body getBody() {
        return body;
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