package main.core.filter.sng;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFilter;
import main.core.filter.Model;

public class S3_Filter extends AbstractFilter {

	private static final Pattern S3_CurlySameRepetition = Pattern.compile("\\{(\\d+),\\1\\}");

	public S3_Filter() {
		super(Model.S3, "curly brace repetition like \\{M,M\\}");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			Matcher m = S3_CurlySameRepetition.matcher(rawPattern);
			return m.find();
		}
		return false;
	}

	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_DOUBLEBOUNDED);
	}
}