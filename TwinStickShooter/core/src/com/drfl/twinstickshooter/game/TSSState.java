package com.drfl.twinstickshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.drfl.twinstickshooter.view.*;

public class TSSState {

    /**
     * Enumerator for game states.
     */
    public enum GameState {MAIN_MENU, PLAYING, GAME_OVER, SCOREBOARD, MAP_SELECT}

    /**
     * Enumerator for game events.
     */
    public enum GameEvent {START, MC_DIED, MAIN, HIGHSCORE, EXIT, CHOOSE}

    /**
     * Current game state.
     */
    private GameState currState;

    /**
     * Instance of the game associated with this state machine.
     */
    private final TSSGame game;

    /**
     * Constructs a state machine belonging to a game with a certain initial state.
     *
     * @param state The initial state
     * @param game The game instance to associate
     */
    TSSState(GameState state, TSSGame game) {
        this.currState = state;
        this.game = game;
    }

    /**
     * Changes state according to received event. Switchs the active game screen as the state changes.
     *
     * @param event The event to process
     */
    public void processState(GameEvent event) {

        switch(currState) {

            case MAIN_MENU:
                if(event == GameEvent.START) {
                    switchState(new TSSMapSelect(game), GameState.MAP_SELECT);

                } else if(event == GameEvent.HIGHSCORE) {
                    switchState(new TSSScoreboard(game), GameState.SCOREBOARD);

                } else if(event == GameEvent.EXIT) {
                    Gdx.app.exit();
                }
            break;

            case MAP_SELECT:
                if(event == GameEvent.CHOOSE) {
                    switchState(new TSSView(game), GameState.PLAYING);
                }
            break;

            case PLAYING:
                if(event == GameEvent.MC_DIED) {
                    switchState(new TSSGameOver(game), GameState.GAME_OVER);
                }
            break;

            case GAME_OVER:
                if(event == GameEvent.HIGHSCORE) {
                    switchState(new TSSScoreboard(game), GameState.SCOREBOARD);

                } else if(event == GameEvent.MAIN) {
                    switchState(new TSSMainMenu(game), GameState.MAIN_MENU);
                }
            break;

            case SCOREBOARD:
                if(event == GameEvent.MAIN) {
                    switchState(new TSSMainMenu(game), GameState.MAIN_MENU);
                }
            break;
        }
    }

    /**
     * Switchs to a new screen and state.
     *
     * @param screen The screen to change the game to
     * @param state The state to change to
     */
    private void switchState(ScreenAdapter screen, GameState state) {
        this.currState = state;
        game.setScreen(screen);
    }
}