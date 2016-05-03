package recreateArtifacts.nodeFinal;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import main.core.RegexProjectSet;
import main.core.filter.AbstractFilter;
import main.core.filter.Model;
import main.io.DumpUtil;
import main.io.IOUtil;
import main.io.LoadUtil;
import main.parse.PythonParsingException;
import main.parse.QuoteRuleException;
import recreateArtifacts.PathUtil;

public class Main_CreateTexFromManual {
	
	public static void main(String[] args) throws IllegalArgumentException, IOException, QuoteRuleException, PythonParsingException {
		
		// load and process the corpus
		TreeSet<RegexProjectSet> corpus = LoadUtil
				.loadRegexProjectSetInput(IOUtil.readLines(PathUtil.pathToCorpusFile()));
		double corpusSize = corpus.size() + 0.0;
		TreeSet<Integer> corpusProjectIDs = aggregateProjectIDs(corpus);
		double allProjectsSize = corpusProjectIDs.size() + 0.0;
		
		// input and output paths
		File cleanRoot = new File(PathUtil.pathToManuallyReviewedNodes());
		File nodeCountFile = new File(PathUtil.getPathNodeFinal() + "output/nodeCount.tex");
		
		
		// some strings for building the table
		DecimalFormat df3 = new DecimalFormat("#.0");
		String midline = "\\midrule\n";
		String between = " & ";
		String end = "\\\\\n";
		String caption = "How frequently is each alternative expression style used?";
		String tableLatex = "\\begin{table*}[ht]\n\\begin{scriptsize}\\begin{center}\n" +
			"\\caption{" + caption + "}\n" + "\\label{table:nodeCount}\n" +
			"\\begin{tabular}\n{lll@{}rrrr}\n";
		String topRow = "name & description & example & nPatterns & \\% patterns & nProjects & \\% projects \\\\ \n\\toprule[0.16em]\n";
		double width = 1.5;
		String widthS = df3.format(width);
		String beforePattern = "\\begin{minipage}{" + widthS +
			"in}\\begin{verbatim}\n";
		String afterPattern = "\\end{verbatim}\\end{minipage}\n";
		String corpusProjectIDCount = "%corpusProjectIDs.size(): "+corpusProjectIDs.size()+"\n";

		
		// build the table
		StringBuilder report = new StringBuilder();
		Map<String, HashMap<String, AbstractFilter>> filtersMap = Model.getFiltersMaps();
		report.append(DumpUtil.getLatexDocSetup());
		report.append(tableLatex + topRow);
		report.append(corpusProjectIDCount);
		int counter = 0;
		for (File cleanDir : cleanRoot.listFiles()) {
			if (!cleanDir.isHidden() && cleanDir.isDirectory()) {
				counter++;
				
				// no extra files in the input folder, please
				String groupName = cleanDir.getName();
				HashMap<String, AbstractFilter> filters = filtersMap.get(groupName);
				
				for (File nodeFile : cleanDir.listFiles()) {
					if (nodeFile.getName().endsWith(".tsv")) {
						TreeSet<RegexProjectSet> cleanRTNodeMembers = LoadUtil
								.loadRegexProjectSetInput(IOUtil.readLines(nodeFile.getAbsolutePath()));
						TreeSet<Integer> fileMemberIDs = aggregateProjectIDs(cleanRTNodeMembers);
						int nProjects = fileMemberIDs.size();
						int nPatterns = cleanRTNodeMembers.size();
						double percentPatterns = 100*(cleanRTNodeMembers.size() /
							corpusSize);
						double percentProjects = 100*(fileMemberIDs.size() /
							allProjectsSize);
						
						String nodeName = nodeFile.getName().replaceAll(".tsv", "").replaceAll("_", " ");
						AbstractFilter filter = filters.get(nodeName);

						report.append(nodeName);
						report.append(between);
						report.append(filter.getDescription());
						report.append(between);
//						report.append(beforePattern);
						report.append(DumpUtil.verbatimWrap(filter.getExample()));
//						report.append(afterPattern);
						report.append(between);
						report.append(commafy(nPatterns));
						report.append(between);
						report.append(df3.format(percentPatterns)+"\\%");
						report.append(between);
						report.append(commafy(nProjects));
						report.append(between);
						report.append(df3.format(percentProjects)+"\\%");
						report.append(end);
					}
				}
				if(counter<5){
					report.append(midline);
				}
			}
		}
		String tableFoot = "\\bottomrule[0.13em]\n\\end{tabular}\n\\end{center}\\end{scriptsize}\\end{table*}\n\\end{document}\n";
		report.append(tableFoot);
		IOUtil.createAndWrite(nodeCountFile, report.toString());
	}

	private static TreeSet<Integer> aggregateProjectIDs(
			TreeSet<RegexProjectSet> regexSet) {
		TreeSet<Integer> allProjectIDs = new TreeSet<Integer>();
		for (RegexProjectSet member : regexSet) {
			allProjectIDs.addAll(member.getProjectIDSet());
		}
		return allProjectIDs;
	}
	
	public static String percentify(double d, double sum) {
		DecimalFormat df = new DecimalFormat("##0.#");
		return df.format(100 * round(d / sum, 3));
	}
	
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String commafy(int n) {
		return NumberFormat.getNumberInstance(Locale.US).format(n);
	}
}
