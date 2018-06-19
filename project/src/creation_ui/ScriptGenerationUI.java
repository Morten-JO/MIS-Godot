package creation_ui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;

import scriptbuilder.ScriptBuilder;
import settings.MISProjectSettings;

import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class ScriptGenerationUI extends JDialog {
	private JTextArea scriptTextArea;
	private boolean generated;
	
	
	/**
	 * Create the panel.
	 */
	public ScriptGenerationUI(String generatedText, String scriptName) {
		setTitle("Preview script");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		setBounds(100, 100, 700, 800);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 442, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 292, Short.MAX_VALUE)
		);
		String editedText = "";
		String[] lines = generatedText.split("\n");
		for(int i = 0; i < lines.length; i++){
			editedText += (i+1)+": "+lines[i]+"\n";
		}
		
		JScrollPane scrollPaneScript = new JScrollPane();
		
		scriptTextArea = new JTextArea();
		scriptTextArea.setBackground(SystemColor.menu);
		scrollPaneScript.setViewportView(scriptTextArea);
		scriptTextArea.setText(editedText);
		
		JButton btnCreateScript = new JButton("Create Script");
		btnCreateScript.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean res = ScriptBuilder.buildScript(generatedText, scriptName);
				if(res){
					generated = true;
					dispose();
				} else{
					generated = false;
				}
			}
		});
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				generated = false;
				dispose();
			}
		});
		GroupLayout groupLayout_1 = new GroupLayout(getContentPane());
		groupLayout_1.setHorizontalGroup(
			groupLayout_1.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout_1.createSequentialGroup()
					.addContainerGap(242, Short.MAX_VALUE)
					.addComponent(btnCreateScript)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addComponent(scrollPaneScript, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout_1.setVerticalGroup(
			groupLayout_1.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout_1.createSequentialGroup()
					.addComponent(scrollPaneScript, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnCreateScript, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout_1);
		
	}
	
	public void showDialog(){
		setVisible(true);
	}
	
	public boolean getGenerated(){
		return generated;
	}
}
