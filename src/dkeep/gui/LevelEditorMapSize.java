package dkeep.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dkeep.logic.Game;

import javax.swing.JLabel;
import javax.swing.JSlider;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class LevelEditorMapSize extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private static LevelEditorMapSize dialog;
	private JSlider height, width;

	/**
	 * Launch the application.
	 */
	public static void start() {
		try {
			dialog = new LevelEditorMapSize();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LevelEditorMapSize() {
		setBounds(100, 100, 278, 171);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		initializeLabelsSliders();
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						LevelEditor.setMapHeight(height.getValue());
						LevelEditor.setMapWidth(width.getValue());
						LevelEditor.initialize();
						LevelEditor.setStatus("Map Height: " + LevelEditor.getMapHeight() + " Map Width: " + LevelEditor.getMapWidth());
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	private void initializeLabelsSliders() {
		JLabel lblMapHeight = new JLabel("Map Height:");
		lblMapHeight.setBounds(16, 10, 77, 16);
		contentPanel.add(lblMapHeight);
		
		JLabel lblMapWidth = new JLabel("Map Width:");
		lblMapWidth.setBounds(16, 51, 77, 16);
		contentPanel.add(lblMapWidth);
		
		height = new JSlider(5,10,10);
		height.setBounds(95, 6, 139, 43);
		height.setMajorTickSpacing(1);
		height.setPaintLabels(true);
		height.setMinorTickSpacing(1);
		height.setSnapToTicks(true);
		height.setPaintTicks(true);
		contentPanel.add(height);
		
		width = new JSlider(5,10,10);
		width.setBounds(95, 49, 139, 43);
		width.setMajorTickSpacing(1);
		width.setPaintLabels(true);
		width.setMinorTickSpacing(1);
		width.setSnapToTicks(true);
		width.setPaintTicks(true);
		contentPanel.add(width);
	}
}