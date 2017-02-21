package dkeep.cli;

import java.util.Scanner;
import dkeep.logic.Game;

//TODO "Create these strategies for the Guard, ensuring that your code allows for easy adding of other strategies in the future (without the need to modify existing code). Debate with your colleagues or teacher possible solutions for this."
//TODO know current guard type?
//TODO toString print map? cli method?
//TODO Entity superclass reuse / Game class has entities

public class Main {
	
	public static void main(String[] args) {
		
		Game game = new Game();
		game.getCurrMap().printMap();
		
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		
		//Input loop, calls game's next tick
		do {
			
			System.out.println("Input a direction (WASD):");
			
			do {
				kbdInput = keyboard.nextLine();
				if(kbdInput.isEmpty()) {
					kbdInput = " "; //Prevent crash when input is empty
				}
			} while(kbdInput.charAt(0) != 'w' && kbdInput.charAt(0) != 's'
					&& kbdInput.charAt(0) != 'a' && kbdInput.charAt(0) != 'd');
		
			game.nextTick(kbdInput.charAt(0)); //Update game logic
			game.getCurrMap().printMap(); //Show updated map

		} while(!(kbdInput.charAt(0) == 'q') && game.getRunning());

		keyboard.close();
	}
}