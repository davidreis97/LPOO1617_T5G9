
public class Map {
	
	Hero hero;
	Guard guards[];
	
	char dungeonMap[][];
	
	void printMap() {
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
	void updateGuardPosition(){
		
		
		for(int i = 0; i < guards.length; i++){
			
			dungeonMap[guards[i].y][guards[i].x] = ' ';
			
			if(guards[i].guardPath[guards[i].stepCounter].equals("w")) {
				guards[i].y--;
			} else if(guards[i].guardPath[guards[i].stepCounter].equals("a")) {
				guards[i].x--;
		    } else if(guards[i].guardPath[guards[i].stepCounter].equals("s")) {
		    	guards[i].y++;
			} else if(guards[i].guardPath[guards[i].stepCounter].equals("d")) {
				guards[i].x++;
			}
			
			dungeonMap[guards[i].y][guards[i].x] = 'G';
			
			guards[i].stepCounter++;
			if(guards[i].stepCounter >= guards[i].guardPath.length){
				guards[i].stepCounter = 0;
			}
		}
	}
	
	String updateMap(String kbdInput) {
		
		int tempherox = hero.x; int tempheroy = hero.y;
		
		updateGuardPosition();
		
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
		
		//Wall collision
		if(dungeonMap[tempheroy][tempherox] == ' ') {
			dungeonMap[hero.y][hero.x] = ' ';
			dungeonMap[tempheroy][tempherox] = 'H';
			hero.x = tempherox; hero.y = tempheroy;
			
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
			dungeonMap[hero.y][hero.x] = ' ';
			dungeonMap[tempheroy][tempherox] = 'H';
			hero.x = tempherox; hero.y = tempheroy;
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
		
		//Guard collision
		if(dungeonMap[hero.y - 1][hero.x] == 'G' ||
				dungeonMap[hero.y + 1][hero.x] == 'G' ||
				dungeonMap[hero.y][hero.x - 1] == 'G' ||
				dungeonMap[hero.y][hero.x + 1] == 'G' || 
				dungeonMap[hero.y][hero.x] == 'G') {
			return "Caught";
		}
		
		return "Normal";
	}
}
