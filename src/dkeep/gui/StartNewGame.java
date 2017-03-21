package dkeep.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dkeep.logic.Game;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartNewGame extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void launch() {
		try {
			StartNewGame dialog = new StartNewGame();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public StartNewGame() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 351, 109);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblOgreNumber = new JLabel("Number of Ogres:");
		lblOgreNumber.setBounds(20, 17, 122, 16);
		contentPanel.add(lblOgreNumber);
		
		JLabel lblGuardType = new JLabel("Guard Type:");
		lblGuardType.setBounds(20, 45, 105, 16);
		contentPanel.add(lblGuardType);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(141, 12, 33, 26);
		contentPanel.add(spinner);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(96, 41, 105, 27);
		contentPanel.add(comboBox);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Game("Dungeon");
				GUI.updateGUIStatus();
				dispose();
			}
		});
		btnStart.setBounds(213, 12, 117, 29);
		contentPanel.add(btnStart);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.updateGUIStatus();
				dispose();
			}
		});
		btnCancel.setBounds(213, 40, 117, 29);
		contentPanel.add(btnCancel);
	}
}
