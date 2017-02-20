import java.awt.Point;
import java.util.Scanner;

public class Main {
		
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
				{'X', 'H', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
				{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
	
	private static char guardPath[] = {'a', 's', 's', 's', 's', 'a', 'a', 'a', 'a', 'a', 'a', 's',
			                           'd', 'd', 'd', 'd', 'd', 'd', 'd', 'w', 'w', 'w', 'w', 'w'};
	
	//Initialize 1st map values
	private static Map mapInit_1() {
		
		Hero hero = new Hero(new Point(1, 1));
		
		//Guard setup
		Guard guard = new Guard(new Point(8, 1), guardPath);
		Guard guards[] = new Guard[1];
		guards[0] = guard;

		return new Map(hero, dungeonMap_1, guards);
	}

	//Initialize 2nd map values
	private static Map mapInit_2() {
		
		Hero hero = new Hero(new Point(1, 8));
		
		//Ogre setup
		Ogre ogre = new Ogre(new Point(4, 1), new Point(3, 1));
		Ogre ogres[] = new Ogre[1];
		ogres[0] = ogre;
		
		return new Map(hero, dungeonMap_2, ogres);
	}
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;

		Map currMap = mapInit_1();
		int mapCounter = 1; //Keeps track of current map
		
		//Game loop
		do {
			
			System.out.println("Input a direction (WASD):");
			
			currMap.printMap();
			
			do {
				kbdInput = keyboard.nextLine();
				if(kbdInput.isEmpty()) {
					kbdInput = " "; //Prevent crash when input is empty
				}
			} while(kbdInput.charAt(0) != 'w' && kbdInput.charAt(0) != 's'
					&& kbdInput.charAt(0) != 'a' && kbdInput.charAt(0) != 'd');
			
			String status = currMap.updateMap(kbdInput.charAt(0), mapCounter);
		
			if(status.equals("Exit")) {
				if(mapCounter == 1) { //Change to map 2
					mapCounter++;
					currMap = mapInit_2();
				} else { //Exited map 2
					currMap.printMap();
					System.out.println("You won!");
					keyboard.close();
					return;
				}
			} else if(status.equals("Caught")) {
				currMap.printMap();
				System.out.println("You lost!");
				keyboard.close();
				return;
			}
			
		} while(!(kbdInput.charAt(0) == 'q'));

		keyboard.close();
	}
}
