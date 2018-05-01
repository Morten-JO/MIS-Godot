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

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
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

public class RuleDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTextField;
	private JComboBox<String> propertyComboBox;
	private JPanel ruleMainCardLayout;
	private JComboBox<MISNode> nodeComboBox;
	private JDialog holder;
	private JComboBox nodePropertyComboBox;
	private JComboBox nodeRuleTypeComboBox;
	private JTextField userBoundsXMin;
	private JTextField userBoundsXMax;
	private JTextField userBoundsYMin;
	private JTextField userBoundsYMax;
	private JTextField userBoundsZMin;
	private JTextField userBoundsZMax;
	private JTextField userBoundsRotationMin;
	private JTextField userBoundsRotationMax;
	private JTextField userBoundsScaleXMin;
	private JTextField userBoundsScaleXMax;
	private JTextField userBoundsScaleYMin;
	private JTextField userBoundsScaleYMax;
	private JPanel cardLayoutUserBounds;
	private JPanel clUserBoundsPosition;
	private JPanel clUserBoundsRotation;
	private JPanel clUserBoundsScale;
	private boolean cancel = false;

	/**
	 * Create the dialog.
	 */
	public RuleDialog(JFrame frame, List<MISNode> nodeList) {
		super(frame);
		holder = this;
		setTitle("Add new rule");
		setBounds(100, 100, 450, 300);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
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
		propertyComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"NodeProperty", "NodeStructure", "General", "Instantiation", "Custom"}));
		
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
		
		nodeComboBox = new JComboBox<MISNode>();
		
		DefaultComboBoxModel<MISNode> model = new DefaultComboBoxModel<MISNode>();
		for(int i = 0; i < nodeList.size(); i++){
			model.addElement(nodeList.get(i));
		}
		nodeComboBox.setModel(model);
		nodeComboBox.setRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(renderer instanceof JLabel && value instanceof MISNode){
					MISNode info = (MISNode)value;
					String nodeString;
					nodeString = info.index +" - "+info.name;
					((JLabel)renderer).setText(nodeString);
				}
				return renderer;
				
			}
		});
		
		JLabel lblNodeRuleType = new JLabel("Node rule type:");
		lblNodeRuleType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		nodeRuleTypeComboBox = new JComboBox();
		nodeRuleTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Bounds", "Control"}));
		
		JLabel lblNodeProperty = new JLabel("Node Property:");
		lblNodeProperty.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		nodePropertyComboBox = new JComboBox();
		nodePropertyComboBox.setModel(new DefaultComboBoxModel(new String[] {"Position", "Rotation", "Scale"}));
		nodePropertyComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String propertyType = (String)nodePropertyComboBox.getSelectedItem();
				if(propertyType.equals("Position")){
					CardLayout layout = (CardLayout) cardLayoutUserBounds.getLayout();
					layout.show(cardLayoutUserBounds, "position");
				} else if(propertyType.equals("Rotation")){
					CardLayout layout = (CardLayout) cardLayoutUserBounds.getLayout();
					layout.show(cardLayoutUserBounds, "rotation");
				} else if(propertyType.equals("Scale")){
					CardLayout layout = (CardLayout) cardLayoutUserBounds.getLayout();
					layout.show(cardLayoutUserBounds, "scale");
				}
			}
		});
		
		cardLayoutUserBounds = new JPanel();
		
		GroupLayout gl_ruleNodePropertyPanel = new GroupLayout(ruleNodePropertyPanel);
		gl_ruleNodePropertyPanel.setHorizontalGroup(
			gl_ruleNodePropertyPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
					.addGroup(gl_ruleNodePropertyPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ruleNodePropertyPanel.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
								.addComponent(lblNodeNameProperty)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(nodeComboBox, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
								.addComponent(lblNodeRuleType)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(nodeRuleTypeComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
								.addComponent(lblNodeProperty)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(nodePropertyComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(cardLayoutUserBounds, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_ruleNodePropertyPanel.setVerticalGroup(
			gl_ruleNodePropertyPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleNodePropertyPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ruleNodePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNodeNameProperty)
						.addComponent(nodeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_ruleNodePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNodeRuleType)
						.addComponent(nodeRuleTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_ruleNodePropertyPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNodeProperty)
						.addComponent(nodePropertyComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(cardLayoutUserBounds, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
		);
		cardLayoutUserBounds.setLayout(new CardLayout(0, 0));
		
		clUserBoundsPosition = new JPanel();
		cardLayoutUserBounds.add(clUserBoundsPosition, "position");
		
		JLabel lblNewLabel = new JLabel("x");
		
		JLabel lblNewLabel_1 = new JLabel("y");
		
		userBoundsXMin = new JTextField();
		userBoundsXMin.setColumns(10);
		
		userBoundsXMax = new JTextField();
		userBoundsXMax.setColumns(10);
		
		userBoundsYMin = new JTextField();
		userBoundsYMin.setColumns(10);
		
		userBoundsYMax = new JTextField();
		userBoundsYMax.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("z");
		
		userBoundsZMin = new JTextField();
		userBoundsZMin.setColumns(10);
		
		userBoundsZMax = new JTextField();
		userBoundsZMax.setColumns(10);
		GroupLayout gl_clUserBoundsPosition = new GroupLayout(clUserBoundsPosition);
		gl_clUserBoundsPosition.setHorizontalGroup(
			gl_clUserBoundsPosition.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clUserBoundsPosition.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsXMin, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsXMax, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsYMin, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsYMax, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsZMin, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsZMax, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(127, Short.MAX_VALUE))
		);
		gl_clUserBoundsPosition.setVerticalGroup(
			gl_clUserBoundsPosition.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clUserBoundsPosition.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_clUserBoundsPosition.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(userBoundsXMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(userBoundsXMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(userBoundsYMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(userBoundsYMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2)
						.addComponent(userBoundsZMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(userBoundsZMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		clUserBoundsPosition.setLayout(gl_clUserBoundsPosition);
		
		clUserBoundsRotation = new JPanel();
		cardLayoutUserBounds.add(clUserBoundsRotation, "rotation");
		
		JLabel lblRotation = new JLabel("Rotation:");
		
		userBoundsRotationMin = new JTextField();
		userBoundsRotationMin.setColumns(10);
		
		userBoundsRotationMax = new JTextField();
		userBoundsRotationMax.setColumns(10);
		GroupLayout gl_clUserBoundsRotation = new GroupLayout(clUserBoundsRotation);
		gl_clUserBoundsRotation.setHorizontalGroup(
			gl_clUserBoundsRotation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clUserBoundsRotation.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblRotation)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsRotationMin, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsRotationMax, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(235, Short.MAX_VALUE))
		);
		gl_clUserBoundsRotation.setVerticalGroup(
			gl_clUserBoundsRotation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clUserBoundsRotation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_clUserBoundsRotation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRotation)
						.addComponent(userBoundsRotationMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(userBoundsRotationMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		clUserBoundsRotation.setLayout(gl_clUserBoundsRotation);
		
		clUserBoundsScale = new JPanel();
		cardLayoutUserBounds.add(clUserBoundsScale, "scale");
		
		JLabel lblX = new JLabel("x");
		
		userBoundsScaleXMin = new JTextField();
		userBoundsScaleXMin.setColumns(10);
		
		userBoundsScaleXMax = new JTextField();
		userBoundsScaleXMax.setColumns(10);
		
		JLabel lblY = new JLabel("y");
		
		userBoundsScaleYMin = new JTextField();
		userBoundsScaleYMin.setColumns(10);
		
		userBoundsScaleYMax = new JTextField();
		userBoundsScaleYMax.setColumns(10);
		GroupLayout gl_clUserBoundsScale = new GroupLayout(clUserBoundsScale);
		gl_clUserBoundsScale.setHorizontalGroup(
			gl_clUserBoundsScale.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clUserBoundsScale.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblX)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsScaleXMin, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsScaleXMax, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblY)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsScaleYMin, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userBoundsScaleYMax, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(189, Short.MAX_VALUE))
		);
		gl_clUserBoundsScale.setVerticalGroup(
			gl_clUserBoundsScale.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clUserBoundsScale.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_clUserBoundsScale.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblX)
						.addComponent(userBoundsScaleXMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(userBoundsScaleXMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblY)
						.addComponent(userBoundsScaleYMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(userBoundsScaleYMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		clUserBoundsScale.setLayout(gl_clUserBoundsScale);
		
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
	
	public MISRule showDialog(){
		setVisible(true);
		return null;
	}
	
	
	
	public MISRule getRuleFromDialog(){
		if(cancel){
			return null;
		}
		if(!nameTextField.getText().isEmpty()){
			MISRuleType type = MISRuleType.valueOf((String)propertyComboBox.getSelectedItem());
			MISNode node = null;
			MISRule rule = null;
			if(type == MISRuleType.NodeProperty || type == MISRuleType.NodeStructure){
				node = (MISNode)nodeComboBox.getSelectedItem();
				String ruleType = (String)nodeRuleTypeComboBox.getSelectedItem();
				if(ruleType.equals("Bounds")){
					String propertyType = (String)nodePropertyComboBox.getSelectedItem();
					if(propertyType.equals("Position")){
						MISRuleNodePosition tempRule = new MISRuleNodePosition(nameTextField.getText(), node, MISRuleNode.options.valueOf((String)nodeRuleTypeComboBox.getSelectedItem()));
						tempRule.xBounds = new MISBounds(Float.parseFloat(userBoundsXMin.getText()), Float.parseFloat(userBoundsXMax.getText()));
						tempRule.yBounds = new MISBounds(Float.parseFloat(userBoundsYMin.getText()), Float.parseFloat(userBoundsYMax.getText()));
						tempRule.zBounds = new MISBounds(Float.parseFloat(userBoundsZMin.getText()), Float.parseFloat(userBoundsZMax.getText()));
						rule = tempRule;
					} else if(propertyType.equals("Rotation")){
						MISRuleNodeRotation tempRule = new MISRuleNodeRotation(nameTextField.getText(), node, MISRuleNode.options.valueOf((String)nodeRuleTypeComboBox.getSelectedItem()));
						tempRule.rotationBounds = new MISBounds(Float.parseFloat(userBoundsRotationMin.getText()), Float.parseFloat(userBoundsRotationMax.getText()));
						rule = tempRule;
					} else if(propertyType.equals("Scale")){
						MISRuleNodeScale tempRule = new MISRuleNodeScale(nameTextField.getText(), node, MISRuleNode.options.valueOf((String)nodeRuleTypeComboBox.getSelectedItem()));
						tempRule.xBounds = new MISBounds(Float.parseFloat(userBoundsScaleXMin.getText()), Float.parseFloat(userBoundsScaleXMax.getText()));
						tempRule.yBounds = new MISBounds(Float.parseFloat(userBoundsScaleYMin.getText()), Float.parseFloat(userBoundsScaleYMax.getText()));
						rule = tempRule;
					}
					if(rule != null){
						return rule;
					}
				} else{
					System.out.println("Control not implemented yet.");
				}
				rule = new MISRuleNode(nameTextField.getText(), node, MISRuleNode.options.Bounds);
			}else{
				rule = new MISRule(nameTextField.getText());
			}
			return rule;
		}
		return null;
	}
}
