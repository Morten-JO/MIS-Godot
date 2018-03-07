package ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;

public class StartMenuChooseProject extends JPanel {
	private final Action loadProject = new SwingAction_1();
	private final Action createNewProject = new SwingAction_2();

	/**
	 * Create the panel.
	 */
	public StartMenuChooseProject() {
		setLayout(null);
		this.setSize(700, 700);
		
		JLabel lblChooseAction = new JLabel("Choose Action");
		lblChooseAction.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseAction.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		lblChooseAction.setBounds(231, 62, 193, 48);
		add(lblChooseAction);
		
		JLabel lblExistingProjects = new JLabel("Existing Projects");
		lblExistingProjects.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		lblExistingProjects.setBounds(70, 143, 118, 28);
		add(lblExistingProjects);
		
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(Color.CYAN);
		list.setBounds(70, 182, 583, 343);
		add(list);
		
		JButton btnLoadProject = new JButton("Load Project");
		btnLoadProject.setAction(loadProject);
		btnLoadProject.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		btnLoadProject.setBounds(70, 536, 250, 64);
		add(btnLoadProject);
		
		JButton btnNewButton = new JButton("Create new project");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setAction(createNewProject);
		btnNewButton.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		btnNewButton.setBounds(390, 536, 263, 64);
		add(btnNewButton);

	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "loadProjectAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "newProjectAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
