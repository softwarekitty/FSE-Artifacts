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
		
		/**
		 * get the data from the input files into AnswerColumns
		 */
		List<AnswerColumn> pairsFrom2 = getColumns(inputPath + "pairs_from_2.csv", ",");
		List<AnswerColumn> pairsFrom3 = getColumns(inputPath + "pairs_from_3.csv", ",");
		HashMap<String, Pattern> codePatternMap = getCodeMap(inputPath + "RenamingRegexes.tsv", "\t");
		HashMap<String, List<String>> codeAnswerMap = getAnswerMap(IOUtil.readLines(inputPath + "compositionAnswers.tsv"), "\t");
		List<AnswerColumn> compositionAnswers = getCompositionAnswerColumns(codePatternMap, codeAnswerMap);

		/**
		 * determine what experiments belong to each edge
		 */
		TreeSet<EdgeExperimentsList> edges = new TreeSet<EdgeExperimentsList>();
		List<String> sourceLines = IOUtil.readLines(inputPath + "EXP_EDG_LST.tsv");
		for (String line : sourceLines) {
			String[] parts = line.split("\t");
			String edgeIndex = parts[0];
			String edgeDescription = parts[1];
			int nPairs = (parts.length - 2) / 2;
			LinkedList<ExperimentPair> experiments = new LinkedList<ExperimentPair>();
			for (int i = 0; i < nPairs; i++) {
				experiments.add(new ExperimentPair(parts[i + 2], parts[i + 2 + nPairs], pairsFrom2, pairsFrom3,
						compositionAnswers));
			}
			edges.add(new EdgeExperimentsList(edgeIndex, edgeDescription, experiments));
		}
		
		/**
		 * build the table
		 */
		String caption = "Averaged Info About Edges (sorted by lowest of either p-value)";
		String edgeTableHeader = "\\begin{table*}\\begin{footnotesize}\\begin{center}\\caption{" + caption
				+ "}\\label{table:testedEdgesTable}\\begin{tabular}\n" + "{llccccccc}\n"
				+ "Index & Nodes & Pairs & Match1 & Match2 & $H_0^{match} $ & Compose1 & Compose2 &  $H_0^{comp}$ \\bigstrut \\\\\n"
				+ "\\toprule[0.16em]\n";
		String edgeTableFooter = "\\bottomrule[0.13em]\\end{tabular}\\end{center}\\end{footnotesize}\\end{table*}\n\\end{document}\n";
		StringBuilder latexContent = new StringBuilder();
		latexContent.append(DumpUtil.getLatexDocSetup());
		latexContent.append(edgeTableHeader);
		int rowCounter = 0;
		for (EdgeExperimentsList eel : edges) {
			if (rowCounter++ == 4) {
				latexContent.append("\\midrule[0.16em]\n");
			}
			latexContent.append(eel.getLatexRow(rowCounter));
		}
		latexContent.append(edgeTableFooter);
		String outputPath = PathUtil.getPathEdge() + "output/";
		File edgeTableFile = new File(outputPath + "testedEdgesTable.tex");
		IOUtil.createAndWrite(edgeTableFile, latexContent.toString());
	}

	private static List<AnswerColumn> getCompositionAnswerColumns(HashMap<String, Pattern> codePatternMap,
			HashMap<String, List<String>> codeAnswerMap) {
		List<AnswerColumn> compositionAnswers = new ArrayList<AnswerColumn>(60);
		for (Entry<String, Pattern> entry : codePatternMap.entrySet()) {
			String code = entry.getKey();
			Pattern p = entry.getValue();
			List<String> answers = codeAnswerMap.get(code);
			if (answers == null) {
				System.err.println("null answer list for code: " + code);
			} else if (answers.size() != 30) {
				System.err.println("wrong number of answers(" + answers.size() + ") for code: " + code);
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

	private static List<AnswerColumn> getColumns(String filePath, String delim) throws IOException {
		List<String> lines = IOUtil.readLines(filePath);

		String[] colNames = lines.get(0).split(delim);
		int nCols = colNames.length;
		int nRows = lines.size() - 1;

		// assume that the first line is column headers, well formed matrix,
		// etc.
		// notice the divergence from convention - this is an array of columns
		Double[][] values = new Double[nCols][nRows];
		for (int i = 0; i < nRows; i++) {
			String[] rowValues = lines.get(i + 1).split(delim);
			for (int j = 0; j < nCols; j++) {
				values[j][i] = rowValues[j].equals("NA") ? Double.NaN : Double.parseDouble(rowValues[j]);
			}
		}
		List<AnswerColumn> answerColumns = new ArrayList<AnswerColumn>(nCols);
		for (int j = 0; j < nCols; j++) {
			answerColumns.add(new AnswerColumn(colNames[j], values[j]));
		}
		return answerColumns;
	}

	private static HashMap<String, Pattern> getCodeMap(String path, String delim) throws IOException {
		List<String> lines = IOUtil.readLines(path);
		HashMap<String, Pattern> codeMap = new HashMap<String, Pattern>();

		for (String line : lines) {
			// System.out.println(line);
			String[] tokens = line.split(delim);
			Pattern p = Pattern.compile(tokens[1].substring(0, tokens[1].length() - 1));
			Pattern y = codeMap.put(tokens[0], p);
			if (y != null) {
				System.err.println("duplicate key");
			}
		}
		return codeMap;
	}

	private static HashMap<String, List<String>> getAnswerMap(List<String> lines, String delim) {
		HashMap<String, List<String>> answerMap = new HashMap<String, List<String>>();

		for (String line : lines) {
			String[] tokens = line.split(delim);
			String tempCode = "";
			boolean isCode = true;
			for (String token : tokens) {
				if (isCode) {
					tempCode = token;
				} else {
					List<String> answers = answerMap.get(tempCode);
					if (answers == null) {
						answers = new LinkedList<String>();
					}
					answers.add(token);
					answerMap.put(tempCode, answers);
				}
				isCode = !isCode;
			}
		}
		return answerMap;
	}
}
