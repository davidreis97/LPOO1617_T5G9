package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.drfl.twinstickshooter.TSSGame;

public class TSSView extends ScreenAdapter {

    /**
     * How much meters does a pixel represent.
     */
    public final static float PIXEL_TO_METER = 0.04f; //TODO check this value

    /**
     * The width of the viewport in meters. The height is
     * automatically calculated using the screen ratio.
     */
    private static final float VIEWPORT_WIDTH = 20; //TODO check this value

    /**
     * The game this screen belongs to.
     */
    private final TSSGame game;

    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    /**
     * Create game view using libGDX screen
     * @param game game this screen belongs to
     */
    public TSSView(TSSGame game) {
        this.game = game;

        camera = createCamera();
    }

    /**
     * Creates the camera used to show the viewport.
     *
     * @return the camera
     */
    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera(VIEWPORT_WIDTH / PIXEL_TO_METER, VIEWPORT_WIDTH / PIXEL_TO_METER * ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

//        if (DEBUG_PHYSICS) {
//            debugRenderer = new Box2DDebugRenderer();
//            debugCamera = camera.combined.cpy();
//            debugCamera.scl(1 / PIXEL_TO_METER);
//        }

        return camera;
    }
}