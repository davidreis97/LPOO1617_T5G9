package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.drfl.twinstickshooter.model.entities.EnemyModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * The enemy body in Box2D.
 */
public class EnemyBody extends EntityBody {

    /**
     * Constructs an enemy body using an enemy model.
     *
     * @param world The world this enemy belongs to
     * @param model The model representing this enemy
     */
    public EnemyBody(World world, EnemyModel model) {

        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = ENEMY_BODY;
        fixtureDef.filter.maskBits = (short) (COLLIDE_ALL ^ ENEMY_BODY);

        createFixture(body, new float[]{4,0, 28,0, 4,TILESIZE, 28,TILESIZE}, TILESIZE, TILESIZE, fixtureDef);
    }
}