package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import settings.MISProjectSettings;

public class ApplicationWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationWindow window = new ApplicationWindow();
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
	public ApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
