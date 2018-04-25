package creation_ui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import data_types.MISScene;
import enums.MISListType;
import enums.MISType;
import loaders.MISLoader;
import project.MISProject;
import project.MISProjectInformation;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.AbstractListModel;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateNewProjectSettings extends JPanel {
	private final ButtonGroup networkSide = new ButtonGroup();
	private JTextField textFieldClientBuild;
	private ApplicationWindow frame;
	
	
	private String projectName;
	private String projectLocation;
	private String godotLocation;
	private JButton btnBack;
	private JButton btnCreate;
	private JList listProtocols;
	private JCheckBox chckbxUi;
	private JSpinner spinnerSessionTimeout;
	private JCheckBox chckbxEncryption;
	private JComboBox comboBoxSavingType;
	private JSpinner spinnerMMPSPC;
	private JSpinner spinnerRefreshRate;
	private JRadioButton rdbtnServerside;
	private JRadioButton rdbtnClientSide;
	
	public void putVariables(String projName, String projLoc, String godotLoc){
		this.projectName = projName;
		this.projectLocation = projLoc;
		this.godotLocation = godotLoc;
	}
	
	/**
	 * Create the panel.
	 */
	public CreateNewProjectSettings(ApplicationWindow frame) {
		this.setSize(700, 700);
		
		this.frame = frame;
		
		initializeComponents();
		handleEvents();
		
	}

	private void initializeComponents() {
		JLabel lblTitle = new JLabel("");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setEnabled(false);
		lblTitle.setIcon(new ImageIcon(CreateNewProjectSettings.class.getResource("/resources/MIS_LOGO.png")));
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblProjectName = new JLabel("Project Name:");
		lblProjectName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblProjectNameField = new JLabel("");
		lblProjectNameField.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		rdbtnClientSide = new JRadioButton("Clientside");
		networkSide.add(rdbtnClientSide);
		
		rdbtnServerside = new JRadioButton("Serverside");
		networkSide.add(rdbtnServerside);
		
		JLabel lblRefreshRate = new JLabel("Refresh rate:");
		
		spinnerRefreshRate = new JSpinner();
		
		JLabel lblMmpspc = new JLabel("MMPSPC:");
		lblMmpspc.setToolTipText("Max messages per client per second");
		
		spinnerMMPSPC = new JSpinner();
		
		JLabel lblSavingType = new JLabel("Saving type:");
		
		comboBoxSavingType = new JComboBox();
		comboBoxSavingType.setModel(new DefaultComboBoxModel(new String[] {"Text", "Database"}));
		
		chckbxEncryption = new JCheckBox("Encryption");
		
		JLabel lblSessionTimeout = new JLabel("Session Timeout:");
		
		spinnerSessionTimeout = new JSpinner();
		
		chckbxUi = new JCheckBox("UI on run");
		
		JLabel lblNewLabel = new JLabel("Required clientbuild:");
		
		textFieldClientBuild = new JTextField();
		textFieldClientBuild.setColumns(10);
		
		listProtocols = new JList();
		listProtocols.setBackground(SystemColor.menu);
		listProtocols.setModel(new AbstractListModel() {
			String[] values = new String[] {"TCP", "UDP"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JLabel lblProtocols = new JLabel("Protocols:");
		
		btnBack = new JButton("Back");
		
		btnBack.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		btnCreate = new JButton("Create");
		
		btnCreate.setFont(new Font("Dialog", Font.PLAIN, 17));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(60)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(rdbtnClientSide)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(rdbtnServerside, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblProjectName, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblProjectNameField, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
								.addComponent(chckbxEncryption)
								.addComponent(chckbxUi)
								.addComponent(lblProtocols)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(listProtocols, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textFieldClientBuild, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblSessionTimeout)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(spinnerSessionTimeout))
									.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(lblRefreshRate)
											.addComponent(lblMmpspc)
											.addComponent(lblSavingType))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(comboBoxSavingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(spinnerRefreshRate, Alignment.LEADING)
												.addComponent(spinnerMMPSPC, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))))))
							.addPreferredGap(ComponentPlacement.RELATED, 307, Short.MAX_VALUE)))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnBack)
					.addPreferredGap(ComponentPlacement.RELATED, 522, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addGap(18))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectName)
						.addComponent(lblProjectNameField))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnClientSide)
						.addComponent(rdbtnServerside))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRefreshRate)
						.addComponent(spinnerRefreshRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMmpspc)
						.addComponent(spinnerMMPSPC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSavingType)
						.addComponent(comboBoxSavingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxEncryption)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSessionTimeout)
						.addComponent(spinnerSessionTimeout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxUi)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(textFieldClientBuild, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblProtocols)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(listProtocols)
					.addPreferredGap(ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBack)
						.addComponent(btnCreate))
					.addContainerGap())
		);
		setLayout(groupLayout);
	}

	private void handleEvents() {
		
		btnCreate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(checkProblems()){
					ArrayList<MISScene> scenes = MISLoader.loadScenesByLocation(godotLocation);
					MISProject project = new MISProject();
					
					int spinnerRefreshRateValue = (Integer)spinnerRefreshRate.getValue();
					int spinnerMMPSPCValue = (Integer)spinnerMMPSPC.getValue();
					int spinnerSessionTimeoutValue = (Integer) spinnerSessionTimeout.getValue();
					
					project.maxMessagesPerClientPerSecond = spinnerMMPSPCValue;
					project.refreshRate = spinnerRefreshRateValue;
					project.timeOutDuration = spinnerSessionTimeoutValue;
					project.projectName = projectName;
					project.projectLocation = projectLocation;
					project.godotProjectLocation = godotLocation;
					project.minimumBuildVersion = Double.parseDouble(textFieldClientBuild.getText());
					project.listType = MISListType.ARRAY;
					project.targetEngine = MISType.Godot;
					project.scenes = scenes;
					
					MISProject.project = project;
					boolean success = MISProject.saveProject();
					
					MISProject.printProjectData();
					
					if(success){
						Date dateNow = new Date();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						
						MISProjectInformation info = new MISProjectInformation(project.projectName, project.projectLocation, format.format(dateNow), true);
						ArrayList<MISProjectInformation> infos = MISLoader.loadProjectLocations();
						infos.add(info);
						MISLoader.saveProjectLocation(infos);
						
						frame.frame.dispose();
						new MainViewWindow();
					} else{
						System.err.println("Could not save project "+project.projectName);
					}
							
				} else{
					System.out.println("Problems not fine.");
				}
			}
		});
		
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				frame.showCreateProjectMenu();
			}
		});
	}
	
	private boolean checkProblems(){
		if(!rdbtnClientSide.isSelected() && !rdbtnServerside.isSelected()){
			System.out.println("Radio button problems");
			return false;
		}
		
		int spinnerRefreshRateValue = (Integer)spinnerRefreshRate.getValue();
		if(spinnerRefreshRateValue <= 0){
			System.out.println("SpinnerRefresh problems");
			return false;
		}
		
		int spinnerMMPSPCValue = (Integer)spinnerMMPSPC.getValue();
		if(spinnerMMPSPCValue <= 0){
			System.out.println("spinnerMMP problems");
			return false;
		}
		
		int spinnerSessionTimeoutValue = (Integer) spinnerSessionTimeout.getValue();
		if(spinnerSessionTimeoutValue < 0){
			System.out.println("spinnersessiontrimeout problems");
			return false;
		}
		
		if(textFieldClientBuild.getText().isEmpty()){
			System.out.println("clientbuild problems");
			return false;
		}
		
		if(listProtocols.isSelectionEmpty()){
			System.out.println("protocols problems");
			return false;
		}
		return true;
		
	}
}