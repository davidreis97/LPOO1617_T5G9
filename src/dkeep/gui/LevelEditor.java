package dkeep.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dkeep.logic.Entity;
import dkeep.logic.Game;
import dkeep.logic.KeepMap;
import dkeep.logic.Map;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class LevelEditor {

	private static char nextChar = ' ';
	private JFrame frame;
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

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LevelEditor window = new LevelEditor();
					window.frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
	public LevelEditor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Game.changeMap("Keep");
		ArrayList<Entity> emptyList = new ArrayList<Entity>();
		
		KeepMap map = new KeepMap();
		char emptyMap[][] = new char[10][10];
		map.setMap(emptyMap);
		
		Game.setEntities(emptyList);
		Game.setMapObject(map);
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 477, 398);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblOptions = new JLabel("Ready to edit");
		lblOptions.setBounds(16, 354, 556, 16);
		frame.getContentPane().add(lblOptions);
		
		JPanel panel = new SimpleGraphicsPanel(true);
		panel.setBounds(16, 22, 320, 320);
		frame.getContentPane().add(panel);
		
		JLabel lblEntities = new JLabel("Press to add:");
		lblEntities.setBounds(348, 22, 100, 16);
		frame.getContentPane().add(lblEntities);
		
		btnHero = new JButton("Hero");
		btnHero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextChar = 'H';
				lblOptions.setText("Current Entity: Hero");
				panel.requestFocusInWindow();
			}
		});
		btnHero.setBounds(344, 45, 127, 29);
		frame.getContentPane().add(btnHero);
		
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
		
		btnClearAll = new JButton("Clear all Map");
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeepMap map = new KeepMap();
				char emptyMap[][] = new char[10][10];
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
		
		JLabel lblDestructiveOptions = new JLabel("Options:");
		lblDestructiveOptions.setBounds(348, 228, 123, 16);
		frame.getContentPane().add(lblDestructiveOptions);
		
		JLabel lblFinalize = new JLabel("Finalize:");
		lblFinalize.setBounds(348, 315, 61, 16);
		frame.getContentPane().add(lblFinalize);
		
		panel.repaint();
		panel.requestFocusInWindow();
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
		lblOptions.setText("Current Entity: Mace (Please place the mace before doing anything else)");
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

}