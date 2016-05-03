package main.core.edges;

import java.util.List;

import recreateArtifacts.edgeTests.AnswerColumn;

public interface RFormattable {

	public String formatData(List<AnswerColumn> answerColumns, int offset, int nColumns);

	public int getNColumns();

}
