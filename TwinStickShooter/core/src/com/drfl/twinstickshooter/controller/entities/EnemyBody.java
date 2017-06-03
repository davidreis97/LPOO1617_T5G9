package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.drfl.twinstickshooter.model.entities.EnemyModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * The main char body in Box2D.
 */
public class EnemyBody extends EntityBody {

    /**
     * Constructs a main char body using a main char model.
     *
     * @param world the physical world this space ship belongs to.
     * @param model the model representing this space ship.
     */
    public EnemyBody(World world, EnemyModel model) {
        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;
        int width = TILESIZE, height = TILESIZE;

        createFixture(body, new float[]{4,0, 28,0, 4,TILESIZE, 28,TILESIZE},
                width, height, density, friction, restitution, ENEMY_BODY, (short) (MAINCHAR_BODY | BULLET_BODY | TILE_ENTITY));
    }
}