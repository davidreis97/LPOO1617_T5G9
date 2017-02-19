import java.awt.Point;

public class Map {
	
	private Hero hero;
	private Guard guards[];
	private Ogre ogres[];
	private char dungeonMap[][];
	
	Map(Hero h, char dM[][], Guard g[], Ogre o[]){
		hero = h;
		guards = g;
		dungeonMap = dM;
		ogres = o;
	}
	
	void printMap() {
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
	//Returns a string with collision type based on the char at coords
	String checkCollisionType(Point coords) {
		
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
	Point calcNewCoords(Point coords, char direction) {
		
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
	//TODO bounds checking
	void updateOgrePosition(char ogreDirection, char clubDirection) {
		
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
	
	void updateGuardPosition() {
		
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
	String updateMap(String kbdInput) {
		
		int tempherox = hero.getX(); int tempheroy = hero.getY();
		
		updateGuardPosition();
		//updateOgrePosition();
		updateOgrePosition(kbdInput.charAt(0), kbdInput.charAt(0));
		
		//Input Processing (Generates next hero position)
		if(kbdInput.equals("w") || kbdInput.equals("W")) {
			tempheroy--;
		} else if(kbdInput.equals("a") || kbdInput.equals("A")) {
			tempherox--;
	    } else if(kbdInput.equals("s") || kbdInput.equals("S")) {
	    	tempheroy++;
		} else if(kbdInput.equals("d") || kbdInput.equals("D")) {
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

	Hero getHero() {
		return hero;
	}

	void setHero(Hero hero) {
		this.hero = hero;
	}

	Guard[] getGuards() {
		return guards;
	}

	void setGuards(Guard guards[]) {
		this.guards = guards;
	}

	Ogre[] getOgres() {
		return ogres;
	}

	void setOgres(Ogre ogres[]) {
		this.ogres = ogres;
	}

	char[][] getDungeonMap() {
		return dungeonMap;
	}

	void setDungeonMap(char dungeonMap[][]) {
		this.dungeonMap = dungeonMap;
	}
}
