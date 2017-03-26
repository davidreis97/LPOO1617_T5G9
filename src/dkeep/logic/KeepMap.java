package dkeep.logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class KeepMap implements Map,Serializable {
	
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
	public char[][] getMap() {
		
		char[][] tempMap = new char[keepMap.length][keepMap.length]; //CAUTION assumes map is square
		
		for(int i = 0; i < keepMap.length; i++) {
			tempMap[i] = keepMap[i].clone();
		}

		return tempMap;
	}
	
	public boolean doMove(Point coords, String entityType, int index) {
		
		if(coords.x >= 10 || coords.x < 0 || coords.y >= 10 || coords.y < 0){
			return false;
		}
		
		char collision = keepMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			if(Game.getEntities().get(index).getRepresentation() == '$' && entityType == "Ogre") {
				Game.getEntities().get(index).setRepresentation('0');
			}else if(Game.getEntities().get(index).getRepresentation() == '$' && entityType == "Club") {
				Game.getEntities().get(index).setRepresentation('*');
			}
			return true;
		case 'k':
			if(entityType.equals("Hero")) {
				Game.getEntities().get(index).setRepresentation('K');
				keepMap[coords.y][coords.x] = ' ';
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

	public void initMap(String guardtype) {
		
		ArrayList<Entity> entities = Game.getEntities();
		entities.clear();
		entities.add(new Hero(new Point(1, 8), 'A', true));
		
		Random ogreCoords = new Random();
		
		for(int i = 0; i < Game.getNumOgres(); i++) {
			
			Point ogreRandCoords = new Point(ogreCoords.nextInt(8) + 1, ogreCoords.nextInt(6) + 1);
			
			entities.add(new Ogre(ogreRandCoords, '0'));
			entities.add(new Club(ogreRandCoords, '*'));
		}
		
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
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				if(keepMap[x][y] == 'I'){
					keepMap[x][y] = 'S';
				}
			}
		}
	}
	
	public void setMap(char newMap[][]){
		keepMap = newMap;
	}
}
