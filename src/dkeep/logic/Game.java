package dkeep.logic;

import java.awt.Point;

public class Game {
	private Map currMap;
	private Entity entities[];
	private static String state; //ASK enum?
	
	//TODO init game function?
	public Game() {
		Entity entities[] = new Entity[2];
		entities[0] = new Hero(new Point(1, 1), 'H');
		entities[1] = new Guard(new Point(8, 1), 'G');
		this.entities = entities;
		this.currMap = new DungeonMap();
		state = "Playing";
	}
	
	
	//Returns new coords based on given coords and direction
	private Point calcNewCoords(Point coords, char direction) {
		
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
	
	public void updateGame(char userInput) {
		
		Point heroNewCoords = new Point(0, 0);
		int i;
		
		for(i = 0; i < entities.length; i++) {
			if(entities[i] instanceof Hero) {
				heroNewCoords = calcNewCoords(entities[i].coords, userInput);
				break; //CAUTION assumes one Hero
			}
		}
		
		if(currMap.doMove(heroNewCoords)) {
			entities[i].coords = heroNewCoords;
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