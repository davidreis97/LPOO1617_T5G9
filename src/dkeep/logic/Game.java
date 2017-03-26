package dkeep.logic;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Class responsible for all game logic and game world data.<br>
 * Stores current map and entities and allows manipulation of entities.
 */
public class Game {
	private static Map currMap;
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static String state;
	private static int heroIndex;
	private static int numOgres;
	private static String guardType;
	
	/**
	 * Class constructor.
	 *
	 * @param  startingMap Map object to start game with
	 * @param  guardType   Type of guard to use
	 * @param  numOgres    Number of Ogres for Keep level
	 */
	public Game(String startingMap, String guardType, int numOgres) {
		setGuardType(guardType);
		setNumOgres(numOgres);
		changeMap(startingMap, guardType); //Starting map
	}

	/**
	 * Calculates new coords based on current coords and a direction.
	 * 
	 * @param  coords     current Entity coords
	 * @param  direction  movement direction
	 * @return            new coords for Entity (same if invalid direction)
	 */
	private static Point calcNewCoords(Point coords, char direction) {
		
		Point newCoords = new Point(coords.x, coords.y);
		
		switch(direction) {
		case 'w':
			newCoords.y--;
			break;
		case 's':
			newCoords.y++;
			break;
		case 'a':
			newCoords.x--;
			break;
		case 'd':
			newCoords.x++;
			break;
		}
		
		return newCoords;
	}
	
	/**
	 * Attempts to move an Entity to new coords according to direction.
	 * 
	 * @param  coords     current Entity coords
	 * @param  direction  movement direction
	 * @param  entityType Entity derived class name
	 * @param  index      index of current Entity from entities array
	 * @return            new coords for Entity (same if no movement)
	 */
	public static Point move(Point coords, char direction, String entityType, int index) {
		
		Point newCoords = calcNewCoords(coords, direction);
		
		if(currMap.doMove(newCoords, entityType, index)) {
			return newCoords;
		} else return coords;
	}
	
	/**
	 * Checks entities adjacent to given Entity, passed as an index of the entities array.
	 * 
	 * @param  index index of current Entity from entities array
	 * @return       array of indices of adjacent entities
	 */
	public static ArrayList<Integer> isAdjacentTo(int index) {
		
		ArrayList<Integer> adjacencies = new ArrayList<Integer>();
		
		Point checkCoords = entities.get(index).coords;
		Point adj_1 = calcNewCoords(checkCoords, 'w');
		Point adj_2 = calcNewCoords(checkCoords, 'a');
		Point adj_3 = calcNewCoords(checkCoords, 's');
		Point adj_4 = calcNewCoords(checkCoords, 'd');
		
		int i = 0;
		for(i = 0; i < entities.size(); i++) {
			if(index != i) {
				if(entities.get(i).coords.equals(adj_1) || entities.get(i).coords.equals(adj_2) ||
						entities.get(i).coords.equals(adj_3) || entities.get(i).coords.equals(adj_4)) {
					adjacencies.add(i);
				}
			}
		}
		
		return adjacencies;
	}
	
	/**
	 * Checks if Entity collides with a Entity when moving in a specified direction.
	 * 
	 * @param  index     index of Entity to test from entities array
	 * @param  direction movement direction
	 * @return           index of Entity that collided or -1 if no collision
	 */
	public static int entityCol(int index, char direction) {
		
		Point testCoords = calcNewCoords(entities.get(index).coords, direction);
		
		boolean found = false;
		
		int i = 0;
		for(i = 0; i < entities.size(); i++) {
			if(index != i) {
				if(entities.get(i).coords.equals(testCoords)) {
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			return i;
		} else return -1;
	}
	
	/**
	 * Updates all game logic and game world data by calling movement functions from every Entity, doing Entity collision and checking for game end.
	 * 
	 * @param  userInput    movement direction inputted by user
	 * @param  doEntityMove if false only player moves
	 */
	public static void updateGame(char userInput, boolean doEntityMove) {

		Hero hero = (Hero) entities.get(heroIndex);
		
		if(entityCol(heroIndex, userInput) == -1) hero.coords = move(hero.coords, userInput, "Hero", heroIndex);
		
		if(hero.doCollisionStage1(isAdjacentTo(heroIndex))) { 
			state = "Lose";
			return;
		}
		
		if(doEntityMove) {
			for(int i = 0; i < entities.size(); i++) {
				if(heroIndex != i) entities.get(i).nextMovement(i);
			}
		}

		if(hero.doCollisionStage2(isAdjacentTo(heroIndex))) {
			state = "Lose";
			return;
		}
	}
	
	/**
	 * Changes current Map with a new Map of given type and initializes it.
	 * 
	 * @param  mapType type of map to instantiate (Dungeon or Keep)
	 * @param  guardType type of guard to instantiate (Rookie, Drunken, Suspicious)
	 */
	public static void changeMap(String mapType, String guardType) {
		
		switch(mapType) {
		case "Dungeon":
			currMap = new DungeonMap();
			break;
		case "Keep":
			currMap = new KeepMap();
			break;
		default:
			System.out.println("Error: no map initialized");
		}
		
		currMap.initMap(guardType);
	}
	
	/**
	 * @return map of current game Map object
	 */
	public static char[][] getMap() {
		return currMap.getMap();
	}
	
	/**
	 * @return current game Map object
	 */
	public static Map getMapObject(){
		return currMap;
	}
	
	/**
	 * @return current game entities
	 */
	public static ArrayList<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * @return current game state
	 */
	public static String getState() {
		return state;
	}
	
	/**
	 * @return Hero object index in entities array
	 */
	public static int getHeroIndex() {
		return heroIndex;
	}
	
	/**
	 * @return number of Ogres in play
	 */
	public static int getNumOgres() {
		return numOgres;
	}

	/**
	 * @return type of Guard object in play
	 */
	public static String getGuardType() {
		return guardType;
	}
	
	/**
	 * @param map Map object to set
	 */
	public static void setMapObject(Map map){
		currMap = map;
	}

	/**
	 * @param entities new array of entities to set
	 */
	public static void setEntities(ArrayList<Entity> entities) {
		Game.entities = entities;
	}

	/**
	 * @param state new game state to set
	 */
	public static void setState(String state) {
		Game.state = state;
	}

	/**
	 * @param heroIndex new index (in entities array) for Hero object to set
	 */
	public static void setHeroIndex(int heroIndex) {
		Game.heroIndex = heroIndex;
	}

	/**
	 * @param numOgres new number of ogres to set
	 */
	public static void setNumOgres(int numOgres) {
		Game.numOgres = numOgres;
	}

	/**
	 * @param guardType new type of Guard to set, doesn't actually change current Guard object, just String representation of it
	 */
	public static void setGuardType(String guardType) {
		Game.guardType = guardType;
	}
	
}