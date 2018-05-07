package server_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import creation_ui.ApplicationWindow;
import server.Server;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

import static java.lang.Math.toIntExact;
import javax.swing.JButton;

public class ServerApplicationWindow extends JFrame {

	private JPanel contentPane;
	private JLabel lblPlayersConnectedValue;
	private JLabel lblServerUptimeValue;
	private JLabel lblMessagesSentValue;
	private JLabel lblMessagesReceivedValue;
	private JLabel lblTotalPlayersValue;
	private JList<String> connectedUsersList;
	private DefaultListModel<String> model;
	private Server server;
	private JLabel lblStatusValue;
	private JButton btnStartServer;
	private boolean isStarted = false;
	
	public int receivedMessagesFromLeft = 0;
	public int sentMessagesToLeft = 0;
	
	private Timer standardUpdateTimer;
	private Timer clientUpdateTimer;
	
	/**
	 * Create the frame.
	 */
	public ServerApplicationWindow(Server server, boolean autoStart) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		setTitle("Server UI");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			 @Override
	         public void windowClosing(WindowEvent e) {
				 if(isStarted){
					 server.stopServer();
				 } 
				 setVisible(false);
				 dispose();
			 }
		});
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.server = server;
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblPlayersConnected = new JLabel("Players Connected:");
		
		lblPlayersConnectedValue = new JLabel("0");
		
		JLabel lblServerUptime = new JLabel("Server Uptime:");
		
		lblServerUptimeValue = new JLabel("00:00:00");
		
		JLabel lblMessagesSent = new JLabel("Messages Sent:");
		
		lblMessagesSentValue = new JLabel("0");
		
		JLabel lblMessagesReceived = new JLabel("Messages received:");
		
		lblMessagesReceivedValue = new JLabel("0");
		
		JLabel lblConnectedUsers = new JLabel("Connected Users:");
		
		JLabel lblTotalPlayers = new JLabel("Total Players joined:");
		
		lblTotalPlayersValue = new JLabel("0");
		
		JLabel lblStatus = new JLabel("Status:");
		
		lblStatusValue = new JLabel(" ");
		
		btnStartServer = new JButton("Start Server");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblPlayersConnected)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblPlayersConnectedValue, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblStatus)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblStatusValue, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnStartServer))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblServerUptime)
										.addComponent(lblMessagesSent))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblServerUptimeValue, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
										.addComponent(lblMessagesSentValue, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblMessagesReceived)
										.addComponent(lblTotalPlayers))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblMessagesReceivedValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblTotalPlayersValue, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))))
						.addComponent(lblConnectedUsers)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayersConnected)
						.addComponent(lblPlayersConnectedValue)
						.addComponent(lblServerUptime)
						.addComponent(lblServerUptimeValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMessagesSent)
						.addComponent(lblMessagesSentValue)
						.addComponent(lblStatus)
						.addComponent(lblStatusValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMessagesReceived)
								.addComponent(lblMessagesReceivedValue))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTotalPlayers)
								.addComponent(lblTotalPlayersValue))
							.addGap(13))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnStartServer)
							.addGap(18)))
					.addComponent(lblConnectedUsers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
		);
		
		connectedUsersList = new JList<String>();
		model = new DefaultListModel<String>();

		connectedUsersList.setModel(model);
		scrollPane.setViewportView(connectedUsersList);
		contentPane.setLayout(gl_contentPane);
		setStatusNotRunning();
		if(autoStart){
			isStarted = true;
			btnStartServer.setText("Stop server");
		}
		btnStartServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(isStarted){
					if(server.stopServer()){
						setRunningStatus("Stopped server");
						btnStartServer.setText("Start Server");
						isStarted = false;
					} else{
						setNotRunningStatus("Failed to stop server");
					}
					
				} else{
					if(server.startServer()){
						setStatusRunning();
						isStarted = true;
						btnStartServer.setText("Stop Server");
					} else{
						setNotRunningStatus("Failed to start server");
					}
					
				}
			}
		});
		
	}
	
	public void stopServerUI(){
		setStatusNotRunning();
		standardUpdateTimer.cancel();
		clientUpdateTimer.cancel();
		server.timeStartedInMillis = System.currentTimeMillis();
		updateTimeElapsed();
	}
	
	public void updateServerInfo(){
		lblPlayersConnectedValue.setText(""+server.clientList.size());
		lblTotalPlayersValue.setText(""+server.totalPlayers);
		updateTimeElapsed();
		updateReceivedSent();
	}
	
	public void startServerUI(){
		setStatusRunning();
		standardUpdateTimer = new Timer();
		standardUpdateTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				updateServerInfo();
			}
		}, 50, 1000);
		
		
		clientUpdateTimer = new Timer();
		clientUpdateTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				updateServer();
			}
		}, 50, 10000);
	}

	private void updateTimeElapsed(){
		long elapsedInMillis = System.currentTimeMillis() - server.timeStartedInMillis;
		int seconds = (int) (elapsedInMillis / 1000) % 60 ;
		int minutes = (int) ((elapsedInMillis / (1000*60)) % 60);
		int hours   = (int) ((elapsedInMillis / (1000*60*60)) % 24);
		String secondsString = "";
		if(seconds < 10){
			secondsString = "0"+seconds;
		} else{
			secondsString = ""+seconds;
		}
		String minutesString = "";
		if(minutes < 10){
			minutesString = "0"+minutes;
		} else {
			minutesString = ""+minutes;
		}
		String hoursString = "";
		if(hours < 10){
			hoursString = "0"+hours;
		} else{
			hoursString = ""+hours;
		}
		lblServerUptimeValue.setText(hoursString+":"+minutesString+":"+secondsString);
	}
	
	private void updateReceivedSent(){
		int receivedTotal = receivedMessagesFromLeft;
		int sentTotal = sentMessagesToLeft;
		for(int i = 0; i < server.clientList.size(); i++){
			receivedTotal += server.clientList.get(i).getMessagesReceived();
			sentTotal += server.clientList.get(i).getMessagesSent();
		}
		lblMessagesReceivedValue.setText(""+receivedTotal);
		lblMessagesSentValue.setText(""+sentTotal);
	}


	public void updateServer(){
		model.removeAllElements();
		for(int i = 0; i < server.clientList.size(); i++){
			int lastResponseTime = (int)(System.currentTimeMillis() - server.clientList.get(i).getLastResponse()) / 1000;
			String showString = "#"+(i+1)+" : "+server.clientList.get(i).getSocket().getInetAddress().getHostAddress()+" Last response: "+lastResponseTime+" secs";
			if(server.clientList.get(i).getRoom() != null){
				showString += " | In room: "+server.clientList.get(i).getRoom().getRoomID();
			} else{
				boolean isInQueue = false;
				for(int j = 0; j < server.queues.size(); j++){
					if(server.queues.get(j).client == server.clientList.get(i)){
						isInQueue = true;
						break;
					}
				}
				if(isInQueue){
					showString += " | In Queue";
				} else{
					showString += " | Idling";
				}
			}
			model.addElement(showString);
		}
	}
	
	public void setStatusRunning(){
		lblStatusValue.setForeground(Color.GREEN);
		lblStatusValue.setText("Running");
	}
	
	public void setStatusNotRunning(){
		lblStatusValue.setForeground(Color.GREEN);
		lblStatusValue.setText("Not Running");
	}
	
	public void setStatusError(){
		lblStatusValue.setForeground(Color.RED);
		lblStatusValue.setText("Error");
	}
	
	public void setRunningStatus(String status){
		lblStatusValue.setForeground(Color.GREEN);
		lblStatusValue.setText(status);
	}
	
	public void setNotRunningStatus(String status){
		lblStatusValue.setForeground(Color.RED);
		lblStatusValue.setText(status);
	}
}