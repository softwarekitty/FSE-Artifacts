package main.core.filter.sng;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.filter.AbstractFilter;
import main.core.filter.ModelNames;

public class S2_Filter extends AbstractFilter {

	// private static final Pattern S2_repeatingChars =
	// Pattern.compile("(ELEMENT•DOWN•.•UP•)\\1+");
	private static final Pattern S2_repeatingElement = Pattern.compile("(ELEMENT•[^ ]+)\\1");

	public S2_Filter() {
		super(ModelNames.S2, "explicit sequential repetition");
	}

	// an old attempt replaced repeating chars, but this was not entirely sound
	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		String tokenStream = getTokenStream(rawPattern);
		// Matcher m1 = S2_repeatingChars.matcher(tokenStream);
		// String charPairsRemoved = m1.replaceAll("$1");
		Matcher m = S2_repeatingElement.matcher(tokenStream);
		return m.find();
	}
}