package com.drfl.twinstickshooter.controller.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.drfl.twinstickshooter.view.TSSView.PIXEL_TO_METER;

/**
 * The tile entity body in Box2D.
 */
public class TileEntity extends EntityBody {

    /**
     * Constructs a tile entity body.
     *
     * @param world The world this tile entity belongs to
     * @param coords The position of this tile entity's body in pixels
     * @param width The width of the fixture of this tile entity in pixels
     * @param height The height of the fixture of this tile entity in pixels
     */
    public TileEntity(World world, Vector2 coords, float width, float height) {

        super(world, new Vector2(coords.x * PIXEL_TO_METER + width * PIXEL_TO_METER / 2,
                coords.y * PIXEL_TO_METER + height * PIXEL_TO_METER / 2));

        float density = 0.0f, friction = 0.0f, restitution = 0.0f;

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = TILE_ENTITY;
        fixtureDef.filter.maskBits = COLLIDE_ALL;

        createFixture(body, new float[]{0,0, width,0, 0,height, width,height}, Math.round(width), Math.round(height), fixtureDef);
    }
}