package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * A abstract view extending EntityView so that it also handles
 * animation frames.
 */
public abstract class AnimatedEntityView extends EntityView {

    //NOTEME javadoc
    /**
     * Array of animations for entity movement, indexes are
     * aligned with Enum AnimDirection from EntityModel.java
     */
    private Array<Animation> animations = new Array<>();

    //NOTEME javadoc
    /**
     * Array of sprites for when no movement happens, indexes are
     * aligned with Enum AnimDirection from EntityModel.java
     */
    private Array<TextureRegion> idleSprites = new Array<>();

    //NOTEME javadoc
    /**
     * Current animation direction.
     */
    private EntityModel.AnimDirection direction = EntityModel.AnimDirection.DOWN;

    //NOTEME javadoc
    /**
     * Previous animation direction.
     */
    private EntityModel.AnimDirection previousDirection = EntityModel.AnimDirection.DOWN;

    //NOTEME javadoc
    /**
     * Time the entity has been in the same animation cycle.
     */
    private float stateTime = 0;

    //NOTEME javadoc
    /**
     * Maximum amount of time entity should spend in the hurt state.
     */
    private final static float HURT_FRAMES = 0.5f;

    //NOTEME javadoc
    /**
     * Current time spent in hurt state.
     */
    private float hurtTime = 0;

    //NOTEME javadoc
    /**
     * Creates an animated view belonging to a game with a certain sprite sheet
     * and frame time for animations.
     *
     * @param game The game this view belongs to
     * @param spriteSheet The sprite sheet to use for animations
     * @param frameTime The time between each animation frame
     */
    AnimatedEntityView(TSSGame game, Texture spriteSheet, float frameTime) {

        super(game);
        createGraphics(spriteSheet, frameTime);
        sprite.setBounds(0, 0, TILESIZE, TILESIZE);
    }

    //NOTEME javadoc
    /**
     * Calls the creation of the animations and idle textures for
     * a given sprite sheet and frame time.
     *
     * @param spriteSheet The sprite sheet to use for animations
     * @param frameTime The time between each animation frame
     */
    private void createGraphics(Texture spriteSheet, float frameTime) {
        createAnimations(spriteSheet, frameTime);
        createIdleRegions(spriteSheet);
    }

    //NOTEME javadoc
    /**
     * Stub implementation, not supposed to be called.
     */
    @Override
    public Sprite createSprite(TSSGame game) {
        return new Sprite();
    }

    //NOTEME javadoc
    /**
     * Draws the sprite to the screen after choosing the appropriate sprite
     * according to the current animation state enumerator. The enumerator works as
     * an index as the arrays of animation sprites and idle sprites are aligned with
     * the directions the enumerator represents.
     * Replace with EnumMap if strict adherence to Java mentality is needed.
     *
     * @param batch The sprite batch to be used for drawing
     */
    @Override
    public void draw(SpriteBatch batch) {

        stateTime += Gdx.graphics.getDeltaTime();
        if(hurtTime > 0) hurtTime -= Gdx.graphics.getDeltaTime();

        if(direction != EntityModel.AnimDirection.NONE) {
            sprite.setRegion((TextureRegion) animations.get(direction.ordinal()).getKeyFrame(stateTime, true));

        } else sprite.setRegion(idleSprites.get(previousDirection.ordinal()));

        sprite.draw(batch);
    }

    //NOTEME javadoc
    /**
     * Updates the animated view animation state according to model
     * animation direction and model flags.
     *
     * @param model The model used to update this view
     */
    @Override
    public void update(EntityModel model) {

        super.update(model);
        EntityModel.AnimDirection modelDir = model.getAnimDirection();

        if(hurtTime < 0) {
            model.setHurt(false);
            hurtTime = 0;
        } else if(model.isHurt() && hurtTime == 0) {
            hurtTime = HURT_FRAMES;
        }

        if(this.direction != modelDir) {
            this.previousDirection = this.direction;
            this.stateTime = 0;
        }

        this.direction = modelDir;
        model.setAnimDirection(EntityModel.AnimDirection.NONE);
    }

    //NOTEME javadoc
    /**
     * Creates idle textures for all 4 directions.
     *
     * @param spriteSheet The sprite sheet to use for the directions
     */
    private void createIdleRegions(Texture spriteSheet) {

        TextureRegion downRegion = new TextureRegion(spriteSheet, 0, 0, TILESIZE, TILESIZE);

        idleSprites.add(new TextureRegion(spriteSheet, 0, TILESIZE * 3, TILESIZE, TILESIZE));
        idleSprites.add(new TextureRegion(spriteSheet, 0, TILESIZE, TILESIZE, TILESIZE));
        idleSprites.add(downRegion);
        idleSprites.add(new TextureRegion(spriteSheet, 0, TILESIZE * 2, TILESIZE, TILESIZE));
        idleSprites.add(downRegion);
    }

    //NOTEME javadoc
    /**
     * Creates animations for all 4 directions.
     *
     * @param spriteSheet The sprite sheet to use for the directions
     * @param frameTime The time between frames in seconds
     */
    private void createAnimations(Texture spriteSheet, float frameTime) {

        TextureRegion[][] spriteRegion = TextureRegion.split(spriteSheet, TILESIZE, TILESIZE);

        TextureRegion[] downFrames = new TextureRegion[4];
        TextureRegion[] leftFrames = new TextureRegion[4];
        TextureRegion[] rightFrames = new TextureRegion[4];
        TextureRegion[] upFrames = new TextureRegion[4];

        System.arraycopy(spriteRegion[3], 0, upFrames, 0, 4);
        animations.add(new Animation<>(frameTime, upFrames));

        System.arraycopy(spriteRegion[1], 0, leftFrames, 0, 4);
        animations.add(new Animation<>(frameTime, leftFrames));

        System.arraycopy(spriteRegion[0], 0, downFrames, 0, 4);
        Animation<TextureRegion> downAnim = new Animation<>(frameTime, downFrames);
        animations.add(downAnim);

        System.arraycopy(spriteRegion[2], 0, rightFrames, 0, 4);
        animations.add(new Animation<>(frameTime, rightFrames));

        animations.add(downAnim);
    }
}