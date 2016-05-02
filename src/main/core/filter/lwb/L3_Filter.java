package main.core.filter.lwb;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.Model;

public class L3_Filter extends AbstractFeatureFilter {

	public L3_Filter() {
		super(Model.L3, "one-or-more repetition using plus", "[A-Z][a-z]+");
	}

	@Override
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_ADDITIONAL);
	}
}