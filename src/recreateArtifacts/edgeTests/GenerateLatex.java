package recreateArtifacts.edgeTests;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.edges.AnswerColumn;
import main.io.IOUtil;


public class GenerateLatex {	
	private static DecimalFormat df10 = new DecimalFormat("0.000000000");

	
	private static String sourceFilename = "EXP_EDG_LST.tsv";
	private static File g_out_directory = null;//new File(IOUtil.dataPath + IOUtil.OUT +IOUtil.G_PATH);
	

	public static void main(String[] args) throws IOException, InterruptedException {
		createTestedEdgesTable();
		//createGroupTable();

	}
	
	private static void createGroupTable() throws IOException {
		int regexI = 0;
		int refactorI = 1;
		double[][] pValues = new double[6][2];
		String between = " & ";
		String end = "\\\\\n";
		String caption = "some Group table caption";
		String groupTableHeader = "\\begin{table*}\\begin{small}\\begin{center}\\caption{"+caption+"}\\label{table:groupANOVATable}\\begin{tabular}\n"+
				"{lcc}\n"+
				"group & Pregex & Prefactoring \\\\\n"+
				"\\toprule[0.16em]\n";
		String groupTableFooter = "\\bottomrule[0.13em]\\end{tabular}\\end{center}\\end{small}\\end{table*}\n";
		
		StringBuilder latexContent = new StringBuilder();
		
		
		latexContent.append(groupTableHeader);
		int counter = 0;
		for (File gFile : g_out_directory.listFiles()) {
			if (!gFile.isHidden()) {
				CharSequence fileNameWithoutExtension = gFile.getName().subSequence(0, gFile.getName().indexOf("."));
				List<String> lines = IOUtil.readLines(gFile.getAbsolutePath());
				String regexLine = lines.get(1);
				String refactoringLine = lines.get(2);
				pValues[counter][regexI] = getPFromLine(regexLine);
				pValues[counter][refactorI] = getPFromLine(refactoringLine);
				
				latexContent.append(fileNameWithoutExtension);
				latexContent.append(between);
				latexContent.append(Composer.formatPValue(pValues[counter][0]));
				latexContent.append(between);
				latexContent.append(Composer.formatPValue(pValues[counter][1]));
				latexContent.append(end);
				counter++;
			}
		}
		latexContent.append(groupTableFooter);
		File groupTableFile = null;//new File(IOUtil.paperPath+IOUtil.TABLE+"groupANOVATable.tex");
		IOUtil.createAndWrite(groupTableFile, latexContent.toString());
	}
	
	private static double getPFromLine(String line){
		String[] parts = line.split("\\s+");
		String pValueString = parts[5];
		if(pValueString.equals("<2e-16")){
			return 0;
		}else{
			return Double.parseDouble(pValueString);
		}
	}

	private static void createTestedEdgesTable() throws IOException, InterruptedException{
		List<AnswerColumn> pairsFrom2 = null;//IOUtil.getColumns(IOUtil.getLines(IOUtil.dataPath +IOUtil.ORIGINAL + "pairs_from_2.csv"), ",");
			List<AnswerColumn> pairsFrom3 = null;//IOUtil.getColumns(IOUtil.getLines(IOUtil.dataPath +IOUtil.ORIGINAL + "pairs_from_3.csv"), ",");
			
			HashMap<String, Pattern> codePatternMap = null;//IOUtil.getCodeMap(IOUtil.getLines(IOUtil.dataPath +IOUtil.ORIGINAL + "RenamingRegexes.tsv"), "\t");
				HashMap<String, List<String>> codeAnswerMap = null;//IOUtil.getAnswerMap(IOUtil.getLines(IOUtil.dataPath +IOUtil.ORIGINAL + "compositionAnswers.tsv"), "\t");
				List<AnswerColumn> compositionAnswers = getCompositionAnswerColumns(codePatternMap, codeAnswerMap);
			
			TreeSet<EdgeExperimentsList> edges = new TreeSet<EdgeExperimentsList>();
			List<String> sourceLines = null;//IOUtil.getLines(IOUtil.dataPath+IOUtil.ORIGINAL+sourceFilename);
			for(String line : sourceLines){
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
			String caption = "Averaged Info About Edges (sorted by lowest of either p-value)";//What Edges Between Equivalent Nodes Have Significantly Different Readability (By Matching Accuracy And Composition Tasks)?
			String edgeTableHeader = "\\begin{table*}\\begin{small}\\begin{center}\\caption{"+caption+"}\\label{table:testedEdgesTable}\\begin{tabular}\n"+
			"{llccccccc}\n"+
			"Index & Nodes & Pairs & Match1 & Match2 & $H_0^{match} $ & Compose1 & Compose2 &  $H_0^{comp}$ \\bigstrut \\\\\n"+
			"\\toprule[0.16em]\n";
			String edgeTableFooter = "\\bottomrule[0.13em]\\end{tabular}\\end{center}\\end{small}\\end{table*}\n";
			StringBuilder latexContent = new StringBuilder();
			latexContent.append(edgeTableHeader);
			int rowCounter = 0;
			for(EdgeExperimentsList eel : edges){
				if(rowCounter++ == 5){
					latexContent.append("\\midrule[0.16em]\n");
				}
				latexContent.append(eel.getLatexRow(rowCounter));
			}		
			latexContent.append(edgeTableFooter);
			File edgeTableFile = null;//new File(IOUtil.paperPath+IOUtil.TABLE+"testedEdgesTable.tex");
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
