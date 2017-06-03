package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

public abstract class AnimatedEntityView extends EntityView {

    /**
     * The animation used for each movement direction
     */
    protected Animation<TextureRegion> downAnim;
    protected Animation<TextureRegion> leftAnim;
    protected Animation<TextureRegion> rightAnim;
    protected Animation<TextureRegion> upAnim;

    /**
     * The texture used for idle entities
     */
    protected TextureRegion downRegion;
    protected TextureRegion leftRegion;
    protected TextureRegion rightRegion;
    protected TextureRegion upRegion;

    /**
     * Animation states for the entity
     */
    protected EntityModel.AnimDirection direction = EntityModel.AnimDirection.DOWN;
    protected EntityModel.AnimDirection previousDirection = EntityModel.AnimDirection.DOWN;

    /**
     * Time the entity has been on the same animation cycle
     */
    protected float stateTime = 0;

    AnimatedEntityView(TSSGame game, Texture spriteSheet, float frameTime) {
        super(game);
        createGraphics(spriteSheet, frameTime);
        sprite.setBounds(0, 0, TILESIZE, TILESIZE);
    }

    private void createGraphics(Texture spriteSheet, float frameTime) {
        createAnimations(spriteSheet, frameTime);
        createIdleRegions(spriteSheet);
    }

    /**
     * Stub implementation, not supposed to be called
     */
    @Override
    public Sprite createSprite(TSSGame game) {
        return new Sprite();
    }

    @Override
    public void draw(SpriteBatch batch) {

        stateTime += Gdx.graphics.getDeltaTime();

        //TODO divide into functions
        switch(this.direction) {
            case DOWN:
                sprite.setRegion(downAnim.getKeyFrame(stateTime, true));
                break;
            case LEFT:
                sprite.setRegion(leftAnim.getKeyFrame(stateTime, true));
                break;
            case RIGHT:
                sprite.setRegion(rightAnim.getKeyFrame(stateTime, true));
                break;
            case UP:
                sprite.setRegion(upAnim.getKeyFrame(stateTime, true));
                break;
            case NONE:
                switch(this.previousDirection) {
                    case DOWN:
                        sprite.setRegion(downRegion);
                        break;
                    case LEFT:
                        sprite.setRegion(leftRegion);
                        break;
                    case RIGHT:
                        sprite.setRegion(rightRegion);
                        break;
                    case UP:
                        sprite.setRegion(upRegion);
                        break;
                    case NONE:
                        sprite.setRegion(downRegion);
                        break;
                }
                break;
        }

        sprite.draw(batch);
    }

    @Override
    public void update(EntityModel model) {

        super.update(model);
        EntityModel.AnimDirection modelDir = model.getDirection();

        if(this.direction != modelDir) {
            this.previousDirection = this.direction;
            this.stateTime = 0;
        }

        this.direction = modelDir;
        model.setDirection(EntityModel.AnimDirection.NONE);
    }

    /**
     * Creates the texture used when the main character is stopped.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the texture used when the main character is stopped.
     */
    private void createIdleRegions(Texture spriteSheet) {

        downRegion = new TextureRegion(spriteSheet, 0, 0, TILESIZE, TILESIZE);

        leftRegion = new TextureRegion(spriteSheet, 0, TILESIZE, TILESIZE, TILESIZE);

        rightRegion = new TextureRegion(spriteSheet, 0, TILESIZE * 2, TILESIZE, TILESIZE);

        upRegion = new TextureRegion(spriteSheet, 0, TILESIZE * 3, TILESIZE, TILESIZE);
    }

    /**
     * Creates the animations used when an entity is moving
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    private void createAnimations(Texture spriteSheet, float frameTime) {

        TextureRegion[][] spriteRegion = TextureRegion.split(spriteSheet, TILESIZE, TILESIZE);

        TextureRegion[] downFrames = new TextureRegion[4];
        TextureRegion[] leftFrames = new TextureRegion[4];
        TextureRegion[] rightFrames = new TextureRegion[4];
        TextureRegion[] upFrames = new TextureRegion[4];

        System.arraycopy(spriteRegion[0], 0, downFrames, 0, 4);
        this.downAnim = new Animation<TextureRegion>(frameTime, downFrames);

        System.arraycopy(spriteRegion[1], 0, leftFrames, 0, 4);
        this.leftAnim = new Animation<TextureRegion>(frameTime, leftFrames);

        System.arraycopy(spriteRegion[2], 0, rightFrames, 0, 4);
        this.rightAnim = new Animation<TextureRegion>(frameTime, rightFrames);

        System.arraycopy(spriteRegion[3], 0, upFrames, 0, 4);
        this.upAnim = new Animation<TextureRegion>(frameTime, upFrames);
    }
}
