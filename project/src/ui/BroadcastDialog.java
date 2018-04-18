package ui;

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
import broadcasts.MISBroastcastMessage;
import data_types.MISReceiver;
import data_types.MISReceiverAll;
import rules.MISRule;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
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

	/**
	 * Create the dialog.
	 */
	public BroadcastDialog() {
		setBounds(100, 100, 450, 300);
		setTitle("Add broadcast");
		getContentPane().setLayout(new BorderLayout());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblBroadcastName = new JLabel("Broadcast name:");
		lblBroadcastName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		textFieldBroadcastName = new JTextField();
		textFieldBroadcastName.setColumns(10);
		
		JLabel lblBroadcastType = new JLabel("Broadcast type:");
		lblBroadcastType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		comboBoxBroadcastType = new JComboBox();
		comboBoxBroadcastType.setModel(new DefaultComboBoxModel(new String[] {"Data", "Value", "Message"}));
		
		JLabel lblBroadcastReceivers = new JLabel("Broadcast receivers:");
		lblBroadcastReceivers.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		comboBoxBroadcastReceivers = new JComboBox();
		comboBoxBroadcastReceivers.setModel(new DefaultComboBoxModel(new String[] {"All", "Team", "Person"}));
		
		typeCardPanel = new JPanel();
		
		JLabel lblBroadcastFrequency = new JLabel("Broadcast Frequency:");
		lblBroadcastFrequency.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		textFieldFrequency = new JTextField();
		textFieldFrequency.setColumns(10);
		
		JLabel lblTimesPerMinute = new JLabel("Times per minute");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(lblBroadcastType, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblBroadcastName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
									.addComponent(comboBoxBroadcastType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(textFieldBroadcastName, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addComponent(lblBroadcastReceivers)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(comboBoxBroadcastReceivers, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblBroadcastFrequency)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTimesPerMinute))
						.addComponent(typeCardPanel, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastName)
						.addComponent(textFieldBroadcastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastType)
						.addComponent(comboBoxBroadcastType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastReceivers)
						.addComponent(comboBoxBroadcastReceivers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBroadcastFrequency)
						.addComponent(textFieldFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTimesPerMinute))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(typeCardPanel, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
		);
		typeCardPanel.setLayout(new CardLayout(0, 0));
		
		typeMessagePanel = new JPanel();
		typeCardPanel.add(typeMessagePanel, "name_103386201152226");
		
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
		
		JPanel typeDataPanel = new JPanel();
		typeCardPanel.add(typeDataPanel, "name_105111410123258");
		
		JPanel typeValuePanel = new JPanel();
		typeCardPanel.add(typeValuePanel, "name_105123477429562");
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
			
		} else if(receivers.equals("Person")){
			
		}
		if(type.equals("Message")){
			String message = textFieldMessagePanelMessage.getText();
			MISBroastcastMessage broadcast = new MISBroastcastMessage(broadcastName, timesPerMinute, message);
			broadcast.receiver = receiver;
			return broadcast;
		} else if(type.equals("Data")){
			
		} else if(type.equals("Value")){
			
		}
		return null;
	}
}
