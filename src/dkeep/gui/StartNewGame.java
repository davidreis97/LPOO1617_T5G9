package dkeep.gui;

import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;

import dkeep.logic.Game;

public class StartNewGame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static JComboBox<String> comboBox;
	private static JSlider slider;

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
		setBounds(100, 100, 402, 126);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		initializeOptionsElements();
		
		initializeStartButton();
		
		initializeCancelButton();
	}

	private void initializeCancelButton() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.updateGUIStatus();
				dispose();
			}
		});
		btnCancel.setBounds(279, 53, 117, 29);
		contentPanel.add(btnCancel);
	}

	private void initializeStartButton() {
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Game("Dungeon", (String) comboBox.getSelectedItem(), slider.getValue());
				GUI.updateGUIStatus();
				dispose();
			}
		});
		btnStart.setBounds(279, 18, 117, 29);
		contentPanel.add(btnStart);
	}

	private void initializeOptionsElements() {
		JLabel lblOgreNumber = new JLabel("Number of Ogres:");
		lblOgreNumber.setBounds(20, 58, 122, 16);
		contentPanel.add(lblOgreNumber);
		
		JLabel lblGuardType = new JLabel("Guard Type:");
		lblGuardType.setBounds(20, 23, 105, 16);
		contentPanel.add(lblGuardType);
		
		slider = new JSlider(1,5,1);
		slider.setBounds(137, 53, 113, 44);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(1);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		contentPanel.add(slider);
		
		String[] guardtypes = new String[] {"Rookie", "Drunken",
        "Suspicious"};

		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(guardtypes);
		comboBox = new JComboBox<>(comboModel);
		comboBox.setBounds(100, 19, 172, 27);
		contentPanel.add(comboBox);
	}
}
