package com.drfl.twinstickshooter;

import com.drfl.twinstickshooter.view.TSSView;

public class TSSState {

    public enum GameState {MAIN_MENU, PLAYING};
    public enum GameEvent {START};

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
                    gameScreen.initInstance(gameScreen);
                    game.setScreen(gameScreen);
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