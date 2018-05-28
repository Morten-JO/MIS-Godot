package creation_ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import data_types.MISBounds;
import enums.MISRuleType;
import nodes.MISNode;
import rules.MISRule;
import rules.MISRuleNode;
import rules.MISRuleNodePosition;
import rules.MISRuleNodeRotation;
import rules.MISRuleNodeScale;
import settings.MISProjectSettings;
import triggers.MISTrigger;

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
	private JComboBox receiverValueComboBox;
	private JComboBox receiverTypeComboBox;
	private JComboBox actionTypeComboBox;

	/**
	 * Create the dialog.
	 */
	public TriggerDialog(JFrame frame, List<MISNode> nodeList) {
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
		
		triggerTypeCardLayout = new JPanel();
		
		JLabel lblActionType = new JLabel("Action Type:");
		lblActionType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		actionTypeComboBox = new JComboBox();
		actionTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Message"}));
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
		valueTargetTypeComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String propertyType = (String)valueTargetTypeComboBox.getSelectedItem();
				if(propertyType.equals("Value")){
					CardLayout layout = (CardLayout) triggerTypeCardLayout.getLayout();
					layout.show(triggerTypeCardLayout, "value");
				} 
			}
		});
		
		textFieldValueTarget = new JTextField();
		textFieldValueTarget.setColumns(10);
		
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
		
		DefaultComboBoxModel<MISNode> model = new DefaultComboBoxModel<MISNode>();
		for(int i = 0; i < nodeList.size(); i++){
			model.addElement(nodeList.get(i));
		}
		
		JPanel actionTypeCardLayout = new JPanel();
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
		actionTypeCardLayout.setLayout(new CardLayout(0, 0));
		
		actionMessageCardLayout = new JPanel();
		actionTypeCardLayout.add(actionMessageCardLayout, "name_282500502630713");
		
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		messageTextField = new JTextField();
		messageTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Receiver Type:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		receiverTypeComboBox = new JComboBox();
		receiverTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"All", "Person", "Team", "Not Person", "Not Team"}));
		
		receiverTypeCardLayout = new JPanel();
		GroupLayout gl_actionMessageCardLayout = new GroupLayout(actionMessageCardLayout);
		gl_actionMessageCardLayout.setHorizontalGroup(
			gl_actionMessageCardLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_actionMessageCardLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(receiverTypeCardLayout, GroupLayout.PREFERRED_SIZE, 379, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_actionMessageCardLayout.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
								.addComponent(lblMessage)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(messageTextField, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_actionMessageCardLayout.createSequentialGroup()
								.addComponent(lblNewLabel)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(receiverTypeComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addContainerGap(17, Short.MAX_VALUE))
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
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(receiverTypeCardLayout, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
		);
		receiverTypeCardLayout.setLayout(new CardLayout(0, 0));
		
		receiverTypeValue = new JPanel();
		receiverTypeCardLayout.add(receiverTypeValue, "name_283820807000775");
		
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
		actionMessageCardLayout.setLayout(gl_actionMessageCardLayout);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Create Rule");
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
	
	
	
	public MISTrigger getTriggerFromDialog(){
		if(cancel){
			return null;
		}
		
		return null;
	}
}
