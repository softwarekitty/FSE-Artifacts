package main.core.filter.lit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.filter.ModelNames;

public class T3_Filter extends AbstractLITRequiredFilter {
	public static final Pattern CCC_WRAPPED_CHAR = Pattern.compile("(?<=\\[).(?=\\])");

	public T3_Filter() {
		super(ModelNames.T3, "has char-class-wrapped literals like [\\$]");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			Matcher m = CCC_WRAPPED_CHAR.matcher(rawPattern);
			return m.find();
		}
		return false;
	}
}
