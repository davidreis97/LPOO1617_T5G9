import java.util.Scanner;

//TODO unbuffered input (InputStreamReader?)

public class main {
		
	
	public static void secondMap(){
		//Map map2 = new Map();
	}
	
	
	public static void main(String[] args) {
		
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
		
		String guardPath[] = {"a","s","s","s","s","a","a","a","a","a","a","s",
								"d","d","d","d","d","d","d","w","w","w","w","w"};
		
		Hero hero = new Hero(1,1); //Hero(x,y)
		
		Guard guard = new Guard(8,1,guardPath); //Guard(x,y,patrol[])
		Guard guards[] = new Guard[1];
		guards[0] = guard;
		
		Map map1 = new Map(hero,dungeonMap,guards);
				
		do {
			
			System.out.println("Input a direction (WASD):");
			
			map1.printMap();
			
			kbdInput = keyboard.nextLine();
			
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
