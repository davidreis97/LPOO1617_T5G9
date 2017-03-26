package dkeep.logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class implementing Map interface with Dungeon specific behavior.
 */
public class DungeonMap implements Map, Serializable {
	
	private char dungeonMap[][] =
        {{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
			{'X', ' ', ' ', ' ', 'I', ' ', 'X', ' ', ' ', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
			{'X', ' ', 'I', ' ', 'I', ' ', 'X', ' ', ' ', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', 'X', ' ', 'X'},
			{'X', ' ', 'I', ' ', 'I', ' ', 'X', 'k', ' ', 'X'},
			{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
	
	@Override
	public char[][] getMap() {
		
		char[][] tempMap = new char[dungeonMap.length][dungeonMap.length];
		
		for(int i = 0; i < dungeonMap.length; i++) {
			tempMap[i] = dungeonMap[i].clone();
		}

		return tempMap;
	}
	
	@Override
	public boolean doMove(Point coords, String entityType, int index) {
		
		char collision = dungeonMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			return true;
		case 'k':
			checkDoors(entityType);
			return false;
		case 'S':
			nextMap();
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public void checkDoors(String entityType) {
		dungeonMap[5][0] = 'S';
		dungeonMap[6][0] = 'S';
	}

	@Override
	public void initMap(String guardType) {
		ArrayList<Entity> entities = Game.getEntities();
		entities.clear();
		entities.add(new Hero(new Point(1, 1), 'H'));
		if (guardType.equals("Suspicious")){
			entities.add(new SuspiciousGuard(new Point(8, 1), 'G'));
		}else if (guardType.equals("Drunken")){
			entities.add(new DrunkenGuard(new Point(8, 1), 'G'));
		}else{
			entities.add(new RookieGuard(new Point(8, 1), 'G'));
		}
		Game.setEntities(entities);
		Game.setState("Playing");
		Game.setHeroIndex(0);
	}
	
	@Override
	public void nextMap() {
		Game.changeMap("Keep","");
	}
}
