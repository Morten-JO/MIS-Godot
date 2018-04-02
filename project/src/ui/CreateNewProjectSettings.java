package ui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
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

public class CreateNewProjectSettings extends JPanel {
	private final ButtonGroup networkSide = new ButtonGroup();
	private JTextField textFieldClientBuild;

	/**
	 * Create the panel.
	 */
	public CreateNewProjectSettings() {
		this.setSize(700, 700);
		
		JLabel lblTitle = new JLabel("");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setEnabled(false);
		lblTitle.setIcon(new ImageIcon(CreateNewProjectSettings.class.getResource("/resources/MIS_LOGO.png")));
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblProjectName = new JLabel("Project Name:");
		lblProjectName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblProjectNameField = new JLabel("");
		lblProjectNameField.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JRadioButton rdbtnClientSide = new JRadioButton("Clientside");
		networkSide.add(rdbtnClientSide);
		
		JRadioButton rdbtnServerside = new JRadioButton("Serverside");
		networkSide.add(rdbtnServerside);
		
		JLabel lblRefreshRate = new JLabel("Refresh rate:");
		
		JSpinner spinnerRefreshRate = new JSpinner();
		
		JLabel lblMmpspc = new JLabel("MMPSPC:");
		lblMmpspc.setToolTipText("Max messages per client per second");
		
		JSpinner spinnerMMPSPC = new JSpinner();
		
		JLabel lblSavingType = new JLabel("Saving type:");
		
		JComboBox comboBoxSavingType = new JComboBox();
		comboBoxSavingType.setModel(new DefaultComboBoxModel(new String[] {"Text", "Database"}));
		
		JCheckBox chckbxEncryption = new JCheckBox("Encryption");
		
		JLabel lblSessionTimeout = new JLabel("Session Timeout:");
		
		JSpinner spinnerSessionTimeout = new JSpinner();
		
		JCheckBox chckbxUi = new JCheckBox("UI on run");
		
		JLabel lblNewLabel = new JLabel("Required clientbuild:");
		
		textFieldClientBuild = new JTextField();
		textFieldClientBuild.setColumns(10);
		
		JList listProtocols = new JList();
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
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JButton btnCreate = new JButton("Create");
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
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblSessionTimeout)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(spinnerSessionTimeout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblRefreshRate)
										.addComponent(lblMmpspc)
										.addComponent(lblSavingType))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(comboBoxSavingType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(spinnerRefreshRate, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(spinnerMMPSPC, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))))
								.addComponent(chckbxUi)
								.addComponent(lblProtocols)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(listProtocols, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
										.addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textFieldClientBuild, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(ComponentPlacement.RELATED, 307, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnBack)
							.addGap(530)
							.addComponent(btnCreate)
							.addGap(0, 0, Short.MAX_VALUE)))
					.addContainerGap())
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
		
		initializeComponents();
		handleEvents();
		
		
	}

	private void initializeComponents() {
		// TODO Auto-generated method stub
		
	}

	private void handleEvents() {
		// TODO Auto-generated method stub
		
	}
}
