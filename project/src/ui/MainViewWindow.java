package ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import settings.MISProjectSettings;

import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;

import data_types.MISNode;
import data_types.MISScene;
import project.MISProject;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class MainViewWindow {
	private JFrame frame;
	private JList<MISNode> nodeList;

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
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnNewMenu.add(mntmSave);
		
		JMenuItem mntmLoadScene = new JMenuItem("Load Scene");
		mnNewMenu.add(mntmLoadScene);
		
		JMenuItem mntmNewProject = new JMenuItem("New Project");
		mnNewMenu.add(mntmNewProject);
		
		JMenuItem mntmProjectSettings = new JMenuItem("Project Settings");
		mnNewMenu.add(mntmProjectSettings);
		
		JMenuItem mntmBuildSettings = new JMenuItem("Build Settings");
		mnNewMenu.add(mntmBuildSettings);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		mnNewMenu.add(mntmRun);
		
		JMenuItem mntmBuild = new JMenuItem("Build");
		mnNewMenu.add(mntmBuild);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mnNewMenu.add(mntmQuit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mnEdit.add(mntmRedo);
		
		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);
		
		JMenuItem mntmConsole = new JMenuItem("Console");
		mnWindow.add(mntmConsole);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JMenuItem mntmManual = new JMenuItem("Manual");
		mnHelp.add(mntmManual);
		
		JMenuItem mntmReportABug = new JMenuItem("Report a bug");
		mnHelp.add(mntmReportABug);
		
		JMenuItem mntmSuggestAnImprovement = new JMenuItem("Suggest an improvement");
		mnHelp.add(mntmSuggestAnImprovement);
		
		JPanel RightPanel = new JPanel();
		
		JPanel MiddlePanel = new JPanel();
		
		JPanel BottomPanel = new JPanel();
		
		createList(scene);
		
		nodeList.setBackground(SystemColor.menu);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(nodeList, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(MiddlePanel, GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(RightPanel, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE))
				.addComponent(BottomPanel, GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(RightPanel, GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
						.addComponent(MiddlePanel, GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
						.addComponent(nodeList, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(BottomPanel, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}

	
	public void createList(MISScene scene){
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
							System.out.println("Show element: "+nodeList.getSelectedIndex());
						}
					});
					JMenuItem remove = new JMenuItem("Remove");
					remove.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("Remove element: "+nodeList.getSelectedIndex());
						}
					});
					menu.add(show);
					menu.add(remove);
					menu.show(nodeList, arg0.getPoint().x, arg0.getPoint().y);
					
				}
			}
		});
	}
}
