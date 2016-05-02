package main.core.filter.lit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.filter.Model;

public class T2_Filter extends AbstractLITRequiredFilter {
	private static final Pattern T2_HEX = Pattern.compile("\\\\x[a-f0-9A-F]{2}");

	public T2_Filter() {
		super(Model.T2, "has HEX literal like \\verb!\\xF5!");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			Matcher m = T2_HEX.matcher(rawPattern);
			return m.find();
		}
		return false;
	}
}