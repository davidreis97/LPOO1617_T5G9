package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.drfl.twinstickshooter.model.entities.MainCharModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * The main char body in Box2D.
 */
public class MainCharBody extends EntityBody {

    /**
     * Constructs a main char body using a main char model.
     *
     * @param world The world this main char belongs to
     * @param model The model representing this main char
     */
    public MainCharBody(World world, MainCharModel model) {

        super(world, model);

        float density = 0.5f, friction = 0.4f, restitution = 0.5f;

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = MAINCHAR_BODY;
        fixtureDef.filter.maskBits = COLLIDE_ALL;

        createFixture(body, new float[]{4,0, 28,0, 4,TILESIZE, 28,TILESIZE}, TILESIZE, TILESIZE, fixtureDef);
    }
}