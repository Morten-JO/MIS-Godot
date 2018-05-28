package nodes;

public class MISControl extends MISNode{

	public static String[] collectiveMatches = {"Button", "ToolButton", "ColorPickerButton", "CheckButton", "MenuButton", "CheckBox", "OptionButton", "LinkButton", "TextureButton",
												"Panel", "Range", "ProgressBar", "HScrollBar", "VScrollBar", "SpinBox", "VSlider", "HSlider", "TextureProgress", "Container",
												"ButtonGroup", "ColorPicker", "HBoxContainer", "VBoxContainer", "GraphNode", "MarginContainer", "HSplitContainer", "VSplitContainer",
												"GridContainer", "PanelContainer", "CenterContainer", "ScrollContainer", "Label", "LineEdit", "Popup", "PopupPanel", "WindowDialog",
												"AcceptDialog", "ConfirmationDialog", "FileDialog", "PopupMenu", "PopupDialog", "Tree", "VSeparator", "HSeparator", "TabContainer",
												"Tabs", "ItemList", "ReferenceFrame", "HButtonArray", "VButtonArray", "RichTextLabel", "GraphEdit", "ColorFrame", "TextureFrame",
												"Patch9Frame", "VideoPlayer"};
	
	public static boolean isType(String type){
		for(int i = 0; i < collectiveMatches.length; i++){
			if(type.equals(collectiveMatches[i])){
				return true;
			}
		}
		return false;
	}
	
	public MISControl(){
		super();
	}
}