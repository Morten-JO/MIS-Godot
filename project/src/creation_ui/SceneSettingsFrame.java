package creation_ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data_types.MISRoomSettings;
import data_types.MISScene;
import project.MISProject;
import settings.MISProjectSettings;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.CardLayout;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class SceneSettingsFrame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MISScene scene;
	private JSpinner spinnerMinimumPlayers;
	private JCheckBox chckbxAutoQueue;
	private JSpinner spinnerMaximumPlayers;
	private JButton saveButton;
	private JButton exitButton;
	private boolean wasAnythingChanged = false;
	private JCheckBox chckbxRoomInScene;
	private JPanel cardLayoutPanel;
	private JSpinner spinnerTeamsInRoom;

	/**
	 * Create the dialog.
	 */
	public SceneSettingsFrame(JFrame owner, MISScene scene) {
		super(owner);
		setTitle("Scene settings - "+scene.name);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		this.scene = scene;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		chckbxRoomInScene = new JCheckBox("Room Scene");
		chckbxRoomInScene.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				saveButton.setEnabled(true);
				CardLayout layout = (CardLayout) cardLayoutPanel.getLayout();
				if(chckbxRoomInScene.isSelected()){
					layout.show(cardLayoutPanel, "roomCard");
				} else{
					layout.show(cardLayoutPanel, "noRoomCard");
				}
			}
		});
		
		cardLayoutPanel = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxRoomInScene)
						.addComponent(cardLayoutPanel, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(chckbxRoomInScene)
					.addGap(7)
					.addComponent(cardLayoutPanel, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(69, Short.MAX_VALUE))
		);
		cardLayoutPanel.setLayout(new CardLayout(0, 0));
		
		JPanel roomCardPanel = new JPanel();
		cardLayoutPanel.add(roomCardPanel, "roomCard");
		
		JLabel lblMinimumPlayers = new JLabel("Minimum players:");
		
		spinnerMinimumPlayers = new JSpinner();
		spinnerMinimumPlayers.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				saveButton.setEnabled(true);
			}
		});
		
		JLabel lblMaximumPlayers = new JLabel("Maximum players:");
		
		spinnerMaximumPlayers = new JSpinner();
		spinnerMaximumPlayers.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				saveButton.setEnabled(true);
			}
		});
		chckbxAutoQueue = new JCheckBox("Auto Queue");
		chckbxAutoQueue.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				saveButton.setEnabled(true);
			}
		});
		JLabel lblTeamLayout = new JLabel("Teams In Room:");
		
		spinnerTeamsInRoom = new JSpinner();
		spinnerTeamsInRoom.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				wasAnythingChanged = true;
				saveButton.setEnabled(true);
			}
		});
		
		GroupLayout gl_roomCardPanel = new GroupLayout(roomCardPanel);
		gl_roomCardPanel.setHorizontalGroup(
			gl_roomCardPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_roomCardPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_roomCardPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxAutoQueue)
						.addGroup(gl_roomCardPanel.createSequentialGroup()
							.addGroup(gl_roomCardPanel.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(Alignment.LEADING, gl_roomCardPanel.createSequentialGroup()
									.addComponent(lblTeamLayout)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(spinnerTeamsInRoom))
								.addGroup(Alignment.LEADING, gl_roomCardPanel.createSequentialGroup()
									.addComponent(lblMinimumPlayers)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(spinnerMinimumPlayers, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))
							.addGap(49)
							.addComponent(lblMaximumPlayers)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spinnerMaximumPlayers, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(79, Short.MAX_VALUE))
		);
		gl_roomCardPanel.setVerticalGroup(
			gl_roomCardPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_roomCardPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_roomCardPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinimumPlayers)
						.addComponent(spinnerMinimumPlayers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMaximumPlayers)
						.addComponent(spinnerMaximumPlayers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addComponent(chckbxAutoQueue)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_roomCardPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTeamLayout)
						.addComponent(spinnerTeamsInRoom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		roomCardPanel.setLayout(gl_roomCardPanel);
		
		JPanel noRoomCardPanel = new JPanel();
		cardLayoutPanel.add(noRoomCardPanel, "noRoomCard");
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				saveButton = new JButton("Save");
				saveButton.setActionCommand("OK");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
			}
			{
				exitButton = new JButton("Exit");
				exitButton.setActionCommand("Cancel");
				buttonPane.add(exitButton);
			}
		}
		
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wasAnythingChanged){
					updateValues();
					saveProject();
					saveButton.setEnabled(false);
				}
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			
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
		
		CardLayout layout = (CardLayout) cardLayoutPanel.getLayout();
		layout.show(cardLayoutPanel, "noRoomCard");
		setValues();
		saveButton.setEnabled(false);
	}
	
	protected void saveProject() {
		boolean roomInScene = chckbxRoomInScene.isSelected();
		if(roomInScene){
			int minimumPlayers = (Integer) spinnerMinimumPlayers.getValue();
			int maximumPlayers = (Integer) spinnerMaximumPlayers.getValue();
			boolean autoQueue = chckbxAutoQueue.isSelected();
			int teamSize = (Integer) spinnerTeamsInRoom.getValue();
			MISRoomSettings roomSettings = new MISRoomSettings(minimumPlayers, maximumPlayers, teamSize, autoQueue);
			scene.roomSettings = roomSettings;
		}
		if(MISProject.saveProject()){
			wasAnythingChanged = false;
			saveButton.setEnabled(false);
		}
	}

	protected void updateValues() {
		// TODO Auto-generated method stub
		
	}
	
	protected void setValues(){
		if(scene.roomSettings != null){
			chckbxRoomInScene.setSelected(true);
			spinnerMinimumPlayers.setValue(scene.roomSettings.minimumPlayers);
			spinnerMaximumPlayers.setValue(scene.roomSettings.maximumPlayers);
			spinnerTeamsInRoom.setValue(scene.roomSettings.teams);
			chckbxAutoQueue.setSelected(scene.roomSettings.autoQueue);
		} else{
			chckbxRoomInScene.setSelected(false);
		}
	}

	public void showDialog(){
		setVisible(true);
	}
}
