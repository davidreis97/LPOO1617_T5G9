package com.drfl.twinstickshooter;

import com.drfl.twinstickshooter.view.TSSGameOver;
import com.drfl.twinstickshooter.view.TSSMainMenu;
import com.drfl.twinstickshooter.view.TSSView;

public class TSSState {

    public enum GameState {MAIN_MENU, PLAYING, GAME_OVER};
    public enum GameEvent {START, MC_DIED, MAIN};

    private GameState currState;
    private final TSSGame game;

    public TSSState(GameState state, TSSGame game) {
        this.currState = state;
        this.game = game;
    }

    public void processState(GameEvent event) {

        switch(currState) {
            case MAIN_MENU:
                if(event == GameEvent.START) {
                    this.currState = GameState.PLAYING;
                    TSSView gameScreen = new TSSView(game);
                    gameScreen.setInstance(gameScreen);
                    game.setScreen(gameScreen);
                }
                break;
            case PLAYING:
                if(event == GameEvent.MC_DIED) {
                    this.currState = GameState.GAME_OVER;
                    TSSGameOver over = new TSSGameOver(game);
                    game.setScreen(over);
                }
                break;
            case GAME_OVER:
                if(event == GameEvent.MAIN) {
                    this.currState = GameState.MAIN_MENU;
                    TSSMainMenu menu = new TSSMainMenu(game);
                    game.setScreen(menu);
                }
        }
    }
}