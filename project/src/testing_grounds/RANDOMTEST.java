package testing_grounds;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import data_types.MISVariable;
import loaders.MISLoader;

public class RANDOMTEST {

	public static void main(String[] args) {
		String[] projects = new String[]{"resources/testresources/", "resources/superawesomeproject/", "resources/OwnageProject/", "resources/NotACopy/"};
		String[] dates = new String[]{LocalDate.now().toString(), LocalDate.now().toString(), LocalDate.now().toString(), LocalDate.now().toString()};
		MISLoader.saveProjectLocation(projects, dates);
					
		
	}

}
