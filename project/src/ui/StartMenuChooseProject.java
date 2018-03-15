package ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.Component;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;

import loaders.MISLoader;
import project.MISProject;

import javax.swing.JList;
import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

public class StartMenuChooseProject extends JPanel {
	
	private JButton btnNewButton;
	private JButton btnLoadProject;
	private JList<String> list;
	private JTextField textFieldSearch;

	/**
	 * Create the panel.
	 */
	public StartMenuChooseProject() {
		this.setSize(700, 700);
		
		initializeComponents();
		handleEvents();
		
		
	}

	private void handleEvents() {
		btnLoadProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex >= 0){
					String pathOfProject = list.getSelectedValue();
					//Load the project selected
					MISProject.loadProject(pathOfProject);
					MISProject.printProjectData();
				}
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
		});
	}

	private void initializeComponents() {
		JLabel lblTitle = new JLabel("");
		lblTitle.setEnabled(false);
		lblTitle.setIcon(new ImageIcon(StartMenuChooseProject.class.getResource("/resources/MIS_LOGO.png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		JLabel lblExistingProjects = new JLabel("Existing Projects:");
		lblExistingProjects.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		DefaultListModel<String> model = new DefaultListModel<>();
		list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		String[] projectLocations = MISLoader.loadProjectLocations();
		for(int i = 0; i < projectLocations.length; i++){
			model.addElement(projectLocations[i]);
		}
		list.setBackground(Color.WHITE);
		
		btnLoadProject = new JButton("Load Project");
		btnLoadProject.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		btnNewButton = new JButton("Create new project");
		btnNewButton.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		JButton btnImportProject = new JButton("Import");
		btnImportProject.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JButton btnRemoveProject = new JButton("Remove");
		btnRemoveProject.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JButton btnRun = new JButton("Run");
		btnRun.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		textFieldSearch = new JTextField();
		textFieldSearch.setFont(new Font("Dialog", Font.PLAIN, 17));
		textFieldSearch.setColumns(10);
		
		JLabel lblCreator = new JLabel("Created by Morten J\u00F8rvad");
		lblCreator.setFont(new Font("Sitka Small", Font.PLAIN, 11));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(70)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblExistingProjects, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblSearch))
								.addComponent(list, GroupLayout.PREFERRED_SIZE, 338, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCreator)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(textFieldSearch)
									.addComponent(btnRun, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnRemoveProject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnImportProject, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
									.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnLoadProject, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 680, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExistingProjects, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSearch)
						.addComponent(textFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(list, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnLoadProject)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnImportProject, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRemoveProject)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRun)
							.addPreferredGap(ComponentPlacement.RELATED, 161, Short.MAX_VALUE)
							.addComponent(lblCreator)))
					.addGap(144))
		);
		setLayout(groupLayout);
	}
}
