package com.drfl.twinstickshooter;

import com.drfl.twinstickshooter.view.TSSMainMenu;
import com.drfl.twinstickshooter.view.TSSView;

public class TSSState {

    public enum GameState {MAIN_MENU, PLAYING};
    public enum GameEvent {START, MC_DIED};

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
                    TSSView gameScreen = new TSSView(game, new TSSServer());
                    gameScreen.setInstance(gameScreen);
                    game.setScreen(gameScreen);
                }
                break;
            case PLAYING:
                if(event == GameEvent.MC_DIED) {
                    this.currState = GameState.MAIN_MENU;
                    TSSMainMenu menuScreen = new TSSMainMenu(game);
                    game.setScreen(menuScreen);
                }
                break;
        }
    }

    public GameState getCurrState() {
        return currState;
    }

    public void setCurrState(GameState currState) {
        this.currState = currState;
    }
}