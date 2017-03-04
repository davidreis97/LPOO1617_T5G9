package dkeep.logic;

import java.awt.Point;
import java.util.ArrayList;

public class Game {
	private static Map currMap;
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static String state; //ASK replace strings with enums overall
	private static int heroIndex; //CAUTION assumes one Hero
	
	public Game(String startingMap) {
		changeMap(startingMap); //Starting map
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
	
	//Checks adjacency (no diagonals) to Guard, Ogre and Ogre's club
	private static boolean isAdjacent(int index) {
		
		Point checkCoords = entities.get(index).coords;
		Point adj_1 = calcNewCoords(checkCoords, 'w');
		Point adj_2 = calcNewCoords(checkCoords, 'a');
		Point adj_3 = calcNewCoords(checkCoords, 's');
		Point adj_4 = calcNewCoords(checkCoords, 'd');
		
		for(int i = 0; i < entities.size(); i++) {
			if(index != i) {
				if(entities.get(i).coords.equals(adj_1)) {
					return true;
				} else if(entities.get(i).coords.equals(adj_2)) {
					return true;
				} else if(entities.get(i).coords.equals(adj_3)) {
					return true;
				} else if(entities.get(i).coords.equals(adj_4)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void updateGame(char userInput, boolean debugging) {

		entities.get(heroIndex).coords = move(entities.get(heroIndex).coords, userInput, "Hero", heroIndex);

		if(isAdjacent(heroIndex)) {
			state = "Lose";
			return;
		}
		
		//TODO delete
//		entities.get(1).coords = move(entities.get(1).coords, userInput, "Ogre", 1);
//		entities.get(2).setCoords(entities.get(1).coords);
//		entities.get(2).coords = move(entities.get(2).coords, 'w', "Club", 2);
		
		for(int i = 0; i < entities.size(); i++) {
			if(heroIndex != i 
					&& !(entities.get(i).getClass().getSimpleName().equals("Ogre") && debugging) 
					&& !(entities.get(i).getClass().getSimpleName().equals("Club") && debugging)) {
				entities.get(i).nextMovement(i);
			}
		}
		
		if(isAdjacent(heroIndex)) {
			state = "Lose";
			return;
		}
	}
	
	public static void changeMap(String mapType) { //ASK adding new maps considered extension or modification?
		
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
		
		currMap.initMap();
	}
	
	public static char[][] getMap() {
		return currMap.getMap();
	}
	
	public static Map getMapObject(){
		return currMap;
	}
	
	public static void setMapObject(DungeonMap map){
		currMap = map;
	}
	
	public static void setMapObject(KeepMap map){
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
	
}