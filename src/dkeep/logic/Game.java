package dkeep.logic;

import java.awt.Point;
import java.util.ArrayList;

public class Game {
	private static Map currMap;
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static String state; //TODO replace state with enums
	private static int heroIndex;
	private static int numOgres;
	
	public Game(String startingMap, String guardtype, int numOgres) {
		setNumOgres(numOgres);
		changeMap(startingMap, guardtype); //Starting map
	}

	//Returns new coords based on given coords and direction
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
	
	public static Point move(Point coords, char direction, String entityType, int index) {
		
		Point newCoords = calcNewCoords(coords, direction);
		
		if(currMap.doMove(newCoords, entityType, index)) {
			return newCoords;
		} else return coords;
	}
	
	//TODO return int array
	//Returns index of adjacent entities to "index" entity
	public static int isAdjacentTo(int index) {
		
		Point checkCoords = entities.get(index).coords;
		Point adj_1 = calcNewCoords(checkCoords, 'w');
		Point adj_2 = calcNewCoords(checkCoords, 'a');
		Point adj_3 = calcNewCoords(checkCoords, 's');
		Point adj_4 = calcNewCoords(checkCoords, 'd');
		
		boolean found = false;
		
		int i = 0;
		for(i = 0; i < entities.size(); i++) {
			if(index != i) {
				if(entities.get(i).coords.equals(adj_1)) {
					found = true;
					break;
				} else if(entities.get(i).coords.equals(adj_2)) {
					found = true;
					break;
				} else if(entities.get(i).coords.equals(adj_3)) {
					found = true;
					break;
				} else if(entities.get(i).coords.equals(adj_4)) {
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			return i;
		} else return -1;
	}
	
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
	
	public static void updateGame(char userInput, boolean debugging) {

		Hero hero = (Hero) entities.get(heroIndex);
		
		if(entityCol(heroIndex, userInput) == -1) {
			hero.coords = move(hero.coords, userInput, "Hero", heroIndex);
		}
		
		if(hero.doCollision(heroIndex, 1)) {
			state = "Lose";
			return;
		}
		
		for(int i = 0; i < entities.size(); i++) {
			if(heroIndex != i 
					&& !(entities.get(i).getClass().getSimpleName().equals("Ogre") && debugging) 
					&& !(entities.get(i).getClass().getSimpleName().equals("Club") && debugging)) {
				entities.get(i).nextMovement(i);
			}
		}
		
		if(hero.doCollision(heroIndex, 2)) {
			state = "Lose";
			return;
		}
	}
	
	public static void changeMap(String mapType, String guardtype) { //TODO try to remove changeMap from Game.java
		
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
		
		currMap.initMap(guardtype);
	}
	
	public static char[][] getMap() {
		return currMap.getMap();
	}
	
	public static Map getMapObject(){
		return currMap;
	}
	
	public static void setMapObject(Map map){
		currMap = map;
	}

	public static ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public static String getState() {
		return state;
	}
	
	public static int getHeroIndex() {
		return heroIndex;
	}
	
	public static void setEntities(ArrayList<Entity> entities) {
		Game.entities = entities;
	}

	public static void setState(String state) {
		Game.state = state;
	}

	public static void setHeroIndex(int heroIndex) {
		Game.heroIndex = heroIndex;
	}

	public static int getNumOgres() {
		return numOgres;
	}

	public static void setNumOgres(int numOgres) {
		Game.numOgres = numOgres;
	}
	
}