package dkeep.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import dkeep.logic.Entity;
import dkeep.logic.Game;

import javax.swing.JButton;
import java.awt.event.ActionListener;
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
		
		JButton btnStartNewGame = new JButton("Start New Game");
		btnStartNewGame.setBounds(370, 22, 141, 29);
		frame.getContentPane().add(btnStartNewGame);
		
		initializeDirectionalButtons();
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(382, 307, 117, 29);
		frame.getContentPane().add(btnExit);
		
		panel = new SimpleGraphicsPanel(false);
		panel.setBounds(16, 22, 320, 320);
		frame.getContentPane().add(panel);
		
		JButton btnLevelEditor = new JButton("Level Editor");
		btnLevelEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LevelEditor.start();
			}
		});
		btnLevelEditor.setBounds(382, 246, 117, 29);
		frame.getContentPane().add(btnLevelEditor);
		panel.repaint();
		
		
		btnStartNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartNewGame.launch();
			}
		});
		
		updateGUIStatus();
	}
	
	private void initializeDirectionalButtons() {
		btnUp = new JButton("Up");
		btnUp.setBounds(396, 136, 88, 29);
		frame.getContentPane().add(btnUp);
		
		btnLeft = new JButton("Left");
		btnLeft.setBounds(360, 162, 83, 29);
		frame.getContentPane().add(btnLeft);
		
		btnRight = new JButton("Right");
		btnRight.setBounds(436, 162, 88, 29);
		frame.getContentPane().add(btnRight);
		
		btnDown = new JButton("Down");
		btnDown.setBounds(396, 190, 88, 29);
		frame.getContentPane().add(btnDown);
		
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('d', false);
				updateGUIStatus();
			}
		});
		
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('w', false);
				updateGUIStatus();
			}
		});
		
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('a', false);
				updateGUIStatus();
			}
		});
		
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('s', false);
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
		}else{
			btnDown.setEnabled(true);
			btnUp.setEnabled(true);
			btnLeft.setEnabled(true);
			btnRight.setEnabled(true);
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
		boolean hero = false,key = false,wall = false,door = false,ogre = false;
		int ogreToClub = 0;
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
		if (!hero || !key || !wall || !door || !ogre || ogreToClub != 0){
			return false;
		}else{
			updateGUIStatus();
			return true;
		}
	}
}
