package dkeep.logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class responsible for serializing game data (creates/restores a copy since Game is static)
 */
public class GameSaveHelper implements Serializable {
	
	private Map map;
	private ArrayList<Entity> entities;
	private String guardType;
	private String state;
	private int heroIndex;
		
	/**
	 * Sets object member variables as a copy of Game object to save to file. Other methods do the saving.
	 */
	public void gameToObject() {
		map = Game.getMapObject();
		entities = Game.getEntities();
		heroIndex = Game.getHeroIndex();
		state = Game.getState();
		guardType = Game.getGuardType();
	}
	
	/**
	 * Sets Game data to same values as GameSaveHelper object. GameSaveHelper object set to values loaded from file beforehand. Other methods do the loading.
	 */
	public void objectToGame() {
		new Game("Dungeon", guardType, 1);
		Game.setEntities(entities);
		Game.setMapObject(map);
		Game.setHeroIndex(heroIndex);
		Game.setState(state);
		Game.setGuardType(guardType);
	}
}
