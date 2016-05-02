package recreateArtifacts;

import java.io.File;

public class PathUtil {

	public static final String homePath = System.getProperty("user.dir");

	public static String getConnectionString() {
		String absPathToDb = new File(getDBPath()).getPath();
		return "jdbc:sqlite:" + absPathToDb;
	}

	public static String getDBPath(){
		return getArtifactPath() + "merged_report.db";
	}

	public static String getPathCorpus() {
		return homePath + getRelPath()+"corpus/";
	}

	public static String getPathEdge() {
		return homePath + getRelPath()+"edgeTests/";
	}

	public static String getPathNodeFilter() {
		return homePath + getRelPath()+"nodeFilter/";
	}

	public static String getPathNodeFinal() {
		return homePath + getRelPath()+"nodeFinal/";
	}

	public static String getPathSource() {
		return homePath + getRelPath()+"sourceInfo/";
	}

	private static String getRelPath(){
		return "/src/recreateArtifacts/";
	}

	public static String pathToManuallyReviewedNodes(){
		return homePath + "/artifacts/manuallyReviewedNodes/";
	}

	public static String pathToComprehensionData(){
		return homePath + "/artifacts/comprehensionData/";
	}

	private static String getArtifactPath(){
		return homePath + "/artifacts/";
	}

	public static String pathToCorpusFile() {
		return getArtifactPath() + "fullCorpus.tsv";
	}

	public static String pathToFilteredCorpus() {
		return getArtifactPath() + "filteredCorpus.tsv";
	}

}
