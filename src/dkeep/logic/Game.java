package dkeep.logic;

import java.awt.Point;
import java.util.Random;

public class Game {

	private boolean running;
	private Map currMap;
	private int mapCounter; //Keeps track of current map
	
	public Game() {
		setCurrMap(mapInit_2());
		mapCounter = 2;
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
			{'X', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};

	private static char guardPath[] = {'a', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a', 'a', 's',
		                           'd', 'd', 'd', 'd', 'd', 'd', 'd', 'w', 'w', 'w', 'w', 'w'};

	//Initialize 1st map values
	public static Map mapInit_1() {
		
		Hero hero = new Hero(new Point(1, 1), false);
		
		//Guard setup
		Guard guard = new Guard(new Point(8, 1), guardPath);
		Guard guards[] = new Guard[1];
		guards[0] = guard;

		return new Map(hero, dungeonMap_1, guards);
	}

	//Initialize 2nd map values
	public static Map mapInit_2() {
		
		Hero hero = new Hero(new Point(1, 8), true);
		
		Random rand = new Random();
		int ogreN = rand.nextInt(3) + 1;
		Ogre ogres[] = new Ogre[ogreN];
		
		for(int n = 0; n < ogreN; n++) {
			ogres[n] = new Ogre(new Point(4, 1), new Point(3, 1));
		}
		
		return new Map(hero, dungeonMap_2, ogres);
	}
	
	public void nextTick(char nextInput) {
		
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
	}
	
	public boolean getRunning() {
		return running;
	}

	public Map getCurrMap() {
		return currMap;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setCurrMap(Map currMap) {
		this.currMap = currMap;
	}
}
