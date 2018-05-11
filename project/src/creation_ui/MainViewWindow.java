package creation_ui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.InputMap;
import javax.swing.JComponent;

import settings.MISProjectSettings;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;

import data_types.MISRoomSettings;
import data_types.MISScene;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import main.Main;
import nodes.MISNode;
import project.MISProject;
import receivers.MISReceiver;
import rules.MISRule;
import rules.MISRuleNode;
import rules.MISRuleNodePosition;
import rules.MISRuleNodeRotation;
import rules.MISRuleNodeScale;
import scriptbuilder.ScriptBuilder;
import server.Server;
import server_ui.ServerApplicationWindow;

import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionListener;

import broadcasts.MISBroadcast;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.CardLayout;
import java.awt.CheckboxMenuItem;
import java.awt.FlowLayout;
import javax.swing.JCheckBoxMenuItem;

public class MainViewWindow {
	private JFrame frame;
	private JList<MISNode> nodeList;
	private JList<MISRule> rulesList;
	private JTextPane textPaneConsole;
	private JLabel lblUserNode;
	private JLabel lblUserScript;
	private JLabel lblUserIndex;
	private JLabel lblUserType;
	private JLabel lblUserParent;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoadScene;
	private JMenuItem mntmNewProject;
	private JMenuItem mntmProjectSettings;
	private JMenuItem mntmBuildSettings;
	private JMenuItem mntmRun;
	private JMenuItem mntmBuild;
	private JMenuItem mntmQuit;
	private boolean wasChanged = true;
	private JMenu mnFile;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JMenuItem mntmConsole;
	private JMenuItem mntmAbout;
	private JMenuItem mntmManual;
	private JMenuItem mntmReportABug;
	private JMenuItem mntmSuggestAnImprovement;
	private JMenu mnEdit;
	private JMenu mnWindow;
	private JMenu mnHelp;
	private JMenuItem mntmBroadcasts;
	private MISScene currentScene;
	private DefaultListModel<MISRule> ruleModel;
	private JPanel nodeInformationPanel;
	private JPanel ruleInformationPanel;
	private JLabel lblRuleNodeUserInput;
	private JLabel lblRuleNodeUserProperty;
	private JLabel lblRuleNodeUserType;
	private JLabel lblRuleNodeUserName;
	private JLabel lblRuleUserType;
	private JLabel lblRuleUserName;
	private JPanel middlePanel;
	private JMenuItem mntmAddBroadcast;
	private JMenuItem mntmSceneSettings;
	private DefaultListModel<MISNode> model;
	private JMenuItem mntmBuildScript;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainViewWindow window = new MainViewWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainViewWindow() {
		
		initialize(MISProject.project.scenes.get(0));
		handleEvents();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(MISScene scene) {
		this.currentScene = scene;
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationWindow.class.getResource("/resources/MIS_Icon128.png")));
		frame.setName("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		frame.setTitle("MIS for Godot - Version: "+MISProjectSettings.MIS_VERSION);
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(1200, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmLoadScene = new JMenuItem("Load Scene");
		
		mnFile.add(mntmLoadScene);
		
		mntmNewProject = new JMenuItem("New Project");
		
		mnFile.add(mntmNewProject);
		
		mntmProjectSettings = new JMenuItem("Project Settings");
		
		mnFile.add(mntmProjectSettings);
		
		mntmBuildSettings = new JMenuItem("Build Settings");
		
		mnFile.add(mntmBuildSettings);
		
		mntmRun = new JMenuItem("Run");
		
		mnFile.add(mntmRun);
		
		mntmBuild = new JMenuItem("Build");
		
		mnFile.add(mntmBuild);
		
		mntmBuildScript = new JMenuItem("Build Script");
		mnFile.add(mntmBuildScript);
		
		mntmQuit = new JMenuItem("Quit");
		
		mnFile.add(mntmQuit);
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmUndo = new JMenuItem("Undo");
		
		Action undo = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		
		Action redo = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		
		JPanel contentPane = (JPanel) frame.getContentPane();
		
		InputMap undoIMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap undoAMap = contentPane.getActionMap();
		undoIMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), "undo");
		undoAMap.put("undo", undo);
		
		InputMap redoIMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap redoAMap = contentPane.getActionMap();
		redoIMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), "redo");
		redoAMap.put("redo", redo);
		
		
		mnEdit.add(mntmUndo);
		
		mntmRedo = new JMenuItem("Redo");
		
		mntmRedo.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), "redo");
		
		mnEdit.add(mntmRedo);
		
		mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		mntmConsole = new JMenuItem("Clear Console");
		
		mnWindow.add(mntmConsole);
		
		mntmBroadcasts = new JMenuItem("Broadcasts");
		mnWindow.add(mntmBroadcasts);
		
		mntmAddBroadcast = new JMenuItem("Add Broadcast");
		mnWindow.add(mntmAddBroadcast);
		
		mntmSceneSettings = new JMenuItem("Scene Settings");
		mnWindow.add(mntmSceneSettings);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		
		mnHelp.add(mntmAbout);
		
		mntmManual = new JMenuItem("Manual");
		
		mnHelp.add(mntmManual);
		
		mntmReportABug = new JMenuItem("Report a bug");
		
		mnHelp.add(mntmReportABug);
		
		mntmSuggestAnImprovement = new JMenuItem("Suggest an improvement");
		
		mnHelp.add(mntmSuggestAnImprovement);
		
		middlePanel = new JPanel();
		middlePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Node-Information", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		
		createNodeList(scene);
		
		nodeList.setBackground(SystemColor.menu);
		
		createRulesList(scene);
		
		JLabel lblNewLabel = new JLabel("Console:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(1110, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1176, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(nodeList, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(middlePanel, GroupLayout.PREFERRED_SIZE, 565, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(rulesList, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(28))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rulesList, GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
						.addComponent(nodeList, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
						.addComponent(middlePanel, GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE))
					.addGap(11)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		middlePanel.setLayout(new CardLayout(0, 0));
		
		nodeInformationPanel = new JPanel();
		middlePanel.add(nodeInformationPanel, "nodeInformation");
		
		JLabel lblScript = new JLabel("Script:");
		
		lblUserScript = new JLabel(" ");
		
		JLabel lblNode = new JLabel("Name:");
		lblNode.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserNode = new JLabel(" ");
		lblUserNode.setFont(new Font("Dialog", Font.PLAIN, 11));
		
		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserType = new JLabel(" ");
		
		JLabel lblIndex = new JLabel("Index:");
		lblIndex.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		JLabel lblParent = new JLabel("Parent:");
		lblParent.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserParent = new JLabel(" ");
		lblUserParent.setFont(new Font("Dialog", Font.PLAIN, 11));
		
		lblUserIndex = new JLabel(" ");
		lblUserIndex.setFont(new Font("Dialog", Font.PLAIN, 11));
		GroupLayout gl_nodeInformationPanel = new GroupLayout(nodeInformationPanel);
		gl_nodeInformationPanel.setHorizontalGroup(
			gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nodeInformationPanel.createSequentialGroup()
					.addGroup(gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_nodeInformationPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNode)
							.addGap(4)
							.addComponent(lblUserNode, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(lblType)
							.addGap(3)
							.addComponent(lblUserType, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addComponent(lblIndex)
							.addGap(6)
							.addComponent(lblUserIndex, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_nodeInformationPanel.createSequentialGroup()
							.addGap(18)
							.addComponent(lblScript)
							.addGap(4)
							.addComponent(lblUserScript, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_nodeInformationPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblParent)
							.addGap(3)
							.addComponent(lblUserParent, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(86, Short.MAX_VALUE))
		);
		gl_nodeInformationPanel.setVerticalGroup(
			gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nodeInformationPanel.createSequentialGroup()
					.addGroup(gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_nodeInformationPanel.createSequentialGroup()
							.addGroup(gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(7)
									.addComponent(lblNode))
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(14)
									.addComponent(lblUserNode))
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(6)
									.addComponent(lblType))
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(12)
									.addComponent(lblUserType)))
							.addGroup(gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(8)
									.addComponent(lblScript))
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(9)
									.addComponent(lblUserScript)))
							.addGroup(gl_nodeInformationPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(14)
									.addComponent(lblParent))
								.addGroup(gl_nodeInformationPanel.createSequentialGroup()
									.addGap(20)
									.addComponent(lblUserParent))))
						.addGroup(gl_nodeInformationPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(lblIndex))
						.addGroup(gl_nodeInformationPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblUserIndex)))
					.addContainerGap(596, Short.MAX_VALUE))
		);
		nodeInformationPanel.setLayout(gl_nodeInformationPanel);
		
		ruleInformationPanel = new JPanel();
		middlePanel.add(ruleInformationPanel, "ruleInformation");
		
		JLabel lblRuleName = new JLabel("Rule Name:");
		lblRuleName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblRuleUserName = new JLabel(" ");
		
		JLabel lblRuleType = new JLabel("Rule type:");
		lblRuleType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblRuleUserType = new JLabel(" ");
		
		JPanel ruleInformationSubPanel = new JPanel();
		GroupLayout gl_ruleInformationPanel = new GroupLayout(ruleInformationPanel);
		gl_ruleInformationPanel.setHorizontalGroup(
			gl_ruleInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleInformationPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ruleInformationPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(ruleInformationSubPanel, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
						.addGroup(gl_ruleInformationPanel.createSequentialGroup()
							.addGap(9)
							.addComponent(lblRuleName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblRuleUserName, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblRuleType)
							.addGap(18)
							.addComponent(lblRuleUserType, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_ruleInformationPanel.setVerticalGroup(
			gl_ruleInformationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleInformationPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ruleInformationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRuleUserName)
						.addComponent(lblRuleUserType)
						.addComponent(lblRuleName)
						.addComponent(lblRuleType))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ruleInformationSubPanel, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(492, Short.MAX_VALUE))
		);
		ruleInformationSubPanel.setLayout(new CardLayout(0, 0));
		
		JPanel ruleInformationSubPanelNode = new JPanel();
		ruleInformationSubPanel.add(ruleInformationSubPanelNode, "name_5638847767634");
		
		JLabel lblRuleNodeName = new JLabel("Node:");
		lblRuleNodeName.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblRuleNodeUserName = new JLabel(" ");
		
		JLabel lblRuleNodeType = new JLabel("Node Rule Type:");
		lblRuleNodeType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblRuleNodeUserType = new JLabel(" ");
		
		JLabel lblRuleNodeProperty = new JLabel("Node Property:");
		lblRuleNodeProperty.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblRuleNodeUserProperty = new JLabel(" ");
		
		JLabel lblRuleNodeInput = new JLabel("Node User Input:");
		lblRuleNodeInput.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblRuleNodeUserInput = new JLabel(" ");
		GroupLayout gl_ruleInformationSubPanelNode = new GroupLayout(ruleInformationSubPanelNode);
		gl_ruleInformationSubPanelNode.setHorizontalGroup(
			gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleInformationSubPanelNode.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ruleInformationSubPanelNode.createSequentialGroup()
							.addGroup(gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_ruleInformationSubPanelNode.createSequentialGroup()
									.addComponent(lblRuleNodeName)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblRuleNodeUserName, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblRuleNodeType))
								.addGroup(gl_ruleInformationSubPanelNode.createSequentialGroup()
									.addComponent(lblRuleNodeProperty)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblRuleNodeUserProperty, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblRuleNodeUserType, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
						.addGroup(gl_ruleInformationSubPanelNode.createSequentialGroup()
							.addComponent(lblRuleNodeInput)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblRuleNodeUserInput, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_ruleInformationSubPanelNode.setVerticalGroup(
			gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ruleInformationSubPanelNode.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRuleNodeName)
						.addComponent(lblRuleNodeUserName)
						.addComponent(lblRuleNodeUserType)
						.addComponent(lblRuleNodeType))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRuleNodeProperty)
						.addComponent(lblRuleNodeUserProperty))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_ruleInformationSubPanelNode.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRuleNodeInput)
						.addComponent(lblRuleNodeUserInput))
					.addContainerGap(57, Short.MAX_VALUE))
		);
		ruleInformationSubPanelNode.setLayout(gl_ruleInformationSubPanelNode);
		ruleInformationPanel.setLayout(gl_ruleInformationPanel);
		
		textPaneConsole = new JTextPane();
		textPaneConsole.setFont(new Font("Dialog", Font.PLAIN, 11));
		textPaneConsole.setBackground(SystemColor.menu);
		scrollPane.setViewportView(textPaneConsole);
		frame.getContentPane().setLayout(groupLayout);
	}

	public void createRulesList(MISScene scene){
		ruleModel = new DefaultListModel<>();
		rulesList = new JList<MISRule>(ruleModel);
		rulesList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rules", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		rulesList.setFont(new Font("Dialog", Font.PLAIN, 17));
		rulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		for(int i = 0; i < scene.ruleList.size(); i++){
			System.out.println("Rules added to model: "+scene.ruleList.get(i).ruleName);
			ruleModel.addElement(scene.ruleList.get(i));
		}
		
		rulesList.setBackground(SystemColor.menu);
		rulesList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(renderer instanceof JLabel && value instanceof MISRule){
					MISRule info = (MISRule)value;
					String indexer = "   ";
					String ruleString = indexer;
					ruleString += "Name: "+info.ruleName;
					((JLabel)renderer).setText(ruleString);
				}
				return renderer;
			}
		});
	}
	
	public void remakeRuleList(MISScene scene){
		ruleModel.removeAllElements();
		for(int i = 0; i < scene.ruleList.size(); i++){
			System.out.println("Rules added to model: "+scene.ruleList.get(i).ruleName);
			ruleModel.addElement(scene.ruleList.get(i));
		}
	}
	
	public void remakeNodeList(MISScene scene){
		model.removeAllElements();
		for(int i = 0; i < scene.nodeList.size(); i++){
			System.out.println("Nodes added to model: "+scene.nodeList.get(i).name);
			model.addElement(scene.nodeList.get(i));
		}
	}
	
	public void createNodeList(MISScene scene){
		model = new DefaultListModel<>();
		nodeList = new JList<MISNode>(model);
		nodeList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Nodes", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		nodeList.setFont(new Font("Dialog", Font.PLAIN, 17));
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		for(int i = 0; i < scene.nodeList.size(); i++){
			System.out.println("Nodes added to model: "+scene.nodeList.get(i).name);
			model.addElement(scene.nodeList.get(i));
		}
		
		nodeList.setBackground(SystemColor.menu);
		nodeList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(renderer instanceof JLabel && value instanceof MISNode){
					MISNode info = (MISNode)value;
					String indexer = "   ";
					String nodeString = indexer;
					MISNode temp = info;
					while(temp.parent != null){
						nodeString += indexer;
						temp = temp.parent;
					}
					nodeString += info.index +" - "+info.name;
					((JLabel)renderer).setText(nodeString);
				}
				return renderer;
			}
		});
	}
	
	public void addTextToConsole(String text){
		textPaneConsole.setText(textPaneConsole.getText()+text+System.lineSeparator());
	}


	private void handleEvents() {
		nodeList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(SwingUtilities.isRightMouseButton(arg0)){
					//Add actions to a string array and do something
					nodeList.setSelectedIndex(nodeList.locationToIndex(arg0.getPoint()));
					
					JPopupMenu menu = new JPopupMenu();
					JMenuItem show = new JMenuItem("Show");
					show.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							//call show function
							showNode(nodeList.getSelectedValue());
							addTextToConsole("Showed node-element #"+nodeList.getSelectedIndex());
						}
					});
					
					JMenuItem addRule = new JMenuItem("Add Rule");
					addRule.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							addTextToConsole("Add rule pressed on node-element #"+nodeList.getSelectedIndex());
							RuleDialog dialog = new RuleDialog(frame ,currentScene.nodeList);
							dialog.showDialog();
							MISRule rule = dialog.getRuleFromDialog();
							if(rule != null){
								System.out.println("Rule is name: "+rule.ruleName);
								currentScene.ruleList.add(rule);
								if(ruleModel == null){
									System.out.println("Rulemodel, fucking null?");
								}
								ruleModel.addElement(rule);
							} else{
								System.out.println("Rule is fucking null");
							}
							
							//open rule dialog box
						}
					});
					JCheckBoxMenuItem shouldSendInformation = new JCheckBoxMenuItem("Refresh");
					shouldSendInformation.setSelected(nodeList.getSelectedValue().shouldSendInformation);
					shouldSendInformation.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							nodeList.getSelectedValue().shouldSendInformation = shouldSendInformation.isSelected();
							addTextToConsole("Changed refresh value of node #"+nodeList.getSelectedIndex()+" to "+shouldSendInformation.isSelected());
						}
					});
					menu.add(show);
					menu.add(addRule);
					menu.add(shouldSendInformation);
					if(nodeList.getSelectedValue().shouldSendInformation){
						JMenuItem refreshInformation = new JMenuItem("Refresh info");
						refreshInformation.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								if(currentScene.roomSettings != null){
									SendInformationDialog infoDialog = new SendInformationDialog(currentScene.roomSettings, "Refresh information");
									infoDialog.showDialog();
									MISReceiver receiver = infoDialog.getReceiver();
									if(receiver != null){
										nodeList.getSelectedValue().informationReceivers = receiver;
									}
								} else{
									addTextToConsole("Failed to open refresh info, check scene room settings.");
								}
							}
						});
						menu.add(refreshInformation);
					}
					JCheckBoxMenuItem isControllableItem = new JCheckBoxMenuItem("Control");
					isControllableItem.setSelected(nodeList.getSelectedValue().isControllable);
					isControllableItem.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							nodeList.getSelectedValue().isControllable = isControllableItem.isSelected();
							addTextToConsole("Changed control value of node #"+nodeList.getSelectedIndex()+" to "+isControllableItem.isSelected());
						}
					});
					menu.add(isControllableItem);
					if(nodeList.getSelectedValue().isControllable){
						JMenuItem controlInfo = new JMenuItem("Control info");
						controlInfo.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								if(currentScene.roomSettings != null){
									SendInformationDialog infoDialog = new SendInformationDialog(currentScene.roomSettings, "Control information");
									infoDialog.showDialog();
									MISReceiver receiver = infoDialog.getReceiver();
									if(receiver != null){
										nodeList.getSelectedValue().controlReceiver = receiver;
									}
								} else{
									addTextToConsole("Failed to open control info, check scene room settings");
								}
							}
						});
						menu.add(controlInfo);
					}
					menu.show(nodeList, arg0.getPoint().x, arg0.getPoint().y);
					
				}
			}
		});
		
		nodeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				showNode(nodeList.getSelectedValue());
				addTextToConsole("Showed node-element #"+nodeList.getSelectedIndex());
			}
		});
		
		rulesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(SwingUtilities.isRightMouseButton(arg0)){
					//Add actions to a string array and do something
					rulesList.setSelectedIndex(rulesList.locationToIndex(arg0.getPoint()));
					
					JPopupMenu menu = new JPopupMenu();
					JMenuItem show = new JMenuItem("Show");
					show.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							showRule(rulesList.getSelectedValue());
							addTextToConsole("Showed rule-element #"+rulesList.getSelectedIndex());
						}
					});
					JMenuItem remove = new JMenuItem("Remove");
					remove.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							MISRule rule = null;
							for(int i = 0; i < ruleModel.size(); i++){
								if(ruleModel.getElementAt(i) == rulesList.getSelectedValue()){
									rule = ruleModel.getElementAt(i);
									ruleModel.remove(i);
									break;
								}
							}
							for(int i = 0; i < currentScene.ruleList.size(); i++){
								if(currentScene.ruleList.get(i) == rule){
									addTextToConsole("Removed rule: "+currentScene.ruleList.get(i).ruleName);
									currentScene.ruleList.remove(i);
									break;
								}
							}
						}
					});
					menu.add(show);
					menu.add(remove);
					menu.show(rulesList, arg0.getPoint().x, arg0.getPoint().y);
					
				}
			}
		});
		
		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(wasChanged){
					boolean saved = MISProject.saveProject();
					mntmSave.setEnabled(false);
					wasChanged = false;
					mnFile.getPopupMenu().setVisible(false);
					if(saved){
						addTextToConsole("Saved project.");
					} else{
						addTextToConsole("Failed to save project.");
					}
					
				}
			}
		});
		
		mntmLoadScene.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String[] possiblities = new String[MISProject.project.scenes.size()];
				for(int i = 0; i < possiblities.length; i++){
					possiblities[i] = MISProject.project.scenes.get(i).name;
				}
				if(possiblities.length > 0){
					String s = (String) JOptionPane.showInputDialog(frame, "Select scene to load:", "Load scene", JOptionPane.PLAIN_MESSAGE, null, possiblities, possiblities[0]);
				
					if ((s != null) && (s.length() > 0)) {
						MISScene scene = null;
						for(int i = 0; i < possiblities.length; i++){
							if(possiblities[i].equals(s)){
								scene = MISProject.project.scenes.get(i);
								break;
							}
						}
						if(scene != null){
							remakeNodeList(scene);
							remakeRuleList(scene);
							currentScene = scene;
							addTextToConsole("Loaded scene: "+scene.name);
						} else{
							addTextToConsole("Failed to load scene");
						}
					    
					}
				}
				
			}
		});
		
		mntmNewProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				MISProject.saveProject();
				mntmSave.setEnabled(false);
				wasChanged = false;
				mnFile.getPopupMenu().setVisible(false);
				frame.dispose();
				String[] args = {};
				Main.main(args);
			}
		});
		
		mntmProjectSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addTextToConsole("Opened project settings.");
				mnFile.getPopupMenu().setVisible(false);
				ProjectSettingsFrame dialog = new ProjectSettingsFrame(frame , MISProject.project);
				dialog.showDialog();
			}
		});
		
		mntmBuildSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "UI for Build Settings is not implemented yet!");
				mnFile.getPopupMenu().setVisible(false);
				addTextToConsole("Opened build settings.");
			}
		});
		
		mntmRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Server server = new Server(MISProject.project.basePort.port, true, false);
				addTextToConsole("Ran project and started server.");
			}
		});
		
		mntmBuild.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Build is not implemented yet!");
				mnFile.getPopupMenu().setVisible(false);
				addTextToConsole("Opened build window.");
			}
		});
		
		mntmBuildScript.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mnFile.getPopupMenu().setVisible(false);
				if(ScriptBuilder.buildScript(MISProject.project, currentScene, currentScene.nodeList.get(0).name, currentScene.nodeList.get(0).type, "localhost")){
					addTextToConsole("Generated script.");
				} else{
					addTextToConsole("Failed to generate script.");
				}
			}
		});
		
		mntmQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				MISProject.saveProject();
				mntmSave.setEnabled(false);
				wasChanged = false;
				mnFile.getPopupMenu().setVisible(false);
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?");
				if(dialogResult == JOptionPane.YES_OPTION){
					frame.dispose();
				}
			}
		});
		
		mntmUndo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				undo();
				mnEdit.getPopupMenu().setVisible(false);
				
			}
		});
		
		mntmRedo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				redo();
				mnEdit.getPopupMenu().setVisible(false);
				
			}
		});
		
		mntmConsole.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textPaneConsole.setText("");
				mnWindow.getPopupMenu().setVisible(false);
				addTextToConsole("Console cleared.");
			}
		});
		
		mntmSceneSettings.addMouseListener(new MouseAdapter(){
				
				@Override
				public void mousePressed(MouseEvent e) {
					mnWindow.getPopupMenu().setVisible(false);
					addTextToConsole("Console cleared.");
					SceneSettingsFrame dialog = new SceneSettingsFrame(frame ,currentScene);
					dialog.showDialog();
				}
		});
		
		mntmAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/Mortenbaws/MIS-Godot"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				mnHelp.getPopupMenu().setVisible(false);
				addTextToConsole("Opened about page.");
			}
		});
		
		mntmManual.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/Mortenbaws/MIS-Godot/blob/master/checkmark.txt"));
					mnHelp.getPopupMenu().setVisible(false);
					addTextToConsole("Opened browser link to manual.");
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		mntmReportABug.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int res = JOptionPane.showConfirmDialog(null, "Bug ui not implemented yet, open temp google document?");
				if(res == JOptionPane.YES_OPTION){
					try {
						Desktop.getDesktop().browse(new URI("https://docs.google.com/document/d/1CbuTvuOiJpqe8KoMobaxs-M-Viy853aHNynyE37MZyo/edit?usp=sharing"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
				mnHelp.getPopupMenu().setVisible(false);
				addTextToConsole("Opened report a bug window.");
			}
		});
		
		mntmSuggestAnImprovement.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int res = JOptionPane.showConfirmDialog(null, "Improvement ui not implemented yet, open temp google document?");
				if(res == JOptionPane.YES_OPTION){
					try {
						Desktop.getDesktop().browse(new URI("https://docs.google.com/document/d/1CbuTvuOiJpqe8KoMobaxs-M-Viy853aHNynyE37MZyo/edit?usp=sharing"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
				mnHelp.getPopupMenu().setVisible(false);
				addTextToConsole("Opened suggest an improvement window.");
			}
		});
		
		mntmAddBroadcast.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				addTextToConsole("Add broadcast pressed on node-element #"+nodeList.getSelectedIndex());
				BroadcastDialog dialog = new BroadcastDialog(currentScene.roomSettings);
				dialog.showDialog();
				MISBroadcast broadcast = dialog.getRuleFromDialog();
				if(broadcast != null){
					currentScene.broadcasts.add(broadcast);
					addTextToConsole("Broadcast "+broadcast.getBroadcastName()+" added.");
				} else{
					addTextToConsole("Broadcast was null, and couldn't be added.");
				}
			}
		});
		
	}
	
	public void saveableActionTaken(){
		mntmSave.setEnabled(true);
		wasChanged = true;
	}
	
	public void showRule(MISRule rule){
		((TitledBorder)middlePanel.getBorder()).setTitle("Rule Information");
		CardLayout layout = (CardLayout) middlePanel.getLayout();
		layout.show(middlePanel, "ruleInformation");
		lblRuleUserName.setText(rule.ruleName);
		if(rule instanceof MISRuleNode){
			lblRuleUserType.setText("Node");
			lblRuleNodeUserName.setText(((MISRuleNode)rule).node.name);
			if(rule instanceof MISRuleNodePosition){
				MISRuleNodePosition positionRule = (MISRuleNodePosition) rule;
				lblRuleNodeUserProperty.setText("Position");
				lblRuleNodeUserType.setText(positionRule.option.name());
				lblRuleNodeUserInput.setText(positionRule.getUserInput());
			} else if(rule instanceof MISRuleNodeRotation){
				MISRuleNodeRotation rotationRule = (MISRuleNodeRotation) rule;
				lblRuleNodeUserProperty.setText("Rotation");
				lblRuleNodeUserType.setText(rotationRule.option.name());
				lblRuleNodeUserInput.setText(rotationRule.getUserInput());
			} else if(rule instanceof MISRuleNodeScale){
				MISRuleNodeScale scaleRule = (MISRuleNodeScale) rule;
				lblRuleNodeUserProperty.setText("Scale");
				lblRuleNodeUserType.setText(scaleRule.option.name());
				lblRuleNodeUserInput.setText(scaleRule.getUserInput());
			} else{
				System.out.println("DOKGDKFGODKFGODKFOGDFKGOKFD");
				System.out.println(rule.getClass());
			}
		} else{
			System.out.println("Else........");
		}
	}
	
	public void showNode(MISNode node){
		if(node == null) return;
		((TitledBorder)middlePanel.getBorder()).setTitle("Node Information");
		CardLayout layout = (CardLayout) middlePanel.getLayout();
		layout.show(middlePanel, "nodeInformation");
		if(node.scriptAttached){
			lblUserScript.setVisible(true);
			lblUserScript.setVisible(true);
			lblUserScript.setText(node.scriptName);
		} else{
			lblUserScript.setVisible(false);
			lblUserScript.setVisible(false);
			lblUserScript.setText("");
		}
		lblUserNode.setText(node.name);
		lblUserIndex.setText(""+node.index);
		lblUserType.setText(node.type);
		if(node.parent != null){
			lblUserParent.setText(node.parent.name);
			lblUserParent.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {
					showNode(node.parent);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {}
			});
		} else{
			lblUserParent.setText("");
		}
	}
	
	private void redo(){
		addTextToConsole("Redo.");
	}
	
	private void undo(){
		addTextToConsole("Undo.");
	}
}
