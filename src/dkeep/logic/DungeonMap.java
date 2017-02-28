package dkeep.logic;

import java.awt.Point;

public class DungeonMap implements Map {

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
	public char[][] getMap() { //ASK how to make immutable
		
		char[][] tempMap = new char[dungeonMap.length][dungeonMap.length]; //CAUTION assumes map is square
		
		for(int i = 0; i < dungeonMap.length; i++) {
			tempMap[i] = dungeonMap[i].clone();
		}

		return tempMap;
	}
	
	public boolean doMove(Point coords) {
		
		char collision = dungeonMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			return true;
		case 'k':
			dungeonMap[5][0] = 'S';
			dungeonMap[6][0] = 'S';
			return false;
		case 'S':
			Game.setState("Win");
			return true;
		default:
			return false;
		}
	}
}
