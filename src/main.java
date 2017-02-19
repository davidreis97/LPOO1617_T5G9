import java.awt.Point;
import java.util.Scanner;

//TODO unbuffered input (InputStreamReader?)

public class main {
		
	public static void secondMap(){
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		
		char dungeonMap[][] =
			{	{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
					{'I', ' ', ' ', ' ', '0', ' ', ' ', ' ', 'k', 'X'},
					{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', 'H', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
					{'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
		
		Hero hero = new Hero(1,8);
		
		Ogre ogre = new Ogre(new Point(4, 1), new Point(3, 1));
		Ogre ogres[] = new Ogre[1];
		ogres[0] = ogre;
	
		Guard guards[] = new Guard[0];
		
		Map map2 = new Map(hero, dungeonMap, guards, ogres);
		
		do {
			
			System.out.println("Input a direction (WASD):");
			
			map2.printMap();
			
			kbdInput = keyboard.nextLine();
			
			String status = map2.updateMap(kbdInput);
		
			
			if(status.equals("Exit")) {
				System.out.println("You won!");
				System.exit(0);
			return;
			} else if(status.equals("Caught")) {
				System.out.println("You lost!");
				map2.printMap();
				return;
			}
		} while(!kbdInput.equals("exit"));
 	}
	
	//TODO proper game loop with state flags for map1 / map2
	public static void main(String[] args) {
		
		//secondMap();
		
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		 
		char dungeonMap[][] =
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
		
		char guardPath[] = {'a','s','s','s','s','a','a','a','a','a','a','s',
								'd','d','d','d','d','d','d','w','w','w','w','w'};
		
		Hero hero = new Hero(1,1); //Hero(x,y)
		
		Guard guard = new Guard(new Point(8, 1), guardPath);
		Guard guards[] = new Guard[1];
		guards[0] = guard;
		
		Ogre ogres[] = new Ogre[0];
		
		Map map1 = new Map(hero,dungeonMap,guards,ogres);
				
		do {
			
			System.out.println("Input a direction (WASD):");
			
			map1.printMap();
			
			kbdInput = keyboard.nextLine(); //CAUTION crashes if user inputs Enter only
			
			String status = map1.updateMap(kbdInput);
		
			
			if(status.equals("Exit")) {
				System.out.println("You won!");
				secondMap();
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
