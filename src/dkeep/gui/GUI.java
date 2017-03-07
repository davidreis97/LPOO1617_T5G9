package dkeep.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dkeep.logic.Entity;
import dkeep.logic.Game;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class GUI {

	private JFrame frame;
	private JTextField textField;

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
		new Game("Dungeon");
		Game.setState("You can start a new game");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 604, 417);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Number of Ogres");
		lblNewLabel.setBounds(16, 23, 113, 16);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(141, 18, 44, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Guard Personality");
		lblNewLabel_1.setBounds(16, 51, 118, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(141, 47, 113, 27);
		frame.getContentPane().add(comboBox);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		textArea.setBounds(16, 88, 397, 276);
		frame.getContentPane().add(textArea);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(26, 373, 390, 16);
		frame.getContentPane().add(lblStatus);
		
		JButton btnStartNewGame = new JButton("Start New Game");
		btnStartNewGame.setBounds(440, 83, 141, 29);
		frame.getContentPane().add(btnStartNewGame);
		
		JButton btnUp = new JButton("Up");
		btnUp.setBounds(464, 150, 88, 29);
		frame.getContentPane().add(btnUp);
		
		JButton btnLeft = new JButton("Left");
		btnLeft.setBounds(425, 173, 83, 29);
		frame.getContentPane().add(btnLeft);
		
		JButton btnRight = new JButton("Right");
		btnRight.setBounds(510, 173, 88, 29);
		frame.getContentPane().add(btnRight);
		
		JButton btnDown = new JButton("Down");
		btnDown.setBounds(464, 196, 88, 29);
		frame.getContentPane().add(btnDown);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(450, 335, 117, 29);
		frame.getContentPane().add(btnExit);
		
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('d', false);
				updateGUIStatus( btnDown,btnUp,btnLeft,btnRight, textArea,lblStatus);
			}
		});
		
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('w', false);
				updateGUIStatus( btnDown,btnUp,btnLeft,btnRight, textArea,lblStatus);
			}
		});
		
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('a', false);
				updateGUIStatus( btnDown,btnUp,btnLeft,btnRight, textArea,lblStatus);
			}
		});
		
		btnStartNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Game("Dungeon");
				updateGUIStatus( btnDown,btnUp,btnLeft,btnRight, textArea,lblStatus);
			}
		});
		
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('s', false);
				updateGUIStatus( btnDown,btnUp,btnLeft,btnRight, textArea,lblStatus);
			}
		});
	}
	
	private void updateGUIStatus(JButton btnDown,JButton btnUp,JButton btnLeft,JButton btnRight, JTextArea textArea, JLabel lblStatus) {
		textArea.setText(printMap(Game.getMap(),Game.getEntities()));
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
}
