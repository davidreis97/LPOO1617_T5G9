import java.awt.Point;

public class Map {
	
	private Hero hero;
	private Guard guards[];
	private Ogre ogres[];
	private char dungeonMap[][];
	
	Map(Hero hero, char dungeonMap[][], Ogre ogres[]){
		this.hero = hero;
		this.dungeonMap = dungeonMap;
		this.guards = new Guard[0];
		this.ogres = ogres;
	}
	
	Map(Hero hero, char dungeonMap[][], Guard guards[]){
		this.hero = hero;
		this.dungeonMap = dungeonMap;
		this.guards = guards;
		this.ogres = new Ogre[0];
	}
	
	public void printMap() {
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
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
		
		Point newCoords = new Point();
		
		switch(direction) {
		case 'w':
			newCoords.x = coords.x;
			newCoords.y = coords.y - 1;
			break;
		case 's':
			newCoords.x = coords.x;
			newCoords.y = coords.y + 1;
			break;
		case 'a':
			newCoords.x = coords.x - 1;
			newCoords.y = coords.y;
			break;
		case 'd':
			newCoords.x = coords.x + 1;
			newCoords.y = coords.y;
			break;
		}
		
		return newCoords;
	}
	
	//TODO force valid movements
	private void updateOgrePosition(char ogreDirection, char clubDirection) {
		
		for(int i = 0; i < ogres.length; i++) {
			
			Point ogreOld = ogres[i].getOgreCoords();
			Point ogreClubOld = ogres[i].getOgreClubCoords();
			String result;
		
			//Clear previous position with ' ' or 'k'
			dungeonMap[ogreOld.y][ogreOld.x] = ogres[i].getOgreChar();
			if(dungeonMap[ogreClubOld.y][ogreClubOld.x] != '0') {
				dungeonMap[ogreClubOld.y][ogreClubOld.x] = ogres[i].getOgreClubChar();
			}
			
			//ogreDirection = ogres[i].generateNewDirection();
			//clubDirection = ogres[i].generateNewDirection();
			
			Point ogreNewCoords = calcNewCoords(ogreOld, ogreDirection); //New coords based on old ogre position
			result = checkCollisionType(ogreNewCoords);
			
			//Ogre movement collision resolving
			if(result.equals("Empty") || result.equals("Club")) {
				dungeonMap[ogreNewCoords.y][ogreNewCoords.x] = '0';
				ogres[i].setOgreCoords(ogreNewCoords);
				ogres[i].setOgreChar(' ');
			} else if(result.equals("Key")) {
				dungeonMap[ogreNewCoords.y][ogreNewCoords.x] = '$';
				ogres[i].setOgreCoords(ogreNewCoords);
				ogres[i].setOgreChar('k'); //Next clear char will be 'k'
			}
			
			Point ogreClubNewCoords = calcNewCoords(ogreNewCoords, clubDirection); //New club coords based on the new ogre position
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
		}
	}
	
	private void updateGuardPosition() {
		
		for(int i = 0; i < guards.length; i++) {
			
			//Clear previous position
			Point guardOldCoords = guards[i].getGuardCoords();
			dungeonMap[guardOldCoords.y][guardOldCoords.x] = ' ';
			
			//New guard coords based on old coords and current direction in path
			Point guardNewCoords = calcNewCoords(guardOldCoords, guards[i].getGuardPath()[guards[i].getStepCounter()]);
			
			//CAUTION not checking guard collision
			dungeonMap[guardNewCoords.y][guardNewCoords.x] = 'G';
			guards[i].setGuardCoords(guardNewCoords);
			
			//Increment step count and reset if path has looped
			guards[i].incrementStepCounter();
		}
	}
	
	//TODO fix map 2 key mechanic
	//TODO shorten function as much as possible
	public String updateMap(char kbdInput) {
		
		int tempherox = hero.getX(); int tempheroy = hero.getY();
		
		updateGuardPosition();
		//updateOgrePosition();
		updateOgrePosition('s', 's');
		
		//Input Processing (Generates next hero position)
		if(kbdInput == 'w' || kbdInput == 'W') {
			tempheroy--;
		} else if(kbdInput == 'a' || kbdInput == 'A') {
			tempherox--;
	    } else if(kbdInput == 's' || kbdInput == 'S') {
	    	tempheroy++;
		} else if(kbdInput == 'd' || kbdInput == 'D') {
			tempherox++;
		}
		
		//Hero Wall collision
		if(dungeonMap[tempheroy][tempherox] == ' ') {
			dungeonMap[hero.getY()][hero.getX()] = ' ';
			dungeonMap[tempheroy][tempherox] = 'H';
			hero.setX(tempherox); hero.setY(tempheroy);
			
		//Lever collision
		} else if(dungeonMap[tempheroy][tempherox] == 'k') {
			for(int i = 0; i< dungeonMap.length; i++) {
				for(int j = 0; j < dungeonMap[i].length; j++) {
					if(dungeonMap[i][j] == 'I') {
						dungeonMap[i][j] = 'S';
					}
				}
			}
		}
		
		//Exit door collision
		if(dungeonMap[tempheroy][tempherox] == 'S' && (tempheroy == 0 || tempheroy == dungeonMap.length - 1 || tempherox == 0 || tempherox == dungeonMap[0].length - 1)) {
			dungeonMap[hero.getY()][hero.getX()] = ' ';
			dungeonMap[tempheroy][tempherox] = 'H';
			hero.setX(tempherox); hero.setY(tempheroy);
			return "Exit";
		}
		
		if(tempherox - 1 < 0) {
			tempherox = 1;
		}
		
		if(tempheroy - 1 < 0) {
			tempheroy = 1;
		}
		
		if(tempherox + 1 > dungeonMap[0].length - 1) {
			tempherox = dungeonMap[0].length - 2;
		}
		
		if(tempheroy + 1 > dungeonMap.length - 1) {
			tempheroy = dungeonMap.length - 2;
		}
		
		//Guard, ogre and club collision
		if(dungeonMap[hero.getY() - 1][hero.getX()] == 'G' ||
				dungeonMap[hero.getY() + 1][hero.getX()] == 'G' ||
				dungeonMap[hero.getY()][hero.getX() - 1] == 'G' ||
				dungeonMap[hero.getY()][hero.getX() + 1] == 'G' || 
				dungeonMap[hero.getY()][hero.getX()] == 'G' ||
				dungeonMap[hero.getY() - 1][hero.getX()] == '0' ||
				dungeonMap[hero.getY() + 1][hero.getX()] == '0' ||
				dungeonMap[hero.getY()][hero.getX() - 1] == '0' ||
				dungeonMap[hero.getY()][hero.getX() + 1] == '0' || 
				dungeonMap[hero.getY()][hero.getX()] == '0' || 
				dungeonMap[hero.getY() - 1][hero.getX()] == '$' ||
				dungeonMap[hero.getY() + 1][hero.getX()] == '$' ||
				dungeonMap[hero.getY()][hero.getX() - 1] == '$' ||
				dungeonMap[hero.getY()][hero.getX() + 1] == '$' || 
				dungeonMap[hero.getY()][hero.getX()] == '$' ||
				dungeonMap[hero.getY() - 1][hero.getX()] == '*' ||
				dungeonMap[hero.getY() + 1][hero.getX()] == '*' ||
				dungeonMap[hero.getY()][hero.getX() - 1] == '*' ||
				dungeonMap[hero.getY()][hero.getX() + 1] == '*' || 
				dungeonMap[hero.getY()][hero.getX()] == '*') {
			return "Caught";
		}
		
		return "Normal";
	}

	public Hero getHero() {
		return hero;
	}

	public Guard[] getGuards() {
		return guards;
	}

	public Ogre[] getOgres() {
		return ogres;
	}

	public char[][] getDungeonMap() {
		return dungeonMap;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public void setGuards(Guard[] guards) {
		this.guards = guards;
	}

	public void setOgres(Ogre[] ogres) {
		this.ogres = ogres;
	}

	public void setDungeonMap(char[][] dungeonMap) {
		this.dungeonMap = dungeonMap;
	}
}
