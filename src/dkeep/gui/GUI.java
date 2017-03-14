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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;
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
		new Game("Dungeon");
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
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(382, 307, 117, 29);
		frame.getContentPane().add(btnExit);
		
		panel = new SimpleGraphicsPanel();
		panel.setBounds(16, 22, 320, 320);
		frame.getContentPane().add(panel);
		panel.repaint();
		
		
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
		
		btnStartNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startNewGame.launch();
			}
		});
		
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.updateGame('s', false);
				updateGUIStatus();
			}
		});
		updateGUIStatus();
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
}
