package creation_ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import data_types.MISRoomSettings;
import nodes.MISNode;
import project.MISProject;
import receivers.MISReceiver;
import receivers.MISReceiverAll;
import receivers.MISReceiverNotPerson;
import receivers.MISReceiverNotTeam;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;
import server.Room;
import settings.MISProjectSettings;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;
import java.awt.CardLayout;

public class SendInformationDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel cardPanelReceivers;
	private JComboBox<String> comboBoxReceiverType;
	private JComboBox<Integer> comboBoxTeam;
	private JComboBox<Integer> comboBoxPerson;
	private boolean cancel;
	private JComboBox<Integer> comboBoxNotPerson;
	private JComboBox<Integer> comboBoxNotTeam;
	
	/**
	 * Create the dialog.
	 */
	public SendInformationDialog(MISRoomSettings settings, MISNode node, boolean refresh, boolean control) {
		if(refresh){
			setTitle("Refresh information");
		} else if(control){
			setTitle("Control information");
		}
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblReceivers = new JLabel("Receivers:");
		
		comboBoxReceiverType = new JComboBox();
		comboBoxReceiverType.setModel(new DefaultComboBoxModel(new String[] {"All", "Person", "Team", "NotPerson", "NotTeam"}));
		
		comboBoxReceiverType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String receiverType = (String) comboBoxReceiverType.getSelectedItem();
				if(receiverType.equals("All")){
					CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
					layout.show(cardPanelReceivers, "receiverAll");
				} else if(receiverType.equals("Person")){
					CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
					layout.show(cardPanelReceivers, "receiverPerson");
				} else if(receiverType.equals("Team")){
					CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
					layout.show(cardPanelReceivers, "receiverTeam");
				} else if(receiverType.equals("NotPerson")){
					CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
					layout.show(cardPanelReceivers, "receiverNotPerson");
				} else if(receiverType.equals("NotTeam")){
					CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
					layout.show(cardPanelReceivers, "receiverNotTeam");
				}
			}
		});
		
		cardPanelReceivers = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(lblReceivers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxReceiverType, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(197, Short.MAX_VALUE))
				.addComponent(cardPanelReceivers, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReceivers)
						.addComponent(comboBoxReceiverType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cardPanelReceivers, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(39, Short.MAX_VALUE))
		);
		cardPanelReceivers.setLayout(new CardLayout(0, 0));
		
		JPanel cardPanelReceiversAll = new JPanel();
		cardPanelReceivers.add(cardPanelReceiversAll, "receiverAll");
		GroupLayout gl_cardPanelReceiversAll = new GroupLayout(cardPanelReceiversAll);
		gl_cardPanelReceiversAll.setHorizontalGroup(
			gl_cardPanelReceiversAll.createParallelGroup(Alignment.LEADING)
				.addGap(0, 424, Short.MAX_VALUE)
		);
		gl_cardPanelReceiversAll.setVerticalGroup(
			gl_cardPanelReceiversAll.createParallelGroup(Alignment.LEADING)
				.addGap(0, 153, Short.MAX_VALUE)
		);
		cardPanelReceiversAll.setLayout(gl_cardPanelReceiversAll);
		
		JPanel cardPanelReceiversTeam = new JPanel();
		cardPanelReceivers.add(cardPanelReceiversTeam, "receiverTeam");
		
		JLabel lblTeam = new JLabel("Team:");
		
		comboBoxTeam = new JComboBox();
		DefaultComboBoxModel<Integer> teamBoxModel = new DefaultComboBoxModel<Integer>();
		for(int i = 0; i < settings.teams; i++){
			teamBoxModel.addElement(i);
		}
		comboBoxTeam.setModel(teamBoxModel);
		GroupLayout gl_cardPanelReceiversTeam = new GroupLayout(cardPanelReceiversTeam);
		gl_cardPanelReceiversTeam.setHorizontalGroup(
			gl_cardPanelReceiversTeam.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardPanelReceiversTeam.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTeam)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxTeam, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(297, Short.MAX_VALUE))
		);
		gl_cardPanelReceiversTeam.setVerticalGroup(
			gl_cardPanelReceiversTeam.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardPanelReceiversTeam.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_cardPanelReceiversTeam.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTeam)
						.addComponent(comboBoxTeam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(128, Short.MAX_VALUE))
		);
		cardPanelReceiversTeam.setLayout(gl_cardPanelReceiversTeam);
		
		JPanel cardPanelReceiversPerson = new JPanel();
		cardPanelReceivers.add(cardPanelReceiversPerson, "receiverPerson");
		
		JLabel lblPerson = new JLabel("Person:");
		
		comboBoxPerson = new JComboBox();
		DefaultComboBoxModel<Integer> personBoxModel = new DefaultComboBoxModel<Integer>();
		for(int i = 0; i < settings.minimumPlayers; i++){
			personBoxModel.addElement(i);
		}
		comboBoxPerson.setModel(personBoxModel);
		GroupLayout gl_cardPanelReceiversPerson = new GroupLayout(cardPanelReceiversPerson);
		gl_cardPanelReceiversPerson.setHorizontalGroup(
			gl_cardPanelReceiversPerson.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardPanelReceiversPerson.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPerson)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxPerson, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(322, Short.MAX_VALUE))
		);
		gl_cardPanelReceiversPerson.setVerticalGroup(
			gl_cardPanelReceiversPerson.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cardPanelReceiversPerson.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_cardPanelReceiversPerson.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPerson)
						.addComponent(comboBoxPerson, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(128, Short.MAX_VALUE))
		);
		cardPanelReceiversPerson.setLayout(gl_cardPanelReceiversPerson);
		
		JPanel cardPanelReceiversNotPerson = new JPanel();
		cardPanelReceivers.add(cardPanelReceiversNotPerson, "receiverNotPerson");
		
		JLabel label = new JLabel("Person:");
		
		comboBoxNotPerson = new JComboBox();
		DefaultComboBoxModel<Integer> notPersonBoxModel = new DefaultComboBoxModel<Integer>();
		for(int i = 0; i < settings.minimumPlayers; i++){
			notPersonBoxModel.addElement(i);
		}
		comboBoxNotPerson.setModel(notPersonBoxModel);
		GroupLayout gl_cardPanelReceiversNotPerson = new GroupLayout(cardPanelReceiversNotPerson);
		gl_cardPanelReceiversNotPerson.setHorizontalGroup(
			gl_cardPanelReceiversNotPerson.createParallelGroup(Alignment.LEADING)
				.addGap(0, 424, Short.MAX_VALUE)
				.addGroup(gl_cardPanelReceiversNotPerson.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxNotPerson, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(322, Short.MAX_VALUE))
		);
		gl_cardPanelReceiversNotPerson.setVerticalGroup(
			gl_cardPanelReceiversNotPerson.createParallelGroup(Alignment.LEADING)
				.addGap(0, 153, Short.MAX_VALUE)
				.addGroup(gl_cardPanelReceiversNotPerson.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_cardPanelReceiversNotPerson.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(comboBoxNotPerson, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(122, Short.MAX_VALUE))
		);
		cardPanelReceiversNotPerson.setLayout(gl_cardPanelReceiversNotPerson);
		
		JPanel cardPanelReceiversNotTeam = new JPanel();
		cardPanelReceivers.add(cardPanelReceiversNotTeam, "receiverNotTeam");
		
		JLabel label_1 = new JLabel("Team:");
		
		comboBoxNotTeam = new JComboBox();
		DefaultComboBoxModel<Integer> notTeamBoxModel = new DefaultComboBoxModel<Integer>();
		for(int i = 0; i < settings.minimumPlayers; i++){
			notTeamBoxModel.addElement(i);
		}
		comboBoxNotTeam.setModel(notTeamBoxModel);
		GroupLayout gl_cardPanelReceiversNotTeam = new GroupLayout(cardPanelReceiversNotTeam);
		gl_cardPanelReceiversNotTeam.setHorizontalGroup(
			gl_cardPanelReceiversNotTeam.createParallelGroup(Alignment.LEADING)
				.addGap(0, 424, Short.MAX_VALUE)
				.addGroup(gl_cardPanelReceiversNotTeam.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxNotTeam, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(297, Short.MAX_VALUE))
		);
		gl_cardPanelReceiversNotTeam.setVerticalGroup(
			gl_cardPanelReceiversNotTeam.createParallelGroup(Alignment.LEADING)
				.addGap(0, 153, Short.MAX_VALUE)
				.addGroup(gl_cardPanelReceiversNotTeam.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_cardPanelReceiversNotTeam.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(comboBoxNotTeam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(122, Short.MAX_VALUE))
		);
		cardPanelReceiversNotTeam.setLayout(gl_cardPanelReceiversNotTeam);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
		MISReceiver receiver = null;
		if(refresh){
			receiver = node.informationReceivers;
		} else if(control){
			receiver = node.controlReceiver;
		}
		if(receiver != null){
			if(receiver instanceof MISReceiverPerson){
				CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
				layout.show(cardPanelReceivers, "receiverPerson");
				int person = ((MISReceiverPerson)receiver).person;
				if(comboBoxPerson.getItemCount() > person){
					comboBoxPerson.setSelectedIndex(person);
				}
				comboBoxReceiverType.setSelectedIndex(1);
			} else if(receiver instanceof MISReceiverAll){
				CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
				layout.show(cardPanelReceivers, "receiverAll");
				comboBoxReceiverType.setSelectedIndex(0);
			} else if(receiver instanceof MISReceiverTeam){
				CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
				layout.show(cardPanelReceivers, "receiverTeam");
				int team = ((MISReceiverTeam)receiver).team;
				if(comboBoxTeam.getItemCount() > team){
					comboBoxTeam.setSelectedIndex(team);
				}
				comboBoxReceiverType.setSelectedIndex(2);
			} else if(receiver instanceof MISReceiverNotTeam){
				CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
				layout.show(cardPanelReceivers, "receiverNotTeam");
				int team = ((MISReceiverNotTeam)receiver).team;
				if(comboBoxNotTeam.getItemCount() > team){
					comboBoxNotTeam.setSelectedIndex(team);
				}
				comboBoxReceiverType.setSelectedIndex(3);
			} else if(receiver instanceof MISReceiverNotPerson){
				CardLayout layout = (CardLayout) cardPanelReceivers.getLayout();
				layout.show(cardPanelReceivers, "receiverNotPerson");
				int person = ((MISReceiverNotPerson)receiver).person;
				if(comboBoxNotPerson.getItemCount() > person){
					comboBoxNotPerson.setSelectedIndex(person);
				}
				comboBoxReceiverType.setSelectedIndex(4);
			}
		}
		
		
		if(refresh && control){
			setVisible(false);
			dispose();
			cancel = true;
			System.out.println("Both refresh and control was checked...");
		}
		
	}

	public void showDialog(){
		if(!cancel){
			this.setVisible(true);
		}
	}
	
	public MISReceiver getReceiver(){
		if(cancel){
			return null;
		}
		MISReceiver receiver = null;
		String receiverType = (String)comboBoxReceiverType.getSelectedItem();
		if(receiverType.equals("All")){
			receiver = new MISReceiverAll();
		} else if(receiverType.equals("Team")){
			int teamChosen = (int)comboBoxTeam.getSelectedItem();
			receiver = new MISReceiverTeam(teamChosen);
		} else if(receiverType.equals("Person")){
			int personChosen = (int)comboBoxPerson.getSelectedItem();
			receiver = new MISReceiverPerson(personChosen);
		} else if(receiverType.equals("NotPerson")){
			int personChosen = (int)comboBoxNotPerson.getSelectedItem();
			receiver = new MISReceiverNotPerson(personChosen);
		} else if(receiverType.equals("NotTeam")){
			int teamChosen = (int)comboBoxNotTeam.getSelectedItem();
			receiver = new MISReceiverNotTeam(teamChosen);
		}
		return receiver;
	}
}
