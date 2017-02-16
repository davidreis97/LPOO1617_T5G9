
public class Map {
	
	private Hero hero;
	private Guard guards[];
	private Ogre ogres[];
	
	private char dungeonMap[][];
	
	public Map(Hero h, char dM[][], Guard g[], Ogre o[]){
		hero = h;
		guards = g;
		dungeonMap = dM;
		ogres = o;;
	}
	
	void printMap() {
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
	void updateOgrePosition(){
		for(int i = 0; i < ogres.length; i++){
			int ogreoldx = ogres[i].getX();
			int ogreoldy = ogres[i].getY();
			int ogreoldclubx = ogres[i].getOgreClubX();
			int ogreoldcluby = ogres[i].getOgreClubY();
			
			if(dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] == '$'){
				dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] = 'k';
			}else{
				dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] = ' ';
			}
			
			if(dungeonMap[ogres[i].getY()][ogres[i].getX()] == '$'){
				dungeonMap[ogres[i].getY()][ogres[i].getX()] = 'k';
			}else{
				dungeonMap[ogres[i].getY()][ogres[i].getX()] = ' ';
			}
			
			String ogreDirection = ogres[i].generateNewDirection();
			String clubDirection = ogres[i].generateNewDirection();
			
			if(ogreDirection.equals("w")) {
				ogres[i].setY(ogres[i].getY() - 1);
			} else if(ogreDirection.equals("a")) {
				ogres[i].setX(ogres[i].getX() - 1);
		    } else if(ogreDirection.equals("s")) {
		    	ogres[i].setY(ogres[i].getY() + 1);
			} else if(ogreDirection.equals("d")) {
				ogres[i].setX(ogres[i].getX() + 1);
			}
			
			if(clubDirection.equals("w")) {
				ogres[i].setOgreClubX(ogres[i].getX()); ogres[i].setOgreClubY(ogres[i].getY() -1);
			} else if(clubDirection.equals("a")) {
				ogres[i].setOgreClubX(ogres[i].getX()-1); ogres[i].setOgreClubY(ogres[i].getY());
		    } else if(clubDirection.equals("s")) {
		    	ogres[i].setOgreClubX(ogres[i].getX()); ogres[i].setOgreClubY(ogres[i].getY() +1);
			} else if(clubDirection.equals("d")) {
				ogres[i].setOgreClubX(ogres[i].getX()+1); ogres[i].setOgreClubY(ogres[i].getY());
			}
			
			//Ogre Wall collision
			if(dungeonMap[ogres[i].getY()][ogres[i].getX()] == 'X' ||
				dungeonMap[ogres[i].getY()][ogres[i].getX()] == 'I'||
				dungeonMap[ogres[i].getY()][ogres[i].getX()] == 'S') {
				
				ogres[i].setX(ogreoldx); ogres[i].setY(ogreoldy);
				ogres[i].setOgreClubX(ogreoldclubx); ogres[i].setOgreClubY(ogreoldcluby);
				updateOgrePosition();
			}else{
				
				//Ogre Club Wall collision, only runs if the ogre new position is valid.
				if(dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] == 'X' ||
					dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] == 'I'||
					dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] == 'S') {
					
					ogres[i].setX(ogreoldx); ogres[i].setY(ogreoldy);
					ogres[i].setOgreClubX(ogreoldclubx); ogres[i].setOgreClubY(ogreoldcluby);
					updateOgrePosition();
				}else{
					if(dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] == 'k'){
						dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] = '$';
					}else{
						dungeonMap[ogres[i].getOgreClubY()][ogres[i].getOgreClubX()] = '*';
					}
				}
				
				if(dungeonMap[ogres[i].getY()][ogres[i].getX()] == 'k'){ //Only change the map if both the club and the ogre are in valid positions
					dungeonMap[ogres[i].getY()][ogres[i].getX()] = '$';
				}else{
					dungeonMap[ogres[i].getY()][ogres[i].getX()] = '0';
				}
			}
		}
	}
	
	void updateGuardPosition(){
		
		
		for(int i = 0; i < guards.length; i++){
			
			dungeonMap[guards[i].getY()][guards[i].getX()] = ' ';
			
			if(guards[i].getGuardPath()[guards[i].getStepCounter()].equals("w")) {
				guards[i].setY(guards[i].getY() - 1);
			} else if(guards[i].getGuardPath()[guards[i].getStepCounter()].equals("a")) {
				guards[i].setX(guards[i].getX() - 1);
		    } else if(guards[i].getGuardPath()[guards[i].getStepCounter()].equals("s")) {
		    	guards[i].setY(guards[i].getY() + 1);
			} else if(guards[i].getGuardPath()[guards[i].getStepCounter()].equals("d")) {
				guards[i].setX(guards[i].getX() + 1);
			}
			
			dungeonMap[guards[i].getY()][guards[i].getX()] = 'G';
			
			guards[i].setStepCounter(guards[i].getStepCounter() + 1);
			if(guards[i].getStepCounter() >= guards[i].getGuardPath().length){
				guards[i].setStepCounter(0);
			}
		}
	}
	
	String updateMap(String kbdInput) {
		
		int tempherox = hero.getX(); int tempheroy = hero.getY();
		
		updateGuardPosition();
		updateOgrePosition();
		
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
