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

import rules.MISRule;
import settings.MISProjectSettings;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
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
	public ProjectSettingsFrame(JFrame owner) {
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblProjectName = new JLabel("Project name:");
		
		textFieldProjectName = new JTextField();
		textFieldProjectName.setColumns(10);
		
		JLabel lblProjectMinimumVersion = new JLabel("Project minimum version:");
		
		textFieldProjectMinimumVersion = new JTextField();
		textFieldProjectMinimumVersion.setColumns(10);
		
		JLabel lblListType = new JLabel("List type:");
		
		comboBoxListType = new JComboBox();
		comboBoxListType.setModel(new DefaultComboBoxModel(new String[] {"ARRAY", "LINKED"}));
		
		JLabel lblRefreshRate = new JLabel("Refresh rate:");
		
		spinnerRefreshRate = new JSpinner();
		
		JLabel lblMm = new JLabel("Mmpcps:");
		lblMm.setToolTipText("Max messages per client per second");
		
		spinnerMMPCPS = new JSpinner();
		
		JLabel lblBasePort = new JLabel("Base port:");
		
		textFieldBasePort = new JTextField();
		textFieldBasePort.setColumns(10);
		
		JLabel lblSessionTimeout = new JLabel("Session timeout:");
		
		spinnerSessionTimeout = new JSpinner();
		spinnerSessionTimeout.setToolTipText("in seconds");
		
		btnCancel = new JButton("Cancel");
		
		btnSave = new JButton("Save");
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wasAnythingChanged){
					
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
	}
	
	public void showDialog(){
		setVisible(true);
	}
}
