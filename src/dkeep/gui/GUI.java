package dkeep.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import dkeep.logic.Entity;
import dkeep.logic.Game;
import dkeep.logic.GameSaveHelper;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class GUI {

	private JFrame frame;
	private static JButton btnDown;
	private static JButton btnUp;
	private static JButton btnLeft;
	private static JButton btnRight;
	private static JLabel lblStatus;
	private static JPanel panel;
	private static JButton btnSaveGame;
	private static JButton btnLoadGame;
	private static JButton btnExit;
	private static JButton btnStartNewGame;
	private static JButton btnLevelEditor;
	private static final JFileChooser fc = new JFileChooser();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		new Game("Dungeon", "Rookie", 0);
		Game.setState("You can start a new game");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 556, 402);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblStatus = new JLabel("Status");
		lblStatus.setBounds(16, 354, 390, 16);
		frame.getContentPane().add(lblStatus);
		
		panel = new SimpleGraphicsPanel(false);
		panel.setBounds(16, 22, 320, 320);
		frame.getContentPane().add(panel);
		
		initializeDownButton();
		initializeUpButton();
		initializeLeftButton();
		initializeRightButton();
		
		initializeSaveButton();		
		initializeLoadButton();
		
		initializeStartExitEditorButtons();
		
		updateGUIStatus();
	}
	
	private void initializeStartExitEditorButtons() {
		
		initializeEditorButton();
		
		initializeStartNewGameButton();
		
		initializeExitButton();
	}

	private void initializeEditorButton() {
		btnLevelEditor = new JButton("Level Editor");
		btnLevelEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LevelEditor.start();
			}
		});
		btnLevelEditor.setBounds(382, 246, 117, 29);
		frame.getContentPane().add(btnLevelEditor);
		
		
	}

	private void initializeStartNewGameButton() {
		btnStartNewGame = new JButton("Start New Game");
		btnStartNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartNewGame.launch();
			}
		});
		btnStartNewGame.setBounds(370, 22, 141, 29);
		frame.getContentPane().add(btnStartNewGame);
	}

	private void initializeExitButton() {
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(382, 307, 117, 29);
		frame.getContentPane().add(btnExit);
	}

	private void initializeSaveButton() {
		btnSaveGame = new JButton("Save Game");
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fc.getSelectedFile();
						writeToFile(file);
						updateGUIStatus();
						lblStatus.setText("Game saved");
					}catch(IOException i) {
						i.printStackTrace();
						lblStatus.setText("Error saving game" + i.getMessage());
					}
				}
			}
		});
		btnSaveGame.setBounds(380, 63, 119, 29);
		frame.getContentPane().add(btnSaveGame);
	}
	
	protected void writeToFile(File file) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		GameSaveHelper gsh = new GameSaveHelper();
		gsh.gameToObject();
		out.writeObject(gsh);
		out.close();
		fileOut.close();
	}

	private void initializeLoadButton(){
		btnLoadGame = new JButton("Load Game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
		            	readFromFile(file);
		                updateGUIStatus();
		                lblStatus.setText("Game loaded");
		             }catch(IOException i) {
		                i.printStackTrace();
		                lblStatus.setText("Error loading game: " + i.getMessage());
		             }catch(ClassNotFoundException c) {
		            	lblStatus.setText("Game class not found");
		                c.printStackTrace();
		                lblStatus.setText("Error loading game: " + c.getMessage());
		             }
		        }
			}
		});
		btnLoadGame.setBounds(380, 90, 119, 29);
		frame.getContentPane().add(btnLoadGame);
	}

	protected void readFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        GameSaveHelper gsh = new GameSaveHelper();
        gsh = (GameSaveHelper) in.readObject();
        gsh.objectToGame();
        in.close();
        fileIn.close();
	}

	private void initializeDownButton() {
		btnDown = new JButton("Down");
		btnDown.setBounds(396, 190, 88, 29);
		frame.getContentPane().add(btnDown);
		
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('s', true);
				updateGUIStatus();
			}
		});
	}
	
	private void initializeUpButton(){
		btnUp = new JButton("Up");
		btnUp.setBounds(396, 136, 88, 29);
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('w', true);
				updateGUIStatus();
			}
		});
		frame.getContentPane().add(btnUp);
	}

	private void initializeLeftButton(){
		btnLeft = new JButton("Left");
		btnLeft.setBounds(360, 162, 83, 29);
		frame.getContentPane().add(btnLeft);
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('a', true);
				updateGUIStatus();
			}
		});
	}
	
	private void initializeRightButton(){
		btnRight = new JButton("Right");
		btnRight.setBounds(436, 162, 88, 29);
		frame.getContentPane().add(btnRight);
		btnRight.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('d', true);
				updateGUIStatus();
			}
		});
	}
	
	protected static void updateGUIStatus() {
		lblStatus.setText(Game.getState());
		if (!Game.getState().equals("Playing")){
			btnDown.setEnabled(false);
			btnUp.setEnabled(false);
			btnLeft.setEnabled(false);
			btnRight.setEnabled(false);
			btnSaveGame.setEnabled(false);
		}else{
			btnDown.setEnabled(true);
			btnUp.setEnabled(true);
			btnLeft.setEnabled(true);
			btnRight.setEnabled(true);
			btnSaveGame.setEnabled(true);
		}
		panel.repaint();
		panel.requestFocusInWindow();
	}

	public static String printMap(char[][] map, ArrayList<Entity> entities) {
		String output = "";
		
		//Fill map with dynamic objects (entities)
		for(int i = entities.size() - 1; i >= 0; i--) {
			map[entities.get(i).getCoords().y][entities.get(i).getCoords().x] = entities.get(i).getRepresentation();
		}
		
		//Prints map with static and dynamic objects
		for(int i = 0; i< map.length; i++) {
			for(int j = 0; j < map.length; j++) {
				output += map[i][j];
				output += " ";
			}
			output += '\n';
		}
		
		return output;
	}

	public static boolean validate() {
		boolean key = false,wall = false,door = false;
		for(char []i: Game.getMap()){
			for(char j: i){
				if(j == 'k'){
					key = true;
				}else if(j == 'X'){
					wall = true;
				}else if(j == 'I'){
					door = true;
				}
			}
		}
		if (!checkEntity() || !key || !wall || !door){
			return false;
		}else{
			updateGUIStatus();
			return true;
		}
		
	}

	private static boolean checkEntity() {
		boolean hero = false, ogre = false;
		int ogreToClub = 0;
		
		for(Entity x: Game.getEntities()){
			if ((x.getRepresentation() == 'H' || x.getRepresentation() == 'A') && hero){
				return false;
			}else if((x.getRepresentation() == 'H' || x.getRepresentation() == 'A') && !hero){
				hero = true;
			}else if (x.getRepresentation() == '0'){
				ogre = true;
				ogreToClub++;
			}else if (x.getRepresentation() == '*'){
				ogreToClub--;
			}
		}
		
		if (!hero || !ogre || ogreToClub != 0){
			return false;
		}
		return true;
	}
}
