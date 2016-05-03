package recreateArtifacts.edgeTests;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.script.ScriptException;

import main.io.DumpUtil;

/**
 * represents a list of experiments for a given edge
 * 
 * @author cc
 *
 */
public class EdgeExperimentsList implements Comparable<EdgeExperimentsList> {

	private final String[] colNames;
	private final double matchingP;
	private final double composingP;
	private final String edgeIndex;
	private final List<ExperimentPair> experiments;
	private static final String between = " & ";
	private static DecimalFormat df3 = new DecimalFormat("0.00");

	// closely tied to one line of the EXP_EDG_LST file
	public EdgeExperimentsList(String edgeIndex, String edgeDescription, List<ExperimentPair> experiments)
			throws ScriptException {
		this.edgeIndex = edgeIndex;
		colNames = edgeDescription.split("->");
		this.experiments = experiments;
		this.matchingP = getPValue(true);
		this.composingP = getPValue(false);
	}

	/**
	 * 
	 * this is the important method - get the p value for the combined matching
	 * and composing data
	 */
	private double getPValue(boolean b) throws ScriptException {
		NamedRarray[] namedRarrays = getWilcoxInputs(b);
		WilcoxTest wt = new WilcoxTest(namedRarrays[0], namedRarrays[1]);
		return wt.getpValue();
	}

	public String getEdgeIndex() {
		return edgeIndex;
	}

	public List<ExperimentPair> getExperiments() {
		return experiments;
	}

	/**
	 * 
	 * constructs the named array input for the R script. The name is associated
	 * with the input to minimize magic strings in the WilcoxTest
	 */
	public NamedRarray[] getWilcoxInputs(boolean isMatching) {
		String name0 = "v0";
		String name1 = "v1";
		StringBuilder array0Builder = new StringBuilder();
		StringBuilder array1Builder = new StringBuilder();
		array0Builder.append(name0 + "=c(");
		array1Builder.append(name1 + "=c(");

		for (int i = 0; i < experiments.size(); i++) {
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
				array0Builder.append(leftValues[k]);
				array1Builder.append(rightValues[k]);
				if (k != leftValues.length - 1 || i != experiments.size() - 1) {
					array0Builder.append(",");
					array1Builder.append(",");
				}
			}
		}
		NamedRarray[] arrays = new NamedRarray[2];
		arrays[0] = new NamedRarray(name0, array0Builder.toString() + ")");
		arrays[1] = new NamedRarray(name1, array1Builder.toString() + ")");
		return arrays;
	}

	/////////////// latex//////////////////

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

	///////////////// compare///////////////

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
