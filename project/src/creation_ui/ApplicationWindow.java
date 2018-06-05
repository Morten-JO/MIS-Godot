package creation_ui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import settings.MISProjectSettings;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.CardLayout;

public class ApplicationWindow {

	public JFrame frame;

	CreateNewProjectSettings createProjectSettings;
	JPanel createProjectPanel;
	JPanel startMenuPanel;
	
	

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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		frame.setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		frame.setTitle("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		startMenuPanel = new StartMenuChooseProject(this);
		frame.getContentPane().add(startMenuPanel, "startMenuPanel");
		
		createProjectPanel = new CreateNewProject(this);
		frame.getContentPane().add(createProjectPanel, "createProjectPanel");
		
		createProjectSettings = new CreateNewProjectSettings(this);
		frame.getContentPane().add(createProjectSettings, "createProjectSettingsPanel");
		
	}
	
	public void showStartMenu(){
		CardLayout layout = (CardLayout) frame.getContentPane().getLayout();
		layout.show(frame.getContentPane(), "startMenuPanel");
	}
	
	public void showCreateProjectMenu(){
		CardLayout layout = (CardLayout) frame.getContentPane().getLayout();
		layout.show(frame.getContentPane(), "createProjectPanel");
	}
	
	public void showCreateProjectSettings(String projName, String projLoc, String godotLoc){
		CardLayout layout = (CardLayout) frame.getContentPane().getLayout();
		layout.show(frame.getContentPane(), "createProjectSettingsPanel");
		createProjectSettings.putVariables(projName, projLoc, godotLoc);
		createProjectSettings.updateVariables();
	}
}
