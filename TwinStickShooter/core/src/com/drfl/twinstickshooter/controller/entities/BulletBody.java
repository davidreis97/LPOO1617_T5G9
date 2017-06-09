package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.drfl.twinstickshooter.model.entities.BulletModel;

/**
 * The bullet body in Box2D.
 */
public class BulletBody extends EntityBody {

    //NOTEME javadoc
    /**
     * Constructs a bullet body using a bullet model.
     *
     * @param world The world this bullet belongs to
     * @param model The model representing this bullet
     */
    public BulletBody(World world, BulletModel model) {

        super(world, model);

        float density = 1f, friction = 0.4f, restitution = 0.5f;
        int width = 9, height = 15;

        createFixture(body, new float[]{0,0, width,0, 0,height, width,height},
                width, height, density, friction, restitution, BULLET_BODY, (short) (COLLIDE_ALL ^ BULLET_BODY));
    }
}