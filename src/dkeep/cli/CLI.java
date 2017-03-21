package dkeep.cli;

import java.util.ArrayList;
import java.util.Scanner;

import dkeep.logic.Entity;
import dkeep.logic.Game;

//Class responsible for displaying the game on the console, requesting user input and calling all the game logic functions
public class CLI {
	
	//Prints a game map to console, fills map with dynamic objects before printing
	public static void printMap(char[][] map, ArrayList<Entity> entities) {
		
		//Fill map with dynamic objects (entities)
		for(int i = entities.size() - 1; i >= 0; i--) {
			map[entities.get(i).getCoords().y][entities.get(i).getCoords().x] = entities.get(i).getRepresentation();
		}
		
		//Prints map with static and dynamic objects
		for(int i = 0; i< map.length; i++) {
			for(int j = 0; j < map.length; j++) {
				System.out.print(map[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	//Entry point and game loop, processes input until game is over
	public static void main(String[] args) {

		new Game("Dungeon","Normal",1); //ASK quality?
		
		Scanner keyboard = new Scanner(System.in);
		String kbdInput;
		String validInput = "wasdq"; //Valid user input

		do {

			printMap(Game.getMap(), Game.getEntities());
			
			System.out.println("Input a direction (WASD):");
			do {
				
				kbdInput = keyboard.nextLine();
				
				if(kbdInput.length() != 1) {
					kbdInput = " "; //Reset input
				}
			} while(!validInput.contains(kbdInput));
		
			Game.updateGame(kbdInput.charAt(0),false);

		} while(!(kbdInput.charAt(0) == 'q') && Game.getState().equals("Playing"));

		printMap(Game.getMap(), Game.getEntities());
		keyboard.close();
		
		if(Game.getState().equals("Win")) {
			System.out.println("You won!");
		} else System.out.println("You were caught, game over.");
	}
}
