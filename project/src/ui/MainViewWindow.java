package ui;

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

import data_types.MISNode;
import data_types.MISScene;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import project.MISProject;
import rules.MISRule;

import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

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
		
		JPanel MiddlePanel = new JPanel();
		MiddlePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Node-Information", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		
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
							.addComponent(MiddlePanel, GroupLayout.PREFERRED_SIZE, 565, Short.MAX_VALUE)
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
						.addComponent(MiddlePanel, GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE))
					.addGap(11)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblNode = new JLabel("Name:");
		lblNode.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserNode = new JLabel(" ");
		lblUserNode.setFont(new Font("Dialog", Font.PLAIN, 11));
		
		JLabel lblScript = new JLabel("Script:");
		
		lblUserScript = new JLabel(" ");
		
		JLabel lblParent = new JLabel("Parent:");
		lblParent.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserParent = new JLabel(" ");
		lblUserParent.setFont(new Font("Dialog", Font.PLAIN, 11));
		
		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserType = new JLabel(" ");
		
		JLabel lblIndex = new JLabel("Index:");
		lblIndex.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblUserIndex = new JLabel(" ");
		lblUserIndex.setFont(new Font("Dialog", Font.PLAIN, 11));
		GroupLayout gl_MiddlePanel = new GroupLayout(MiddlePanel);
		gl_MiddlePanel.setHorizontalGroup(
			gl_MiddlePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MiddlePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_MiddlePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_MiddlePanel.createSequentialGroup()
							.addGap(10)
							.addComponent(lblScript)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblUserScript, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
						.addGroup(gl_MiddlePanel.createSequentialGroup()
							.addComponent(lblNode)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblUserNode, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblType)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblUserType, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblIndex))
						.addGroup(gl_MiddlePanel.createSequentialGroup()
							.addComponent(lblParent)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblUserParent, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)))
					.addGap(112))
				.addGroup(gl_MiddlePanel.createSequentialGroup()
					.addGap(447)
					.addComponent(lblUserIndex, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_MiddlePanel.setVerticalGroup(
			gl_MiddlePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MiddlePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_MiddlePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNode)
						.addComponent(lblUserNode)
						.addComponent(lblType)
						.addComponent(lblUserType)
						.addComponent(lblIndex)
						.addComponent(lblUserIndex))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_MiddlePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblScript)
						.addComponent(lblUserScript))
					.addGap(18)
					.addGroup(gl_MiddlePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblParent)
						.addComponent(lblUserParent))
					.addGap(591))
		);
		MiddlePanel.setLayout(gl_MiddlePanel);
		
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
	
	
	public void createNodeList(MISScene scene){
		DefaultListModel<MISNode> model = new DefaultListModel<>();
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
					JMenuItem remove = new JMenuItem("Remove");
					remove.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							//call remove function
							addTextToConsole("Removed node-element #"+nodeList.getSelectedIndex());
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
					
					menu.add(show);
					menu.add(remove);
					menu.add(addRule);
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
							addTextToConsole("Showed rule-element #"+rulesList.getSelectedIndex());
						}
					});
					JMenuItem remove = new JMenuItem("Remove");
					remove.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							addTextToConsole("Removed rule-element #"+rulesList.getSelectedIndex());
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
							createNodeList(scene);
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
				ApplicationWindow.main(args);
			}
		});
		
		mntmProjectSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "UI for Project Settings is not implemented yet!");
				mnFile.getPopupMenu().setVisible(false);
				addTextToConsole("Opened project settings.");
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
				JOptionPane.showMessageDialog(null, "Run is not implemented yet!");
				mnFile.getPopupMenu().setVisible(false);
				addTextToConsole("Ran project.");
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
		
		mntmAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "About ui is not implemented yet!");
				mnHelp.getPopupMenu().setVisible(false);
				addTextToConsole("Opened about page.");
			}
		});
		
		mntmManual.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.misgodot.com/manual"));
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
				JOptionPane.showMessageDialog(null, "Report a bug ui is not yet implemented!");
				mnHelp.getPopupMenu().setVisible(false);
				addTextToConsole("Opened report a bug window.");
			}
		});
		
		mntmSuggestAnImprovement.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Suggestion ui is not implemented yet!");
				mnHelp.getPopupMenu().setVisible(false);
				addTextToConsole("Opened suggest an improvement window.");
			}
		});
		
	}
	
	public void saveableActionTaken(){
		mntmSave.setEnabled(true);
		wasChanged = true;
	}
	
	public void showNode(MISNode node){
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
