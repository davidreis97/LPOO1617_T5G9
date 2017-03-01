package dkeep.logic;

import java.awt.Point;

public class Game {
	private static Map currMap;
	private Entity entities[];
	private static String state; //ASK enum?
	
	//TODO init game function?
	public Game() {
		Entity entities[] = new Entity[2];
		entities[0] = new Hero(new Point(1, 1), 'H');
		entities[1] = new NormalGuard(new Point(8, 1), 'G');
		//entities[1] = new Ogre(new Point(8, 1), '0');
		this.entities = entities;
		Game.currMap = new DungeonMap();
		Game.state = "Playing";
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
	
	public static Point move(Point coords, char direction) {
		
		Point newCoords = calcNewCoords(coords, direction);
		
		if(currMap.doMove(newCoords)) {
			return newCoords;
		} else return coords;
	}
	
	//Checks adjacency (no diagonals) to Guard, Ogre and Ogre's club
	private boolean isAdjacent(int index) {
		
		Point checkCoords = entities[index].coords;
		Point adj_1 = calcNewCoords(checkCoords, 'w');
		Point adj_2 = calcNewCoords(checkCoords, 'a');
		Point adj_3 = calcNewCoords(checkCoords, 's');
		Point adj_4 = calcNewCoords(checkCoords, 'd');
		
		for(int i = 0; i < entities.length; i++) {
			if(index != i) {
				if(entities[i].coords.equals(adj_1)) {
					return true;
				} else if(entities[i].coords.equals(adj_2)) {
					return true;
				} else if(entities[i].coords.equals(adj_3)) {
					return true;
				} else if(entities[i].coords.equals(adj_4)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void updateGame(char userInput) {

		int heroIndex;
		
		for(heroIndex = 0; heroIndex < entities.length; heroIndex++) {
			if(entities[heroIndex] instanceof Hero) {
				entities[heroIndex].coords = move(entities[heroIndex].coords, userInput);
				break; //CAUTION assumes one Hero
			}
		}
		
		if(isAdjacent(heroIndex)) {
			state = "Lose";
			return;
		}
		
		for(int i = 0; i < entities.length; i++) {
			if(heroIndex != i) {
				entities[i].nextMovement();
			}
		}
		
		if(isAdjacent(heroIndex)) {
			state = "Lose";
			return;
		}
	}
	
	public char[][] getMap() {
		return currMap.getMap();
	}

	public Entity[] getEntities() {
		return entities;
	}

	public static String getState() {
		return state;
	}

	public static void setState(String state) {
		Game.state = state;
	}
}