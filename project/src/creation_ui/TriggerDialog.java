package creation_ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import actions.MISAction;
import actions.MISActionMessage;
import data_types.MISBounds;
import enums.MISRuleType;
import nodes.MISNode;
import project.MISProject;
import receivers.MISReceiver;
import receivers.MISReceiverAll;
import receivers.MISReceiverNotPerson;
import receivers.MISReceiverNotTeam;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;
import rules.MISRule;
import rules.MISRuleNode;
import rules.MISRuleNodePosition;
import rules.MISRuleNodeRotation;
import rules.MISRuleNodeScale;
import settings.MISProjectSettings;
import triggers.MISTrigger;
import triggers.MISTriggerValue;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JComboBox.KeySelectionManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;

import java.awt.CardLayout;
import java.awt.Component;
import triggers.MISTriggerValue.TargetType;
import triggers.MISTriggerValue.ValueComparer;

public class TriggerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> triggerTypeComboBox;
	private JPanel triggerTypeCardLayout;
	private JDialog holder;
	private JComboBox valueTargetTypeComboBox;
	private JComboBox valueComparerComboBox;
	private boolean cancel = false;
	private JTextField textFieldValueTarget;
	private JPanel actionMessageCardLayout;
	private JTextField messageTextField;
	private JPanel triggerValuePropertyPanel;
	private JPanel receiverTypeCardLayout;
	private JPanel receiverTypeValue;
	private JComboBox<Integer> receiverValueComboBox;
	private JComboBox receiverTypeComboBox;
	private JComboBox actionTypeComboBox;
	private JPanel actionTypeCardLayout;
	private JPanel receiverTypeNotValue;
	private JPanel actionNotMessageCardLayout;
	private JPanel triggerNotValuePropertyPanel;

	
	public static void main(String[] args) {
		TriggerDialog dialog = new TriggerDialog(null);
		dialog.showDialog();
	}
	
	/**
	 * Create the dialog.
	 */
	public TriggerDialog(JFrame frame) {
		super(frame);
		holder = this;
		setTitle("Add new trigger");
		setBounds(100, 100, 454, 416);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblTriggerType = new JLabel("Trigger Type:");
		lblTriggerType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		triggerTypeComboBox = new JComboBox<String>();
		triggerTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Value"}));
		triggerTypeComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String propertyType = (String)triggerTypeComboBox.getSelectedItem();
				if(propertyType.equals("Value")){
					CardLayout layout = (CardLayout) triggerTypeCardLayout.getLayout();
					layout.show(triggerTypeCardLayout, "value");
				} else {
					CardLayout layout = (CardLayout) triggerTypeCardLayout.getLayout();
					layout.show(triggerTypeCardLayout, "not_value");
				}
			}
		});
		
		
		triggerTypeCardLayout = new JPanel();
		
		JLabel lblActionType = new JLabel("Action Type:");
		lblActionType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		actionTypeComboBox = new JComboBox();
		actionTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Message"}));
		actionTypeComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String propertyType = (String)actionTypeComboBox.getSelectedItem();
				if(propertyType.equals("Message")){
					CardLayout layout = (CardLayout) actionTypeCardLayout.getLayout();
					layout.show(actionTypeCardLayout, "message");
				}  else{
					CardLayout layout = (CardLayout) actionTypeCardLayout.getLayout();
					layout.show(actionTypeCardLayout, "not_message");
				}
			}
		});
		triggerTypeCardLayout.setLayout(new CardLayout(0, 0));
		
		triggerValuePropertyPanel = new JPanel();
		triggerTypeCardLayout.add(triggerValuePropertyPanel, "value");
		
		JLabel lblValueTarget = new JLabel("Value Target:");
		lblValueTarget.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblValueComparer = new JLabel("Value Comparer:");
		lblValueComparer.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		valueComparerComboBox = new JComboBox();
		valueComparerComboBox.setModel(new DefaultComboBoxModel(ValueComparer.values()));
		
		JLabel lblValueTargetType = new JLabel("Value Target Type:");
		lblValueTargetType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		valueTargetTypeComboBox = new JComboBox();
		valueTargetTypeComboBox.setModel(new DefaultComboBoxModel(TargetType.values()));
		
		
		textFieldValueTarget = new JTextField();
		textFieldValueTarget.setColumns(10);
		textFieldValueTarget.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				fixInvalidInput();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				fixInvalidInput();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				fixInvalidInput();
			}
			
			public void fixInvalidInput(){
				try{
					double value = Double.parseDouble(textFieldValueTarget.getText());
				} catch(NumberFormatException e){
					Runnable doHighlight = new Runnable() {
				        @Override
				        public void run() {
				        	textFieldValueTarget.setText("");
				        }
				    };       
				    SwingUtilities.invokeLater(doHighlight);
					
				}
			}
		});
		
		GroupLayout gl_triggerValuePropertyPanel = new GroupLayout(triggerValuePropertyPanel);
		gl_triggerValuePropertyPanel.setHorizontalGroup(
			gl_triggerValuePropertyPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_triggerValuePropertyPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_triggerValuePropertyPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_triggerValuePropertyPanel.createSequentialGroup()
							.addComponent(lblValueTarget)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldValueTarget, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
						.addGroup(gl_triggerValuePropertyPanel.createSequentialGroup()
							.addComponent(lblValueComparer)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(valueComparerComboBox, 0, 172, Short.MAX_VALUE))
						.addGroup(gl_triggerValuePropertyPanel.createSequentialGroup()
							.addComponent(lblValueTargetType)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(valueTargetTypeComboBox, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(96, GroupLayout.PREFERRED_SIZE))
		);
		gl_triggerValuePropertyPanel.setVerticalGroup(
			gl_triggerValuePropertyPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_triggerValuePropertyPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_triggerValuePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldValueTarget, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblValueTarget))
					.addGap(18)
					.addGroup(gl_triggerValuePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(valueComparerComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblValueComparer))
					.addGap(18)
					.addGroup(gl_triggerValuePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(valueTargetTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblValueTargetType))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		triggerValuePropertyPanel.setLayout(gl_triggerValuePropertyPanel);
		
		actionTypeCardLayout = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(actionTypeCardLayout, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
								.addComponent(lblTriggerType)
								.addGap(4)
								.addComponent(triggerTypeComboBox, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
							.addComponent(triggerTypeCardLayout, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
								.addComponent(lblActionType)
								.addGap(4)
								.addComponent(actionTypeComboBox, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))))
					.addGap(42))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTriggerType)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(triggerTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(6)
					.addComponent(triggerTypeCardLayout, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblActionType)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(actionTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(actionTypeCardLayout, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		triggerNotValuePropertyPanel = new JPanel();
		triggerTypeCardLayout.add(triggerNotValuePropertyPanel, "not_value");
		actionTypeCardLayout.setLayout(new CardLayout(0, 0));
		
		actionMessageCardLayout = new JPanel();
		actionTypeCardLayout.add(actionMessageCardLayout, "message");
		
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		messageTextField = new JTextField();
		messageTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Receiver Type:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		receiverTypeComboBox = new JComboBox();
		receiverTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"All", "Person", "Team", "Not Person", "Not Team"}));
		receiverTypeComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String propertyType = (String)receiverTypeComboBox.getSelectedItem();
				if(!propertyType.equals("All")){
					updateReceiverOptions();
					CardLayout layout = (CardLayout) receiverTypeCardLayout.getLayout();
					layout.show(receiverTypeCardLayout, "value");
				} else{
					CardLayout layout = (CardLayout) receiverTypeCardLayout.getLayout();
					layout.show(receiverTypeCardLayout, "not_value");
				}
			}
		});
		
		
		receiverTypeCardLayout = new JPanel();
		GroupLayout gl_actionMessageCardLayout = new GroupLayout(actionMessageCardLayout);
		gl_actionMessageCardLayout.setHorizontalGroup(
			gl_actionMessageCardLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_actionMessageCardLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
							.addComponent(lblMessage)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(messageTextField, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(receiverTypeComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap(19, Short.MAX_VALUE))
				.addComponent(receiverTypeCardLayout, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
		);
		gl_actionMessageCardLayout.setVerticalGroup(
			gl_actionMessageCardLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_actionMessageCardLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessage)
						.addComponent(messageTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_actionMessageCardLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(receiverTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(receiverTypeCardLayout, GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE))
		);
		receiverTypeCardLayout.setLayout(new CardLayout(0, 0));
		
		receiverTypeValue = new JPanel();
		receiverTypeCardLayout.add(receiverTypeValue, "value");
		
		JLabel lblReceiverValue = new JLabel("Receiver Value:");
		lblReceiverValue.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		receiverValueComboBox = new JComboBox();
		
		
		
		GroupLayout gl_receiverTypeValue = new GroupLayout(receiverTypeValue);
		gl_receiverTypeValue.setHorizontalGroup(
			gl_receiverTypeValue.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverTypeValue.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblReceiverValue)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(receiverValueComboBox, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(122, Short.MAX_VALUE))
		);
		gl_receiverTypeValue.setVerticalGroup(
			gl_receiverTypeValue.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_receiverTypeValue.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_receiverTypeValue.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReceiverValue)
						.addComponent(receiverValueComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		receiverTypeValue.setLayout(gl_receiverTypeValue);
		
		receiverTypeNotValue = new JPanel();
		receiverTypeCardLayout.add(receiverTypeNotValue, "not_value");
		actionMessageCardLayout.setLayout(gl_actionMessageCardLayout);
		
		actionNotMessageCardLayout = new JPanel();
		actionTypeCardLayout.add(actionNotMessageCardLayout, "not_message");
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Create Trigger");
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
		startOfDialog();
	}
	
	private void startOfDialog(){
		String propertyType = (String)actionTypeComboBox.getSelectedItem();
		if(propertyType.equals("Message")){
			CardLayout layout = (CardLayout) actionTypeCardLayout.getLayout();
			layout.show(actionTypeCardLayout, "message");
		}  else{
			CardLayout layout = (CardLayout) actionTypeCardLayout.getLayout();
			layout.show(actionTypeCardLayout, "not_message");
		}
		String propertyType2 = (String)triggerTypeComboBox.getSelectedItem();
		if(propertyType2.equals("Value")){
			CardLayout layout = (CardLayout) triggerTypeCardLayout.getLayout();
			layout.show(triggerTypeCardLayout, "value");
			updateReceiverOptions();
		} else {
			CardLayout layout = (CardLayout) triggerTypeCardLayout.getLayout();
			layout.show(triggerTypeCardLayout, "not_value");
		}
		if(receiverValueComboBox.getModel().getSize() > 0){
			String propertyType3 = (String)receiverTypeComboBox.getSelectedItem();
			if(!propertyType3.equals("All")){
				CardLayout layout = (CardLayout) receiverTypeCardLayout.getLayout();
				layout.show(receiverTypeCardLayout, "value");
			} else{
				CardLayout layout = (CardLayout) receiverTypeCardLayout.getLayout();
				layout.show(receiverTypeCardLayout, "not_value");
			}
		} else{
			CardLayout layout = (CardLayout) receiverTypeCardLayout.getLayout();
			layout.show(receiverTypeCardLayout, "not_value");
		}
	}
	
	private void updateReceiverOptions(){
		String propertyType = (String)receiverTypeComboBox.getSelectedItem();
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>();
		if(propertyType.equals("Team")){
			for(int i = 0; i < MISProject.project.roomSettings.teams; i++){
				model.addElement(i);
			}
		} else if(propertyType.equals("Person")){
			for(int i = 0; i < MISProject.project.roomSettings.minimumPlayers; i++){
				model.addElement(i);
			}
		} else if(propertyType.equals("Not Person")){
			for(int i = 0; i < MISProject.project.roomSettings.minimumPlayers; i++){
				model.addElement(i);
			}
		} else if(propertyType.equals("Not Team")){
			for(int i = 0; i < MISProject.project.roomSettings.teams; i++){
				model.addElement(i);
			}
		}
		receiverValueComboBox.setModel(model);
	}
	
	public void showDialog(){
		setVisible(true);
	}
	
	
	
	public MISTrigger getTriggerFromDialog(){
		if(cancel){
			return null;
		}
		MISTrigger trigger;
		if(triggerTypeComboBox.getSelectedItem().equals("Value")){
			MISAction action = null;
			if(actionTypeComboBox.getSelectedItem().equals("Message")){
				String message = messageTextField.getText();
				MISReceiver receiver;
				String receiverString = (String) receiverTypeComboBox.getSelectedItem();
				if(receiverString.equals("All")){
					receiver = new MISReceiverAll();
				} else if(receiverString.equals("Person")){
					int value = (Integer) receiverValueComboBox.getSelectedItem();
					receiver = new MISReceiverPerson(value);
				} else if(receiverString.equals("Team")){
					int value = (Integer) receiverValueComboBox.getSelectedItem();
					receiver = new MISReceiverTeam(value);
				} else if(receiverString.equals("Not Team")){
					int value = (Integer) receiverValueComboBox.getSelectedItem();
					receiver = new MISReceiverNotTeam(value);
				} else if(receiverString.equals("Not Person")){
					int value = (Integer) receiverValueComboBox.getSelectedItem();
					receiver = new MISReceiverNotPerson(value);
				} else{
					System.out.println("Receiver Bug in TriggerDialog line 480ish");
					receiver = new MISReceiver();
				}
				action = new MISActionMessage(message, receiver);
			}
			double valueTarget = Double.parseDouble(textFieldValueTarget.getText());
			MISTriggerValue.ValueComparer comparer = (MISTriggerValue.ValueComparer)valueComparerComboBox.getSelectedItem();
			MISTriggerValue.TargetType targetType =( MISTriggerValue.TargetType)valueTargetTypeComboBox.getSelectedItem();
			trigger = new MISTriggerValue(action, valueTarget, comparer, targetType);
		} else{
			System.out.println("Trigger bug in TriggerDialog line 490ish");
			trigger = new MISTrigger(null);
		}
		return trigger;
	}
}
