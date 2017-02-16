
public class Map {
	
	Hero hero;
	Guard guards[];
	Ogre ogres[];
	
	char dungeonMap[][];
	
	public Map(Hero h, char dM[][], Guard g[], Ogre o[]){
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
	
	void updateOgrePosition(){
		
		for(int i = 0; i < ogres.length; i++){
			int ogreoldx = ogres[i].x;
			int ogreoldy = ogres[i].y;
			
			if(dungeonMap[ogres[i].y][ogres[i].x] == '$'){
				dungeonMap[ogres[i].y][ogres[i].x] = 'k';
			}else{
				dungeonMap[ogres[i].y][ogres[i].x] = ' ';
			}
			
			if(ogres[i].generateNextStep().equals("w")) {
				ogres[i].y--;
			} else if(ogres[i].generateNextStep().equals("a")) {
				ogres[i].x--;
		    } else if(ogres[i].generateNextStep().equals("s")) {
		    	ogres[i].y++;
			} else if(ogres[i].generateNextStep().equals("d")) {
				ogres[i].x++;
			}
			
			//Ogre Wall collision
			if(dungeonMap[ogres[i].y][ogres[i].x] == 'X' ||
				dungeonMap[ogres[i].y][ogres[i].x] == 'I'||
				dungeonMap[ogres[i].y][ogres[i].x] == 'S') {
				
				ogres[i].x = ogreoldx; ogres[i].y = ogreoldx;
			}
			
			if(dungeonMap[ogres[i].y][ogres[i].x] == 'k'){
				dungeonMap[ogres[i].y][ogres[i].x] = '$';
			}else{
				dungeonMap[ogres[i].y][ogres[i].x] = '0';
			}
			
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
		
		//Ogre collision
		if(dungeonMap[hero.y - 1][hero.x] == '0' ||
				dungeonMap[hero.y + 1][hero.x] == '0' ||
				dungeonMap[hero.y][hero.x - 1] == '0' ||
				dungeonMap[hero.y][hero.x + 1] == '0' || 
				dungeonMap[hero.y][hero.x] == '0' || 
				dungeonMap[hero.y - 1][hero.x] == '$' ||
				dungeonMap[hero.y + 1][hero.x] == '$' ||
				dungeonMap[hero.y][hero.x - 1] == '$' ||
				dungeonMap[hero.y][hero.x + 1] == '$' || 
				dungeonMap[hero.y][hero.x] == '$') {
			return "Caught";
		}
		
		
		
		return "Normal";
	}
}
