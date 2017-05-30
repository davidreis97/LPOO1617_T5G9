package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;
import com.drfl.twinstickshooter.model.entities.MainCharModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * A view representing the main character.
 */
public class MainCharView extends EntityView {

    /**
     * The time between animation frames
     */
    private static final float FRAME_TIME = 0.15f;

    /**
     * The animation used when the character is moving down
     */
    private Animation<TextureRegion> downAnim;
    private Animation<TextureRegion> leftAnim;
    private Animation<TextureRegion> rightAnim;
    private Animation<TextureRegion> upAnim;

    /**
     * The texture used when the ship is not accelerating
     */
    private TextureRegion downRegion;
    private TextureRegion leftRegion;
    private TextureRegion rightRegion;
    private TextureRegion upRegion;

    /**
     * Time since the space ship started the game. Used
     * to calculate the frame to show in animations.
     */
    private float stateTime = 0;

    /**
     * Is the main character moving down
     */
    private EntityModel.AnimDirection direction = EntityModel.AnimDirection.DOWN;
    private EntityModel.AnimDirection previousDirection = EntityModel.AnimDirection.DOWN;

    /**
     * Constructs a main character view.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public MainCharView(TSSGame game) { super(game); }

    /**
     * Creates a sprite representing the main character.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the sprite representing the main character.
     */
    @Override
    public Sprite createSprite(TSSGame game) {

        createAnimations(game);
        createIdleRegions(game);

        return new Sprite(downRegion);
    }

    /**
     * Creates the texture used when the main character is stopped.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the texture used when the main character is stopped.
     */
    private void createIdleRegions(TSSGame game) {

        Texture spriteSheet = game.getAssetManager().get("Engineer.png");
        downRegion = new TextureRegion(spriteSheet, 0, 0, TILESIZE, TILESIZE);

        leftRegion = new TextureRegion(spriteSheet, 0, TILESIZE, TILESIZE, TILESIZE);

        rightRegion = new TextureRegion(spriteSheet, 0, TILESIZE * 2, TILESIZE, TILESIZE);

        upRegion = new TextureRegion(spriteSheet, 0, TILESIZE * 3, TILESIZE, TILESIZE);
    }

    /**
     * Creates the animation used when the ship is accelerating
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used when the ship is accelerating
     */
    private void createAnimations(TSSGame game) {

        Texture spriteSheet = game.getAssetManager().get("Engineer.png");
        TextureRegion[][] spriteRegion = TextureRegion.split(spriteSheet, TILESIZE, TILESIZE);

        TextureRegion[] downFrames = new TextureRegion[4];
        TextureRegion[] leftFrames = new TextureRegion[4];
        TextureRegion[] rightFrames = new TextureRegion[4];
        TextureRegion[] upFrames = new TextureRegion[4];

        System.arraycopy(spriteRegion[0], 0, downFrames, 0, 4);
        this.downAnim = new Animation<TextureRegion>(FRAME_TIME, downFrames);

        System.arraycopy(spriteRegion[1], 0, leftFrames, 0, 4);
        this.leftAnim = new Animation<TextureRegion>(FRAME_TIME, leftFrames);

        System.arraycopy(spriteRegion[2], 0, rightFrames, 0, 4);
        this.rightAnim = new Animation<TextureRegion>(FRAME_TIME, rightFrames);

        System.arraycopy(spriteRegion[3], 0, upFrames, 0, 4);
        this.upAnim = new Animation<TextureRegion>(FRAME_TIME, upFrames);
    }

    /**
     * Updates this ship model. Also save and resets
     * the accelerating flag from the model.
     *
     * @param model the model used to update this view
     */
    @Override
    public void update(EntityModel model) {

        super.update(model);
        EntityModel.AnimDirection modelDir = ((MainCharModel)model).getDirection();

        if(this.direction != modelDir) {
            this.previousDirection = this.direction;
            this.stateTime = 0;
        }

        this.direction = modelDir;
        ((MainCharModel)model).setDirection(EntityModel.AnimDirection.NONE);
    }

    /**
     * Draws the sprite from this view using a sprite batch.
     * Chooses the correct texture or animation to be used
     * depending on whether there's movement and the direction of it.
     *
     * @param batch The sprite batch to be used for drawing.
     */
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
}