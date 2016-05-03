package main.io;

import java.text.DecimalFormat;
import java.util.TreeSet;

import main.core.RegexProjectSet;

/**
 * helps to prepare human readable data dumps
 * 
 * @author cc
 */
public class DumpUtil {

	public static String verbatimWrap(String rawPattern) {
		char[] charsToUse = { '!', '@', '|', ':' };

		for (int i = 0; i < charsToUse.length; i++) {
			char c = charsToUse[i];
			if (rawPattern.indexOf(c) == -1) {
				return "\\cverb" + c + rawPattern + c;
			}
		}
		return "\\cverb•" + rawPattern + "•";
	}

	public static String projectCSV(RegexProjectSet regex) {
		StringBuilder sb = new StringBuilder();
		TreeSet<Integer> projectIDSet = regex.getProjectIDSet();
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

	public static String formatPValue(double pValue) {
		DecimalFormat df = new DecimalFormat("0.000");
		if (pValue < 0.001) {
			return "$<$0.001";
		} else {
			return df.format(pValue);
		}
	}
	
	public static String getLatexDocSetup() {
		String docHead = "\\documentclass[12pt]{article}\n"+
				"\\usepackage{calc}\n"+
				"\\usepackage{enumitem}\n"+
				"\\usepackage[pdftex]{graphicx}\n"+
				"\\usepackage[margin=0.8in]{geometry}\n"+
				"\\usepackage{balance}\n"+
				"\\usepackage{multirow}\n"+
				"\\usepackage{multicol}\n"+
				"\\RequirePackage{booktabs}\n"+
				"\\renewcommand*\\cmidrule{\\midrule[0.001em]} % Thin middle lines\n\\RequirePackage{bigstrut}\n\\setlength\\bigstrutjot{2pt}\n";
		String cverbSetup = "\\usepackage{fancyvrb,newverbs,color}\n"+
				"\\definecolor{cverbbg}{gray}{0.93}\n"+
				"\\definecolor{bverbbg}{gray}{0.975}\n"+
				"\\definecolor{iverbbg}{gray}{0.96}\n"+
				"\\newcommand{\\verbatimfont}[1]{\\def\\verbatim@font{#1}}%\n"+
				"\\newverbcommand{\\cverb}\n"+
				"{\\setbox\\verbbox\\hbox\\bgroup}\n"+
				"{\\egroup\\colorbox{cverbbg}{\\box\\verbbox}}\n\\begin{document}\n";
		return docHead + cverbSetup;
	}

}
