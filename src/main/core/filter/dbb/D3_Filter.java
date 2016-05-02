package main.core.filter.dbb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFilter;
import main.core.filter.Model;

public class D3_Filter extends AbstractFilter {

	/**
	 * this will find candidates that need to be further filtered by manual
	 * inspection
	 */
	private static final Pattern D3_OR = Pattern.compile("(?<=[|])([^ \\\\]+)\\1+|([^ \\\\]+)\\1+(?=[|])");

	public D3_Filter() {
		super(Model.D3, "repetition expressed using an OR");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			Matcher m = D3_OR.matcher(rawPattern);
			return m.find();
		}
		return false;
	}

	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_META_OR);
	}
}