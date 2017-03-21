package dkeep.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dkeep.logic.Game;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
		String[] guardtypes = new String[] {"Normal", "Suspicious",
                "Drunk"};
		
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 402, 109);
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
		
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10, 1);  
		JSpinner spinner = new JSpinner(model);
		spinner.setBounds(135, 12, 61, 26);
		contentPanel.add(spinner);
		
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(guardtypes);
		
		JComboBox comboBox = new JComboBox(comboModel);
		comboBox.setBounds(96, 41, 155, 27);
		contentPanel.add(comboBox);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					new Game("Dungeon",(String) comboBox.getSelectedItem(),(int) spinner.getValue());
					GUI.updateGUIStatus();
					dispose();
				}catch (Exception excep){
					spinner.setValue("NaN");
				}
			}
		});
		btnStart.setBounds(263, 12, 117, 29);
		contentPanel.add(btnStart);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.updateGUIStatus();
				dispose();
			}
		});
		btnCancel.setBounds(263, 40, 117, 29);
		contentPanel.add(btnCancel);
	}
}
