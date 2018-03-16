package project;

public class MISProjectInformation {

	private String projectName;
	private String projectLocation;
	private String projectDateCreation;
	private boolean foundProject;
	
	public MISProjectInformation(String projectName, String projectLocation, String projectDateCreation, boolean foundProject) {
		this.projectName = projectName;
		this.projectLocation = projectLocation;
		this.projectDateCreation = projectDateCreation;
		this.foundProject = foundProject;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public String getProjectLocation() {
		return projectLocation;
	}

	public String getProjectDateCreation() {
		return projectDateCreation;
	}

	public boolean isFoundProject() {
		return foundProject;
	}
	
	
}