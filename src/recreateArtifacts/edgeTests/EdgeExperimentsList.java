package recreateArtifacts.edgeTests;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.script.ScriptException;

import main.io.DumpUtil;
import recreateArtifacts.edgeTests.renjin.WilcoxTest;

public class EdgeExperimentsList implements Comparable<EdgeExperimentsList> {

	private final String[] colNames;
	private final double matchingP;
	private final double composingP;
	private final String edgeIndex;
	private final List<ExperimentPair> experiments;
	private static final String between = " & ";
	private static DecimalFormat df3 = new DecimalFormat("0.00");
	private static DecimalFormat i2 = new DecimalFormat("##");

	public EdgeExperimentsList(String edgeIndex, String edgeDescription, List<ExperimentPair> experiments) throws ScriptException{
		this.edgeIndex = edgeIndex;
		colNames = edgeDescription.split("->");
		this.experiments = experiments;
		this.matchingP = getPValue(true);
		this.composingP = getPValue(false);
	}

	private double getPValue(boolean b) throws ScriptException {
		String[] inputs = getWilcoxInputs(b);
		WilcoxTest wt = new WilcoxTest(inputs[0],inputs[1]);
		return wt.getpValue();
	}

	public String getEdgeIndex() {
		return edgeIndex;
	}

	public List<ExperimentPair> getExperiments() {
		return experiments;
	}

	public String[] getWilcoxInputs(boolean isMatching) {
		StringBuilder sb0 = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		sb0.append("v0=c(");
		sb1.append("v1=c(");

		for (int i=0;i<experiments.size();i++) {
			ExperimentPair ep = experiments.get(i);
			AnswerColumn[] acs = isMatching ? ep.getMatchingColumns() : ep.getComposingColumns();
			AnswerColumn leftAc = acs[0];
			AnswerColumn rightAc = acs[1];
			// should never happen, because we include NAN inputs here, so all
			// should be 30.
			if (leftAc.getValues().length != rightAc.getValues().length) {
				throw new RuntimeException("cannot generate wilcox input with different number of measurements");
			}
			Double[] leftValues = leftAc.getValues();
			Double[] rightValues = rightAc.getValues();
			for (int k = 0; k < leftValues.length; k++) {
				sb0.append(leftValues[k]);
				sb1.append(rightValues[k]);
				if(k!=leftValues.length-1 || i!=experiments.size()-1){
					sb0.append(",");
					sb1.append(",");
				}
			}
		}
		String[] inputs = new String[2];
		inputs[0] = sb0.toString() + ")";
		inputs[1] = sb1.toString() + ")";
		return inputs;
	}
	
	///////////////latex//////////////////

	public String getLatexRow(int edgeNumber) throws IOException, InterruptedException {
		String end = "\\\\\n";
		return "E" + edgeNumber + between + colNames[0] + " -- " + colNames[1] + between + experiments.size() + between
				+ getNumberSectionOfLatex() + end;
	}

	private String getNumberSectionOfLatex() throws IOException, InterruptedException {
		double[] numbers = new double[4];
		double counter = 0;
		for (ExperimentPair ep : experiments) {
			counter++;
			numbers[0] += ep.getMatchingAvgLeft();
			numbers[1] += ep.getMatchingAvgRight();
			numbers[2] += ep.getComposingAvgLeft();
			numbers[3] += ep.getComposingAvgRight();
		}
		for (int i = 0; i < 4; i++) {
			numbers[i] = numbers[i] / counter;
		}
		String matchP = DumpUtil.formatPValue(matchingP);
		String compP = DumpUtil.formatPValue(composingP);
		boolean shouldBoldMatch = false;
		boolean shouldBoldComp = false;

		if (matchingP < 0.1 || composingP < 0.1) {
			if (matchingP < composingP) {
				shouldBoldMatch = true;
			} else if (composingP < matchingP) {
				shouldBoldComp = true;
			}
		}
		matchP = shouldBoldMatch ? "\\textbf{" + matchP + "}" : matchP;
		compP = shouldBoldComp ? "\\textbf{" + compP + "}" : compP;

		return df3.format(numbers[0]) + between + df3.format(numbers[1]) + between + matchP + between
				+ df3.format(numbers[2]) + between + df3.format(numbers[3]) + between + compP;
	}
	
	/////////////////compare///////////////

	// first by matchingP, then by composingP then by experiments.size()
	@Override
	public int compareTo(EdgeExperimentsList o) {
		if (this.matchingP == o.matchingP && this.composingP == o.composingP) {
			return 0;
		} else if (this.matchingP == o.matchingP) {
			if (this.composingP < o.composingP) {
				return -1;
			} else {
				return 1;
			}
		} else if (this.composingP == o.composingP) {
			if (this.matchingP < o.matchingP) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return compareMins(o);
		}
	}

	private int compareMins(EdgeExperimentsList o) {
		double[] pValues = new double[4];
		pValues[0] = this.matchingP;
		pValues[1] = this.composingP;
		pValues[2] = o.matchingP;
		pValues[3] = o.composingP;
		double minValue = Double.MAX_VALUE;
		int minIndex = 0;
		for (int i = 0; i < 4; i++) {
			if (pValues[i] < minValue) {
				minValue = pValues[i];
				minIndex = i;
			}
		}
		if (minIndex < 2) {
			return -1;
		} else {
			return 1;
		}
	}
	
	@Override
	public String toString() {
		return "EdgeExperimentsList [edgeIndex=" + edgeIndex + ", experiments=" + experiments + "]";
	}
}
