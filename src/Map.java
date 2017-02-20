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
	
	//TODO force valid ogre movements
	//CAUTION if guard blocks all movement of ogre, crashes
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
	
	//Checks adjacency (no diagonals) to Guard, Ogre and Ogre's club
	private boolean isAdjacent(Point coords, int range) {
		
		boolean r_1, r_2, r_3, r_4;
		r_1 = r_2 = r_3 = r_4 = false;
		
		String eligible = "G0$*";

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
	
	public void printMap() {
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
	//TODO fix map 2 key mechanic
	//TODO shorten function as much as possible
	//TODO check whole iteration
	//TODO force hero movement
	public String updateMap(char kbdInput) {
		
		Point heroOldCoords = hero.getHeroCoords();
		
		//New coords based on old hero position and player input
		Point heroNewCoords = calcNewCoords(heroOldCoords, kbdInput);
		String result;
		
		result = checkCollisionType(heroNewCoords);
		
		//Hero collision resolving
		if(result == "Empty") {
			dungeonMap[heroOldCoords.y][heroOldCoords.x] = ' ';
			dungeonMap[heroNewCoords.y][heroNewCoords.x] = 'H';
			hero.setHeroCoords(heroNewCoords);
		} else if(result == "Key") {
			for(int i = 0; i < dungeonMap.length; i++) {
					if(dungeonMap[i][0] == 'I') {
						dungeonMap[i][0] = 'S';
				}
			}
		} else if(result == "OpenDoor") {
			dungeonMap[heroOldCoords.y][heroOldCoords.x] = ' ';
			dungeonMap[heroNewCoords.y][heroNewCoords.x] = 'H';
			hero.setHeroCoords(heroNewCoords);
			return "Exit";
		}
		
		if(isAdjacent(heroNewCoords, 1)) {
			return "Caught";
		}
		
		updateGuardPosition();
		updateOgrePosition('s', 's');
		
		if(isAdjacent(heroNewCoords, 1)) {
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
