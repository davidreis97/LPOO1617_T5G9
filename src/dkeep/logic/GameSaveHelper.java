package dkeep.logic;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSaveHelper implements Serializable {
	
	private Map map;
	private ArrayList<Entity> entities;
	private String guardType;
	private String state;
	private int heroIndex;
		
	public void gameToObject() {
		map = Game.getMapObject();
		entities = Game.getEntities();
		heroIndex = Game.getHeroIndex();
		state = Game.getState();
		guardType = Game.getGuardType();
	}
	
	public void objectToGame() {
		new Game("Dungeon", guardType, 1);
		Game.setEntities(entities);
		Game.setMapObject(map);
		Game.setHeroIndex(heroIndex);
		Game.setState(state);
		Game.setGuardType(guardType);
	}
}
