package dkeep.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dkeep.logic.Entity;
import dkeep.logic.Game;
import dkeep.logic.Hero;
import dkeep.logic.KeepMap;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class LevelEditor {

	private static char nextChar = ' ';
	private static JFrame frame;
	private static JLabel lblOptions;
	private static JButton btnHero;
	private static JButton btnOgre;
	private static JButton btnKey;
	private static JButton btnDoor;
	private static JButton btnWall;
	private static JButton btnValidatePlay;
	private static JButton btnEmpty;
	private static JButton btnClearAllEntities;
	private static JButton btnClearAll;
	private static JPanel panel;
	private static JLabel lblEntities;
	private static JCheckBox chckbxArmedHero;
	private static JLabel lblDestructiveOptions;
	private static JLabel lblFinalize;
	private static int mapWidth;
	private static int mapHeight;

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new LevelEditor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LevelEditor() {
		askMapLimits();
	}
	
	private void askMapLimits(){
		LevelEditorMapSize.start();
	}

	/**
	 * Initialize the contents of the frame.
	 * @param j 
	 * @param i 
	 */
	public static void initialize(int height, int width) {
		mapWidth = width;
		mapHeight = height;
		frame = new JFrame();
		
		initializePanel();
		initializeGame();
		initializeFrame();
		initializeButtons();
		initializeLabels();
		initializeArmoredHeroCheckbox();
		
		panel.repaint();
		setStatus("Map Height: " + mapHeight + " Map Width: " + mapWidth);
		frame.setVisible(true);
	}
	
	private static void initializeButtons() {
		initializePlacementMapButtons();
		initializePlacementKeyButton();
		initializePlacementEntityButtons();
		initializeOptionsButtons();
		initializeClearAllButton();
		initializeClearAndEmptyButtons();
	}

	private static void initializePanel() {
		panel = new SimpleGraphicsPanel(true);
		panel.setBounds(16, 22, 320, 320);
		frame.getContentPane().add(panel);
	}

	private static void initializeFrame() {
		frame.setResizable(false);
		frame.setBounds(100, 100, 477, 398);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	new Game("Dungeon", "Rookie", 0);
				Game.setState("You can start a new game");
		        GUI.updateGUIStatus();
		    }
		});
	}

	private static void initializeClearAllButton() {
		btnClearAll = new JButton("Clear all Map");
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeepMap map = new KeepMap();
				char emptyMap[][] = new char[getMapWidth()][getMapHeight()];
				for(char []row : emptyMap){
					Arrays.fill(row, ' ');
				}
				map.setMap(emptyMap);
				nextChar = ' ';
				lblOptions.setText("Cleared the map.");
				Game.setMapObject(map);
				panel.repaint();
				panel.requestFocusInWindow();
			}
		});
		btnClearAll.setBounds(344, 276, 127, 29);
		frame.getContentPane().add(btnClearAll);
	}
	
	private static void initializeClearAndEmptyButtons(){
		btnEmpty = new JButton("Empty");
		btnEmpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblOptions.setText("Current Entity: Empty");
				nextChar = ' ';
				panel.requestFocusInWindow();
			}
		});
		btnEmpty.setBounds(344, 187, 127, 29);
		frame.getContentPane().add(btnEmpty);
		
		btnClearAllEntities = new JButton("Clear all Entities");
		btnClearAllEntities.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Entity> emptyList = new ArrayList<Entity>();
				Game.setEntities(emptyList);
				nextChar = ' ';
				lblOptions.setText("Cleared all entities.");
				panel.repaint();
				panel.requestFocusInWindow();
			}
		});
		btnClearAllEntities.setBounds(344, 248, 127, 29);
		frame.getContentPane().add(btnClearAllEntities);
	}

	private static void initializeLabels(){
		lblOptions = new JLabel("Ready to edit");
		lblOptions.setBounds(16, 354, 209, 16);
		frame.getContentPane().add(lblOptions);
		
		lblEntities = new JLabel("Press to add:");
		lblEntities.setBounds(348, 22, 100, 16);
		frame.getContentPane().add(lblEntities);
		
		lblDestructiveOptions = new JLabel("Options:");
		lblDestructiveOptions.setBounds(348, 228, 123, 16);
		frame.getContentPane().add(lblDestructiveOptions);
		
		lblFinalize = new JLabel("Finalize:");
		lblFinalize.setBounds(348, 315, 61, 16);
		frame.getContentPane().add(lblFinalize);
	}

	private static void initializeArmoredHeroCheckbox() {
		chckbxArmedHero = new JCheckBox("Armed Hero");
		chckbxArmedHero.setBounds(224, 350, 112, 23);
		frame.getContentPane().add(chckbxArmedHero);
		chckbxArmedHero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Entity ent : Game.getEntities()){
					if (ent.getRepresentation() == 'A' && !chckbxArmedHero.isSelected()){
						((Hero) ent).setArmed(false);
					}else if (ent.getRepresentation() == 'H' && chckbxArmedHero.isSelected()){
						((Hero) ent).setArmed(true);
					}
				}
				if(nextChar == 'A'){
					setStatus("Current Entity: Hero");
					nextChar = 'H';
				}else if (nextChar == 'H'){
					setStatus("Current Entity: Armed Hero");
					nextChar = 'A';
				}
				panel.repaint();
				panel.requestFocusInWindow();
			}
		});
	}

	private static void initializeOptionsButtons() {
		btnValidatePlay = new JButton("Validate & Play");
		btnValidatePlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GUI.validate()){
					frame.dispose();
				}else{
					lblOptions.setText("ERROR: Invalid Map!");
					nextChar = ' ';
					panel.requestFocusInWindow();
				}
				
			}
		});
		btnValidatePlay.setBounds(344, 333, 127, 29);
		frame.getContentPane().add(btnValidatePlay);	
	}

	public static char getNextChar() {
		return nextChar;
	}

	public static void setNextChar(char nextChar) {
		LevelEditor.nextChar = nextChar;
	}
	
	public static void setStatus(String status){
		lblOptions.setText(status);
	}

	public static void askForMace() {
		btnHero.setEnabled(false);
		btnOgre.setEnabled(false);
		btnKey.setEnabled(false);
		btnDoor.setEnabled(false);
		btnWall.setEnabled(false);
		btnValidatePlay.setEnabled(false);
		btnEmpty.setEnabled(false);
		btnClearAllEntities.setEnabled(false);
		btnClearAll.setEnabled(false);
		lblOptions.setText("Current Entity: Mace");
		nextChar = '*';
	}
	
	public static void finishedMacePlacement() {
		btnHero.setEnabled(true);
		btnOgre.setEnabled(true);
		btnKey.setEnabled(true);
		btnDoor.setEnabled(true);
		btnWall.setEnabled(true);
		btnValidatePlay.setEnabled(true);
		btnEmpty.setEnabled(true);
		btnClearAllEntities.setEnabled(true);
		btnClearAll.setEnabled(true);
		lblOptions.setText("Current Entity: Ogre");
		nextChar = '0';
	}
	
	private static void initializeGame(){
		Game.changeMap("Keep","");
		ArrayList<Entity> emptyList = new ArrayList<Entity>();
		
		KeepMap map = new KeepMap();
		char emptyMap[][] = new char[getMapWidth()][getMapWidth()];
		for(char []row : emptyMap){
			Arrays.fill(row, ' ');
		}
		map.setMap(emptyMap);
		
		Game.setEntities(emptyList);
		Game.setMapObject(map);
	}
	
	private static void initializePlacementMapButtons(){
		btnDoor = new JButton("Door");
		btnDoor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblOptions.setText("Current Entity: Door");
				nextChar = 'I';
				panel.requestFocusInWindow();
			}
		});
		btnDoor.setBounds(344, 129, 127, 29);
		frame.getContentPane().add(btnDoor);
		
		btnWall = new JButton("Wall");
		btnWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblOptions.setText("Current Entity: Wall");
				nextChar = 'X';
				panel.requestFocusInWindow();
			}
		});
		btnWall.setBounds(344, 100, 127, 29);
		frame.getContentPane().add(btnWall);
	}
	
	private static void initializePlacementKeyButton(){
		btnKey = new JButton("Key");
		btnKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextChar = 'k';
				lblOptions.setText("Current Entity: Key");
				panel.requestFocusInWindow();
			}
		});
		btnKey.setBounds(344, 72, 127, 29);
		frame.getContentPane().add(btnKey);
		
	}
	
	private static void initializePlacementEntityButtons(){
		
		initializeHeroButton();
		frame.getContentPane().add(btnHero);
		
		initializeOgreButton();
	}
	
	private static void initializeOgreButton() {
		btnOgre = new JButton("Ogre");
		btnOgre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextChar = '0';
				lblOptions.setText("Current Entity: Ogre");
				panel.requestFocusInWindow();
			}
		});
		btnOgre.setBounds(344, 159, 127, 29);
		frame.getContentPane().add(btnOgre);
	}

	private static void initializeHeroButton() {
		btnHero = new JButton("Hero");
		btnHero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxArmedHero.isSelected()){
					nextChar = 'A';
					lblOptions.setText("Current Entity: Armed Hero");
				}else{
					nextChar = 'H';
					lblOptions.setText("Current Entity: Hero");
				}
				panel.requestFocusInWindow();
			}
		});
		btnHero.setBounds(344, 45, 127, 29);
	}

	public static void setVisible(){
		frame.setVisible(true);
	}
	
	public static void dispose(){
		frame.dispose();
	}

	public static int getMapWidth() {
		return mapWidth;
	}

	public static void setMapWidth(int mapWidth_) {
		mapWidth = mapWidth_;
	}

	public static int getMapHeight() {
		return mapHeight;
	}

	public static void setMapHeight(int mapHeight_) {
		mapHeight = mapHeight_;
	}
}
