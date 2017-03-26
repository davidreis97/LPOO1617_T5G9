package dkeep.logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class implementing Map interface with Keep specific behavior.
 */
public class KeepMap implements Map, Serializable {
	
	private int width;
	private int height;
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

	/**
	 * Class constructor, uses default width and height.
	 */
	public KeepMap() {
		this.width = 10;
		this.height = 10;
	}
	
	/**
	 * Class constructor, allows custom width and height.
	 *
	 * @param  width map width
	 * @param  height map height
	 */
	public KeepMap(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public char[][] getMap() {
		
		char[][] tempMap = new char[height][width];

		for(int i = 0; i < keepMap.length; i++) {
			tempMap[i] = keepMap[i].clone();
		}

		return tempMap;
	}
	
	@Override
	public void checkDoors(String entityType) {
		
		if(entityType.equals("Hero") && heroHasKey) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					if(keepMap[y][x] == 'I') {
						keepMap[y][x] = 'S';
					}
				}
			}
		}
	}
	
	@Override
	public boolean doMove(Point coords, String entityType, int index) {
		
		if(outOfBounds(coords)) return false;
		
		char collision = keepMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			revertRepresentation(index, entityType);
			return true;
		case 'I':
			checkDoors(entityType);
			return false;
		case 'k':
			keyCollision(index, entityType, coords);
			return true;
		case 'S':
			nextMap();
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public void initMap(String guardType) {
		
		ArrayList<Entity> entities = Game.getEntities();
		entities.clear();
		entities.add(new Hero(new Point(1, 8), 'A', true));
		
		Random ogreCoords = new Random();
		
		for(int i = 0; i < Game.getNumOgres(); i++) {
			
			Point ogreRandCoords = new Point(ogreCoords.nextInt(8) + 1, ogreCoords.nextInt(5) + 1);
			
			entities.add(new Ogre(ogreRandCoords, '0'));
			entities.add(new Club(ogreRandCoords, '*'));
		}
		
		Game.setEntities(entities);
		Game.setState("Playing");
		Game.setHeroIndex(0);
	}
	
	@Override
	public void nextMap() {
		Game.setState("Win");
	}
	
	/**
	 * Handles key collision for different entities.
	 * 
	 * @param  index      index of current Entity from entities array
	 * @param  entityType Entity derived class name
	 * @param  coords     current Entity coords
	 */
	private void keyCollision(int index, String entityType, Point coords) {
		if(entityType.equals("Hero")) {
			Game.getEntities().get(index).setRepresentation('K');
			keepMap[coords.y][coords.x] = ' ';
			setHeroHasKey(true);
		} else if(entityType.equals("Ogre") || entityType.equals("Club")) {
			Game.getEntities().get(index).setRepresentation('$');
		}
	}
	
	/**
	 * Changes representation of Ogre/Club back to what it was before stepping on door key.
	 * 
	 * @param  index      index of current Entity from entities array
	 * @param  entityType Entity derived class name
	 */
	private void revertRepresentation(int index, String entityType) {
		if(Game.getEntities().get(index).getRepresentation() == '$' && entityType == "Ogre") {
			Game.getEntities().get(index).setRepresentation('0');
		}else if(Game.getEntities().get(index).getRepresentation() == '$' && entityType == "Club") {
			Game.getEntities().get(index).setRepresentation('*');
		}
	}
	
	/**
	 * Bounds checking to avoid exceptions.
	 * 
	 * @param  coords current Entity coords
	 * @return        whether coords are in bounds
	 */
	private boolean outOfBounds(Point coords) {
		if(coords.x >= width || coords.x < 0 || coords.y >= height || coords.y < 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return map width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return map height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return whether Hero entity has door key
	 */
	public boolean getHeroHasKey() {
		return heroHasKey;
	}

	/**
	 * @param  heroHasKey set whether Hero entity has door key
	 */
	public void setHeroHasKey(boolean heroHasKey) {
		this.heroHasKey = heroHasKey;
	}
	
	/**
	 * @param  newMap set new map array
	 */
	public void setMap(char newMap[][]){
		keepMap = newMap;
	}
}
