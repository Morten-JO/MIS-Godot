package server_ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;

public class ServerApplicationWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerApplicationWindow frame = new ServerApplicationWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerApplicationWindow() {
		setTitle("Server UI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblPlayersConnected = new JLabel("Players Connected:");
		
		JLabel lblPlayersConnectedValue = new JLabel("0");
		
		JLabel lblServerUptime = new JLabel("Server Uptime:");
		
		JLabel lblServecrUptimeValue = new JLabel("00:00:00");
		
		JLabel lblMessagesSent = new JLabel("Messages Sent:");
		
		JLabel lblMessagesSentValue = new JLabel("0");
		
		JLabel lblMessagesReceived = new JLabel("Messages received:");
		
		JLabel lblMessagesReceivedValue = new JLabel("0");
		
		JLabel lblConnectedUsers = new JLabel("Connected Users:");
		
		JLabel lblTotalPlayers = new JLabel("Total Players joined:");
		
		JLabel lblTotalPlayersValue = new JLabel("0");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(lblPlayersConnected)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPlayersConnectedValue, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblServerUptime)
										.addComponent(lblMessagesSent))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblServecrUptimeValue, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
										.addComponent(lblMessagesSentValue, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblMessagesReceived)
										.addComponent(lblTotalPlayers))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblMessagesReceivedValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblTotalPlayersValue, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))))
						.addComponent(lblConnectedUsers, Alignment.LEADING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayersConnected)
						.addComponent(lblPlayersConnectedValue)
						.addComponent(lblServerUptime)
						.addComponent(lblServecrUptimeValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessagesSent)
						.addComponent(lblMessagesSentValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessagesReceived)
						.addComponent(lblMessagesReceivedValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalPlayers)
						.addComponent(lblTotalPlayersValue))
					.addGap(13)
					.addComponent(lblConnectedUsers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
	}
}
