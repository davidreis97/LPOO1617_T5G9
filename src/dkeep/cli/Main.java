package dkeep.cli;

import java.util.Scanner;
import dkeep.logic.Game;

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
		
			game.nextTick(kbdInput.charAt(0));
			
			game.getCurrMap().printMap();
		
		} while(!(kbdInput.charAt(0) == 'q') && game.getRunning());

		keyboard.close();
	}
}
