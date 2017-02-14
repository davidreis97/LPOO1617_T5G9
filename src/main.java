import java.util.Scanner;

//TODO unbuffered input (InputStreamReader?)

public class main {

	protected static int heroX = 1;
	protected static int heroY = 1;
	
	protected static char dungeonMap[][] =
		{	{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
			{'X', 'H', ' ', ' ', 'I', ' ', 'X', ' ', 'G', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
			{'X', 'X', 'X', ' ', 'X', 'X', 'X', 'X', ' ', 'X'},
			{'X', ' ', 'I', ' ', 'I', ' ', 'X', 'k', ' ', 'X'},
			{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
	
	protected static void printMap() {
		
		for(int i = 0; i< dungeonMap.length; i++) {
			for(int j = 0; j < dungeonMap[i].length; j++) {
				System.out.print(dungeonMap[i][j]);
			}
			System.out.println();
		}
	}
	
	protected static String updateMap(int tempHeroX, int tempHeroY) {
		

		
		//Wall collision
		if(dungeonMap[tempHeroY][tempHeroX] == ' ') {
			dungeonMap[heroY][heroX] = ' ';
			dungeonMap[tempHeroY][tempHeroX] = 'H';
			heroX = tempHeroX; heroY = tempHeroY;
			
		//Lever collision
		} else if(dungeonMap[tempHeroY][tempHeroX] == 'k') {
			for(int i = 0; i< dungeonMap.length; i++) {
				for(int j = 0; j < dungeonMap[i].length; j++) {
					if(dungeonMap[i][j] == 'I') {
						dungeonMap[i][j] = 'S';
					}
				}
			}
			
		//Exit door collision
		}
		
		if(dungeonMap[tempHeroY][tempHeroX] == 'S' && (tempHeroY == 0 || tempHeroY == dungeonMap.length - 1 || tempHeroX == 0 || tempHeroX == dungeonMap[0].length - 1)) {
			dungeonMap[heroY][heroX] = ' ';
			dungeonMap[tempHeroY][tempHeroX] = 'H';
			heroX = tempHeroX; heroY = tempHeroY;
			return "Exit";
		}
		
		if(tempHeroX - 1 < 0) {
			tempHeroX = 1;
		}
		
		if(tempHeroY - 1 < 0) {
			tempHeroY = 1;
		}
		
		if(tempHeroX + 1 > dungeonMap[0].length - 1) {
			tempHeroX = dungeonMap[0].length - 2;
		}
		
		if(tempHeroY + 1 > dungeonMap.length - 1) {
			tempHeroY = dungeonMap.length - 2;
		}
		
		//Guard collision
		if(dungeonMap[tempHeroY - 1][tempHeroX] == 'G' ||
				dungeonMap[tempHeroY + 1][tempHeroX] == 'G' ||
				dungeonMap[tempHeroY][tempHeroX - 1] == 'G' ||
				dungeonMap[tempHeroY][tempHeroX + 1] == 'G') {
			return "Caught";
		}
		
		return "Normal";
	}
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		printMap();
		
		do {
			
			System.out.println("Input a direction (WASD):");
			
			kbdInput = keyboard.nextLine();
			
			int tempHeroX, tempHeroY;
			tempHeroX = heroX; tempHeroY = heroY;
			
			if(kbdInput.equals("w") || kbdInput.equals("W")) {
				tempHeroY--;
			} else if(kbdInput.equals("a") || kbdInput.equals("A")) {
				tempHeroX--;
		    } else if(kbdInput.equals("s") || kbdInput.equals("S")) {
		    	tempHeroY++;
			} else if(kbdInput.equals("d") || kbdInput.equals("D")) {
				tempHeroX++;
			}
			
			String status = updateMap(tempHeroX, tempHeroY);
		
			printMap();
			
			if(status.equals("Exit")) {
				System.out.println("You won!");
				return;
			} else if(status.equals("Caught")) {
				System.out.println("You lost!");
				return;
			}
		} while(!kbdInput.equals("exit"));

		keyboard.close();
	}
}
