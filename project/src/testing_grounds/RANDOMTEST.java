package testing_grounds;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import data_types.MISVariable;
import loaders.MISLoader;

public class RANDOMTEST {

	public static void main(String[] args) {
		//String[] projects = new String[]{"resources/testresources/", "resources/superawesomeproject/", "resources/OwnageProject/", "resources/NotACopy/"};
		//String[] dates = new String[]{LocalDate.now().toString(), LocalDate.now().toString(), LocalDate.now().toString(), LocalDate.now().toString()};
		//MISLoader.saveProjectLocation(projects, dates);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		JFileChooser directoryChooser = new JFileChooser();
		directoryChooser.setCurrentDirectory(new File("."));
		directoryChooser.setDialogTitle("Choose a directory for generation");
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		directoryChooser.setAcceptAllFileFilterUsed(false);
		
		if(directoryChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			System.out.println("Selected dir: "+directoryChooser.getCurrentDirectory());
			System.out.println("selected file: "+directoryChooser.getSelectedFile()+"\\");
		}			
		
	}

}
