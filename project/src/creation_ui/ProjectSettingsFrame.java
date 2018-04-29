package creation_ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import enums.MISListType;
import project.MISProject;
import rules.MISRule;
import settings.MISProjectSettings;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.DefaultComboBoxModel;

public class ProjectSettingsFrame extends JDialog {

	private JPanel contentPane;
	private JTextField textFieldProjectName;
	private JTextField textFieldProjectMinimumVersion;
	private JTextField textFieldBasePort;
	private JSpinner spinnerRefreshRate;
	private JComboBox comboBoxListType;
	private JSpinner spinnerMMPCPS;
	private JSpinner spinnerSessionTimeout;
	private JButton btnSave;
	private JButton btnCancel;
	private boolean wasAnythingChanged = false;

	/**
	 * Create the frame.
	 */
	public ProjectSettingsFrame(JFrame owner, MISProject project) {
		super(owner);
		setTitle("Project settings");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblProjectName = new JLabel("Project name:");
		
		textFieldProjectName = new JTextField();
		textFieldProjectName.setColumns(10);
		textFieldProjectName.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		JLabel lblProjectMinimumVersion = new JLabel("Project minimum version:");
		
		textFieldProjectMinimumVersion = new JTextField();
		textFieldProjectMinimumVersion.setColumns(10);
		textFieldProjectMinimumVersion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		JLabel lblListType = new JLabel("List type:");
		
		comboBoxListType = new JComboBox();
		comboBoxListType.setModel(new DefaultComboBoxModel(new String[] {"ARRAY", "LINKED"}));
		comboBoxListType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		JLabel lblRefreshRate = new JLabel("Refresh rate:");
		
		spinnerRefreshRate = new JSpinner();
		spinnerRefreshRate.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		JLabel lblMm = new JLabel("Mmpcps:");
		lblMm.setToolTipText("Max messages per client per second");
		
		spinnerMMPCPS = new JSpinner();
		spinnerMMPCPS.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		JLabel lblBasePort = new JLabel("Base port:");
		
		textFieldBasePort = new JTextField();
		textFieldBasePort.setColumns(10);
		textFieldBasePort.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		JLabel lblSessionTimeout = new JLabel("Session timeout:");
		
		spinnerSessionTimeout = new JSpinner();
		spinnerSessionTimeout.setToolTipText("in seconds");
		spinnerSessionTimeout.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				btnSave.setEnabled(true);
			}
		});
		
		btnCancel = new JButton("Exit");
		
		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wasAnythingChanged){
					int dialogResult = JOptionPane.showConfirmDialog(null, "You have unsaved changes, are you sure you want discard the changes?");
					if(dialogResult == JOptionPane.YES_OPTION){
						dispose();
						setVisible(false);
					}
				} else{
					dispose();
					setVisible(false);
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wasAnythingChanged){
					updateValues();
					saveProject();
					btnSave.setEnabled(false);
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblBasePort)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldBasePort, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblProjectName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldProjectName, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(lblListType)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBoxListType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(lblProjectMinimumVersion)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textFieldProjectMinimumVersion, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblMm)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spinnerMMPCPS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblRefreshRate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spinnerRefreshRate, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblSessionTimeout)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spinnerSessionTimeout, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(296, Short.MAX_VALUE)
					.addComponent(btnSave)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectName)
						.addComponent(textFieldProjectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectMinimumVersion)
						.addComponent(textFieldProjectMinimumVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblListType)
						.addComponent(comboBoxListType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRefreshRate)
						.addComponent(spinnerRefreshRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMm)
						.addComponent(spinnerMMPCPS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBasePort)
						.addComponent(textFieldBasePort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSessionTimeout)
						.addComponent(spinnerSessionTimeout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnSave)))
		);
		contentPane.setLayout(gl_contentPane);
		
		setValues(project);
	}
	
	
	
	protected void saveProject() {
		// TODO Auto-generated method stub
		
	}



	protected void updateValues() {
		// TODO Auto-generated method stub
		
	}



	private void setValues(MISProject project) {
		textFieldProjectName.setText(project.projectName);
		textFieldProjectMinimumVersion.setText(""+project.minimumBuildVersion);
		textFieldBasePort.setText(""+project.basePort.port);
		spinnerRefreshRate.getModel().setValue(project.refreshRate);
		spinnerMMPCPS.getModel().setValue(project.maxMessagesPerClientPerSecond);
		spinnerSessionTimeout.getModel().setValue(project.timeOutDuration);
		if(project.listType.equals(MISListType.ARRAY)){
			comboBoxListType.setSelectedIndex(0);
		} else{
			comboBoxListType.setSelectedIndex(1);
		}
	}



	public void showDialog(){
		setVisible(true);
	}
}
