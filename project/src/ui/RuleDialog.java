package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import data_types.MISNode;
import enums.MISRuleType;
import scene.MISRule;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.CardLayout;

public class RuleDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTextField;
	private JComboBox<String> propertyComboBox;
	private JPanel ruleMainCardLayout;
	private JComboBox<MISNode> nodePropertyComboBox;

	/**
	 * Create the dialog.
	 */
	public RuleDialog(List<MISNode> nodeList) {
		setTitle("Add new rule");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblRuleName = new JLabel("Rule name:");
		lblRuleName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		
		JLabel lblRuleType = new JLabel("Rule type:");
		lblRuleType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		propertyComboBox = new JComboBox<String>();
		propertyComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Node Property", "Node Structure", "General", "Instantiation", "Custom"}));
		
		ruleMainCardLayout = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblRuleType, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblRuleName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(propertyComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(nameTextField, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
						.addComponent(ruleMainCardLayout, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(38, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRuleName)
						.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRuleType)
						.addComponent(propertyComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ruleMainCardLayout, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		ruleMainCardLayout.setLayout(new CardLayout(0, 0));
		
		JPanel ruleNodePropertyPanel = new JPanel();
		ruleMainCardLayout.add(ruleNodePropertyPanel, "name_37642783357743");
		
		JLabel lblNodeNameProperty = new JLabel("Node:");
		lblNodeNameProperty.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		nodePropertyComboBox = new JComboBox<MISNode>();
		
		DefaultComboBoxModel<MISNode> model = new DefaultComboBoxModel<MISNode>();
		for(int i = 0; i < nodeList.size(); i++){
			model.addElement(nodeList.get(i));
		}
		nodePropertyComboBox.setModel(model);
		
		GroupLayout gl_ruleNodePropertyPanel = new GroupLayout(ruleNodePropertyPanel);
		gl_ruleNodePropertyPanel.setHorizontalGroup(
			gl_ruleNodePropertyPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
					.addComponent(lblNodeNameProperty)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(nodePropertyComboBox, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(135, Short.MAX_VALUE))
		);
		gl_ruleNodePropertyPanel.setVerticalGroup(
			gl_ruleNodePropertyPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ruleNodePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNodeNameProperty)
						.addComponent(nodePropertyComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(130, Short.MAX_VALUE))
		);
		ruleNodePropertyPanel.setLayout(gl_ruleNodePropertyPanel);
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
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public MISRule getRuleFromDialog(){
		if(!nameTextField.getText().isEmpty()){
			MISRuleType type = MISRuleType.valueOf((String)propertyComboBox.getSelectedItem());
			MISNode node = null;
			if(type == MISRuleType.Node_Property || type == MISRuleType.Node_Structure){
				node = (MISNode)nodePropertyComboBox.getSelectedItem();
			}
			MISRule rule = new MISRule(type, nameTextField.getText(), node);
			return rule;
		}
		return null;
	}
}
