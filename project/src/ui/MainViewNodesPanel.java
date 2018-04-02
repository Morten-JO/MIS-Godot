package ui;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import data_types.MISNode;
import data_types.MISScene;
import loaders.MISLoader;
import project.MISProject;
import project.MISProjectInformation;

import javax.swing.JList;
import javax.swing.JMenuItem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainViewNodesPanel extends JPanel {
	private JList<MISNode> nodeList;
	
	/**
	 * Create the panel.
	 */
	public MainViewNodesPanel() {
		initializeComponents();
		handleEvents();
		if(MISProject.project.scenes.size() > 0){
			remakeList(MISProject.project.scenes.get(0));
		}
	}
	
	public void remakeList(MISScene scene){
		DefaultListModel<MISNode> model = new DefaultListModel<>();
		nodeList = new JList<MISNode>(model);
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
				System.out.println("ummmmmmmmm");
				if(renderer instanceof JLabel && value instanceof MISNode){
					System.out.println("WUT?");
					MISNode info = (MISNode)value;
					((JLabel)renderer).setText(info.name+" - "+info.index);
				}
				return renderer;
			}
		});
	}

	private void initializeComponents() {
		nodeList = new JList();
		
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeList.setBackground(SystemColor.menu);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(nodeList, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(nodeList, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
		);
		setLayout(groupLayout);
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
