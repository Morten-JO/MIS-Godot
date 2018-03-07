package data_types;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import enums.MISListType;
import project.MISProject;
import settings.MISGeneralSettings;

public class MISList {

	private List<MISVariable> list;
	
	public MISList(){
		MISListType type = MISProject.project.listType;
		if(type == MISListType.ARRAY){
			list = new ArrayList<MISVariable>();
		} else if(type == MISListType.LINKED){
			list = new LinkedList<MISVariable>();
		}
	}
	
	public List<MISVariable> getList(){
		return list;
	}
	
	public MISVariable getVariableFromKey(String key){
		for(MISVariable var : list){
			if(var.keyToCompare(key)){
				return var;
			}
		}
		return null;
	}
	
	public boolean addVariableToList(String key){
		return false;
	}
	
}
