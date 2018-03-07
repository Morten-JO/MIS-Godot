package ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;

public class CreateNewProject extends JPanel {
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public CreateNewProject() {
		setLayout(null);
		this.setSize(700, 700);
		
		JLabel lblCreateNewProject = new JLabel("Create new project");
		lblCreateNewProject.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateNewProject.setFont(new Font("Tw Cen MT", Font.PLAIN, 17));
		lblCreateNewProject.setBounds(253, 29, 165, 34);
		add(lblCreateNewProject);
		
		textField = new JTextField();
		textField.setBounds(134, 97, 165, 34);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(78, 107, 46, 14);
		add(lblName);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(134, 163, 165, 34);
		add(textField_1);
		
		JLabel lblNewLabel = new JLabel("Location:");
		lblNewLabel.setBounds(78, 173, 46, 14);
		add(lblNewLabel);
		
		JLabel lblDefaultLocation = new JLabel("Default location:");
		lblDefaultLocation.setHorizontalAlignment(SwingConstants.LEFT);
		lblDefaultLocation.setBounds(88, 206, 211, 14);
		add(lblDefaultLocation);
		
		JLabel lblTargetEngine = new JLabel("Target engine:");
		lblTargetEngine.setHorizontalAlignment(SwingConstants.LEFT);
		lblTargetEngine.setBounds(78, 253, 81, 14);
		add(lblTargetEngine);
		
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(1);
		list.setBackground(Color.LIGHT_GRAY);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Godot", "Unity"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(169, 245, 130, 34);
		add(list);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setHorizontalAlignment(SwingConstants.LEFT);
		btnBrowse.setBounds(309, 169, 67, 23);
		add(btnBrowse);
	}
}
