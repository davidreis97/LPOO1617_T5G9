package dkeep.cli;

import java.util.Scanner;

import dkeep.logic.Entity;
import dkeep.logic.Game;

//Class responsible for displaying the game on the console, requesting user input and calling all the game logic functions
public class CLI {
	
	//Prints a game map to console, fills map with dynamic objects before printing
	private static void printMap(char[][] map, Entity[] entities) {
		
		//Fill map with dynamic objects (entities)
		for(int i = 0; i < entities.length; i++) {
			map[entities[i].getCoords().y][entities[i].getCoords().x] = entities[i].getRepresentation();
		}
		
		//Prints map with static and dynamic objects
		for(int i = 0; i< map.length; i++) {
			for(int j = 0; j < map.length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}

	//Entry point and game loop, processes input until game is over
	public static void main(String[] args) {

		Game game = new Game();

		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		String validInput = "wasdq"; //Valid user input

		do {

			printMap(game.getMap(), game.getEntities());
			
			System.out.println("Input a direction (WASD):");
			do {
				
				kbdInput = keyboard.nextLine();
				
				if(kbdInput.length() != 1) {
					kbdInput = " "; //Reset input
				}
			} while(!validInput.contains(kbdInput));
		
			game.updateGame(kbdInput.charAt(0));

		} while(!(kbdInput.charAt(0) == 'q') && Game.getState().equals("Playing"));

		printMap(game.getMap(), game.getEntities());
		keyboard.close();
		
		if(Game.getState().equals("Win")) {
			System.out.println("You won!");
		} else System.out.println("You were caught, game over.");
	}
}
