package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.physics.box2d.World;

import static sun.dc.pr.Rasterizer.TILE_SIZE;

/**
 * The tile entity for representing tile collisions.
 */
public class TileEntity extends EntityBody {

    /**
     * Constructs a main char body using a main char model.
     *
     * @param world the physical world this tile entity belongs to.
     */
    public TileEntity(World world, float x, float y, float width, float height) {
        super(world, x / TILE_SIZE + width / TILE_SIZE / 2, y / TILE_SIZE + height / TILE_SIZE / 2);

        float density = 0.0f, friction = 0.0f, restitution = 0.0f;

        createFixture(body, new float[]{0,0, width,0, 0,height, width,height},
                (int) width, (int) height, density, friction, restitution, TILE_ENTITY, COLLIDE_ALL);
    }
}
