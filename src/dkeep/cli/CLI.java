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

	private static String userInput(String validInput, Scanner keyboard) {
		
		String userInput;
		
		do {
			userInput = keyboard.nextLine();
			
			if(userInput.length() != 1)	userInput = " "; //Reset input
			
		} while(!validInput.contains(userInput));
		
		return userInput;
	}
	
	public static void gameSetup(Scanner keyboard) {
		String guardInput, ogreInput;
		String validGuard = "abc";
		String validOgre = "12345";
		
		System.out.println("Input type of guard to use:");
		System.out.println("a - Rookie");
		System.out.println("b - Drunken (falls asleep, reverses direction");
		System.out.println("c - Suspicious (reverses direction)");
		
		guardInput = userInput(validGuard, keyboard);
		
		System.out.println("Input number of ogres (1-5):");
		
		ogreInput = userInput(validOgre, keyboard);
	
		switch(guardInput) {
		case "a":
			new Game("Dungeon", "Rookie", Integer.parseInt(ogreInput));
			break;
		case "b":
			new Game("Dungeon", "Drunken", Integer.parseInt(ogreInput));
			break;
		case "c":
			new Game("Dungeon", "Suspicious", Integer.parseInt(ogreInput));
			break;
		}
	}
	
	//Entry point and game loop, processes input until game is over
	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);
		
		gameSetup(keyboard);
		
		String kbdInput;
		String validInput = "wasd";

		do {

			printMap(Game.getMap(), Game.getEntities());
			
			System.out.println("Input a direction (WASD):");
			
			kbdInput = userInput(validInput, keyboard);
		
			Game.updateGame(kbdInput.charAt(0), true);

		} while(Game.getState().equals("Playing"));

		printMap(Game.getMap(), Game.getEntities());
		keyboard.close();
		
		if(Game.getState().equals("Win")) {
			System.out.println("You won!");
		} else System.out.println("You were caught, game over.");
	}
}
