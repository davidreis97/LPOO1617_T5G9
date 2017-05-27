package com.drfl.twinstickshooter.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * A view representing the main character.
 */
public class MainCharView extends EntityView {

//    /**
//     * The time between the animation frames
//     */
//    private static final float FRAME_TIME = 0.05f;
//
//    /**
//     * The animation used when the ship is accelerating
//     */
//    private Animation<TextureRegion> acceleratingAnimation;

    /**
     * The texture used when the ship is not accelerating
     */
    private TextureRegion idleRegion;

//    /**
//     * Time since the space ship started the game. Used
//     * to calculate the frame to show in animations.
//     */
//    private float stateTime = 0;
//
//    /**
//     * Is the space ship accelerating.
//     */
//    private boolean accelerating;

    /**
     * Constructs a space ship .
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     */
    public MainCharView(TSSGame game) {
        super(game);
    }

    /**
     * Creates a sprite representing the main character.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the sprite representing the main character.
     */
    @Override
    public Sprite createSprite(TSSGame game) {
//        acceleratingAnimation = createAcceleratingAnimation(game);
        idleRegion = createIdleRegion(game);

        return new Sprite(idleRegion);
    }

    /**
     * Creates the texture used when the main character is stopped.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the texture used when the main character is stopped.
     */
    private TextureRegion createIdleRegion(TSSGame game) {
        Texture idleTexture = game.getAssetManager().get("Engineer.png");
        return new TextureRegion(idleTexture, 0, 0, TILESIZE, TILESIZE);
//        return new TextureRegion(idleTexture, idleTexture.getWidth(), idleTexture.getHeight());
    }

//    /**
//     * Creates the animation used when the ship is accelerating
//     *
//     * @param game the game this view belongs to. Needed to access the
//     *             asset manager to get textures.
//     * @return the animation used when the ship is accelerating
//     */
//    private Animation<TextureRegion> createAcceleratingAnimation(TSSGame game) {
//        Texture thrustTexture = game.getAssetManager().get("spaceship-thrust.png");
//        TextureRegion[][] thrustRegion = TextureRegion.split(thrustTexture, thrustTexture.getWidth() / 4, thrustTexture.getHeight());
//
//        TextureRegion[] frames = new TextureRegion[4];
//        System.arraycopy(thrustRegion[0], 0, frames, 0, 4);
//
//        return new Animation<TextureRegion>(FRAME_TIME, frames);
//    }

    /**
     * Updates this ship model. Also save and resets
     * the accelerating flag from the model.
     *
     * @param model the model used to update this view
     */
    @Override
    public void update(EntityModel model) {

        super.update(model);
//        accelerating = ((ShipModel)model).isAccelerating();
//        ((ShipModel)model).setAccelerating(false);
    }

    /**
     * Draws the sprite from this view using a sprite batch.
     * Chooses the correct texture or animation to be used
     * depending on the accelerating flag.
     *
     * @param batch The sprite batch to be used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
//        stateTime += Gdx.graphics.getDeltaTime();

//        if (accelerating)
//            sprite.setRegion(acceleratingAnimation.getKeyFrame(stateTime, true));
//        else
            sprite.setRegion(idleRegion);

        sprite.draw(batch);
    }
}