import java.util.Scanner;

//TODO unbuffered input (InputStreamReader?)

public class main {
		
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		Map map1 = new Map();
		
		char dungeonMap1[][] =
			{	{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
					{'X', 'H', ' ', ' ', 'I', ' ', 'X', ' ', 'G', 'X'},
					{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
					{'X', ' ', 'I', ' ', 'I', ' ', 'X', ' ', ' ', 'X'},
					{'X', 'X', 'X', ' ', 'X', 'X', 'X', ' ', ' ', 'X'},
					{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', 'X', 'X', ' ', 'X', 'X', 'X', 'X', ' ', 'X'},
					{'X', ' ', 'I', ' ', 'I', ' ', 'X', 'k', ' ', 'X'},
					{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
		map1.dungeonMap = dungeonMap1;
		
		Hero hero = new Hero();
		hero.x = 1;
		hero.y = 1;
		
		Guard guard = new Guard();
		String guardPath1[] = {"a","s","s","s","s","a","a","a","a","a","a","s",
								"d","d","d","d","d","d","d","w","w","w","w","w"};
		guard.x = 8;
		guard.y = 1;
		guard.stepCounter = 0;
		guard.guardPath = guardPath1;
		
		map1.guards = new Guard[1];
		map1.guards[0] = guard;
		map1.hero = hero;
				
		do {
			
			System.out.println("Input a direction (WASD):");
			
			map1.printMap();
			
			kbdInput = keyboard.nextLine();
			
			String status = map1.updateMap(kbdInput);
		
			
			if(status.equals("Exit")) {
				System.out.println("You won!");
				//Here is where we insert the new map.
				return;
			} else if(status.equals("Caught")) {
				System.out.println("You lost!");
				map1.printMap();
				return;
			}
		} while(!kbdInput.equals("exit"));

		keyboard.close();
	}
}
