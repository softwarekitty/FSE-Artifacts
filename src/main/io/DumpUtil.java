package main.io;

import java.util.TreeSet;

import main.core.RegexProjectSet;



/**
 * helps to prepare human readable data dumps
 * 
 * @author cc
 */
public class DumpUtil {
	
	public static String projectCSV(RegexProjectSet regex){
		StringBuilder sb = new StringBuilder();
		TreeSet<Integer> projectIDSet= regex.getProjectIDSet();
		for (Integer pID : projectIDSet) {
			sb.append(pID);
			sb.append(",");
		}
		String allWithExtraComma = sb.toString();
		return allWithExtraComma.substring(0, allWithExtraComma.length() - 1);
	}

	public static String regexRow(RegexProjectSet regex) {
		return regex.getPattern() + "\t" + projectCSV(regex);
	}

}
