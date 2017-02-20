package dkeep.logic;

import java.awt.Point;

public class Game {

	private boolean running;
	private Map currMap;
	private int mapCounter; //Keeps track of current map
	
	public Game(){
		setCurrMap(mapInit_1());
		mapCounter = 1; //Keeps track of current map
		running = true;
	}
	
	private static char dungeonMap_1[][] =
        {{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
			{'X', 'H', ' ', ' ', 'I', ' ', 'X', ' ', 'G', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
			{'X', ' ', 'I', ' ', 'I', ' ', 'X', ' ', ' ', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', 'X', ' ', 'X'},
			{'X', ' ', 'I', ' ', 'I', ' ', 'X', 'k', ' ', 'X'},
			{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};

	private static char dungeonMap_2[][] =
        {{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
			{'I', ' ', ' ', ' ', '0', ' ', ' ', ' ', 'k', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'H', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};

	private static char guardPath[] = {'a', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a', 'a', 's',
		                           'd', 'd', 'd', 'd', 'd', 'd', 'd', 'w', 'w', 'w', 'w', 'w'};
	

	//Initialize 1st map values
	public static Map mapInit_1() {
		
		Hero hero = new Hero(new Point(1, 1));
		
		//Guard setup
		Guard guard = new Guard(new Point(8, 1), guardPath);
		Guard guards[] = new Guard[1];
		guards[0] = guard;

		return new Map(hero, dungeonMap_1, guards);
	}

	//Initialize 2nd map values
	public static Map mapInit_2() {
		
		Hero hero = new Hero(new Point(1, 8));
		
		//Ogre setup
		Ogre ogre = new Ogre(new Point(4, 1), new Point(3, 1));
		Ogre ogres[] = new Ogre[1];
		ogres[0] = ogre;
		
		return new Map(hero, dungeonMap_2, ogres);
	}

	public boolean getRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void nextTick(char nextInput){
		
		String status = getCurrMap().updateMap(nextInput, mapCounter);
		
		if(status.equals("Exit")) {
			if(mapCounter == 1) { //Change to map 2
				mapCounter++;
				setCurrMap(mapInit_2());
			} else { //Exited map 2
				System.out.println("You won!");
				running = false;
				return;
			}
		} else if(status.equals("Caught")) {
			System.out.println("You lost!");
			running = false;
			return;
		}
		
		getCurrMap().printMap();
	}

	public Map getCurrMap() {
		return currMap;
	}

	public void setCurrMap(Map currMap) {
		this.currMap = currMap;
	}
}
