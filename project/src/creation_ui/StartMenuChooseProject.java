package creation_ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

import java.awt.Component;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;

import loaders.MISLoader;
import project.MISProject;
import project.MISProjectInformation;
import server.Server;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import javafx.stage.FileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ImageIcon;

public class StartMenuChooseProject extends JPanel {
	
	private JButton btnNewButton;
	private JButton btnLoadProject;
	private JList<MISProjectInformation> list;
	private JTextField textFieldSearch;
	private ArrayList<MISProjectInformation> projectLocations;
	private JButton btnRemoveProject;
	private JButton btnImportProject;
	private JButton btnRun;
	private ApplicationWindow frame;
	
	/**
	 * Create the panel.
	 */
	public StartMenuChooseProject(ApplicationWindow frame) {
		this.setSize(700, 700);
		
		initializeComponents();
		handleEvents();
		this.frame = frame;
		
	}

	private void handleEvents() {
		btnLoadProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex >= 0){
					MISProjectInformation informationObject = list.getSelectedValue();
					//Load the project selected
					if(informationObject.isFoundProject()){
						boolean success = MISProject.loadProject(informationObject.getProjectLocation());
						MISProject.printProjectData();
						if(success){
							frame.frame.dispose();
							new MainViewWindow();
						}
					} else{
						JOptionPane.showMessageDialog(null, "Project wasn't found at specified location.");
					}
				}
			}
		});
		
		btnImportProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.addChoosableFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public boolean accept(File f) {
						if(f.isDirectory()){
							return true;
						}
						if(f.getName().equals("project.json")){
							return true;
						}
						return false;
					}
				});
				int returnValue = fileChooser.showOpenDialog(StartMenuChooseProject.this);
				
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					System.out.println("Chosen File: "+file.getAbsolutePath());
					String fileString = file.getParent() + " " + LocalDate.now().toString();
					MISProjectInformation info = MISLoader.getInformationFromString(fileString);
					projectLocations.add(info);
					MISLoader.saveProjectLocation(projectLocations);
					if(textFieldSearch.getText().equals("")){
						remakeList();
					} else{
						remakeList(textFieldSearch.getText());
					}
				}
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				frame.showCreateProjectMenu();
			}
		});
		
		btnRemoveProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex >= 0){
					int returnValue = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove your project?");
					if(returnValue == JOptionPane.YES_OPTION){
						MISProjectInformation informationObject = list.getSelectedValue();
						for(int i = 0; i < projectLocations.size(); i++){
							if(informationObject.getProjectName().equals(projectLocations.get(i).getProjectName())){
								if(informationObject.getProjectDateCreation().equals(projectLocations.get(i).getProjectDateCreation())){
									projectLocations.remove(i);
									break;
								}
							}
						}
						MISLoader.saveProjectLocation(projectLocations);
						if(textFieldSearch.getText().equals("")){
							remakeList();
						} else{
							remakeList(textFieldSearch.getText());
						}
					}
				}
			}
		});
		
		btnRun.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent arg0){
				int selectedIndex = list.getSelectedIndex();
				if(selectedIndex >= 0){
					MISProjectInformation informationObject = list.getSelectedValue();
					//Load the project selected
					if(informationObject.isFoundProject()){
						boolean success = MISProject.loadProject(informationObject.getProjectLocation());
						MISProject.printProjectData();
						if(success){
							new Server(MISProject.project.basePort.port, MISProject.project.uiOnRun, false);
						}
					}
				}
			}
			
		});
		
		textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				listChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				listChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
			private void listChange(){
				if(textFieldSearch.getText().equals("")){
					remakeList();
				} else{
					remakeList(textFieldSearch.getText());
				}
			}
			
		});
	}
	
	private void remakeList(String search){
		list.removeAll();
		DefaultListModel<MISProjectInformation> model = new DefaultListModel<>();
		list.setModel(model);
		for(int i = 0; i < projectLocations.size(); i++){
			MISProjectInformation info = projectLocations.get(i);
			if(info.getProjectLocation().contains(search) || info.getProjectDateCreation().contains(search)){
				model.addElement(projectLocations.get(i));
			}
		}
	}
	
	private void createList(){
		DefaultListModel<MISProjectInformation> model = new DefaultListModel<>();
		list = new JList<MISProjectInformation>(model);
		list.setFont(new Font("Dialog", Font.PLAIN, 17));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		projectLocations = MISLoader.loadProjectLocations();
		for(int i = 0; i < projectLocations.size(); i++){
			model.addElement(projectLocations.get(i));
		}
		list.setBackground(Color.WHITE);
		list.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(renderer instanceof JLabel && value instanceof MISProjectInformation){
					MISProjectInformation info = (MISProjectInformation)value;
					((JLabel)renderer).setText(info.getProjectName()+" - "+info.getProjectDateCreation());
					if(info.isFoundProject()){
						((JLabel) renderer).setForeground(Color.GREEN);
					} else{
						((JLabel) renderer).setForeground(Color.RED);
					}
				}
				return renderer;
			}
		});
	}
	
	private void remakeList(){
		list.removeAll();
		DefaultListModel<MISProjectInformation> model = new DefaultListModel<>();
		list.setModel(model);
		for(int i = 0; i < projectLocations.size(); i++){
			model.addElement(projectLocations.get(i));
		}
	}

	private void initializeComponents() {
		JLabel lblTitle = new JLabel("");
		lblTitle.setEnabled(false);
		lblTitle.setIcon(new ImageIcon(StartMenuChooseProject.class.getResource("/resources/MIS_LOGO.png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		JLabel lblExistingProjects = new JLabel("Existing Projects:");
		lblExistingProjects.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		createList();
		
		btnLoadProject = new JButton("Load Project");
		btnLoadProject.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		btnNewButton = new JButton("Create new project");
		btnNewButton.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		btnImportProject = new JButton("Import");
		btnImportProject.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		btnRemoveProject = new JButton("Remove");
		
		btnRemoveProject.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		btnRun = new JButton("Run");
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
