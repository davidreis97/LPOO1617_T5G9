package com.drfl.twinstickshooter.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.game.TSSState;
import com.drfl.twinstickshooter.model.TSSModel;

public class TSSGameOver extends ScreenAdapter {

        /**
         * The game this screen belongs to.
         */
        private final TSSGame game;

        /**
         * The camera used to show the viewport.
         */
        private final OrthographicCamera camera;

        private float totalTime = 0;

        public TSSGameOver(TSSGame game) {

            this.game = game;

            loadAssets();

            startMusic();

            camera = createCamera();
        }

        private void startMusic() {

            ((Music)game.getAssetManager().get("GameOver.ogg")).setVolume(game.getMusicVolume());
            ((Music)game.getAssetManager().get("GameOver.ogg")).play();
        }

        private OrthographicCamera createCamera() {

            OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
            camera.update();

            return camera;
        }

        private void loadAssets() {

            this.game.getAssetManager().load( "GameOver.png" , Texture.class);
            this.game.getAssetManager().load("GameOver.ogg", Music.class);
            this.game.getAssetManager().finishLoading();
        }

        /**
         * Renders this screen.
         *
         * @param delta time since last renders in seconds.
         */
        @Override
        public void render(float delta) {

            totalTime += delta;

            if(totalTime >= 2.5f) { //TODO magic value
                if(game.checkHighScore(TSSModel.getInstance().getScore())) {
                    game.getStateM().processState(TSSState.GameEvent.HIGHSCORE);
                } else game.getStateM().processState(TSSState.GameEvent.MAIN);

                return;
            }

            game.getBatch().setProjectionMatrix(camera.combined);

            Gdx.gl.glClearColor( 103/255f, 69/255f, 117/255f, 1 );
            Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

            game.getBatch().setShader(null);

            game.getBatch().begin();
            drawElements();
            game.getBatch().end();
        }

        private void drawElements() {

            Texture background = game.getAssetManager().get("GameOver.png");
            game.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void hide() {
            this.dispose();
        }

        @Override
        public void dispose() {
            game.getAssetManager().unload("GameOver.png");
            game.getAssetManager().unload("GameOver.ogg");
        }
}