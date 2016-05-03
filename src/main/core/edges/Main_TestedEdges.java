package main.core.edges;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.io.IOUtil;
import recreateArtifacts.PathUtil;
import recreateArtifacts.edgeTests.AnswerColumn;

public class Main_TestedEdges {

	public static void main(String[] args) throws IOException {
		String inputPath = PathUtil.pathToComprehensionData();
		String outputPath = PathUtil.getPathEdge() + "output/";

		// output for first step of parsing sheets data,
		// and input for next step of running R on these inputs
		String rinputMatchingPath = outputPath + "Rinput/matching/";
		File rinputP2Matchingdir = new File(rinputMatchingPath + "P2/");
		File rinputP3Matchingdir = new File(rinputMatchingPath + "P3/");
		File rinputTMatchingdir = new File(rinputMatchingPath + "T/");
		prepareDirs(Arrays.asList(rinputP2Matchingdir,rinputP3Matchingdir,rinputTMatchingdir));
		populateRinputsMatching(inputPath, rinputP2Matchingdir, rinputP3Matchingdir, rinputTMatchingdir);

		
		HashMap<String, Pattern> codePatternMap = getCodeMap(inputPath + "RenamingRegexes.tsv", "\t");
		HashMap<String, List<String>> codeAnswerMap = getAnswerMap(
				IOUtil.readLines(inputPath + "compositionAnswers.tsv"), "\t");
		List<AnswerColumn> compositionAnswers = getCompositionAnswerColumns(codePatternMap, codeAnswerMap);
		
		
		
//		populateRinputsComposing(rinputP2Matchingdir, rinputP3Matchingdir, compositionAnswers, rinputP2Compositiondir, rinputP3Compositiondir);
	}

	private static void prepareDirs(List<File> dirs) {
		for(File dir : dirs){
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}

	private static void populateRinputsMatching(String inputPath, File rinputP2dir, File rinputP3dir, File rinputTdir)
			throws IOException {
		WilcoxFormatter wilcoxFormatter = new WilcoxFormatter();
		KruskalFormatter kruskalFormatter = new KruskalFormatter();

		List<AnswerColumn> pairsFrom2 = getColumns(inputPath + "pairs_from_2.csv", ",");
		List<AnswerColumn> pairsFrom3 = getColumns(inputPath + "pairs_from_3.csv", ",");
		List<AnswerColumn> tripleLines = getColumns(inputPath + "triples.csv", ",");

		writeInputFiles(pairsFrom2, rinputP2dir, wilcoxFormatter);
		writeInputFiles(pairsFrom3, rinputP3dir, wilcoxFormatter);
		writeInputFiles(tripleLines, rinputTdir, kruskalFormatter);
	}

	private static void writeInputFiles(List<AnswerColumn> answerColumns, File dir, RFormattable formatter) {
		int columnsPerFile = formatter.getNColumns();
		int nFiles = answerColumns.size() / columnsPerFile;
		if (nFiles * columnsPerFile != answerColumns.size()) {
			throw new RuntimeException("number of columns must be evenly divisible by " + columnsPerFile);
		}
		for (int fileIndex = 0; fileIndex < nFiles; fileIndex++) {
			StringBuilder filenameBuilder = new StringBuilder();
			int offset = fileIndex * columnsPerFile;
			for (int colIndex = 0; colIndex < columnsPerFile; colIndex++) {
				filenameBuilder.append(answerColumns.get(offset + colIndex).getRegexCode());
				if (colIndex < columnsPerFile - 1) {
					filenameBuilder.append("_");
				} else {
					filenameBuilder.append(".tsv");
				}
			}
			File f = new File(dir, filenameBuilder.toString());
			IOUtil.createAndWrite(f, formatter.formatData(answerColumns, offset, columnsPerFile));
		}

	}

	public static List<AnswerColumn> getColumns(String filePath, String delim) throws IOException {
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


	private static void populateRinputsComposing(File rinputP2Matchingdir, File rinputP3Matchingdir,
			List<AnswerColumn> compositionAnswers, File rinputP2Compositiondir, File rinputP3Compositiondir) {
		
		Pattern metacode = Pattern.compile("M\\d+R\\dV\\d[^M.]+[^_M.]");
		Pattern highestMetacode = Pattern.compile("M\\d");

		// use the names of the already-written inputs for matching
		
		for (File p2File : rinputP2Matchingdir.listFiles()) {
			if (!p2File.isHidden()) {
				String newFilename = p2File.getName();

				// use the names to find the right answer columns
				AnswerColumn[] pair = new AnswerColumn[2];
				Matcher codeMatcher = metacode.matcher(p2File.getName());
				int pairIndex = 0;
				while (codeMatcher.find()) {
					for (AnswerColumn ac : compositionAnswers) {
						if (codeMatcher.group().equals(ac.getRegexCode())) {
							pair[pairIndex++] = ac;
						}
					}
				}
//				getRCompositionContent(outFile.getAbsolutePath(), pair);
			}
		}	
	}

//	private static void prepareCompositionInputFiles(List<AnswerColumn> compositionAnswers, File p2_in_directory,
//			File p3_in_directory) throws IOException, InterruptedException {
//
//		Pattern metacode = Pattern.compile("M\\d+R\\dV\\d[^M.]+[^_M.]");
//		Pattern highestMetacode = Pattern.compile("M\\d");
//
//		// use the names of the already-written inputs for matching
//		
//		for (File p2File : p2_in_directory.listFiles()) {
//			if (!p2File.isHidden()) {
//				String newFilename = p2File.getName();
//
//				// use the names to find the right answer columns
//				AnswerColumn[] pair = new AnswerColumn[2];
//				Matcher codeMatcher = metacode.matcher(p2File.getName());
//				int pairIndex = 0;
//				while (codeMatcher.find()) {
//					for (AnswerColumn ac : compositionAnswers) {
//						if (codeMatcher.group().equals(ac.getRegexCode())) {
//							pair[pairIndex++] = ac;
//						}
//					}
//				}
//				getRCompositionContent(outFile.getAbsolutePath(), pair);
//			}
//		}
//
//		for (File p3File : p3_in_directory.listFiles()) {
//			if (!p3File.isHidden()) {
//				String newFilename = p3File.getName().replaceAll("tsv", "Rout");
//				File outFile = new File(p3_out_composition_directory, newFilename);
//				AnswerColumn[] pair = new AnswerColumn[2];
//				Matcher codeMatcher = metacode.matcher(p3File.getName());
//				int pairIndex = 0;
//				while (codeMatcher.find()) {
//					for (AnswerColumn ac : compositionAnswers) {
//						if (codeMatcher.group().equals(ac.getRegexCode())) {
//							pair[pairIndex++] = ac;
//						}
//					}
//				}
//				writeROutput(getRCompositionContent(outFile.getAbsolutePath(), pair), scriptTempFile);
//			}
//		}
//
////		for (File mFile : m_in_directory.listFiles()) {
////			if (!mFile.isHidden()) {
////				File inFile = new File(m_in_composition_directory, mFile.getName());
////				Matcher partMatcher = highestMetacode.matcher(mFile.getName());
////				partMatcher.find();
////				String prefixPart = partMatcher.group();
////				AnswerColumn[] columns = new AnswerColumn[6];
////				int colIndex = 0;
////				for (AnswerColumn ac : compositionAnswers) {
////					if (ac.getRegexCode().startsWith(prefixPart)) {
////						columns[colIndex++] = ac;
////					}
////				}
////				IOUtil.createAndWrite(inFile, getMetaCompositionRInContent(columns));
////
////				String newFilename = mFile.getName().replaceAll("csv", "Rout");
////				File outFile = new File(m_out_composition_directory, newFilename);
////				writeROutput(wrapTestIO(inFile.getAbsolutePath(), getANOVATest(), outFile.getAbsolutePath(), ","),
////						scriptTempFile);
////			}
////		}
//	}

//	private static String getMetaCompositionRInContent(AnswerColumn[] columns) {
//		DecimalFormat df3 = new DecimalFormat("0.00");
//		StringBuilder sb = new StringBuilder();
//		Pattern finder = Pattern.compile("M\\d(R\\d)V\\d_(.*)");
//		sb.append("regex,refactoring,accuracy\n");
//		for (AnswerColumn ac : columns) {
//			Matcher m = finder.matcher(ac.getRegexCode());
//			m.find();
//			String regex = m.group(1);
//			String refactoring = m.group(2);
//			for (Double d : ac.getExistingValues()) {
//				sb.append(regex + "," + refactoring + "," + df3.format(d) + "\n");
//			}
//		}
//		return sb.toString();
//	}

}
