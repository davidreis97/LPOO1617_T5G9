package dkeep.logic;

import java.awt.Point;

public class Map {
	
	private Hero hero;
	private Guard guards[];
	private Ogre ogres[];
	private char dungeonMap[][];
	
	public Map(Hero hero, char dungeonMap[][], Ogre ogres[]){
		this.hero = hero;
		this.dungeonMap = dungeonMap;
		this.guards = new Guard[0];
		this.ogres = ogres;
	}
	
	public Map(Hero hero, char dungeonMap[][], Guard guards[]){
		this.hero = hero;
		this.dungeonMap = dungeonMap;
		this.guards = guards;
		this.ogres = new Ogre[0];
	}
	
	//Check if coords are in map bounds
	private boolean isInBounds(Point coords) {
		if(coords.y >= this.dungeonMap.length || coords.y < 0) {
			return false;
		} else if(coords.x >= this.dungeonMap[coords.y].length || coords.x < 0) {
			return false;
		}
		
		return true;
	}
	
	//Returns a string with collision type based on the char at coords
	private String checkCollisionType(Point coords) {
		
		if(!isInBounds(coords)) {
			return "Error";
		}
		
		char collision = dungeonMap[coords.y][coords.x];
		
		switch(collision) {
		case ' ':
			return "Empty";
		case 'X':
			return "Wall";
		case '0':
			return "Ogre";
		case 'k':
			return "Key"; //Works for key/lever depending on current map
		case '*':
			return "Club";
		case 'G':
			return "Guard";
		case 'H':
			return "Hero";
		case 'S':
			return "OpenDoor"; //Works for stairs/doors depending on current map
		case 'I':
			return "Door";
		case 'K':
			return "HeroKey";
		default:
			return "Error";
		}
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
	
	//Update ogre position using old coords and random direction
	//CAUTION if ogre is blocked (not by hero), crashes
	private void updateOgrePosition() {

		for(int i = 0; i < ogres.length; i++) {
			
			Point ogreOld = ogres[i].getOgreCoords();
			Point ogreClubOld = ogres[i].getOgreClubCoords();
			String result; Point ogreNewCoords, ogreClubNewCoords;
		
			//Clear previous position with ' ' or 'k'
			dungeonMap[ogreOld.y][ogreOld.x] = ogres[i].getOgreChar();
			if(dungeonMap[ogreClubOld.y][ogreClubOld.x] != '0') {
				dungeonMap[ogreClubOld.y][ogreClubOld.x] = ogres[i].getOgreClubChar();
			}
			
			do { //Force valid movement
				
				char ogreDirection = ogres[i].generateNewDirection();
				
				ogreNewCoords = calcNewCoords(ogreOld, ogreDirection); //New coords based on old ogre position
				result = checkCollisionType(ogreNewCoords);
				
				//Ogre movement collision resolving
				if(result.equals("Empty") || result.equals("Club") || result.equals("Ogre")) {
					dungeonMap[ogreNewCoords.y][ogreNewCoords.x] = '0';
					ogres[i].setOgreCoords(ogreNewCoords);
					ogres[i].setOgreChar(' ');
				} else if(result.equals("Key")) {
					dungeonMap[ogreNewCoords.y][ogreNewCoords.x] = '$';
					ogres[i].setOgreCoords(ogreNewCoords);
					ogres[i].setOgreChar('k'); //Next clear char will be 'k'
				}
			
			} while(result != "Empty" && result != "Key" && result != "Ogre");

			do { //Force valid club position
				
				char clubDirection = ogres[i].generateNewDirection();
				
				ogreClubNewCoords = calcNewCoords(ogreNewCoords, clubDirection); //New club coords based on the new ogre position
				result = checkCollisionType(ogreClubNewCoords);
				
				//Ogre club collision resolving
				if(result.equals("Empty")) {
					dungeonMap[ogreClubNewCoords.y][ogreClubNewCoords.x] = '*';
					ogres[i].setOgreClubCoords(ogreClubNewCoords);
					ogres[i].setOgreClubChar(' ');
				} else if(result.equals("Key")) {
					dungeonMap[ogreClubNewCoords.y][ogreClubNewCoords.x] = '$';
					ogres[i].setOgreClubCoords(ogreClubNewCoords);
					ogres[i].setOgreClubChar('k'); //Next clear char will be 'k'
				}
			
			} while(result != "Empty" && result != "Key");
		}
	}
	
	//Update guard position using old coords and patrol path
	private void updateGuardPosition() {
		
		for(int i = 0; i < guards.length; i++) {
			
			//Handle logic for guard types, sleeping, reversing path, etc
			guards[i].guardTick();
			
			Point guardOldCoords = guards[i].getGuardCoords();
			
			//Don't update position if sleeping
			if(guards[i].getGuardState().equals("Sleeping")) {
				
				dungeonMap[guardOldCoords.y][guardOldCoords.x] = guards[i].getGuardChar();
			//Update position
			} else {

				dungeonMap[guardOldCoords.y][guardOldCoords.x] = ' ';
				
				Point guardNewCoords;
				
				//Move in opposite direction if movement is reversed
				if(guards[i].isReversed()) {
					switch(guards[i].getGuardPath()[guards[i].getStepCounter()]) {
					case 'w':
						guardNewCoords = calcNewCoords(guardOldCoords, 's');
						break;
					case 's':
						guardNewCoords = calcNewCoords(guardOldCoords, 'w');
						break;
					case 'a':
						guardNewCoords = calcNewCoords(guardOldCoords, 'd');
						break;
					case 'd':
						guardNewCoords = calcNewCoords(guardOldCoords, 'a');
						break;
					default:
						guardNewCoords = guardOldCoords;
						break;
					}
				} else {
					guardNewCoords = calcNewCoords(guardOldCoords, guards[i].getGuardPath()[guards[i].getStepCounter()]);
				}

				//CAUTION not checking guard collision
				dungeonMap[guardNewCoords.y][guardNewCoords.x] = guards[i].getGuardChar();
				guards[i].setGuardCoords(guardNewCoords);
				
				//Update step count and reset if path has looped
				guards[i].updateStepCounter();
			}
		}
	}
	
	//Checks adjacency (no diagonals) to Guard, Ogre and Ogre's club
	private boolean isAdjacent(Point coords, int range, String eligible) {
		
		boolean r_1, r_2, r_3, r_4;
		r_1 = r_2 = r_3 = r_4 = false;

		if(isInBounds(calcNewCoords(coords, 'w'))) {
			r_1 = eligible.contains("" + dungeonMap[coords.y - range][coords.x]);
		}
		
		if(isInBounds(calcNewCoords(coords, 's'))) {
			r_2 = eligible.contains("" + dungeonMap[coords.y + range][coords.x]);
		}
		
		if(isInBounds(calcNewCoords(coords, 'a'))) {
			r_3 = eligible.contains("" + dungeonMap[coords.y][coords.x - range]);
		}
		
		if(isInBounds(calcNewCoords(coords, 'd'))) {
			r_4 = eligible.contains("" + dungeonMap[coords.y][coords.x + range]);
		}
		
		return (r_1 || r_2 || r_3 || r_4);
	}
	
	//Prints a NxM dungeon map
	public void printMap() {
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
	//Updates game state and handles hero movement
	public String updateMap(char kbdInput, int stage) {
		
		Point heroOldCoords = hero.getHeroCoords();
		dungeonMap[heroOldCoords.y][heroOldCoords.x] = ' ';
		
		//New coords based on old hero position and player input
		Point heroNewCoords = calcNewCoords(heroOldCoords, kbdInput);
		String result;

		result = checkCollisionType(heroNewCoords);
		
		//Hero collision resolving
		if(result == "Empty") {
			dungeonMap[heroNewCoords.y][heroNewCoords.x] = hero.getHeroChar();
			hero.setHeroCoords(heroNewCoords);
		} else if(result == "Key") {
			if(stage == 1) { //Map 1 lever behavior
				for(int i = 0; i < dungeonMap.length; i++) {
					if(dungeonMap[i][0] == 'I') {
						dungeonMap[i][0] = 'S';
					}
				}
				dungeonMap[heroOldCoords.y][heroOldCoords.x] = hero.getHeroChar();
			} else { //Map 2 key behavior
				hero.setHeroChar('K');
				dungeonMap[heroNewCoords.y][heroNewCoords.x] = hero.getHeroChar();
				hero.setHeroCoords(heroNewCoords);
			}
		} else if(result == "OpenDoor") {
			dungeonMap[heroNewCoords.y][heroNewCoords.x] = hero.getHeroChar();
			hero.setHeroCoords(heroNewCoords);
			return "Exit";
		} else if(result == "Door" && stage == 2 && hero.getHeroChar() == 'K') { //Hero opens door on map 2
			dungeonMap[heroOldCoords.y][heroOldCoords.x] = hero.getHeroChar();
			dungeonMap[heroNewCoords.y][heroNewCoords.x] = 'S';
		} else dungeonMap[heroOldCoords.y][heroOldCoords.x] = hero.getHeroChar();
		
		//Check adjacency before enemy movement
		if(isAdjacent(heroNewCoords, 1, "G*$")) {
			return "Caught";
		}
		
		//Check if player moved near ogres to stun
		for(int i = 0; i < ogres.length; i++) {
			if(isAdjacent(ogres[i].getOgreCoords(), 1, "AK")) {
				ogres[i].setOgreChar('8');
			}
		}
		
		//Update enemies, guard in stage 1, ogre in stage 2
		if(stage == 1) {
			updateGuardPosition();
		} else {
			updateOgrePosition();
		}

		//Check adjacency after enemy movement
		if(isAdjacent(heroNewCoords, 1, "G0*$")) {
			return "Caught";
		}
		
		return "Normal";
	}
}