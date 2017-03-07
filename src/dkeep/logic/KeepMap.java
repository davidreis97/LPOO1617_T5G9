package dkeep.logic;

import java.awt.Point;
import java.util.ArrayList;

public class KeepMap implements Map {
	
	private boolean heroHasKey = false;
	private char keepMap[][] =
           {{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'k', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};

	//Copies map so the original isn't modified
	public char[][] getMap() { //ASK how to make immutable
		
		char[][] tempMap = new char[keepMap.length][keepMap.length]; //CAUTION assumes map is square
		
		for(int i = 0; i < keepMap.length; i++) {
			tempMap[i] = keepMap[i].clone();
		}

		return tempMap;
	}
	
	public boolean doMove(Point coords, String entityType, int index) {
		
		char collision = keepMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			if(Game.getEntities().get(index).getRepresentation() == '$') { //TODO use flags?
				switch(entityType) {
				case "Ogre":
					Game.getEntities().get(index).setRepresentation('0');
					break;
				case "Club":
					Game.getEntities().get(index).setRepresentation('*');
				}
			}
			return true;
		case 'k':
			if(entityType.equals("Hero")) {
				Game.getEntities().get(index).setRepresentation('K');
				keepMap[1][8] = ' ';
				setHeroHasKey(true);
			} else if(entityType.equals("Ogre") || entityType.equals("Club")) {
				Game.getEntities().get(index).setRepresentation('$');
			}
			return true;
		case 'I':
			if(entityType.equals("Hero")) {
				if(getHeroHasKey()) {
					openDoors();
				}
			}
			return false;
		case 'S':
			nextMap();
			return true;
		default:
			return false;
		}
	}

	public void initMap() {
		ArrayList<Entity> entities = Game.getEntities();
		entities.clear();
		entities.add(new Hero(new Point(1, 8), 'H'));
		entities.add(new Ogre(new Point(4, 1), '0'));
		entities.add(new Club(new Point(4, 2), '*'));
		Game.setEntities(entities);
		Game.setState("Playing");
		Game.setHeroIndex(0);
	}

	public void nextMap() {
		Game.setState("Win");
	}

	public boolean getHeroHasKey() {
		return heroHasKey;
	}

	public void setHeroHasKey(boolean heroHasKey) {
		this.heroHasKey = heroHasKey;
	}

	@Override
	public void openDoors() {
		keepMap[1][0] = 'S';
	}
}
