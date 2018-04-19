package creation_ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import loaders.MISLoader;
import project.MISProjectInformation;

import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateNewProject extends JPanel {
	
	private JTextField nameTextField;
	private JTextField locationTextField;
	private JTextField godotLocationTextField;
	private JButton btnBrowse;
	private JButton btnGodotBrowse;
	private JButton btnLoad;
	private JButton btnBack;
	private JButton btnNext;

	private ApplicationWindow frame;
	private JTextArea textAreaProblems;
	
	/**
	 * Create the panel.
	 */
	public CreateNewProject(ApplicationWindow frame) {
		this.setSize(700, 700);
		
		this.frame = frame;
		
		initializeComponents();
		handleEvents();
		checkProblems();
	}

	private void handleEvents() {
		nameTextField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkProblems();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkProblems();
			}
			
		});
		
		locationTextField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkProblems();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkProblems();
			}
			
		});
		
		godotLocationTextField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkProblems();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkProblems();
			}
			
		});
	
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				int returnValue = fileChooser.showOpenDialog(CreateNewProject.this);
				
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					locationTextField.setText(file.getAbsolutePath());
				}
			}
		});
		
		btnGodotBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				int returnValue = fileChooser.showOpenDialog(CreateNewProject.this);
				
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					godotLocationTextField.setText(file.getAbsolutePath());
				}
			}
		});
		
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
		});
		
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				frame.showStartMenu();
			}
		});
		
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(checkProblems()){
					frame.showCreateProjectSettings(nameTextField.getText(), locationTextField.getText(), godotLocationTextField.getText());
				}
			}
		});
	}

	private void initializeComponents() {
		JLabel lblTitle = new JLabel("");
		lblTitle.setEnabled(false);
		lblTitle.setIcon(new ImageIcon(CreateNewProject.class.getResource("/resources/MIS_LOGO.png")));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		
		nameTextField = new JTextField();
		nameTextField.setFont(new Font("Dialog", Font.PLAIN, 17));
		nameTextField.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		locationTextField = new JTextField();
		locationTextField.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblDefaultLocation = new JLabel("Default location:");
		lblDefaultLocation.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblDefaultLocation.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		btnNext = new JButton("Next");
		btnNext.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblGodotProject = new JLabel("Godot Project:");
		lblGodotProject.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		godotLocationTextField = new JTextField();
		godotLocationTextField.setFont(new Font("Dialog", Font.PLAIN, 17));
		godotLocationTextField.setColumns(10);
		
		btnGodotBrowse = new JButton("Browse");
		
		btnLoad = new JButton("Load");
		
		textAreaProblems = new JTextArea();
		textAreaProblems.setForeground(Color.RED);
		textAreaProblems.setEditable(false);
		textAreaProblems.setFont(new Font("Dialog", Font.PLAIN, 13));
		textAreaProblems.setBackground(SystemColor.menu);
		
		JLabel lblProblems = new JLabel("Problems:");
		lblProblems.setFont(new Font("Dialog", Font.PLAIN, 17));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 680, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(58)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLocation, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(locationTextField, Alignment.LEADING)
										.addComponent(nameTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)))
								.addComponent(lblDefaultLocation)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblGodotProject)
									.addGap(18)
									.addComponent(godotLocationTextField))
								.addComponent(lblProblems)
								.addComponent(textAreaProblems))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnGodotBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnLoad, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnBack)
					.addPreferredGap(ComponentPlacement.RELATED, 532, Short.MAX_VALUE)
					.addComponent(btnNext)
					.addGap(26))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(locationTextField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLocation)
						.addComponent(btnBrowse))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblDefaultLocation)
					.addGap(61)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGodotProject)
						.addComponent(godotLocationTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnGodotBrowse))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnLoad)
					.addGap(12)
					.addComponent(lblProblems)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textAreaProblems, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBack)
						.addComponent(btnNext))
					.addContainerGap())
		);
		setLayout(groupLayout);
	}

	private boolean checkProblems(){
		ArrayList<String> problems = new ArrayList<String>();
		
		if(nameTextField.getText() == "" || nameTextField.getText().isEmpty()){
			problems.add("Invalid name, please type a proper name.");
		}
		
		boolean validLocation = false;
		if(locationTextField.getText() != "" && !locationTextField.getText().isEmpty()){
			File f = new File(locationTextField.getText());
			if(f.isDirectory()){
				validLocation = true;
			}
		}
		
		if(!validLocation){
			problems.add("Invalid location, please select a proper location.");
		}
		
		validLocation = false;
		if(godotLocationTextField.getText() != "" && !godotLocationTextField.getText().isEmpty()){
			File filesMainFolder = new File(godotLocationTextField.getText());
			if(filesMainFolder.isDirectory()){
				File[] filesInDirectory = filesMainFolder.listFiles();
				for(int i = 0 ; i<  filesInDirectory.length; i++){
					if(filesInDirectory[i].getName().equals("engine.cfg")){
						validLocation = true;
						break;
					}
				}
			}
		}
		
		if(!validLocation){
			problems.add("Invalid project, couldn't find engine file.");
		}
		
		textAreaProblems.setText("");
		for(int i = 0; i < problems.size(); i++){
			textAreaProblems.setText(textAreaProblems.getText()+problems.get(i)+System.lineSeparator());
		}
		
		if(problems.size() > 0){
			return false;
		} else{
			return true;
		}
		
	}
}
