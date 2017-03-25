package dkeep.logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

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
	
	//Copies map so the original isn't modified
	public char[][] getMap() {
		
		char[][] tempMap = new char[dungeonMap.length][dungeonMap.length]; //CAUTION assumes map is square
		
		for(int i = 0; i < dungeonMap.length; i++) {
			tempMap[i] = dungeonMap[i].clone();
		}

		return tempMap;
	}
	
	public boolean doMove(Point coords, String entityType, int index) { //CAUTION Entity type and index are unused variables
		
		char collision = dungeonMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			return true;
		case 'k':
			openDoors();
			return false;
		case 'S':
			nextMap();
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public void openDoors(){
		dungeonMap[5][0] = 'S';
		dungeonMap[6][0] = 'S';
	}

	public void initMap(String guardtype) {
		ArrayList<Entity> entities = Game.getEntities();
		entities.clear();
		entities.add(new Hero(new Point(1, 1), 'H'));
		if (guardtype.equals("Suspicious")){
			entities.add(new SuspiciousGuard(new Point(8, 1), 'G'));
		}else if (guardtype.equals("Drunken")){
			entities.add(new DrunkenGuard(new Point(8, 1), 'G'));
		}else{
			entities.add(new RookieGuard(new Point(8, 1), 'G'));
		}
		Game.setEntities(entities);
		Game.setState("Playing");
		Game.setHeroIndex(0);
	}
	
	public void nextMap() {
		Game.changeMap("Keep","");
	}
}
