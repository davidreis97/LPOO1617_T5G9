package dkeep.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import dkeep.logic.Entity;
import dkeep.logic.Game;
import dkeep.logic.GameSaveHelper;

//Class responsible for displaying the game on the console, requesting user input and calling all the game logic functions
public class CLI {
	
	//Prints a game map to console, fills map with dynamic objects before printing
	public static void printMap(char[][] map, ArrayList<Entity> entities) {
		
		//Fill map with dynamic objects (entities)
		for(int i = entities.size() - 1; i >= 0; i--) {
			map[entities.get(i).getCoords().y][entities.get(i).getCoords().x] = entities.get(i).getRepresentation();
		}
		
		//Prints map with static and dynamic objects
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
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
	
	private static void constructGame(String guardType, int numOgres) {
		switch(guardType) {
		case "a":
			new Game("Dungeon", "Rookie", numOgres);
			break;
		case "b":
			new Game("Dungeon", "Drunken", numOgres);
			break;
		case "c":
			new Game("Dungeon", "Suspicious", numOgres);
			break;
		}
	}
	
	private static void gameSetup(Scanner keyboard) {
		String guardInput, ogreInput;
		String validGuard = "abc";
		String validOgre = "12345";
		
		System.out.println("Input type of guard to use:");
		System.out.println("  a - Rookie");
		System.out.println("  b - Drunken (falls asleep, reverses direction");
		System.out.println("  c - Suspicious (reverses direction)");
		
		guardInput = userInput(validGuard, keyboard);
		
		System.out.println("Input number of ogres (1-5):");
		
		ogreInput = userInput(validOgre, keyboard);
	
		constructGame(guardInput, Integer.parseInt(ogreInput));
	}
	
	private static void saveGame(Scanner keyboard) {
		
		String savePath = System.getProperty("user.dir") + "/saves";
		new File(savePath).mkdir();
		
		System.out.println("Insert savefile name:");
		String filename = keyboard.nextLine();

		try {
			File file = new File(savePath + "/" + filename);
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			GameSaveHelper gsh = new GameSaveHelper();
			gsh.gameToObject();
			out.writeObject(gsh);
			out.close();
			fileOut.close();
		}catch(IOException i) {
			System.out.println("Error saving game: " + i.getMessage());
		}
	}
	
	private static void printSaves() {
		
		String savePath = System.getProperty("user.dir") + "/saves";
		
	    File folder = new File(savePath);
	    File[] filenames = folder.listFiles();
	    
	    System.out.println("Insert savefile to load:");
	    
	    if(!(filenames == null)) {
		    for(int i = 0; i < filenames.length; i++) {
		    	System.out.println("- " + filenames[i].getName());
		    }
	    }
	}
	
	private static void loadGame(Scanner keyboard) {
		
		printSaves();
		
		String savePath = System.getProperty("user.dir") + "/saves";

		String filename = keyboard.nextLine();
		
        try {
            File file = new File(savePath + "/" + filename);
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameSaveHelper gsh = new GameSaveHelper();
            gsh = (GameSaveHelper) in.readObject();
            gsh.objectToGame();
            in.close();
            fileIn.close();
         } catch(IOException i) {
            System.out.println("Error loading game: " + i.getMessage());
         } catch(ClassNotFoundException c) {
            System.out.println("Error loading game: " + c.getMessage());
         }
	}
	
	private static void gameLoop(Scanner keyboard) {
		
		String kbdInput;
		String validInput = "wasdtf";
		
		do {

			printMap(Game.getMap(), Game.getEntities());
			
			System.out.println("Controls: (WASD) moves | (T)o file saves | (F)rom file loads:");
			
			kbdInput = userInput(validInput, keyboard);
		
			if(kbdInput.charAt(0) == 't') {
				saveGame(keyboard);
			} else if(kbdInput.charAt(0) == 'f') {
				loadGame(keyboard);
			} else Game.updateGame(kbdInput.charAt(0), true);
			
		} while(Game.getState().equals("Playing"));
	}
	
	//Entry point and game loop, processes input until game is over
	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);
		
		gameSetup(keyboard);
		gameLoop(keyboard);
		
		printMap(Game.getMap(), Game.getEntities());
		keyboard.close();
		
		if(Game.getState().equals("Win")) {
			System.out.println("You won!");
		} else System.out.println("You were caught, game over.");
	}
}
