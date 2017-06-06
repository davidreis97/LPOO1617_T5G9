package com.drfl.twinstickshooter;

import com.badlogic.gdx.Gdx;
import com.drfl.twinstickshooter.view.TSSGameOver;
import com.drfl.twinstickshooter.view.TSSMainMenu;
import com.drfl.twinstickshooter.view.TSSScoreboard;
import com.drfl.twinstickshooter.view.TSSView;

public class TSSState {

    public enum GameState {MAIN_MENU, PLAYING, GAME_OVER, SCOREBOARD}
    public enum GameEvent {START, MC_DIED, MAIN, HIGHSCORE, EXIT}

    private GameState currState;
    private final TSSGame game;

    public TSSState(GameState state, TSSGame game) {
        this.currState = state;
        this.game = game;
    }

    public void processState(GameEvent event) {

        switch(currState) {

            case MAIN_MENU:
                if(event == GameEvent.START) { //Transit to game screen
                    this.currState = GameState.PLAYING;
                    TSSView gameScreen = new TSSView(game);
                    gameScreen.setInstance(gameScreen);
                    game.setScreen(gameScreen);
                } else if(event == GameEvent.HIGHSCORE) { //Transit to scoreboard
                    this.currState = GameState.SCOREBOARD;
                    TSSScoreboard scoreScreen = new TSSScoreboard(game);
                    game.setScreen(scoreScreen);
                } else if(event == GameEvent.EXIT) {
                    Gdx.app.exit();
                }
            break;

            case PLAYING:
                if(event == GameEvent.MC_DIED) { //Transit to game over screen
                    this.currState = GameState.GAME_OVER;
                    TSSGameOver over = new TSSGameOver(game);
                    game.setScreen(over);
                }
            break;

            case GAME_OVER:
                if(event == GameEvent.HIGHSCORE) { //Transit to scoreboard
                    this.currState = GameState.SCOREBOARD;
                    TSSScoreboard scoreScreen = new TSSScoreboard(game);
                    game.setScreen(scoreScreen);
                } else if(event == GameEvent.MAIN) { //Transit to main menu
                    this.currState = GameState.MAIN_MENU;
                    TSSMainMenu menu = new TSSMainMenu(game);
                    game.setScreen(menu);
                }
            break;

            case SCOREBOARD:
                if(event == GameEvent.MAIN) { //Transit to main menu
                    this.currState = GameState.MAIN_MENU;
                    TSSMainMenu menu = new TSSMainMenu(game);
                    game.setScreen(menu);
                }
            break;
        }
    }
}