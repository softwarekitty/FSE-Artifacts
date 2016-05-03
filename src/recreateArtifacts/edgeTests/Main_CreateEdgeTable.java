package recreateArtifacts.edgeTests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import main.io.DumpUtil;
import main.io.IOUtil;
import recreateArtifacts.PathUtil;

public class Main_CreateEdgeTable {
	

	public static void main(String[] args) throws IOException, InterruptedException, ScriptException {
		String inputPath = PathUtil.pathToComprehensionData();		
		List<AnswerColumn> pairsFrom2 = EdgeUtil.getColumns(inputPath + "pairs_from_2.csv", ",");
		List<AnswerColumn> pairsFrom3 = EdgeUtil.getColumns(inputPath + "pairs_from_3.csv", ",");
		
		HashMap<String, Pattern> codePatternMap = EdgeUtil.getCodeMap(inputPath + "RenamingRegexes.tsv", "\t");
		HashMap<String, List<String>> codeAnswerMap = EdgeUtil.getAnswerMap(
				IOUtil.readLines(inputPath + "compositionAnswers.tsv"), "\t");
		List<AnswerColumn> compositionAnswers = getCompositionAnswerColumns(codePatternMap, codeAnswerMap);
		
		TreeSet<EdgeExperimentsList> edges = new TreeSet<EdgeExperimentsList>();
		List<String> sourceLines = IOUtil.readLines(inputPath+"EXP_EDG_LST.tsv");
		for(String line: sourceLines){
			String[] parts = line.split("\t");
			String edgeIndex = parts[0];
			String edgeDescription = parts[1];
			int nPairs = (parts.length-2)/2;
			LinkedList<ExperimentPair> experiments = new LinkedList<ExperimentPair>();
			for(int i = 0;i<nPairs;i++){
				experiments.add(new ExperimentPair(parts[i+2],parts[i+2+nPairs],pairsFrom2,pairsFrom3, compositionAnswers));
			}
			edges.add(new EdgeExperimentsList(edgeIndex,edgeDescription,experiments));
		}
		String caption = "Averaged Info About Edges (sorted by lowest of either p-value)";
		String edgeTableHeader = "\\begin{table*}\\begin{footnotesize}\\begin{center}\\caption{"+caption+"}\\label{table:testedEdgesTable}\\begin{tabular}\n"+
		"{llccccccc}\n"+
		"Index & Nodes & Pairs & Match1 & Match2 & $H_0^{match} $ & Compose1 & Compose2 &  $H_0^{comp}$ \\bigstrut \\\\\n"+
		"\\toprule[0.16em]\n";
		String edgeTableFooter = "\\bottomrule[0.13em]\\end{tabular}\\end{center}\\end{footnotesize}\\end{table*}\n\\end{document}\n";
		StringBuilder latexContent = new StringBuilder();
		latexContent.append(DumpUtil.getLatexDocSetup());
		latexContent.append(edgeTableHeader);
		int rowCounter = 0;
		for(EdgeExperimentsList eel : edges){
			if(rowCounter++ == 4){
				latexContent.append("\\midrule[0.16em]\n");
			}
			latexContent.append(eel.getLatexRow(rowCounter));
		}		
		latexContent.append(edgeTableFooter);
		String outputPath = PathUtil.getPathEdge() + "output/";
		File edgeTableFile =new File(outputPath+"testedEdgesTable.tex");
		IOUtil.createAndWrite(edgeTableFile, latexContent.toString());
	}

	private static List<AnswerColumn> getCompositionAnswerColumns(
			HashMap<String, Pattern> codePatternMap,
			HashMap<String, List<String>> codeAnswerMap) {
		List<AnswerColumn> compositionAnswers = new ArrayList<AnswerColumn>(60);
		for (Entry<String, Pattern> entry : codePatternMap.entrySet()) {
			String code = entry.getKey();
			Pattern p = entry.getValue();
			List<String> answers = codeAnswerMap.get(code);
			if (answers == null) {
				System.err.println("null answer list for code: " + code);
			} else if (answers.size() != 30) {
				System.err.println("wrong number of answers(" + answers.size() +
					") for code: " + code);
			} else {
				Double[] answerValues = new Double[30];
				int index = 0;
				for (String answer : answers) {
					Matcher m = p.matcher(answer);
					answerValues[index++] = m.find() ? 1.0 : 0.0;
				}
				compositionAnswers.add(new AnswerColumn(code, answerValues));
			}
		}
		return compositionAnswers;
	}
}
