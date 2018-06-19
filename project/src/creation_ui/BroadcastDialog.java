package creation_ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import broadcasts.MISBroadcast;
import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastValue;
import data_types.MISRoomSettings;
import broadcasts.MISBroadcastMessage;
import receivers.MISReceiver;
import receivers.MISReceiverAll;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;
import rules.MISRule;
import settings.MISProjectSettings;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.CardLayout;
import javax.swing.JTextArea;

public class BroadcastDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldBroadcastName;
	private JComboBox comboBoxBroadcastReceivers;
	private JComboBox comboBoxBroadcastType;
	private JLabel lblBroadcastName;
	private JPanel typeCardPanel;
	private JPanel typeMessagePanel;
	private JTextField textFieldFrequency;
	private JTextField textFieldMessagePanelMessage;
	private boolean cancel = false;
	private JPanel typeDataPanel;
	private JPanel typeValuePanel;
	private JPanel receiverCardPanel;
	private JPanel receiverAllPanel;
	private JPanel receiverTeamPanel;
	private JPanel receiverPersonPanel;
	private JLabel lblTeam;
	private JComboBox<Integer> comboBoxReceiverTeam;
	private JLabel lblPerson;
	private JComboBox<Integer> comboBoxReceiverPerson;
	private JLabel lblnameOfBroadcast;
	private JLabel lbltypeOfBroadcastvalue;
	private JLabel lblwhoReceivesBroadcast;
	private JLabel lbltheTeamThat;
	private JLabel lblthePersonThat;
	private JLabel lblallPlayersAre;

	/**
	 * Create the dialog.
	 */
	public BroadcastDialog(MISRoomSettings roomSettings) {
		setBounds(100, 100, 450, 300);
		setTitle("Add broadcast");
		getContentPane().setLayout(new BorderLayout());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblBroadcastName = new JLabel("Broadcast name:");
		lblBroadcastName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		textFieldBroadcastName = new JTextField();
		textFieldBroadcastName.setColumns(10);
		
		JLabel lblBroadcastType = new JLabel("Broadcast type:");
		lblBroadcastType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		comboBoxBroadcastType = new JComboBox();
		comboBoxBroadcastType.setModel(new DefaultComboBoxModel(new String[] {"Message", "Data", "Value"}));
		
		comboBoxBroadcastType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String propertyType = (String)comboBoxBroadcastType.getSelectedItem();
				if(propertyType.equals("Message")){
					CardLayout layout = (CardLayout) typeCardPanel.getLayout();
					layout.show(typeCardPanel, "messagePanel");
				} else if(propertyType.equals("Data")){
					CardLayout layout = (CardLayout) typeCardPanel.getLayout();
					layout.show(typeCardPanel, "dataPanel");
				} else if(propertyType.equals("Value")){
					CardLayout layout = (CardLayout) typeCardPanel.getLayout();
					layout.show(typeCardPanel, "valuePanel");
				}
			}
		});
		
		JLabel lblBroadcastReceivers = new JLabel("Broadcast receivers:");
		lblBroadcastReceivers.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		comboBoxBroadcastReceivers = new JComboBox();
		if(roomSettings != null){
			comboBoxBroadcastReceivers.setModel(new DefaultComboBoxModel(new String[] {"All", "Team", "Person"}));
		} else{
			comboBoxBroadcastReceivers.setModel(new DefaultComboBoxModel(new String[] {"All"}));
		}
		
		comboBoxBroadcastReceivers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String receiverType = (String)comboBoxBroadcastReceivers.getSelectedItem();
				if(receiverType.equals("All")){
					CardLayout layout = (CardLayout) receiverCardPanel.getLayout();
					layout.show(receiverCardPanel, "receiverAll");
				} else if(receiverType.equals("Team")){
					CardLayout layout = (CardLayout) receiverCardPanel.getLayout();
					layout.show(receiverCardPanel, "receiverTeam");
				} else if(receiverType.equals("Person")){
					CardLayout layout = (CardLayout) receiverCardPanel.getLayout();
					layout.show(receiverCardPanel, "receiverPerson");
				}
			}
		});
		
		typeCardPanel = new JPanel();
		
		JLabel lblBroadcastFrequency = new JLabel("Broadcast Frequency:");
		lblBroadcastFrequency.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		textFieldFrequency = new JTextField();
		textFieldFrequency.setColumns(10);
		
		JLabel lblTimesPerMinute = new JLabel("[Send times per minute]");
		
		receiverCardPanel = new JPanel();
		
		lblnameOfBroadcast = new JLabel("[Name of broadcast]");
		
		lbltypeOfBroadcastvalue = new JLabel("[Type of broadcast(value)]");
		
		lblwhoReceivesBroadcast = new JLabel("[Who receives broadcast]");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblBroadcastType, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblBroadcastName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(textFieldBroadcastName, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
										.addComponent(comboBoxBroadcastType, 0, 142, Short.MAX_VALUE)))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblBroadcastReceivers)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBoxBroadcastReceivers, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblnameOfBroadcast)
								.addComponent(lbltypeOfBroadcastvalue)
								.addComponent(lblwhoReceivesBroadcast))
							.addGap(103))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblBroadcastFrequency)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTimesPerMinute))
						.addComponent(typeCardPanel, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
						.addComponent(receiverCardPanel, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastName)
						.addComponent(textFieldBroadcastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblnameOfBroadcast))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastType)
						.addComponent(comboBoxBroadcastType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbltypeOfBroadcastvalue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastReceivers)
						.addComponent(comboBoxBroadcastReceivers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblwhoReceivesBroadcast))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastFrequency)
						.addComponent(textFieldFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTimesPerMinute))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(typeCardPanel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(receiverCardPanel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
		);
		receiverCardPanel.setLayout(new CardLayout(0, 0));
		
		receiverAllPanel = new JPanel();
		receiverCardPanel.add(receiverAllPanel, "receiverAll");
		
		lblallPlayersAre = new JLabel("[All players are receiving this broadcast]");
		GroupLayout gl_receiverAllPanel = new GroupLayout(receiverAllPanel);
		gl_receiverAllPanel.setHorizontalGroup(
			gl_receiverAllPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverAllPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblallPlayersAre)
					.addContainerGap(367, Short.MAX_VALUE))
		);
		gl_receiverAllPanel.setVerticalGroup(
			gl_receiverAllPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverAllPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblallPlayersAre)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		receiverAllPanel.setLayout(gl_receiverAllPanel);
		
		receiverTeamPanel = new JPanel();
		receiverCardPanel.add(receiverTeamPanel, "receiverTeam");
		
		lblTeam = new JLabel("Team:");
		
		comboBoxReceiverTeam = new JComboBox<Integer>();
		if(roomSettings != null){
			DefaultComboBoxModel<Integer> boxModel = new DefaultComboBoxModel<Integer>();
			for(int i = 0; i < roomSettings.teams; i++){
				boxModel.addElement(i);
			}
			comboBoxReceiverTeam.setModel(boxModel);
		}
		
		lbltheTeamThat = new JLabel("[The team that receives the broadcast]");
		GroupLayout gl_receiverTeamPanel = new GroupLayout(receiverTeamPanel);
		gl_receiverTeamPanel.setHorizontalGroup(
			gl_receiverTeamPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverTeamPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTeam)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxReceiverTeam, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lbltheTeamThat)
					.addContainerGap(222, Short.MAX_VALUE))
		);
		gl_receiverTeamPanel.setVerticalGroup(
			gl_receiverTeamPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverTeamPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_receiverTeamPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTeam)
						.addComponent(comboBoxReceiverTeam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbltheTeamThat))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		receiverTeamPanel.setLayout(gl_receiverTeamPanel);
		
		receiverPersonPanel = new JPanel();
		receiverCardPanel.add(receiverPersonPanel, "receiverPerson");
		
		CardLayout layout = (CardLayout) receiverCardPanel.getLayout();
		layout.show(receiverCardPanel, "receiverAll");
		
		lblPerson = new JLabel("Person:");
		
		comboBoxReceiverPerson = new JComboBox<Integer>();
		if(roomSettings != null){
			DefaultComboBoxModel<Integer> boxModel = new DefaultComboBoxModel<Integer>();
			for(int i = 0; i < roomSettings.minimumPlayers; i++){
				boxModel.addElement(i);
			}
			comboBoxReceiverPerson.setModel(boxModel);
		}
		
		lblthePersonThat = new JLabel("[The person that receives this broadcast]");
		GroupLayout gl_receiverPersonPanel = new GroupLayout(receiverPersonPanel);
		gl_receiverPersonPanel.setHorizontalGroup(
			gl_receiverPersonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverPersonPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPerson)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxReceiverPerson, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblthePersonThat)
					.addContainerGap(212, Short.MAX_VALUE))
		);
		gl_receiverPersonPanel.setVerticalGroup(
			gl_receiverPersonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverPersonPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_receiverPersonPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPerson)
						.addComponent(comboBoxReceiverPerson, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblthePersonThat))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		receiverPersonPanel.setLayout(gl_receiverPersonPanel);
		typeCardPanel.setLayout(new CardLayout(0, 0));
		
		typeMessagePanel = new JPanel();
		typeCardPanel.add(typeMessagePanel, "messagePanel");
		
		JLabel lblMessagePanelMessage = new JLabel("Message:");
		lblMessagePanelMessage.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		textFieldMessagePanelMessage = new JTextField();
		textFieldMessagePanelMessage.setColumns(10);
		GroupLayout gl_typeMessagePanel = new GroupLayout(typeMessagePanel);
		gl_typeMessagePanel.setHorizontalGroup(
			gl_typeMessagePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_typeMessagePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMessagePanelMessage)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldMessagePanelMessage, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_typeMessagePanel.setVerticalGroup(
			gl_typeMessagePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_typeMessagePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_typeMessagePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessagePanelMessage)
						.addComponent(textFieldMessagePanelMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(68, Short.MAX_VALUE))
		);
		typeMessagePanel.setLayout(gl_typeMessagePanel);
		
		typeDataPanel = new JPanel();
		typeCardPanel.add(typeDataPanel, "dataPanel");
		
		typeValuePanel = new JPanel();
		typeCardPanel.add(typeValuePanel, "valuePanel");
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Create Broadcast");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
						cancel = true;
					}
				});
			}
		}
	}
	
	public void showDialog(){
		setVisible(true);
	}
	
	public MISBroadcast getRuleFromDialog(){
		if(cancel) return null;
		String broadcastName = textFieldBroadcastName.getText();
		float timesPerMinute = Float.valueOf(textFieldFrequency.getText());
		String type = (String)comboBoxBroadcastType.getSelectedItem();
		String receivers = (String)comboBoxBroadcastReceivers.getSelectedItem();
		MISReceiver receiver = null;
		if(receivers.equals("All")){
			receiver = new MISReceiverAll();
		} else if(receivers.equals("Team")){
			receiver = new MISReceiverTeam((int)comboBoxReceiverTeam.getSelectedItem());
		} else if(receivers.equals("Person")){
			receiver = new MISReceiverPerson((int)comboBoxReceiverPerson.getSelectedItem());
		}
		if(type.equals("Message")){
			String message = textFieldMessagePanelMessage.getText();
			MISBroadcastMessage broadcast = new MISBroadcastMessage(broadcastName, timesPerMinute, message);
			broadcast.receiver = receiver;
			return broadcast;
		} else if(type.equals("Data")){
			System.out.println("Error, Data broadcast not implemented yet, BroadcastDialog, adding temp broadcast");
			MISBroadcastData broadcast = new MISBroadcastData(broadcastName, timesPerMinute);
			broadcast.receiver = receiver;
			return broadcast;
		} else if(type.equals("Value")){
			System.out.println("Error, Value broadcast not implemented yet, BroadcastDialog, adding temp broadcast");
			MISBroadcastValue broadcast = new MISBroadcastValue(broadcastName, timesPerMinute);
			broadcast.receiver = receiver;
			return broadcast;
		}
		return null;
	}
}
