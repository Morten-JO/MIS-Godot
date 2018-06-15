package creation_ui;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.SystemColor;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;

public class ScriptGenerationUI extends JPanel {
	private JTextArea scriptTextArea;

	/**
	 * Create the panel.
	 */
	public ScriptGenerationUI(String generatedText) {
		
		JButton btnCreateScript = new JButton("Create Script");
		
		JButton btnCancel = new JButton("Cancel");
		
		JScrollPane scrollPaneScript = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPaneScript, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCreateScript)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneScript, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnCreateScript))
					.addContainerGap())
		);
		
		scriptTextArea = new JTextArea();
		scriptTextArea.setBackground(SystemColor.menu);
		scrollPaneScript.setViewportView(scriptTextArea);
		String editedText = "";
		String[] lines = generatedText.split("\n");
		for(int i = 0; i < lines.length; i++){
			editedText += (i+1)+": "+lines[i]+"\n";
		}
		scriptTextArea.setText(editedText);
		setLayout(groupLayout);

	}
}
