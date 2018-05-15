package server_ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import server.Client;
import server.Server;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import nodes.MISNode;

import javax.swing.JScrollPane;
import javax.swing.JList;

public class ServerMessageLog extends JFrame {

	private JPanel contentPane;
	private JList<String> listSentMessages;
	private JList<String> listReceivedMessages;

	

	/**
	 * Create the frame.
	 */
	public ServerMessageLog(Server server) {
		setTitle("Server Message Log");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JComboBox<Client> comboBox = new JComboBox<Client>();
		comboBox.setRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(renderer instanceof JLabel && value instanceof Client){
					Client info = (Client)value;
					String nodeString;
					nodeString = info.getIp() +" - "+info.id;
					((JLabel)renderer).setText(nodeString);
				}
				return renderer;
				
			}
		});
		DefaultComboBoxModel<Client> model = new DefaultComboBoxModel<Client>();
		for(int i = 0; i < server.clientList.size(); i++){
			model.addElement(server.clientList.get(i));
		}
		comboBox.setModel(model);
		JLabel lblPlayer = new JLabel("Player:");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(109)
							.addComponent(lblPlayer)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPlayer))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JScrollPane scrollPaneReceivedMessages = new JScrollPane();
		tabbedPane.addTab("Received Messages", null, scrollPaneReceivedMessages, null);
		
		listReceivedMessages = new JList<String>();
		DefaultListModel<String> receivedModel = new DefaultListModel<String>();
		listReceivedMessages.setModel(receivedModel);
		scrollPaneReceivedMessages.setViewportView(listReceivedMessages);
		
		JScrollPane scrollPaneSentMessages = new JScrollPane();
		tabbedPane.addTab("Sent Messages", null, scrollPaneSentMessages, null);
		
		
		
		listSentMessages = new JList<String>();
		DefaultListModel<String> sentModel = new DefaultListModel<String>();
		listSentMessages.setModel(sentModel);
		scrollPaneSentMessages.setViewportView(listSentMessages);
		
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateElements(comboBox, receivedModel, sentModel);
			}
		});
		updateElements(comboBox, receivedModel, sentModel);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
	}
	
	private void updateElements(JComboBox<Client> comboBox, DefaultListModel<String> receivedModel, DefaultListModel<String> sentModel){
		Client propertyType = (Client) comboBox.getSelectedItem();
		if(propertyType != null){
			sentModel.removeAllElements();
			for(int i = 0; i < propertyType.sentMessagesDataStorage.size(); i++){
				sentModel.addElement(propertyType.sentMessagesDataStorage.get(i));
			}
			for(int i = 0; i < propertyType.receivedMessagesDataStorage.size(); i++){
				receivedModel.addElement(propertyType.receivedMessagesDataStorage.get(i));
			}
		}
	}
}
